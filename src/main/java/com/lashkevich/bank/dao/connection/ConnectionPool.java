package com.lashkevich.bank.dao.connection;

import com.lashkevich.bank.configuration.ConfigurationService;
import com.lashkevich.bank.exception.ConnectionPoolException;
import com.lashkevich.bank.exception.TransactionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {

    private static final String CONNECTION_IS_NULL = "Connection cannot be null";
    private static final String INCORRECT_CONNECTION_ENTER_MESSAGE = "Returned connection does not exist";

    private static final Lock INSTANCE_LOCK = new ReentrantLock();
    private static final Lock CONNECTION_LOCK = new ReentrantLock();
    private static final Condition CONNECTION_CONDITION = CONNECTION_LOCK.newCondition();

    private static ConnectionPool instance;
    private Map<String, Connection> busyTransactionalConnections;
    private Deque<Connection> freeConnections;
    private Deque<Connection> busyConnections;

    private ConnectionPool() {
        freeConnections = new ArrayDeque<>();
        busyConnections = new ArrayDeque<>();
        busyTransactionalConnections = new HashMap<>();
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            try {
                INSTANCE_LOCK.lock();
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            } finally {
                INSTANCE_LOCK.unlock();
            }
        }

        return instance;
    }


    public void initializeConnectionPool(int connectionsNumber) throws ConnectionPoolException {
        try {
            CONNECTION_LOCK.lock();
            Class.forName(ConfigurationService.getJdbcDriverName());

            for (int i = 0; i < connectionsNumber; i++) {
                freeConnections.push(new ProxyConnection(DriverManager.getConnection(ConfigurationService.getJdbcUrl(),
                        ConfigurationService.getJdbcUsername(), ConfigurationService.getJdbcPassword())));
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new ConnectionPoolException(e);
        } finally {
            CONNECTION_LOCK.unlock();
        }
    }

    public int getFreeConnectionsSize() {
        return freeConnections.size();
    }

    public int getBusyConnectionsSize() {
        return busyConnections.size();
    }

    public Connection acquireConnection() throws ConnectionPoolException {
        CONNECTION_LOCK.lock();
        try {
            Connection connection = takeConnection();
            busyConnections.push(connection);
            return connection;
        } finally {
            CONNECTION_LOCK.unlock();
        }
    }

    public Connection acquireTransactionalConnection() throws ConnectionPoolException {
        try {
            CONNECTION_LOCK.lock();
            if (!busyTransactionalConnections.containsKey(Thread.currentThread().getName())) {
                throw new TransactionException("There is no started transactions for this thread");
            }

            return busyTransactionalConnections.get(Thread.currentThread().getName());
        } finally {
            CONNECTION_LOCK.unlock();
        }
    }

    public Connection reserveTransactionalConnection() throws ConnectionPoolException {
        Connection connection = takeConnection();
        busyTransactionalConnections.put(Thread.currentThread().getName(), connection);
        busyConnections.add(connection);
        return connection;
    }

    public void putBackConnection(Connection connection) throws ConnectionPoolException {
        if (connection == null) {
            throw new ConnectionPoolException(CONNECTION_IS_NULL);
        }

        try {
            CONNECTION_LOCK.lock();
            if (!busyTransactionalConnections.containsKey(Thread.currentThread().getName())) {
                if (busyConnections.remove(connection)) {
                    freeConnections.add(connection);
                    CONNECTION_CONDITION.signal();
                } else {
                    throw new RuntimeException(INCORRECT_CONNECTION_ENTER_MESSAGE);
                }
            }
        } finally {
            CONNECTION_LOCK.unlock();
        }
    }

    public void closeConnections() {
        try {
            CONNECTION_LOCK.lock();
            for (Connection connection : busyConnections) {
                ProxyConnection proxyConnection = (ProxyConnection) connection;
                proxyConnection.getConnection().close();
            }

            for (Connection connection : freeConnections) {
                ProxyConnection proxyConnection = (ProxyConnection) connection;
                proxyConnection.getConnection().close();
            }

            for (Connection connection : busyTransactionalConnections.values()) {
                ProxyConnection proxyConnection = (ProxyConnection) connection;
                proxyConnection.getConnection().close();
            }

            freeConnections = new ArrayDeque<>();
            busyConnections = new ArrayDeque<>();
            busyTransactionalConnections = new HashMap<>();
        } catch (SQLException e) {
            throw new ConnectionPoolException(e);
        } finally {
            CONNECTION_LOCK.unlock();
        }
    }

    public void putBackTransactionalConnection() {
        Connection transactionalConnection = busyTransactionalConnections.remove(Thread.currentThread().getName());
        putBackConnection(transactionalConnection);
    }

    private Connection takeConnection() {
        try {
            CONNECTION_LOCK.lock();
            if (freeConnections.isEmpty()) {
                CONNECTION_LOCK.unlock();
                CONNECTION_CONDITION.await();
                CONNECTION_LOCK.lock();
            }

            return freeConnections.poll();
        } catch (InterruptedException e) {
            throw new ConnectionPoolException(e);
        } finally {
            CONNECTION_LOCK.unlock();
        }
    }

}

package com.lashkevich.bank.dao.connection.transaction;

import com.lashkevich.bank.dao.connection.ConnectionPool;
import com.lashkevich.bank.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// This transaction implementation supports only 'REQUIRED' propagation
public class BankTransactionManager {

    private static final Map<String, Integer> TRANSACTIONAL_THREADS = new HashMap<>();

    private static final Lock INSTANCE_LOCK = new ReentrantLock();
    private static BankTransactionManager instance;

    private BankTransactionManager() {
    }

    public static BankTransactionManager getInstance() {
        if (instance == null) {
            try {
                INSTANCE_LOCK.lock();
                if (instance == null) {
                    instance = new BankTransactionManager();
                }
            } finally {
                INSTANCE_LOCK.unlock();
            }
        }

        return instance;
    }


    public BankTransaction createTransaction() {
        try {
            Connection connection;
            if (TRANSACTIONAL_THREADS.containsKey(Thread.currentThread().getName())) {
                TRANSACTIONAL_THREADS.put(Thread.currentThread().getName(),
                        TRANSACTIONAL_THREADS.get(Thread.currentThread().getName()) + 1);
                connection = ConnectionPool.getInstance().acquireConnection();
            } else {
                TRANSACTIONAL_THREADS.put(Thread.currentThread().getName(), 1);
                connection = ConnectionPool.getInstance().reserveTransactionalConnection();
                connection.setAutoCommit(false);
            }

            return new BankTransaction(connection);
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    public void closeTransaction(BankTransaction transaction) {
        Integer transactionsNumber = TRANSACTIONAL_THREADS.get(Thread.currentThread().getName());
        if (transactionsNumber == 1) {
            TRANSACTIONAL_THREADS.remove(Thread.currentThread().getName());
            transaction.closeTransaction();
        } else {
            TRANSACTIONAL_THREADS.put(Thread.currentThread().getName(), transactionsNumber - 1);
        }
    }

    public void rollback(BankTransaction transaction) {
        if (TRANSACTIONAL_THREADS.get(Thread.currentThread().getName()) == 1) {
            transaction.rollback();
        }
    }

    public void commit(BankTransaction transaction) {
        if (TRANSACTIONAL_THREADS.get(Thread.currentThread().getName()) == 1) {
            transaction.commit();
        }
    }

}
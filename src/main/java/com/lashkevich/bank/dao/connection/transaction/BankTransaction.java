package com.lashkevich.bank.dao.connection.transaction;

import com.lashkevich.bank.dao.connection.ConnectionPool;
import com.lashkevich.bank.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

public class BankTransaction {

    private final Connection connection;

    public BankTransaction(Connection connection) {
        this.connection = connection;
    }


    protected void commit() throws TransactionException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    protected void rollback() throws TransactionException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    protected void closeTransaction() {
        try {
            connection.setAutoCommit(true);
            ConnectionPool.getInstance().putBackTransactionalConnection();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

}
package com.lashkevich.bank.dao.connection.transaction.impl;

import com.lashkevich.bank.dao.connection.ConnectionPool;
import com.lashkevich.bank.dao.connection.transaction.Transaction;
import com.lashkevich.bank.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

public class BankTransaction
        implements Transaction {

    private final Connection connection;

    public BankTransaction(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void commit() throws TransactionException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public void rollback() throws TransactionException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public void closeTransaction() {
        try {
            connection.setAutoCommit(true);
            ConnectionPool.getInstance().putBackTransactionalConnection();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

}
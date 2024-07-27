package com.lashkevich.bank.dao.connection.transaction;


import com.lashkevich.bank.exception.TransactionException;

public interface Transaction {

    void commit() throws TransactionException;

    void rollback() throws TransactionException;

    void closeTransaction() throws TransactionException;

}

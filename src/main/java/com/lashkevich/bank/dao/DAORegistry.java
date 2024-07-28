package com.lashkevich.bank.dao;

import com.lashkevich.bank.dao.impl.AccountDAOImpl;
import com.lashkevich.bank.dao.impl.BankDAOImpl;
import com.lashkevich.bank.dao.impl.ClientDAOImpl;
import com.lashkevich.bank.dao.impl.TransactionDAOImpl;

import java.util.function.Supplier;

public enum DAORegistry {

    BANK_DAO(BankDAOImpl::new),
    CLIENT_DAO(ClientDAOImpl::new),
    ACCOUNT_DAO(AccountDAOImpl::new),
    TRANSACTION_DAO(TransactionDAOImpl::new),
    ;

    private final Supplier<AbstractDAO<?>> supplier;
    private AbstractDAO<?> dao;

    DAORegistry(Supplier<AbstractDAO<?>> supplier) {
        this.supplier = supplier;
    }

    public AbstractDAO<?> getDAO() {
        if (dao == null) {
            dao = supplier.get();
        }

        return dao;
    }

}

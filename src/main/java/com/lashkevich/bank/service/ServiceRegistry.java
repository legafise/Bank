package com.lashkevich.bank.service;

import com.lashkevich.bank.entity.Entity;
import com.lashkevich.bank.service.impl.AccountServiceImpl;
import com.lashkevich.bank.service.impl.BankServiceImpl;
import com.lashkevich.bank.service.impl.ClientServiceImpl;
import com.lashkevich.bank.service.impl.TransactionServiceImpl;

import java.util.function.Supplier;

public enum ServiceRegistry {

    CLIENT_SERVICE(ClientServiceImpl::new),
    ACCOUNT_SERVICE(AccountServiceImpl::new),
    TRANSACTION_SERVICE(TransactionServiceImpl::new),
    BANK_SERVICE(BankServiceImpl::new),
    ;

    private final Supplier<Service<? extends Entity>> supplier;
    private Service<? extends Entity> service;

    ServiceRegistry(Supplier<Service<? extends Entity>> supplier) {
        this.supplier = supplier;
    }

    public Service<? extends Entity> getService() {
        if (service == null) {
            service = supplier.get();
        }

        return service;
    }

}
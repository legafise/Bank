package com.lashkevich.bank.service;

import com.lashkevich.bank.entity.Bank;
import com.lashkevich.bank.entity.Currency;

public interface BankService
        extends Service<Bank> {

    void addClientAndOpenAccount(long clientId, long bankId, Currency currency);

}

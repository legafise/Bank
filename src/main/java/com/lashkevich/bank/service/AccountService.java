package com.lashkevich.bank.service;

import com.lashkevich.bank.entity.Account;

import java.math.BigDecimal;

public interface AccountService
        extends Service<Account> {

    void topUpBalance(long accountId, BigDecimal amount);

}

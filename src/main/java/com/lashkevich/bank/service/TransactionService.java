package com.lashkevich.bank.service;

import com.lashkevich.bank.entity.Transaction;

import java.math.BigDecimal;

public interface TransactionService
        extends Service<Transaction> {

    Transaction transferCash(long accountFromId, long accountToId, BigDecimal amount);

}

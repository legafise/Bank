package com.lashkevich.bank.dao.mapper.impl;

import com.lashkevich.bank.dao.mapper.EntityMapper;
import com.lashkevich.bank.entity.Account;
import com.lashkevich.bank.entity.Currency;
import com.lashkevich.bank.entity.Transaction;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountMapper
        implements EntityMapper<Account> {

    @Override
    public Account map(ResultSet resultSet) throws SQLException {
        long accountId = resultSet.getLong("account_id");
        long bankId = resultSet.getLong("bank_id");
        long clientId = resultSet.getLong("client_id");
        Currency currency = Currency.valueOf(resultSet.getString("currency"));
        BigDecimal balance = resultSet.getBigDecimal("balance");

        Account account = new Account();
        account.setId(accountId);
        account.setBankId(bankId);
        account.setClientId(clientId);
        account.setCurrency(currency);
        account.setBalance(balance);
        account.setTransactions(new ArrayList<>());

        do {
            long transactionId = resultSet.getLong("transaction_id");
            if (!resultSet.wasNull()) {
                Transaction transaction = new Transaction();
                transaction.setId(transactionId);
                transaction.setFromAccountId(resultSet.getLong("from_account_id"));
                transaction.setToAccountId(resultSet.getLong("to_account_id"));
                transaction.setAmount(resultSet.getBigDecimal("amount"));
                transaction.setCurrency(Currency.valueOf(resultSet.getString("transaction_currency")));
                transaction.setDateTime(resultSet.getTimestamp("date_time").toLocalDateTime());
                account.getTransactions().add(transaction);
            }
        } while (resultSet.next());


        return account;
    }

}

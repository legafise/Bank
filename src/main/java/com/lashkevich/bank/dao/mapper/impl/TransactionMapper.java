package com.lashkevich.bank.dao.mapper.impl;

import com.lashkevich.bank.dao.mapper.EntityMapper;
import com.lashkevich.bank.entity.Currency;
import com.lashkevich.bank.entity.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionMapper
        implements EntityMapper<Transaction> {

    @Override
    public Transaction map(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(resultSet.getLong("id"));
        transaction.setFromAccountId(resultSet.getLong("from_account_id"));
        transaction.setToAccountId(resultSet.getLong("to_account_id"));
        transaction.setAmount(resultSet.getBigDecimal("amount"));
        transaction.setCurrency(Currency.valueOf(resultSet.getString("currency")));
        transaction.setDateTime(resultSet.getTimestamp("date_time").toLocalDateTime());

        return transaction;
    }

}

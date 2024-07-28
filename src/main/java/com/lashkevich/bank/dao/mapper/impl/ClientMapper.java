package com.lashkevich.bank.dao.mapper.impl;

import com.lashkevich.bank.dao.mapper.EntityMapper;
import com.lashkevich.bank.entity.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientMapper
        implements EntityMapper<Client> {

    @Override
    public Client map(ResultSet resultSet) throws SQLException {
        long clientId = resultSet.getLong("client_id");
        String clientName = resultSet.getString("client_name");
        ClientType clientType = ClientType.valueOf(resultSet.getString("client_type"));

        Client client = new Client();
        client.setId(clientId);
        client.setName(clientName);
        client.setType(clientType);
        client.setAccounts(new ArrayList<>());

        do {
            long accountId = resultSet.getLong("account_id");
            if (!resultSet.wasNull()) {
                Account account = new Account();
                account.setId(accountId);
                account.setBankId(resultSet.getLong("bank_id"));
                account.setClientId(resultSet.getLong("client_id"));
                account.setCurrency(Currency.valueOf(resultSet.getString("currency")));
                account.setBalance(resultSet.getBigDecimal("balance"));
                account.setTransactions(new ArrayList<>());

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

                client.getAccounts().add(account);
            }
        } while (resultSet.next());

        return client;
    }

}


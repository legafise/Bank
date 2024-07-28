package com.lashkevich.bank.dao.mapper.impl;

import com.lashkevich.bank.dao.mapper.EntityMapper;
import com.lashkevich.bank.entity.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BankMapper
        implements EntityMapper<Bank> {

    @Override
    public Bank map(ResultSet resultSet) throws SQLException {
        long bankId = resultSet.getLong("bank_id");
        String bankName = resultSet.getString("bank_name");
        BigDecimal individualFee = resultSet.getBigDecimal("individual_fee");
        BigDecimal legalEntityFee = resultSet.getBigDecimal("legal_entity_fee");

        Bank bank = new Bank();
        bank.setId(bankId);
        bank.setName(bankName);
        bank.setIndividualFee(individualFee);
        bank.setLegalEntityFee(legalEntityFee);
        bank.setAccounts(new ArrayList<>());
        bank.setClients(new ArrayList<>());

        do {
            long accountId = resultSet.getLong("account_id");
            if (!resultSet.wasNull()) {
                Account account = new Account();
                account.setId(accountId);
                account.setBankId(resultSet.getLong("bank_id"));
                account.setClientId(resultSet.getLong("client_id"));
                account.setCurrency(Currency.valueOf(resultSet.getString("currency")));
                account.setBalance(resultSet.getBigDecimal("balance"));
                bank.getAccounts().add(account);
            }

            long clientId = resultSet.getLong("client_id");
            if (!resultSet.wasNull()) {
                Client client = new Client();
                client.setId(clientId);
                client.setName(resultSet.getString("client_name"));
                client.setType(ClientType.valueOf(resultSet.getString("client_type")));
                bank.getClients().add(client);
            }
        } while (resultSet.next());

        return bank;
    }

}

package com.lashkevich.bank.dao.mapper.impl;

import com.lashkevich.bank.dao.mapper.EntityMapper;
import com.lashkevich.bank.entity.Bank;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BankMapper
        implements EntityMapper<Bank> {

    @Override
    public Bank map(ResultSet resultSet) throws SQLException {
        Bank bank = new Bank();
        bank.setId(resultSet.getLong("id"));
        bank.setName(resultSet.getString("name"));
        bank.setIndividualFee(resultSet.getBigDecimal("individual_fee"));
        bank.setLegalEntityFee(resultSet.getBigDecimal("legal_entity_fee"));

        return bank;
    }

}

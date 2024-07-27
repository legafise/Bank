package com.lashkevich.bank.dao.impl;

import com.lashkevich.bank.dao.BankDAO;
import com.lashkevich.bank.dao.connection.ConnectionPool;
import com.lashkevich.bank.dao.mapper.impl.BankMapper;
import com.lashkevich.bank.entity.Bank;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BankDAOImpl
        implements BankDAO {

    private static final String FIND_ALL_BANKS_SQL = "SELECT bank.id, bank.name, bank.individual_fee, bank.legal_entity_fee FROM bank;";

    private final BankMapper bankMapper;

    public BankDAOImpl() {
        bankMapper = new BankMapper();
    }

    @Override
    public Bank findById(long id) {
        return null;
    }

    @Override
    public List<Bank> findAll() {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_BANKS_SQL);
            List<Bank> banks = new ArrayList<>();

            while (resultSet.next()) {
                banks.add(bankMapper.map(resultSet));
            }

            return banks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Bank entity) {

    }

    @Override
    public void update(Bank entity) {

    }

    @Override
    public void delete(long id) {

    }

}

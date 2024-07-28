package com.lashkevich.bank.dao.impl;

import com.lashkevich.bank.dao.BankDAO;
import com.lashkevich.bank.dao.connection.ConnectionPool;
import com.lashkevich.bank.dao.mapper.impl.BankMapper;
import com.lashkevich.bank.entity.Bank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankDAOImpl
        implements BankDAO {

    private static final String INSERT_BANK_SQL = "INSERT INTO Bank (name, individual_fee, legal_entity_fee) VALUES (?, ?, ?)";
    private static final String SELECT_BANK_BY_ID =
            "SELECT b.id AS bank_id, b.name AS bank_name, b.individual_fee, b.legal_entity_fee, " +
                    "a.id AS account_id, a.bank_id, a.client_id, a.currency, a.balance, " +
                    "c.id AS client_id, c.name AS client_name, c.type AS client_type " +
                    "FROM Bank b " +
                    "LEFT JOIN Account a ON b.id = a.bank_id " +
                    "LEFT JOIN Client c ON a.client_id = c.id " +
                    "WHERE b.id = ?";
    private static final String SELECT_ALL_BANKS =
            "SELECT b.id AS bank_id, b.name AS bank_name, b.individual_fee, b.legal_entity_fee, " +
                    "a.id AS account_id, a.bank_id, a.client_id, a.currency, a.balance, " +
                    "c.id AS client_id, c.name AS client_name, c.type AS client_type " +
                    "FROM Bank b " +
                    "LEFT JOIN Account a ON b.id = a.bank_id " +
                    "LEFT JOIN Client c ON a.client_id = c.id";
    private static final String UPDATE_BANK_SQL = "UPDATE Bank SET name = ?, individual_fee = ?, legal_entity_fee = ? WHERE id = ?";
    private static final String DELETE_BANK_SQL = "DELETE FROM Bank WHERE id = ?";


    private final BankMapper bankMapper;

    public BankDAOImpl() {
        bankMapper = new BankMapper();
    }

    @Override
    public Optional<Bank> findById(long id) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BANK_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            Bank bank = null;
            if (resultSet.next()) {
                bank = bankMapper.map(resultSet);
            }

            return Optional.ofNullable(bank);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Bank> findAll() {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_BANKS);

            List<Bank> banks = new ArrayList<>();
            while (resultSet.next()) {
                Bank bank = bankMapper.map(resultSet);
                banks.add(bank);
            }

            return banks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Bank bank) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BANK_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, bank.getName());
            preparedStatement.setBigDecimal(2, bank.getIndividualFee());
            preparedStatement.setBigDecimal(3, bank.getLegalEntityFee());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bank.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Bank bank) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BANK_SQL)) {
            preparedStatement.setString(1, bank.getName());
            preparedStatement.setBigDecimal(2, bank.getIndividualFee());
            preparedStatement.setBigDecimal(3, bank.getLegalEntityFee());
            preparedStatement.setLong(4, bank.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BANK_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

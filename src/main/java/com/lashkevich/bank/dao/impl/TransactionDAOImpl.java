package com.lashkevich.bank.dao.impl;

import com.lashkevich.bank.dao.TransactionDAO;
import com.lashkevich.bank.dao.connection.ConnectionPool;
import com.lashkevich.bank.dao.mapper.impl.TransactionMapper;
import com.lashkevich.bank.entity.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDAOImpl
        implements TransactionDAO {

    private static final String INSERT_TRANSACTION_SQL = "INSERT INTO Transaction (from_account_id, to_account_id, amount, currency, date_time) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_TRANSACTION_BY_ID = "SELECT id, from_account_id, to_account_id, amount, currency, date_time FROM Transaction WHERE id = ?";
    private static final String SELECT_ALL_TRANSACTIONS = "SELECT id, from_account_id, to_account_id, amount, currency, date_time FROM Transaction";
    private static final String UPDATE_TRANSACTION_SQL = "UPDATE Transaction SET from_account_id = ?, to_account_id = ?, amount = ?, currency = ?, date_time = ? WHERE id = ?";
    private static final String DELETE_TRANSACTION_SQL = "DELETE FROM Transaction WHERE id = ?";

    private final TransactionMapper transactionMapper;

    public TransactionDAOImpl() {
        this.transactionMapper = new TransactionMapper();
    }

    @Override
    public void save(Transaction transaction) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, transaction.getFromAccountId());
            preparedStatement.setLong(2, transaction.getToAccountId());
            preparedStatement.setBigDecimal(3, transaction.getAmount());
            preparedStatement.setString(4, transaction.getCurrency().name());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(transaction.getDateTime()));
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Transaction> findById(long id) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TRANSACTION_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() ? Optional.of(transactionMapper.map(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_TRANSACTIONS);

            List<Transaction> transactions = new ArrayList<>();
            while (resultSet.next()) {
                transactions.add(transactionMapper.map(resultSet));
            }

            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Transaction transaction) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TRANSACTION_SQL)) {
            preparedStatement.setLong(1, transaction.getFromAccountId());
            preparedStatement.setLong(2, transaction.getToAccountId());
            preparedStatement.setBigDecimal(3, transaction.getAmount());
            preparedStatement.setString(4, transaction.getCurrency().name());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(transaction.getDateTime()));
            preparedStatement.setLong(6, transaction.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TRANSACTION_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

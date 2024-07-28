package com.lashkevich.bank.dao.impl;

import com.lashkevich.bank.dao.AccountDAO;
import com.lashkevich.bank.dao.connection.ConnectionPool;
import com.lashkevich.bank.dao.mapper.impl.AccountMapper;
import com.lashkevich.bank.entity.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDAOImpl
        implements AccountDAO {

    private static final String INSERT_ACCOUNT_SQL = "INSERT INTO Account (bank_id, client_id, currency, balance) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ACCOUNT_BY_ID =
            "SELECT a.id AS account_id, a.bank_id, a.client_id, a.currency, a.balance, " +
                    "t.id AS transaction_id, t.from_account_id, t.to_account_id, t.amount, t.currency AS transaction_currency, t.date_time " +
                    "FROM Account a " +
                    "LEFT JOIN Transaction t ON a.id = t.from_account_id OR a.id = t.to_account_id " +
                    "WHERE a.id = ?";
    private static final String SELECT_ALL_ACCOUNTS =
            "SELECT a.id AS account_id, a.bank_id, a.client_id, a.currency, a.balance, " +
                    "t.id AS transaction_id, t.from_account_id, t.to_account_id, t.amount, t.currency AS transaction_currency, t.date_time " +
                    "FROM Account a " +
                    "LEFT JOIN Transaction t ON a.id = t.from_account_id OR a.id = t.to_account_id";
    private static final String UPDATE_ACCOUNT_SQL = "UPDATE Account SET bank_id = ?, client_id = ?, currency = ?, balance = ? WHERE id = ?";
    private static final String DELETE_ACCOUNT_SQL = "DELETE FROM Account WHERE i d = ?";

    private final AccountMapper accountMapper;

    public AccountDAOImpl() {
        this.accountMapper = new AccountMapper();
    }

    @Override
    public void save(Account account) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ACCOUNT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, account.getBankId());
            preparedStatement.setLong(2, account.getClientId());
            preparedStatement.setString(3, account.getCurrency().name());
            preparedStatement.setBigDecimal(4, account.getBalance());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Account> findById(long id) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            Account account = null;
            if (resultSet.next()) {
                account = accountMapper.map(resultSet);
            }

            return Optional.ofNullable(account);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Account> findAll() {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_ACCOUNTS);

            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                Account account = accountMapper.map(resultSet);
                accounts.add(account);
            }

            return accounts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Account account) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT_SQL)) {
            preparedStatement.setLong(1, account.getBankId());
            preparedStatement.setLong(2, account.getClientId());
            preparedStatement.setString(3, account.getCurrency().name());
            preparedStatement.setBigDecimal(4, account.getBalance());
            preparedStatement.setLong(5, account.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ACCOUNT_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
package com.lashkevich.bank.dao.impl;

import com.lashkevich.bank.dao.ClientDAO;
import com.lashkevich.bank.dao.connection.ConnectionPool;
import com.lashkevich.bank.dao.mapper.impl.ClientMapper;
import com.lashkevich.bank.entity.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDAOImpl
        implements ClientDAO {

    private static final String INSERT_CLIENT_SQL = "INSERT INTO Client (name, type) VALUES (?, ?)";
    private static final String SELECT_CLIENT_BY_ID =
            "SELECT c.id AS client_id, c.name AS client_name, c.type AS client_type, " +
                    "a.id AS account_id, a.bank_id, a.client_id, a.currency, a.balance, " +
                    "t.id AS transaction_id, t.from_account_id, t.to_account_id, t.amount, t.currency AS transaction_currency, t.date_time " +
                    "FROM Client c " +
                    "LEFT JOIN Account a ON c.id = a.client_id " +
                    "LEFT JOIN Transaction t ON a.id = t.from_account_id OR a.id = t.to_account_id " +
                    "WHERE c.id = ?";
    private static final String SELECT_ALL_CLIENTS =
            "SELECT c.id AS client_id, c.name AS client_name, c.type AS client_type, " +
                    "a.id AS account_id, a.bank_id, a.client_id, a.currency, a.balance, " +
                    "t.id AS transaction_id, t.from_account_id, t.to_account_id, t.amount, t.currency AS transaction_currency, t.date_time " +
                    "FROM Client c " +
                    "LEFT JOIN Account a ON c.id = a.client_id " +
                    "LEFT JOIN Transaction t ON a.id = t.from_account_id OR a.id = t.to_account_id";
    private static final String UPDATE_CLIENT_SQL = "UPDATE Client SET name = ?, type = ? WHERE id = ?";
    private static final String DELETE_CLIENT_SQL = "DELETE FROM Client WHERE id = ?";

    private final ClientMapper clientMapper;

    public ClientDAOImpl() {
        this.clientMapper = new ClientMapper();
    }

    @Override
    public void save(Client client) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CLIENT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getType().name());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Client> findById(long id) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_CLIENT_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            Client client = null;
            if (resultSet.next()) {
                client = clientMapper.map(resultSet);
            }
            return Optional.ofNullable(client);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Client> findAll() {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_CLIENTS);

            List<Client> clients = new ArrayList<>();
            while (resultSet.next()) {
                Client client = clientMapper.map(resultSet);
                clients.add(client);
            }

            return clients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Client client) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENT_SQL)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getType().name());
            preparedStatement.setLong(3, client.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CLIENT_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}


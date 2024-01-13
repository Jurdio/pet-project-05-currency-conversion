package edu.exchanger.currencyexchanger.repositories;

import edu.exchanger.currencyexchanger.models.Currency;
import edu.exchanger.currencyexchanger.util.DataBaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepository implements CrudRepository<Currency> {
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM Currencies WHERE id = ?";
    private static final String SELECT_BY_CODE_QUERY = "SELECT * FROM Currencies WHERE code = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM Currencies";
    private static final String INSERT_QUERY = "INSERT INTO Currencies (code, fullName, sign) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE Currencies SET code = ?, fullName = ?, sign = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM Currencies WHERE id = ?";
    private Currency createCurrencyFromResultSet(ResultSet resultSet){
        try {
            return Currency.builder()
                    .id(resultSet.getInt("id"))
                    .code(resultSet.getString("code"))
                    .fullName(resultSet.getString("fullName"))
                    .sign(resultSet.getString("sign"))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Currency> findById(int id) {
        try (Connection connection = DataBaseUtil.getConnect().orElseThrow()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
                preparedStatement.setInt(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    return Optional.ofNullable(createCurrencyFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Currency> findByCode(String name){
        try (Connection connection = DataBaseUtil.getConnect().orElseThrow();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_CODE_QUERY)) {
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return Optional.ofNullable(createCurrencyFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();
        try (Connection connection = DataBaseUtil.getConnect().orElseThrow();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY))    {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                currencies.add(createCurrencyFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencies;
    }

    @Override
    public void save(Currency currency) {
        try (Connection connection = DataBaseUtil.getConnect().orElseThrow()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, currency.getCode());
                preparedStatement.setString(2, currency.getFullName());
                preparedStatement.setString(3, currency.getSign());
                preparedStatement.executeUpdate();

                // Отримуємо останній вставлений ідентифікатор
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        int lastInsertId = resultSet.getInt(1);
                        currency.setId(lastInsertId);
                    }
                } catch (SQLFeatureNotSupportedException e) {
                    // В разі винятку, використовуйте інший метод для отримання last_insert_rowid()
                    try (Statement statement = connection.createStatement()) {
                        try (ResultSet resultSet = statement.executeQuery("SELECT last_insert_rowid()")) {
                            if (resultSet.next()) {
                                int lastInsertId = resultSet.getInt(1);
                                currency.setId(lastInsertId);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Currency currency) {
        try (Connection connection = DataBaseUtil.getConnect().orElseThrow()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
                preparedStatement.setString(1, currency.getCode());
                preparedStatement.setString(2, currency.getFullName());
                preparedStatement.setString(3, currency.getSign());
                preparedStatement.setInt(4, currency.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = DataBaseUtil.getConnect().orElseThrow()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
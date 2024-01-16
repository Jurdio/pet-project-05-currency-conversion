package edu.exchanger.currencyexchanger.repositories;

import edu.exchanger.currencyexchanger.domain.ExchangeRate;
import edu.exchanger.currencyexchanger.util.DataBaseUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesRepository implements CrudRepository<ExchangeRate> {
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM ExchangeRates WHERE id = ?";
    private static final String SELECT_BY_CODE_QUERY = "SELECT * FROM ExchangeRates WHERE code = ?";
    private static final String SELECT_BY_CODES_QUERY = "SELECT * FROM ExchangeRates WHERE " + "BaseCurrencyId=? AND TargetCurrencyId=?";

    private static final String SELECT_ALL_QUERY = "SELECT * FROM ExchangeRates";
    private static final String INSERT_QUERY = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE ExchangeRates SET BaseCurrencyId = ?, TargetCurrencyId = ?, Rate = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM ExchangeRates WHERE id = ?";
    private final CurrencyRepository currencyRepository;

    public ExchangeRatesRepository() {
        currencyRepository = new CurrencyRepository();
    }

    private ExchangeRate createExchangeRateFromResultSet(ResultSet resultSet) {
        try {
            return ExchangeRate.builder()
                    .id(resultSet.getInt("id"))
                    .baseCurrency(currencyRepository.findById(resultSet.getInt("BaseCurrencyId")).get())
                    .targetCurrency(currencyRepository.findById(resultSet.getInt("TargetCurrencyId")).get())
                    .rate(BigDecimal.valueOf(resultSet.getDouble("rate")))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ExchangeRate> findById(int id) {
        ExchangeRate exchangeRate = null;
        try (Connection connection = DataBaseUtil.getConnect().orElseThrow();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                exchangeRate = createExchangeRateFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(exchangeRate);
    }

    public Optional<ExchangeRate> findByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        ExchangeRate exchangeRate = null;
        try (Connection connection = DataBaseUtil.getConnect().orElseThrow();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_CODES_QUERY)) {

            preparedStatement.setInt(1, currencyRepository.findByCode(baseCurrencyCode).get().getId());
            preparedStatement.setInt(2, currencyRepository.findByCode(targetCurrencyCode).get().getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                exchangeRate = createExchangeRateFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(exchangeRate);
    }

    @Override
    public List<ExchangeRate> findAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try (Connection connection = DataBaseUtil.getConnect().orElseThrow();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                exchangeRates.add(createExchangeRateFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exchangeRates;
    }

    @Override
    public void save(ExchangeRate entity) {
        try (Connection connection = DataBaseUtil.getConnect().orElseThrow();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)){


            preparedStatement.setInt(1, entity.getBaseCurrency().getId());
            preparedStatement.setInt(2, entity.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3, entity.getRate());

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(ExchangeRate entity) {
        try (Connection connection = DataBaseUtil.getConnect().orElseThrow();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setInt(1,entity.getBaseCurrency().getId());
            preparedStatement.setInt(2, entity.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3,entity.getRate());
            preparedStatement.setInt(4,entity.getId());

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(int id) {

    }


}

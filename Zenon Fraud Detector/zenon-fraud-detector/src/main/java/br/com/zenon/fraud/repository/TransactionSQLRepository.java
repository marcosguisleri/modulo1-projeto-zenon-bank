package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.exception.RepositoryException;
import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TransactionType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository {

    private static final String INSERT_SQL = """
            INSERT INTO TRANSACTIONS (
                        step,
                        type,
                        amount,
                        origin_name,
                        origin_old_balance,
                        origin_new_balance,
                        destination_name,
                        destination_old_balance,
                        destination_new_balance,
                        fraud,
                        flagged_fraud
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String FIND_BY_ORIGIN_NAME_SQL = """
            SELECT *
            FROM TRANSACTIONS
            WHERE origin_name = ?
            LIMIT 1
            """;

    private final Connection connection;

    public TransactionSQLRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Transaction> findByOriginName(String originName) {
        try (PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ORIGIN_NAME_SQL)) {

            statement.setString(1, originName);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    int step = resultSet.getInt("step");
                    TransactionType transactionType = TransactionType.valueOf(resultSet.getString("type"));
                    BigDecimal amount = resultSet.getBigDecimal("amount");

                    String originCustomerName = resultSet.getString("origin_name");
                    BigDecimal originOldBalance = resultSet.getBigDecimal("origin_old_balance");
                    BigDecimal originNewBalance = resultSet.getBigDecimal("origin_new_balance");

                    Customer originCustomer = new Customer(originCustomerName, originOldBalance, originNewBalance);

                    String destinationCustomerName = resultSet.getString("destination_name");
                    BigDecimal destinationOldBalance = resultSet.getBigDecimal("destination_old_balance");
                    BigDecimal destinationNewBalance = resultSet.getBigDecimal("destination_new_balance");

                    Customer destinationCustomer = new Customer(destinationCustomerName, destinationOldBalance, destinationNewBalance);

                    boolean fraud = resultSet.getBoolean("fraud");
                    boolean flaggedFraud = resultSet.getBoolean("flagged_fraud");

                    return Optional.of(new Transaction(step, transactionType, amount, originCustomer, destinationCustomer, fraud, flaggedFraud));

                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RepositoryException(
                    "Erro ao buscar transação no banco de dados.",
                    e
            );
        }
    }

    @Override
    public void save(Transaction transaction) {
        try (PreparedStatement statement =
                     connection.prepareStatement(INSERT_SQL)) {

            fillStatement(transaction, statement);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(
                    "Erro ao salvar transação no banco de dados.",
                    e
            );
        }
    }

    public void saveBatch(List<Transaction> transactions) throws SQLException {

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statement =
                         connection.prepareStatement(INSERT_SQL)) {

                for (Transaction transaction : transactions) {

                    fillStatement(transaction, statement);

                    statement.addBatch();
                }

                statement.executeBatch();

                connection.commit();

            } catch (SQLException e) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }

                throw new RepositoryException(
                        "Erro ao salvar lote de transações no banco de dados.",
                        e
                );
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RepositoryException(
                        "Erro ao restaurar auto commit da conexão.",
                        e
                );
            }
        }
    }

    private void fillStatement(
            Transaction transaction,
            PreparedStatement statement
    ) throws SQLException {

        statement.setInt(1, transaction.step());
        statement.setString(2, transaction.transactionType().name());
        statement.setBigDecimal(3, transaction.amount());

        statement.setString(4, transaction.origin().name());
        statement.setBigDecimal(5, transaction.origin().oldBalance());
        statement.setBigDecimal(6, transaction.origin().newBalance());

        statement.setString(7, transaction.destination().name());
        statement.setBigDecimal(8, transaction.destination().oldBalance());
        statement.setBigDecimal(9, transaction.destination().newBalance());

        statement.setBoolean(10, transaction.fraud());
        statement.setBoolean(11, transaction.flaggedFraud());

    }

}

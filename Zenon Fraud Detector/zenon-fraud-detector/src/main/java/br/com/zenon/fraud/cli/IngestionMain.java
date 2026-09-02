package br.com.zenon.fraud.cli;

import br.com.zenon.fraud.exception.RepositoryException;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.repository.TransactionSQLRepository;
import br.com.zenon.fraud.service.EfficientTransactionIngestor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class IngestionMain {

    void main() throws IOException, SQLException {

        String url =
                "jdbc:mysql://localhost:3307/zenon_bank";

        String user = "zenon";
        String password = "zenon123";

        String csvPath =
                "data/dataset.csv";

        int threadCount = 32;

        EfficientTransactionIngestor ingestor =
                new EfficientTransactionIngestor();

        long start = System.nanoTime();

        ingestor.readBatch(
                csvPath,
                batch ->
                        saveBatch(
                                url,
                                user,
                                password,
                                batch
                        ),
                threadCount
        );

        long end = System.nanoTime();

        double elapsedSeconds =
                (end - start)
                        / 1_000_000_000.0;

        IO.println(
                "Threads: "
                        + threadCount
                        + " | Tempo total: "
                        + elapsedSeconds
                        + " segundos"
        );

    }

    private void saveBatch(
            String url,
            String user,
            String password,
            List<Transaction> batch
    ) {

        try (Connection connection =
                     DriverManager.getConnection(
                             url,
                             user,
                             password
                     )) {

            TransactionSQLRepository repository =
                    new TransactionSQLRepository(connection);

            repository.saveBatch(batch);

        } catch (SQLException e) {

            throw new RepositoryException(
                    "Erro ao salvar lote no banco.",
                    e
            );
        }

    }

}
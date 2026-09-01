package br.com.zenon.fraud.cli;

import br.com.zenon.fraud.repository.TransactionSQLRepository;
import br.com.zenon.fraud.service.EfficientTransactionIngestor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IngestionMain {

    void main() throws IOException, SQLException {

        String url = "jdbc:mysql://localhost:3307/zenon_bank";
        String user = "zenon";
        String password = "zenon123";

        String csvPath = "data/dataset.csv";

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            TransactionSQLRepository repository =
                    new TransactionSQLRepository(connection);

            EfficientTransactionIngestor ingestor =
                    new EfficientTransactionIngestor();

            long start = System.nanoTime();

            ingestor.readBatch(
                    csvPath,
                    repository::saveBatch
            );

            long end = System.nanoTime();

            double elapsedSeconds =
                    (end - start) / 1_000_000_000.0;

            IO.println(
                    "Tempo total de ingestão em batch: "
                            + elapsedSeconds
                            + " segundos"
            );
        }
    }
}
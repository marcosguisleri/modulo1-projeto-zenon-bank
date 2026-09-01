package br.com.zenon.fraud.cli;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.repository.TransactionSQLRepository;
import br.com.zenon.fraud.service.TransactionIngestor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DBMain {

    void main() throws SQLException, IOException {

        String url = "jdbc:mysql://localhost:3307/zenon_bank";
        String user = "zenon";
        String password = "zenon123";

        String csvPath = "data/dataset.csv";

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            TransactionSQLRepository repository =
                    new TransactionSQLRepository(connection);

            TransactionIngestor ingestor =
                    new TransactionIngestor();

            List<Transaction> transactions =
                    ingestor.readTransactions(csvPath);

            long start = System.nanoTime();

            repository.saveBatch(transactions);

            long end = System.nanoTime();

            double elapsedSeconds =
                    (end - start) / 1_000_000_000.0;

            IO.println(
                    "Tempo de inserção: "
                            + elapsedSeconds
                            + " segundos"
            );

            repository.findByOriginName("C1231006815")
                    .ifPresentOrElse(
                            transaction ->
                                    IO.println("Transação encontrada: "
                                            + transaction
                                    ),
                            () ->
                                    IO.println(
                                            "Transação não encontrada."
                                    )
                    );

            repository.findByOriginName("C12345")
                    .ifPresentOrElse(
                            transaction ->
                                    IO.println(
                                            "Transação encontrada: "
                                                    + transaction
                                    ),
                            () ->
                                    IO.println(
                                            "Transação não encontrada."
                                    )
                    );

        }

    }

}

package br.com.zenon.fraud.service;

import br.com.zenon.fraud.model.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EfficientTransactionIngestor {

    private static final int BATCH_SIZE = 10_000;

    public void readAsStream(
            String csvPath,
            Consumer<Transaction> consumer
    ) throws IOException {

        Path path = Path.of(csvPath);

        try (Stream<String> lines = Files.lines(path)) {
            lines
                    .skip(1)
                    .limit(10_000)
                    .forEach(line -> {

                        Transaction transaction;

                        try {
                            transaction =
                                    TransactionParser.parseLine(line);
                        } catch (Exception e) {
                            System.err.println(
                                    "Erro: " + line + " | " + e.getMessage()
                            );
                            return;
                        }

                        consumer.accept(transaction);
                    });
        }
    }

    public void readBatch(
            String csvPath,
            Consumer<List<Transaction>> consumer
    ) throws IOException {

        Path path = Path.of(csvPath);

        List<Transaction> batch =
                new ArrayList<>(BATCH_SIZE);

        try (Stream<String> lines = Files.lines(path)) {
            lines
                    .skip(1)
                    .forEach(line -> {

                        Transaction transaction;

                        try {
                            transaction =
                                    TransactionParser.parseLine(line);
                        } catch (Exception e) {
                            System.err.println(
                                    "Erro: " + line + " | " + e.getMessage()
                            );
                            return;
                        }

                        batch.add(transaction);

                        if (batch.size() == BATCH_SIZE) {

                            consumer.accept(
                                    new ArrayList<>(batch)
                            );

                            batch.clear();
                        }
                    });
        }

        if (!batch.isEmpty()) {
            consumer.accept(
                    new ArrayList<>(batch)
            );
        }
    }
}
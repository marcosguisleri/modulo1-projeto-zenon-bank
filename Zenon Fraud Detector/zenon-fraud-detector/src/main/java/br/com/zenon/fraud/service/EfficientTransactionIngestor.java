package br.com.zenon.fraud.service;

import br.com.zenon.fraud.model.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
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
            Consumer<List<Transaction>> consumer,
            int threadCount
    ) throws IOException {

        Path path = Path.of(csvPath);

        List<Transaction> batch =
                new ArrayList<>(BATCH_SIZE);

        List<Future<?>> futures =
                new ArrayList<>();

        ExecutorService executor =
                new ThreadPoolExecutor(
                        threadCount,
                        threadCount,
                        0L,
                        TimeUnit.MILLISECONDS,
                        new ArrayBlockingQueue<>(threadCount * 2),
                        new ThreadPoolExecutor.CallerRunsPolicy()
                );

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
                                    "Erro: "
                                            + line
                                            + " | "
                                            + e.getMessage()
                            );
                            return;
                        }

                        batch.add(transaction);

                        if (batch.size() == BATCH_SIZE) {

                            List<Transaction> batchToProcess =
                                    new ArrayList<>(batch);

                            batch.clear();

                            Future<?> future =
                                    executor.submit(() ->
                                            consumer.accept(batchToProcess)
                                    );

                            futures.add(future);
                        }
                    });

            if (!batch.isEmpty()) {

                List<Transaction> batchToProcess =
                        new ArrayList<>(batch);

                Future<?> future =
                        executor.submit(() ->
                                consumer.accept(batchToProcess)
                        );

                futures.add(future);
            }

        } finally {
            executor.shutdown();
        }

        waitForTasks(futures);
    }

    private void waitForTasks(
            List<Future<?>> futures
    ) {

        for (Future<?> future : futures) {
            try {
                future.get();

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                throw new RuntimeException(
                        "A ingestão foi interrompida.",
                        e
                );

            } catch (ExecutionException e) {

                throw new RuntimeException(
                        "Erro durante o processamento de um lote.",
                        e.getCause()
                );
            }
        }
    }

}
package br.com.zenon.fraud.service;

import br.com.zenon.fraud.model.ReportResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TransactionReport {

    private long totalTransactions;
    private long totalFrauds;
    private BigDecimal totalAmount;

    public ReportResult generateReport(String csvPath) throws IOException {

        totalTransactions = 0;
        totalFrauds = 0;
        totalAmount = BigDecimal.ZERO;

        Path path = Path.of(csvPath);

        try (Stream<String> lines = Files.lines(path)) {

            lines.skip(1)
                    .forEach(line -> {
                        totalTransactions++;

                        String[] fields = line.split(",");

                        if (Integer.parseInt(fields[9]) == 1) {
                            totalFrauds++;
                        }

                        BigDecimal amount = new BigDecimal(fields[2]);
                        totalAmount = totalAmount.add(amount);
                    });
        }

        return new ReportResult(
                totalTransactions,
                totalFrauds,
                totalAmount
        );
    }
}
package br.com.zenon.fraud.service;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.Type;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionIngestor {

    public List<Transaction> readLines(String locale) throws IOException {
        Path path = Path.of(locale);
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .skip(1)
                    .limit(1000)
                    .map(this::parseLine)
                    .collect(Collectors.toList());
        }
    }

    private Transaction parseLine(String line) {
        String[] fields = line.split(",");

        int step = Integer.parseInt(fields[0]);
        Type type = Type.valueOf(fields[1]);
        BigDecimal amount = new BigDecimal(fields[2]);

        Customer origin = new Customer(fields[3], new BigDecimal(fields[4]), new BigDecimal(fields[5]));
        Customer destination = new Customer(fields[6], new BigDecimal(fields[7]), new BigDecimal(fields[8]));

        boolean isFraud = Integer.parseInt(fields[9]) == 1;
        boolean isFlaggedFraud = Integer.parseInt(fields[10]) == 1;

        return new Transaction(step, type, amount, origin, destination, isFraud, isFlaggedFraud);
    }

}

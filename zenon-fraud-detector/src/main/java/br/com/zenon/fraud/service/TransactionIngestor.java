package br.com.zenon.fraud.service;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.Type;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TransactionIngestor {

    public List<Transaction> readLines(String locale) throws IOException {
        Path path = Path.of(locale);
        List<String> lines = Files.readAllLines(path);
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 1; i <= 100000; i++) {
            try {
                transactions.add(parseLine(lines.get(i)));
            } catch (Exception e) {
                System.err.println("Erro: " + lines.get(i) + " | " + e.getMessage());
            }
        }

        return transactions;
    }

    private Transaction parseLine(String line) {
        line = line.replace("\"", "");
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

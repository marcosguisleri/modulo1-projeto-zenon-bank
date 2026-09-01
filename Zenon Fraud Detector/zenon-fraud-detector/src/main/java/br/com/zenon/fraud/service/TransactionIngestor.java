package br.com.zenon.fraud.service;

import br.com.zenon.fraud.model.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static br.com.zenon.fraud.service.TransactionParser.parseLine;

public class TransactionIngestor {

    public List<Transaction> readTransactions(String csvPath) throws IOException {
        Path path = Path.of(csvPath);
        List<String> lines = Files.readAllLines(path);
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 1; i <= 10_000; i++) {
            try {
                transactions.add(parseLine(lines.get(i)));
            } catch (Exception e) {
                System.err.println("Erro: " + lines.get(i) + " | " + e.getMessage());
            }
        }

        return transactions;
    }

}

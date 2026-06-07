package br.com.zenon.fraud.cli;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.service.TransactionIngestor;

import java.io.IOException;
import java.util.List;

public class Main {

    void main() throws IOException {

        String locale = "data/dataset.csv";

        TransactionIngestor ingestor = new TransactionIngestor();

        List<Transaction> transactions = ingestor.readLines(locale);

        for (int i = 0; i < 10; i++) {
            System.out.println(transactions.get(i));
        }

    }
}
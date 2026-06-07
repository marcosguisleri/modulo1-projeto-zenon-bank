package br.com.zenon.fraud.cli;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.service.TransactionIngestor;

import java.io.IOException;
import java.util.List;

public class Main {

    void main() throws IOException {

        String locale = "data/paysim_with_bad_data.csv";

        TransactionIngestor ingestor = new TransactionIngestor();

        List<Transaction> transactions = ingestor.readLines(locale);

        IO.println(transactions.size() + " transações válidas");
        for (Transaction t : transactions) {
            IO.println(t);
        }

    }
}
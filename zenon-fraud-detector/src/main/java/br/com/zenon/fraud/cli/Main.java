package br.com.zenon.fraud.cli;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.Type;
import br.com.zenon.fraud.service.FraudAnalyzer;
import br.com.zenon.fraud.service.TransactionIngestor;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class Main {

    void main() throws IOException {

        String locale = "data/dataset.csv";

        TransactionIngestor ingestor = new TransactionIngestor();
        FraudAnalyzer fraudAnalyzer = new FraudAnalyzer();

        List<Transaction> transactions = ingestor.readLines(locale);

        IO.println("1. Total de Fraudes: " + fraudAnalyzer.countFrauds(transactions));

        IO.println("2. Top 3 Fraudes de Maior Valor: ");
        for (BigDecimal amount : fraudAnalyzer.topFraudsByAmount(transactions)) {
            IO.println(amount.setScale(2, RoundingMode.HALF_UP).toPlainString());
        }

        IO.println("3. Clientes Suspeitos: ");
        for (String nameOrig : fraudAnalyzer.topSuspiciousOrigins(transactions)) {
            IO.println(nameOrig);
        }

        IO.println("4. Prejuízo Total: " + fraudAnalyzer.totalFraudAmount(transactions));

        IO.println("5. Fraudes por Tipo: ");
        for (Map.Entry<Type, Long> entry : fraudAnalyzer.fraudCountByType(transactions).entrySet()) {
            IO.println(" - " + entry.getKey() + ": " + entry.getValue());
        }

    }
}
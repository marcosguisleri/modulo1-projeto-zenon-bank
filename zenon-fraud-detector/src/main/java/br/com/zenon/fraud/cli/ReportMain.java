package br.com.zenon.fraud.cli;

import br.com.zenon.fraud.service.TransactionReport;

import java.io.IOException;

public class ReportMain {

    void main() throws IOException {

        String locale = "data/dataset.csv";

        TransactionReport report = new TransactionReport();

        ReportResult result = report.generateReport(locale);

        IO.println("Total de linhas: " + result.totalTransactions());
        IO.println("Total de fraudes: " + result.totalFrauds());
        IO.println("Valor total transacionado:: " + result.totalAmount());

    }

}

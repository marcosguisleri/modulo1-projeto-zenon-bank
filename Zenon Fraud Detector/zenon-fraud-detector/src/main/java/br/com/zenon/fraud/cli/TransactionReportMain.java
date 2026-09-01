package br.com.zenon.fraud.cli;

import br.com.zenon.fraud.model.ReportResult;
import br.com.zenon.fraud.service.TransactionReport;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class TransactionReportMain {

    void main(String[] args) throws IOException {

        Locale locale = resolveLocale(args);

        if (locale == null) {
            return;
        }

        String filePath = "data/dataset.csv";

        TransactionReport report = new TransactionReport();

        ReportResult result = report.generateReport(filePath);

        NumberFormat currencyFormat =
                NumberFormat.getCurrencyInstance(locale);

        ResourceBundle bundle =
                ResourceBundle.getBundle("report", locale);

        IO.println(
                bundle.getString("report.totalTransactions")
                        + ": "
                        + result.totalTransactions()
        );

        IO.println(
                bundle.getString("report.totalFrauds")
                        + ": "
                        + result.totalFrauds()
        );

        IO.println(
                bundle.getString("report.totalValue")
                        + ": "
                        + currencyFormat.format(result.totalAmount())
        );

    }

    private static Locale resolveLocale(String[] args) {
        if (args.length == 0) {
            IO.println("Informe o idioma: pt ou en");
            return null;
        }

        String selectedLanguage = args[0];

        if (selectedLanguage.equalsIgnoreCase("pt")) {
            return Locale.of("pt", "BR");
        }

        if (selectedLanguage.equalsIgnoreCase("en")) {
            return Locale.of("en", "US");
        }

        IO.println("Idioma inválido. Use pt ou en.");
        return null;
    }

}

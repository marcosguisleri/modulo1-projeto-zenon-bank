package br.com.zenon.fraud.cli;

import java.math.BigDecimal;

public record ReportResult(
        long totalTransactions,
        long totalFrauds,
        BigDecimal totalAmount
) {
}

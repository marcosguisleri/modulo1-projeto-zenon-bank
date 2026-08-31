package br.com.zenon.fraud.model;

import java.math.BigDecimal;

public record ReportResult(
        long totalTransactions,
        long totalFrauds,
        BigDecimal totalAmount
) {
}

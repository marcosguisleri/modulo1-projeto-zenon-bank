package br.com.zenon.fraud.model;

import java.math.BigDecimal;

public record Customer(
        String name,
        BigDecimal oldBalance,
        BigDecimal newBalance
) {
    public Customer {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name should not be empty.");
        }

        if (oldBalance == null) {
            throw new IllegalArgumentException("oldBalance should not be empty.");
        }

        if (oldBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("oldBalance should be positive: " + oldBalance);
        }

        if (newBalance == null) {
            throw new IllegalArgumentException("newBalance should not be empty.");
        }

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("newBalance should be positive: " + newBalance);
        }
    }
}

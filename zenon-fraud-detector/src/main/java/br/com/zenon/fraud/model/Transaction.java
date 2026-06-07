package br.com.zenon.fraud.model;

import java.math.BigDecimal;

public record Transaction(
        int step,
        Type type,
        BigDecimal amount,
        Customer origin,
        Customer destination,
        boolean fraud,
        boolean flaggedFraud
){
    public Transaction {
        if (step <= 0) {
            throw new IllegalArgumentException("step should be positive: " + step);
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount should be positive: " + amount);
        }

        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }

        if (origin == null) {
            throw new IllegalArgumentException("Origin cannot be null.");
        }

        if (destination == null) {
            throw new IllegalArgumentException("Destination cannot be null.");
        }
    }
}

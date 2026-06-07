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
) {
    @Override
    public String toString() {
        return "Transaction{" +
                "step=" + step +
                ", type=" + type +
                ", amount=" + amount +
                ", origin=" + origin +
                ", destination=" + destination +
                ", fraud=" + fraud +
                ", flaggedFraud=" + flaggedFraud +
                '}';
    }
}

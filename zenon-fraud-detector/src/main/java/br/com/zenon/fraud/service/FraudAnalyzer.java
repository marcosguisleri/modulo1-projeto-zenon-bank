package br.com.zenon.fraud.service;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TransactionType;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    public Long countFrauds(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Transaction::fraud)
                .count();
    }

    public List<BigDecimal> topFraudsByAmount(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Transaction::fraud)
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(3)
                .map(Transaction::amount)
                .toList();
    }

    public List<String> topSuspiciousOrigins(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Transaction::fraud)
                .map(t -> t.origin().name())
                .distinct()
                .limit(5)
                .toList();
    }

    public BigDecimal totalFraudAmount(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Transaction::fraud)
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<TransactionType, Long> fraudCountByType(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Transaction::fraud)
                .collect(Collectors.groupingBy(Transaction::transactionType, Collectors.counting()));
    }
}

package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.model.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {

    private final Map<String, Transaction> transactionsByOriginName;

    public TransactionMapRepository(List<Transaction> transactions) {
        this.transactionsByOriginName = transactions.stream()
                .collect(Collectors.toMap(
                        t -> t.origin().name(),
                        t -> t
                ));
    }

    @Override
    public Optional<Transaction> findByOriginName(String originName) {
        return Optional.ofNullable(transactionsByOriginName.get(originName));
    }
}

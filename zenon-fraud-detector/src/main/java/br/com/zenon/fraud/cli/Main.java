package br.com.zenon.fraud.cli;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.Type;
import br.com.zenon.fraud.repository.TransactionListRepository;
import br.com.zenon.fraud.repository.TransactionMapRepository;
import br.com.zenon.fraud.service.FraudAnalyzer;
import br.com.zenon.fraud.service.TransactionIngestor;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

    void main() throws IOException {

        String locale = "data/dataset.csv";
        TransactionIngestor ingestor = new TransactionIngestor();
        List<Transaction> transactions = ingestor.readLines(locale);

        TransactionListRepository listRepository = new TransactionListRepository(transactions);
        TransactionMapRepository mapRepository = new TransactionMapRepository(transactions);

        IO.println("--- Busca com List ---");
        listRepository.findByOriginName("C1231006815").ifPresentOrElse(
                IO::println,
                () -> IO.println("Transação não encontrada para o cliente C1231006815")
        );
        listRepository.findByOriginName("C12345").ifPresentOrElse(
                IO::println,
                () -> IO.println("Transação não encontrada para o cliente C12345")
        );

        IO.println("\n--- Busca com Map ---");
        mapRepository.findByOriginName("C1231006815").ifPresentOrElse(
                IO::println,
                () -> IO.println("Transação não encontrada para o cliente C1231006815")
        );
        mapRepository.findByOriginName("C12345").ifPresentOrElse(
                IO::println,
                () -> IO.println("Transação não encontrada para o cliente C12345")
        );

        IO.println("\n--- Benchmark (pior caso: C1868032458) ---");

        long inicioList = System.nanoTime();
        listRepository.findByOriginName("C1868032458");
        long fimList = System.nanoTime();
        IO.println("Tempo List: " + (fimList - inicioList) + " ns");

        long inicioMap = System.nanoTime();
        mapRepository.findByOriginName("C1868032458");
        long fimMap = System.nanoTime();
        IO.println("Tempo Map: " + (fimMap - inicioMap) + " ns");

    }
}
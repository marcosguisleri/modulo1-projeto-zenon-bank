package br.com.zenon.fraud.cli;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;

import java.math.BigDecimal;

import static br.com.zenon.fraud.model.Type.*;

public class Main {

    void main() {

        Customer origin1 = new Customer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36"));
        Customer origin2 = new Customer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0"));

        Customer destination1 = new Customer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0"));
        Customer destination2 = new Customer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63"));

        Transaction transaction1 = new Transaction(1, PAYMENT, new BigDecimal("9839.64"), origin1, destination1, false, false);
        Transaction transaction2 = new Transaction(743, CASH_OUT, new BigDecimal("850002.52"), origin2, destination2, true, false);

        IO.println(transaction1);
        IO.println();
        IO.println(transaction2);

    }
}

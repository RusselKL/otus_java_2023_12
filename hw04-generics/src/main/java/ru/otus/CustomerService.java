package ru.otus;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final Map<Customer, String> sortedCustomers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> firstEntry = ((TreeMap<Customer, String>) sortedCustomers).firstEntry();
        return copyOf(firstEntry);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = ((TreeMap<Customer, String>) sortedCustomers).higherEntry(customer);
        return copyOf(higherEntry);
    }

    public void add(Customer customer, String data) {
        sortedCustomers.put(customer, data);
    }

    private Map.Entry<Customer, String> copyOf(Map.Entry<Customer, String> entry) {
        if (entry != null) {
            return Map.entry(
                    new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores()),
                    entry.getValue()
            );
        } else {
            return null;
        }
    }

}

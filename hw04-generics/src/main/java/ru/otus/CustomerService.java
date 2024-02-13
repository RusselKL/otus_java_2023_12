package ru.otus;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> sortedCustomers = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> firstEntry = sortedCustomers.firstEntry();
        if (firstEntry != null) {
            return Map.entry(
                    new Customer(firstEntry.getKey().getId(), firstEntry.getKey().getName(), firstEntry.getKey().getScores()),
                    firstEntry.getValue()
            );
        } else {
            return null;
        }
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = sortedCustomers.higherEntry(customer);
        if (higherEntry != null) {
            return Map.entry(
                    new Customer(higherEntry.getKey().getId(), higherEntry.getKey().getName(), higherEntry.getKey().getScores()),
                    higherEntry.getValue()
            );
        } else {
            return null;
        }
    }

    public void add(Customer customer, String data) {
        sortedCustomers.put(customer, data);
    }

}

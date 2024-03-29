package ru.otus.currency;

import java.util.List;

/**
 * Класс валюты, с существующими номиналами
 */
public enum Currency {
    RUB(50, 100, 1000, 5000),
    USD(5, 10, 20, 50, 100),
    EUR(20, 50, 100, 200, 500);

    private final List<Integer> nominals;

    Currency(Integer... nominals) {
        this.nominals = List.of(nominals);
    }

    public List<Integer> getNominals() {
        return nominals;
    }
}

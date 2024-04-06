package ru.otus.atm;

import ru.otus.currency.Currency;

import java.util.List;

/**
 * Класс банкоматов Тинькофф, предполагаем что банк работает с 3мя валютами и имеет базовый функционал ATM
 */
public class TinkoffATM extends ATM {
    private static final String bankName = "Tinkoff";
    private static final List<Currency> currencyCodes = List.of(Currency.RUB, Currency.USD, Currency.EUR);

    public TinkoffATM(String id) {
        super(id, bankName, currencyCodes);
    }
}

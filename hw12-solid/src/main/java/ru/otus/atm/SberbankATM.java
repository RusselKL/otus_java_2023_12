package ru.otus.atm;

import ru.otus.currency.Currency;

import java.util.List;

/**
 * Класс банкоматов Сбербанк, предполагаем что банк работает с 1ой валютой и имеет базовый функционал ATM
 */
public class SberbankATM extends ATM {
    private static final String bankName = "Sberbank";
    private static final List<Currency> currencyCodes = List.of(Currency.RUB);

    public SberbankATM(String id) {
        super(id, bankName, currencyCodes);
    }
}

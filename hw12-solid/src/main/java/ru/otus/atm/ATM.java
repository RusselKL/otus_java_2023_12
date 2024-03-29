package ru.otus.atm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.currency.Currency;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Базовый функционал всех банкоматов
 */
public abstract class ATM {
    private static final Logger logger = LoggerFactory.getLogger(ATM.class);
    private static final Integer INITIAL_NOMINALS_AMOUNT = 0;
    private final String bankName;
    protected final String id;
    protected final Map<Currency, Map<Integer, Integer>> balance;

    protected ATM(String id, String bankName, List<Currency> currencyCodes) {
        this.id = id;
        this.bankName = bankName;
        this.balance = configureBalance(currencyCodes);
    }

    /**
     * Принимает одну банкноту на счет
     *
     * @param value    - номинал банкноты
     * @param currency - валюта
     */
    public void deposit(Integer value, Currency currency) {
        if (balance.get(currency) != null) {
            var nominals = currency.getNominals();
            if (nominals.contains(value)) {
                var nominalsInBalance = balance.get(currency);
                nominalsInBalance.put(value, nominalsInBalance.get(value) + 1);
            } else {
                logger.atInfo()
                        .log(String.format("\nNominal: %1$d do not exist for %2$s\nAvailable nominals: %3$s",
                                value,
                                currency.name(),
                                nominals
                        ));
            }
        } else {
            logger.atInfo()
                    .log(String.format("\nCurrency: %1$s do not supported by %2$s ATM (id:%3$s)\nAvailable currencies: %4$s",
                            currency.name(),
                            bankName,
                            id,
                            balance.keySet()
                    ));
        }
    }

    /**
     * Выдает запрошенную сумму мин.количеством банкнот
     *
     * @param value    - сумма
     * @param currency - валюта
     */
    public void withdrawal(Integer value, Currency currency) {
        var withdrawalNominals = new TreeMap<Integer, Integer>();
        if (balance.get(currency) != null) {
            var nominals = new TreeMap<>(balance.get(currency)).reversed();
            var withdrawalValue = value;
            for (Integer nominal : nominals.keySet()) {
                while (nominal <= withdrawalValue && nominals.get(nominal) > 0) {
                    withdrawalValue = withdrawalValue - nominal;
                    nominals.put(nominal, nominals.get(nominal) - 1);
                    withdrawalNominals.merge(nominal, 1, Integer::sum);
                }
            }
            if (withdrawalValue == 0) {
                balance.put(currency, nominals);
                logger.atInfo()
                        .log(String.format("Withdrawal success in %1$s ATM (id:%2$s): nominals used %3$s",
                                bankName,
                                id,
                                withdrawalNominals
                        ));
            } else {
                logger.atInfo()
                        .log(String.format("\nWithdrawal error in %1$s ATM (id:%2$s): not enough nominals for requested value %3$d\nAvailable nominals: %4$s",
                                bankName,
                                id,
                                value,
                                nominals
                        ));
            }
        } else {
            logger.atInfo()
                    .log(String.format("\nCurrency: %1$s do not supported by %2$s ATM (id:%3$s)\nAvailable currencies: %4$s",
                            currency.name(),
                            bankName,
                            id,
                            balance.keySet()
                    ));
        }
    }

    private Map<Currency, Map<Integer, Integer>> configureBalance(List<Currency> currencyCodes) {
        var balance = new HashMap<Currency, Map<Integer, Integer>>();
        for (Currency currency : currencyCodes) {
            var nominals = new TreeMap<Integer, Integer>();
            currency.getNominals()
                    .forEach(nominal -> nominals.put(nominal, INITIAL_NOMINALS_AMOUNT));
            balance.put(currency, nominals);
        }
        return balance;
    }

    /**
     * Выводит баланс банкомата
     */
    public void logBalance() {
        var balanceInfo = new StringBuilder();
        for (Map.Entry<Currency, Map<Integer, Integer>> entry : balance.entrySet()) {
            balanceInfo
                    .append(entry.getKey())
                    .append(":\n");
            var nominals = entry.getValue();
            for (Map.Entry<Integer, Integer> nominalEntry : nominals.entrySet()) {
                balanceInfo
                        .append(nominalEntry.getKey())
                        .append(" -> ")
                        .append(nominalEntry.getValue())
                        .append(", ");
            }
            balanceInfo.replace(balanceInfo.length() - 2, balanceInfo.length(), "\n");
        }
        logger.atInfo()
                .log(String.format("\n%1$s ATM (id:%2$s) balance:\n%3$s",
                        bankName,
                        id,
                        balanceInfo
                ));
    }
}

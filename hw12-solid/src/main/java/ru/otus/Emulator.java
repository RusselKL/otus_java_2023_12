package ru.otus;

import ru.otus.atm.ATM;
import ru.otus.atm.SberbankATM;
import ru.otus.atm.TinkoffATM;
import ru.otus.currency.Currency;

public class Emulator {
    public static void main(String[] args) {
        ATM atm1 = new TinkoffATM("1");
        atm1.deposit(100, Currency.USD);
        atm1.deposit(100, Currency.USD);
        atm1.deposit(125, Currency.USD);
        atm1.deposit(500, Currency.RUB);
        atm1.deposit(20, Currency.USD);
        atm1.deposit(20, Currency.USD);
        atm1.deposit(10, Currency.USD);
        atm1.withdrawal(150, Currency.USD);
        atm1.withdrawal(100, Currency.USD);
        atm1.logBalance();

        ATM atm2 = new TinkoffATM("2");
        atm2.deposit(500, Currency.RUB);
        atm2.deposit(10, Currency.USD);
        atm2.logBalance();

        ATM atm3 = new SberbankATM("1");
        atm3.deposit(500, Currency.RUB);
        atm3.deposit(10, Currency.USD);
        atm3.logBalance();
    }
}

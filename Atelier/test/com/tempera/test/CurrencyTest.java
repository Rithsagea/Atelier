package com.tempera.test;

import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;

public class CurrencyTest {
	public static void main(String[] args) {
		System.out.println(new Price(Currency.COPPER, 10000));
		System.out.println(new Price(Currency.COPPER, 1000));
		System.out.println(new Price(Currency.COPPER, 100));
		System.out.println(new Price(Currency.COPPER, 10));
		System.out.println(new Price(Currency.COPPER, 1));
	}
}

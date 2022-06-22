package com.atelier.test;

import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.Currency.Price;

public class CurrencyTest {
	public static void main(String[] args) {
		System.out.println(new Price(10000, Currency.COPPER));
		System.out.println(new Price(1000, Currency.COPPER));
		System.out.println(new Price(100, Currency.COPPER));
		System.out.println(new Price(10, Currency.COPPER));
		System.out.println(new Price(1, Currency.COPPER));
	}
}

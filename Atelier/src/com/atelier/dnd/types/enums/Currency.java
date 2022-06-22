package com.atelier.dnd.types.enums;

import com.atelier.AtelierLanguageManager;

public enum Currency {
	COPPER(1), SILVER(10), ELECTRUM(50), GOLD(100), PLATINUM(1000);

	private final String label;
	private final int value;

	private Currency(int value) {
		this.value = value;
		label = AtelierLanguageManager.getInstance().get(this, "label");
	}

	public String getLabel() {
		return label;
	}

	public int getValue() {
		return value;
	}

	public static double convert(Currency from, Currency to, int value) {
		return ((double) value) * from.getValue() / to.getValue();
	}

	public static class Price {

		private final Currency currency;
		private final int quantity;

		private static final Currency[] CURRENCY_PRIORITY = { GOLD, SILVER, COPPER };

		public Price(int quantity, Currency currency) {
			double value;

			for (Currency c : CURRENCY_PRIORITY) {
				value = convert(currency, c, quantity);
				if (value == Math.rint(value)) {
					this.currency = c;
					this.quantity = (int) value;
					return;
				}
			}

			this.currency = currency;
			this.quantity = quantity;
		}

		public Currency getCurrency() {
			return currency;
		}

		public int getQuantity() {
			return quantity;
		}

		@Override
		public String toString() {
			return String.format("%d %s", quantity, currency.getLabel());
		}
	}
}

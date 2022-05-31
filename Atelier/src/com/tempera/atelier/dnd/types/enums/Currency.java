package com.tempera.atelier.dnd.types.enums;

public enum Currency {
	COPPER("cp", 1),
	SILVER("sp", 10),
	ELECTRUM("ep", 50),
	GOLD("gp", 100),
	PLATINUM("pp", 1000);
	
	private final String label;
	private final int value;
	
	private Currency(String label, int value) {
		this.label = label;
		this.value = value;
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
		
		private static final Currency[] CURRENCY_PRIORITY = {
				GOLD, SILVER, COPPER
		};
		
		public Price(int quantity, Currency currency) {
			double value;
			
			for(Currency c : CURRENCY_PRIORITY) {
				value = convert(currency, c, quantity);
				if(value == Math.rint(value)) {
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

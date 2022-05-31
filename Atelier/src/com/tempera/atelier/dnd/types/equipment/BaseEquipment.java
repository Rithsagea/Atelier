package com.tempera.atelier.dnd.types.equipment;

import com.tempera.atelier.dnd.types.enums.Currency.Price;

public abstract class BaseEquipment implements Equipment {

	private String name;
	private String description;
	private String source;
	private Price price;
	private int weight;
	
	public BaseEquipment(String name, String description, String source, Price price, int weight) {
		this.name = name;
		this.description = description;
		this.source = source;
		this.price = price;
		this.weight = weight;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public String getSource() {
		return source;
	}

	@Override
	public Price getPrice() {
		return price;
	}

	@Override
	public int getWeight() {
		return weight;
	}

}

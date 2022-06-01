package com.tempera.atelier.dnd.types.equipment;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;

public abstract class BaseEquipment implements Equipment {

	private Set<EquipmentType> categories;
	
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
		
		this.categories = new HashSet<>(); 
	}
	
	protected void addCategories(EquipmentType...categories) {
		Collections.addAll(this.categories, categories);
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
	
	@Override
	public Set<EquipmentType> getCategories() {
		return Collections.unmodifiableSet(categories);
	}

}

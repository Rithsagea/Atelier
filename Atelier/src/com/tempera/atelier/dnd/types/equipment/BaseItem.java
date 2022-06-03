package com.tempera.atelier.dnd.types.equipment;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;

public abstract class BaseItem implements Item {

	private transient Set<EquipmentType> categories;

	private transient String name;
	private transient String description;
	private transient String source;
	private transient Price price;
	private transient double weight;

	private int amount;

	public BaseItem(String name, String description, String source, Price price,
		double weight) {
		this.name = name;
		this.description = description;
		this.source = source;
		this.price = price;
		this.weight = weight;

		this.amount = 1;

		this.categories = new HashSet<>();
	}

	protected void addCategories(EquipmentType... categories) {
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
	public double getUnitWeight() {
		return weight;
	}

	@Override
	public Set<EquipmentType> getCategories() {
		return Collections.unmodifiableSet(categories);
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public boolean isStackable(Item item) {
		return getClass().equals(item.getClass());
	}

	@Override
	public void stack(Item item) {
		amount += item.getAmount();
	}
	
	@Override
	public String toString()
	{
		return name + " x" + amount;
		
	}

}

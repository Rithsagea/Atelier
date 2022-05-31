package com.tempera.atelier.dnd.types.equipment;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentCategory;

public abstract class Weapon extends BaseEquipment {
	private Set<EquipmentCategory> categories;
	
	public Weapon(String name, String description, String source, Price price, int weight) {
		super(name, description, source, price, weight);
		
		categories = new HashSet<>(); categories.add(EquipmentCategory.WEAPON);
	}
	
	protected void addCategories(EquipmentCategory... categories) {
		Collections.addAll(this.categories, categories);
	}

	@Override
	public final String getType() {
		return "weapon";
	}
	
	@Override
	public Set<EquipmentCategory> getCategories() {
		return Collections.unmodifiableSet(categories);
	}
}

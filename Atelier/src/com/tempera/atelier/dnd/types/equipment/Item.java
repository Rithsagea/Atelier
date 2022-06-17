package com.tempera.atelier.dnd.types.equipment;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tempera.atelier.discord.Menu;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.equipment.attributes.ItemAttribute;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface Item {

	public String getType();
	public String getName();
	public String getDescription();
	public Price getPrice();

	public double getWeight();

	public Set<EquipmentType> getCategories();
	public Set<ItemAttribute> getAttributes();

	public int getAmount();
	public void setAmount(int amount);
	public boolean isStackable(Item equipment);
	
	public Menu getMenu();
}

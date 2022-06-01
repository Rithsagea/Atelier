package com.tempera.atelier.dnd.types.equipment;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property="type")
public interface Equipment {
	
	public String getType();
	public String getName();
	public String getDescription();
	public String getSource();
	
	public Price getPrice();
	public int getWeight();
	
	public Set<EquipmentType> getCategories();
}

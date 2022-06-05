package com.tempera.atelier.dnd.types.equipment.attributes;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface ItemAttribute {
	public String getName();
	public String getDescription();
}

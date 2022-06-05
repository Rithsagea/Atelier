package com.tempera.atelier.dnd.types.equipment.attributes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tempera.atelier.dnd.types.IndexedItem;

@IndexedItem("source")
public class SourceAttribute implements ItemAttribute {

	private String source;
	
	@JsonCreator
	public SourceAttribute(@JsonProperty("source") String source) {
		this.source = source;
	}
	
	@Override
	public String getName() {
		return "Source";
	}

	@Override
	public String getDescription() {
		return source;
	}

}
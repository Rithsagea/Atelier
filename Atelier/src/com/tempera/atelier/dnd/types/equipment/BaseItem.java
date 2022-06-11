package com.tempera.atelier.dnd.types.equipment;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.aaagarbage.Menu;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.equipment.attributes.ItemAttribute;
import com.tempera.atelier.dnd.types.equipment.attributes.SourceAttribute;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public abstract class BaseItem implements Item {

	private transient Set<EquipmentType> categories;

	private transient String name;
	private transient String description;
	private transient Price price;
	private transient double weight;

	private transient Set<ItemAttribute> attributes;
	private int amount;

	public BaseItem(String name, String description, String source, Price price,
		double weight) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.weight = weight;

		this.amount = 1;

		this.attributes = new HashSet<>();
		this.categories = new HashSet<>();
		
		addAttributes(new SourceAttribute(source));
	}

	protected void addCategories(EquipmentType... categories) {
		Collections.addAll(this.categories, categories);
	}

	protected void addAttributes(ItemAttribute...attributes) {
		Collections.addAll(this.attributes, attributes);
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
	public Price getPrice() {
		return new Price(price.getQuantity() * getAmount(), price.getCurrency());
	}

	@Override
	public double getWeight() {
		return weight * getAmount();
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
	public Set<ItemAttribute> getAttributes() {
		return Collections.unmodifiableSet(attributes);
	}
	
	@Override
	public String toString() {
		return name + " x " + amount;
	}
	
	private class BaseItemMenu implements Menu {

		@Override
		public Message initialize() {
			MessageBuilder mb = new MessageBuilder();
			EmbedBuilder eb = new EmbedBuilder();
			
			eb.setTitle(getAmount() > 1 ? 
				String.format("%s (%d)", getName(), getAmount()) : getName());
			eb.appendDescription(String.format("*%s*\n%s, %.0f lb.\n\n", getType(), getPrice(), getWeight()));
			if(getDescription() != null) eb.appendDescription(getDescription());
			
			for(ItemAttribute attr : getAttributes()) {
				eb.addField(attr.getName(), attr.getDescription(), false);
			}
			
			mb.setEmbeds(eb.build());
			
			return mb.build();
		}

		@Override
		public void onButtonInteract(ButtonInteractionEvent event) { }

		@Override
		public void onSelectInteract(SelectMenuInteractionEvent event) { }
		
	}
	
	@Override
	public Menu getMenu() {
		return new BaseItemMenu();
	}

}

package com.atelier.dnd.types.equipment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory {
	private ArrayList<Item> contents;

	public Inventory() {
		contents = new ArrayList<>();
	}

	public List<Item> getContents() {
		return Collections.unmodifiableList(contents);
	}

	public void addItem(Item item) {
		for (int i = 0; i < contents.size(); i++) {
			if (item.isStackable(contents.get(i))) {
				contents.get(i).setAmount(item.getAmount() + contents.get(i).getAmount());
				return;
			}
		}

		int nextFree = contents.indexOf(null);
		if (nextFree == -1)
			contents.add(item);
		else
			contents.set(nextFree, item);
	}
}

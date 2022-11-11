package com.rithsagea.phb.races.elf;

import com.atelier.dnd.character.CharacterRace;
import com.atelier.dnd.character.DescriptionTrait;

public class Elf extends CharacterRace {

	public static class ElfAgeTrait extends DescriptionTrait {}
	public static class ElfSizeTrait extends DescriptionTrait {}
	public static class ElfLanguageTrait extends DescriptionTrait {}

	@Override
	protected void init() {
		registerTrait("age", new ElfAgeTrait());
		registerTrait("size", new ElfSizeTrait());
		registerTrait("darkvision", new ElfDarkvisionTrait());
		registerTrait("keen-senses", new ElfKeenSensesTrait());
		registerTrait("fey-ancestry", new ElfFeyAncestryTrait());
		registerTrait("trance", new ElfTranceTrait());
		registerTrait("langages", new ElfLanguageTrait());
	}
	
}

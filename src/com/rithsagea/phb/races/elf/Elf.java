package com.rithsagea.phb.races.elf;

import com.atelier.database.AtelierType;
import com.atelier.dnd.character.attributes.CharacterRace;
import com.atelier.dnd.character.attributes.DescriptionTrait;

@AtelierType("elf")
public class Elf extends CharacterRace {

	@AtelierType("elf-age")
	public static class ElfAgeTrait extends DescriptionTrait {}
	@AtelierType("elf-size")
	public static class ElfSizeTrait extends DescriptionTrait {}
	@AtelierType("elf-language")
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

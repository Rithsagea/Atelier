package com.atelier.database;

import com.atelier.dnd.character.NullClass;
import com.atelier.dnd.character.NullRace;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.rithsagea.phb.classes.rogue.Rogue;
import com.rithsagea.phb.classes.rogue.RogueExpertiseFeature;
import com.rithsagea.phb.classes.rogue.SneakAttackFeature;
import com.rithsagea.phb.classes.rogue.ThievesCantFeature;
import com.rithsagea.phb.races.elf.Elf;
import com.rithsagea.phb.races.elf.Elf.ElfAgeTrait;
import com.rithsagea.phb.races.elf.Elf.ElfLanguageTrait;
import com.rithsagea.phb.races.elf.Elf.ElfSizeTrait;
import com.rithsagea.phb.races.elf.ElfDarkvisionTrait;
import com.rithsagea.phb.races.elf.ElfFeyAncestryTrait;
import com.rithsagea.phb.races.elf.ElfKeenSensesTrait;
import com.rithsagea.phb.races.elf.ElfTranceTrait;

public class Types {
	public static void registerTypes(ObjectMapper mapper) {
		registerClasses(mapper);
		registerRaces(mapper);
	}

	private static void registerClasses(ObjectMapper mapper) {
		mapper.registerSubtypes(
			new NamedType(NullClass.class, "null"),

			new NamedType(Rogue.class, "rogue"),
			new NamedType(RogueExpertiseFeature.class, "rogue-expertise"),
			new NamedType(SneakAttackFeature.class, "sneak-attack"),
			new NamedType(ThievesCantFeature.class, "thieves-cant"));
	}

	private static void registerRaces(ObjectMapper mapper) {
		mapper.registerSubtypes(
			new NamedType(NullRace.class, "null"),

			new NamedType(Elf.class, "elf"),
			new NamedType(ElfAgeTrait.class, "elf-age"),
			new NamedType(ElfSizeTrait.class,"elf-size"),
			new NamedType(ElfDarkvisionTrait.class, "elf-darkvision"),
			new NamedType(ElfKeenSensesTrait.class, "elf-keen-senses"),
			new NamedType(ElfFeyAncestryTrait.class, "elf-fey-ancestry"),
			new NamedType(ElfTranceTrait.class, "elf-trance"),
			new NamedType(ElfLanguageTrait.class, "elf-language")
		);
	}

}

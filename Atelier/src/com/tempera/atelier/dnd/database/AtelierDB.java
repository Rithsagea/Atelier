package com.tempera.atelier.dnd.database;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;
import org.mongojack.ObjectMapperConfigurer;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.tempera.atelier.Config;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.types.character.attributes.ProficiencyFeature;
import com.tempera.atelier.dnd.types.character.classes.Rogue;
import com.tempera.atelier.dnd.types.spread.PointBuySpread;

public class AtelierDB {
	
	private static final boolean OFFLINE_MODE = false;
	
	private MongoClient client;
	private MongoDatabase db;
	
	private MongoCollection<User> userCollection;
	private MongoCollection<Sheet> sheetCollection;
	
	private ReplaceOptions replaceUpsertOption;
	
	private Map<Long, User> users;
	private Map<UUID, Sheet> sheets;
	
	public AtelierDB(Config config) {
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(config.getDatabaseUrl()))
				.uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
				.build();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		registerSubtypes(mapper);
		
		ObjectMapperConfigurer.configureObjectMapper(mapper);
		
		client = MongoClients.create(settings);
		db = client.getDatabase(config.getDatabaseName());
		
		userCollection = JacksonMongoCollection.builder()
				.withObjectMapper(mapper)
				.build(db.getCollection("users", User.class), User.class, UuidRepresentation.JAVA_LEGACY);
		sheetCollection = JacksonMongoCollection.builder()
				.withObjectMapper(mapper)
				.build(db.getCollection("sheets", Sheet.class), Sheet.class, UuidRepresentation.JAVA_LEGACY);
	
		replaceUpsertOption = new ReplaceOptions().upsert(true);
		
		users = new HashMap<>();
		sheets = new HashMap<>();
	}
	
	private void registerSubtypes(ObjectMapper mapper) {
		//ability spreads
		mapper.registerSubtypes(
				new NamedType(PointBuySpread.class, "pointBuy"));
		
		//character classes
		mapper.registerSubtypes(
				new NamedType(Rogue.class, "rogue"));
		
		//attributes
		mapper.registerSubtypes(
				new NamedType(ProficiencyFeature.class, "feature-proficiency"));
	}
	
	public void disconnect() {
		save();
		
		client.close();
	}
	
	private User findUser(long id) {
		if(OFFLINE_MODE) return null;
		return userCollection.find(Filters.eq("_id", id)).first();
	}
	
	private void updateUser(User user) {
		if(OFFLINE_MODE) return;
		userCollection.replaceOne(Filters.eq("_id", user.getId()), user, replaceUpsertOption);
	}
	
	private Sheet findSheet(UUID id) {
		if(OFFLINE_MODE) return null;
		Sheet sheet = sheetCollection.find(Filters.eq("_id", id)).first();
		if(sheet != null) sheet.reload();
		return sheet;
	}
	
	private void updateSheet(Sheet sheet) {
		sheetCollection.replaceOne(Filters.eq("_id", sheet.getId()), sheet, replaceUpsertOption);
	}
	
	public Collection<Sheet> listSheets() {
		if(!OFFLINE_MODE) this.sheetCollection.find()
			.forEach((Sheet sheet) -> {
				if(!sheets.containsKey(sheet.getId()))
					sheets.put(sheet.getId(), sheet);
			});
		
		return sheets.values();
	}
	
	/**
	 * Finds a user based on an id. Returns a blank user if it does not exist
	 * @param id the numerical discord id of the user
	 */
	public User getUser(long id) {
		User user = users.get(id);
		if(user == null) {
			user = findUser(id);
			if(user == null) {
				user = new User(id);
			}
			
			users.put(id, user);
		}
		
		return user;
	}
	
	/**
	 * Finds a sheet based on an id. Returns null if it does not exist
	 * @param id the uuid of the sheet
	 */
	public Sheet getSheet(UUID id) {
		Sheet sheet = sheets.get(id);
		if(sheet == null) {
			sheet = findSheet(id);
			sheets.put(id, sheet);
		}
		
		return sheet;
	}
	
	public void addSheet(Sheet sheet) {
		sheets.put(sheet.getId(), sheet);
	}
	
	public void save() {
		if(OFFLINE_MODE) return;
		
		for(User user : users.values()) {
			updateUser(user);
		}
		
		for(Sheet sheet : sheets.values()) {
			updateSheet(sheet);
		}
	}
}
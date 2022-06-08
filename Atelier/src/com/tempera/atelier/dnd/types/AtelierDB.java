package com.tempera.atelier.dnd.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;
import org.mongojack.ObjectMapperConfigurer;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

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
import com.tempera.atelier.discord.User;

public class AtelierDB {

	private static final boolean OFFLINE_MODE = false;

	private MongoClient client;
	private MongoDatabase db;

	private MongoCollection<User> userCollection;
	private MongoCollection<Sheet> sheetCollection;
	private MongoCollection<Campaign> campaignCollection;

	private ReplaceOptions replaceUpsertOption;

	private Map<Long, User> users;
	private Map<UUID, Sheet> sheets;
	private Map<UUID, Campaign> campaigns;

	private TypeRegistry typeRegistry;

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
		campaignCollection = JacksonMongoCollection.builder()
			.withObjectMapper(mapper)
			.build(db.getCollection("campaigns", Campaign.class), Campaign.class, UuidRepresentation.JAVA_LEGACY);

		replaceUpsertOption = new ReplaceOptions().upsert(true);

		users = new HashMap<>();
		sheets = new HashMap<>();
		campaigns = new HashMap<>();

		if (!OFFLINE_MODE) {
			userCollection.find().forEach((User user) -> users.put(user.getId(), user));
			sheetCollection.find().forEach((Sheet sheet) -> {
				sheets.put(sheet.getId(), sheet);
				sheet.reload();
			});
			campaignCollection.find().forEach((Campaign campaign) -> 
				campaigns.put(campaign.getId(), campaign));
		}
	}

	private void registerSubtypes(ObjectMapper mapper) {
		String basePackage = "";
//		basePackage = "com.tempera.atelier.dnd.types";
		ConfigurationBuilder cb = new ConfigurationBuilder().forPackage(basePackage);

		typeRegistry = new TypeRegistry();

		for (Class<?> clazz : new Reflections(cb).getTypesAnnotatedWith(IndexedItem.class)) {
			String id = clazz.getAnnotation(IndexedItem.class).value();
			mapper.registerSubtypes(new NamedType(clazz, id));
			typeRegistry.registerType(id, clazz);
		}
	}

	public void disconnect() {
		save();
		client.close();
	}

	private void updateUser(User user) {
		if(OFFLINE_MODE) return;
		userCollection.replaceOne(Filters.eq("_id", user.getId()), user, replaceUpsertOption);
	}

	private void updateSheet(Sheet sheet) {
		if(OFFLINE_MODE) return;
		sheetCollection.replaceOne(Filters.eq("_id", sheet.getId()), sheet, replaceUpsertOption);
	}
	
	private void updateCampaign(Campaign campaign) {
		if(OFFLINE_MODE) return;
		campaignCollection.replaceOne(Filters.eq("_id", campaign.getId()), campaign, replaceUpsertOption);
	}

	public Collection<Sheet> listSheets() {
		return sheets.values();
	}
	
	public Collection<Campaign> listCampaigns() {
		return campaigns.values();
	}

	public User getUser(long id) {
		User user = users.get(id);
		if (user == null) {
			user = new User(id);
			users.put(id, user);
		}

		return user;
	}

	public Sheet getSheet(UUID id) {
		return sheets.get(id);
	}

	public void addSheet(Sheet sheet) {
		updateSheet(sheet);
		sheets.put(sheet.getId(), sheet);
	}
	
	public Campaign getCampaign(UUID id) {
		return campaigns.get(id);
	}
	
	public void addCampaign(Campaign campaign) {
		updateCampaign(campaign);
		campaigns.put(campaign.getId(), campaign);
	}

	public void save() {
		if (OFFLINE_MODE) return;

		users.values().forEach(this::updateUser);
		sheets.values().forEach(this::updateSheet);
		campaigns.values().forEach(this::updateCampaign);
	}

	public TypeRegistry getTypeRegistry() {
		return typeRegistry;
	}
}

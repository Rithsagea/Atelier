package com.atelier.dnd.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;
import org.mongojack.ObjectMapperConfigurer;

import com.atelier.Config;
import com.atelier.discord.User;
import com.atelier.dnd.types.alpha.TypeRegistry;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

public class AtelierDB {

	private static AtelierDB INSTANCE;
	public static AtelierDB init(Config config) {
		if(INSTANCE == null) INSTANCE = new AtelierDB(config);
		return INSTANCE;
	}
	
	public static AtelierDB getInstance() {
		return INSTANCE;
	}
	
	private static final boolean OFFLINE_MODE = false;

	private MongoClient client;
	private MongoDatabase db;

	private MongoCollection<User> userCollection;
	private MongoCollection<Sheet> sheetCollection;
	private MongoCollection<Campaign> campaignCollection;
	private MongoCollection<Track> atelierTrack;

	private ReplaceOptions replaceUpsertOption = new ReplaceOptions().upsert(true);;

	private Map<Long, User> users = new HashMap<>();
	private Map<UUID, Sheet> sheets = new HashMap<>();
	private Map<UUID, Campaign> campaigns = new HashMap<>();
	private Map<UUID, Track> tracks = new HashMap<>();

	private TypeRegistry typeRegistry = new TypeRegistry();

	private AtelierDB(Config config) {
		MongoClientSettings settings = MongoClientSettings.builder()
			.applyConnectionString(new ConnectionString(config.getDatabaseUrl()))
			.uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
			.build();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		Types.registerTypes(typeRegistry);
		typeRegistry.configureMapper(mapper);
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
		atelierTrack = JacksonMongoCollection.builder()
			.withObjectMapper(mapper)
			.build(db.getCollection("tracks", Track.class), Track.class, UuidRepresentation.JAVA_LEGACY);

		if (!OFFLINE_MODE) {
			userCollection.find().forEach((User user) -> users.put(user.getId(), user));
			sheetCollection.find().forEach((Sheet sheet) -> {
				sheets.put(sheet.getId(), sheet);
				sheet.reload();
			});
			campaignCollection.find().forEach((Campaign campaign) -> 
				campaigns.put(campaign.getId(), campaign));
			atelierTrack.find().forEach((Track track) ->
				tracks.put(track.getId(), track));
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

	private void updateTrack(Track track) {
		if(OFFLINE_MODE) return;
		atelierTrack.replaceOne(Filters.eq("_id", track.getId()), track, replaceUpsertOption);
	}
	
	public Collection<Sheet> listSheets() {
		return sheets.values();
	}
	
	public Collection<Campaign> listCampaigns() {
		return campaigns.values();
	}

	public Collection<Track> listTracks(){
		return tracks.values();
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

	public Track getTrack(UUID id) {
		return tracks.get(id);
	}
	
	public void addTrack(Track track) {
		updateTrack(track);
		tracks.put(track.getId(), track);
	}
	
	public void save() {
		if (OFFLINE_MODE) return;

		users.values().forEach(this::updateUser);
		sheets.values().forEach(this::updateSheet);
		campaigns.values().forEach(this::updateCampaign);
		tracks.values().forEach(this::updateTrack);
	}

	public TypeRegistry getTypeRegistry() {
		return typeRegistry;
	}
}

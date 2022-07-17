package com.atelier.database;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.atelier.Config;
import com.atelier.discord.User;
import com.atelier.dnd.types.Campaign;
import com.atelier.dnd.types.Sheet;
import com.atelier.dnd.types.Track;
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
	
	private Map<Long, User> users = new HashMap<>();
	private Map<UUID, Sheet> sheets = new HashMap<>();
	private Map<UUID, Campaign> campaigns = new HashMap<>();
	private Map<UUID, Track> tracks = new HashMap<>();
	
	private MongoClient client;
	private MongoDatabase database;
	private ReplaceOptions replaceUpsertOption = new ReplaceOptions().upsert(true);
	
	private MongoCollection<User> userCollection;
	
	private AtelierDB(Config config) {
		MongoClientSettings settings = MongoClientSettings.builder()
				.codecRegistry(TypeRegistry.getInstance().getCodecRegistry())
				.applyConnectionString(new ConnectionString(config.getDatabaseUrl()))
				.build();
				
		client = MongoClients.create(settings);
		database = client.getDatabase(config.getDatabaseName());
		
		load();
	}

	public void disconnect() {
		save();
	}
	
	public Collection<Sheet> listSheets() {
		return null;
	}
	
	public Collection<Campaign> listCampaigns() {
		return null;
	}

	public Collection<Track> listTracks() {
		return null;
	}
	
	public User getUser(long id) {
		return users.get(id);
	}
	
	public void addUser(User user) {
		users.put(user.getId(), user);
	}

	public Sheet getSheet(UUID id) {
		return sheets.get(id);
	}

	public void addSheet(Sheet sheet) {
	}
	
	public Campaign getCampaign(UUID id) {
		return campaigns.get(id);
	}
	
	public void addCampaign(Campaign campaign) {
	}

	public Track getTrack(UUID id) {
		return tracks.get(id);
	}
	
	public void addTrack(Track track) {
	}
	
	public void load() {
		userCollection = database.getCollection("users", User.class);
		userCollection.find().forEach((User user) -> users.put(user.getId(), user));
	}
	
	private void updateUser(User user) {
		userCollection.replaceOne(Filters.eq("_id", user.getId()), user, replaceUpsertOption);
	}
	
	public void save() {
		users.values().forEach(this::updateUser);
	}
}

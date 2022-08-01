package com.atelier.database;

import java.util.HashMap;
import java.util.Map;

import com.atelier.Config;
import com.atelier.discord.User;
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
	
	public User getUser(long id) {
		return users.get(id);
	}
	
	public void addUser(User user) {
		users.put(user.getId(), user);
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

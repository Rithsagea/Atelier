package com.atelier.database;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.atelier.Config;
import com.atelier.discord.AtelierUser;
import com.atelier.dnd.AtelierCharacter;
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
	
	private Map<Long, AtelierUser> users = new HashMap<>();
	private Map<UUID, AtelierCharacter> characters = new HashMap<>();
	
	private MongoClient client;
	private MongoDatabase database;
	private ReplaceOptions replaceUpsertOption = new ReplaceOptions().upsert(true);
	
	private MongoCollection<AtelierUser> userCollection;
	private MongoCollection<AtelierCharacter> characterCollection;
	
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
	
	public AtelierUser getUser(long id) {
		if(!users.containsKey(id)) users.put(id, new AtelierUser(id));
		
		return users.get(id);
	}
	
	public void addUser(AtelierUser user) {
		users.put(user.getId(), user);
	}
	
	public Collection<AtelierUser> listUsers() {
		return users.values();
	}
	
	public AtelierCharacter getCharacter(UUID id) {
		return characters.get(id);
	}
	
	public void addCharacter(AtelierCharacter character) {
		characters.put(character.getId(), character);
	}

	public Collection<AtelierCharacter> listCharacters() {
		return characters.values();
	}
	
	public void load() {
		userCollection = database.getCollection("users", AtelierUser.class);
		characterCollection = database.getCollection("characters", AtelierCharacter.class);
		
		userCollection.find().forEach((AtelierUser user) -> users.put(user.getId(), user));
		characterCollection.find().forEach((AtelierCharacter character) -> characters.put(character.getId(), character));
	}
	
	private void updateUser(AtelierUser user) {
		userCollection.replaceOne(Filters.eq("_id", user.getId()), user, replaceUpsertOption);
	}
	
	private void updateCharacter(AtelierCharacter character) {
		characterCollection.replaceOne(Filters.eq("_id", character.getId().toString()), character, replaceUpsertOption);
	}
	
	public void save() {
		users.values().forEach(this::updateUser);
		characters.values().forEach(this::updateCharacter);
		
//		userCollection.insertMany(new ArrayList<>(users.values()));
//		characterCollection.insertMany(new ArrayList<>(characters.values()));
	}
}

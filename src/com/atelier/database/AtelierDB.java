package com.atelier.database;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import com.atelier.Config;
import com.atelier.discord.AtelierUser;
import com.atelier.dnd.campaign.Campaign;
import com.atelier.dnd.campaign.Scene;
import com.atelier.dnd.character.AtelierCharacter;
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
	private Map<UUID, Campaign> campaigns = new HashMap<>();
	
	private Config config;
	private MongoClient client;
	private MongoDatabase database;
	private ReplaceOptions replaceUpsertOption = new ReplaceOptions().upsert(true);
	
	private MongoCollection<AtelierUser> userCollection;
	private MongoCollection<AtelierCharacter> characterCollection;
	private MongoCollection<Campaign> campaignCollection;
	
	private AtelierDB(Config config) {
		this.config = config;
		
	}

	public void connect() {
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

	public Stream<AtelierCharacter> listCharacters() {
		return characters.values().stream();
	}

	public Campaign getCampaign(UUID id) {
		return campaigns.get(id);
	}

	public void addCampaign(Campaign campaign) {
		campaigns.put(campaign.getId(), campaign);
	}

	public Stream<Campaign> listCampaigns() {
		return campaigns.values().stream();
	}

	public Scene getScene(UUID id) {
		try {
			return listScenes().filter(s -> s.getId().equals(id)).findFirst().get();
		} catch(NoSuchElementException e) {
			return null; //scene does not exist
		}
	}

	public Stream<Scene> listScenes() {
		return campaigns.values().stream().map(c -> c.getScenes()).flatMap(Function.identity());
	}
	
	public void load() {
		userCollection = database.getCollection("users", AtelierUser.class);
		characterCollection = database.getCollection("characters", AtelierCharacter.class);
		campaignCollection = database.getCollection("campaign", Campaign.class);
		
		userCollection.find().forEach((AtelierUser user) -> users.put(user.getId(), user));
		characterCollection.find().forEach((AtelierCharacter character) -> {
			character.reload();
			characters.put(character.getId(), character);
		});
		campaignCollection.find().forEach((Campaign campaign) -> campaigns.put(campaign.getId(), campaign));
	}
	
	private void updateUser(AtelierUser user) {
		userCollection.replaceOne(Filters.eq("_id", user.getId()), user, replaceUpsertOption);
	}
	
	private void updateCharacter(AtelierCharacter character) {
		characterCollection.replaceOne(Filters.eq("_id", character.getId().toString()), character, replaceUpsertOption);
	}

	private void updateCampaign(Campaign campaign) {
		campaignCollection.replaceOne(Filters.eq("_id", campaign.getId().toString()), campaign, replaceUpsertOption);
	}
	
	public void save() {
		users.values().forEach(this::updateUser);
		characters.values().forEach(this::updateCharacter);
		campaigns.values().forEach(this::updateCampaign);
		
//		userCollection.insertMany(new ArrayList<>(users.values()));
//		characterCollection.insertMany(new ArrayList<>(characters.values()));
	}
}

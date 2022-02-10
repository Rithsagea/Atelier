package com.rithsagea.atelier.dnd.database;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.rithsagea.atelier.Config;

public class AtelierDB {
	private MongoClient client;
	private MongoDatabase db;
	
	private MongoCollection<User> users;
	
	public AtelierDB(Config config) {
		CodecRegistry codecs = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(PojoCodecProvider.builder()
						.automatic(true)
						.register(User.class)
						.build()));
		
		client = MongoClients.create(MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(config.getDatabaseUrl()))
				.codecRegistry(codecs)
				.build());
		
		db = client.getDatabase(config.getDatabaseName());
		users = db.getCollection("users", User.class);
	}
	
	public User findUser(long id) {
		return users.find(Filters.eq("_id", id)).first();
	}
	
	public void updateUser(User user) {
		users.replaceOne(Filters.eq("_id", user.getId()), user, new ReplaceOptions().upsert(true));
	}
}

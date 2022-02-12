package com.rithsagea.atelier.dnd.database;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import com.rithsagea.atelier.Config;

public class AtelierDB {
	private MongoClient client;
	private MongoDatabase db;
	
	private MongoCollection<User> users;
	private MongoCollection<Sheet> sheets;
	
	private ReplaceOptions replaceUpsertOption;
	
	public AtelierDB(Config config) {
		CodecRegistry codecs = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(PojoCodecProvider.builder()
						.automatic(true)
						.register(User.class)
						.register(Sheet.class)
						.build()));
		
		client = MongoClients.create(MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(config.getDatabaseUrl()))
				.codecRegistry(codecs)
				.build());
		
		db = client.getDatabase(config.getDatabaseName());
		users = db.getCollection("users", User.class);
		sheets = db.getCollection("sheets", Sheet.class);
		
		replaceUpsertOption = new ReplaceOptions().upsert(true);
	}
	
	public void disconnect() {
		client.close();
	}
	
	public User findUser(long id) {
		return users.find(Filters.eq("_id", id)).first();
	}
	
	public void updateUser(User user) {
		users.replaceOne(Filters.eq("_id", user.getId()), user, replaceUpsertOption);
	}
	
	public Sheet findSheet(UUID id) {
		return sheets.find(Filters.eq("_id", id)).first();
	}
	
	public Set<Sheet> listSheets() {
		Set<Sheet> sheets = new HashSet<>();
		this.sheets.find()
			.projection(Projections.fields(
					Projections.include("_id")))
			.forEach((Sheet sheet) -> sheets.add(sheet));
		
		return sheets;
	}
	
	public void updateSheet(Sheet sheet) {
		sheets.replaceOne(Filters.eq("_id", sheet.getId()), sheet, replaceUpsertOption);
	}
}

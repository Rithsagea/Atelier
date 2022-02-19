package com.rithsagea.atelier.dnd.database;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;
import org.mongojack.ObjectMapperConfigurer;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;

public class AtelierDB {
	
	private MongoClient client;
	private MongoDatabase db;
	
	private MongoCollection<User> users;
	private MongoCollection<Sheet> sheets;
	
	private ReplaceOptions replaceUpsertOption;
	
	public AtelierDB(Config config) {
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(config.getDatabaseUrl()))
				.uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
				.build();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		ObjectMapperConfigurer.configureObjectMapper(mapper);
		
		client = MongoClients.create(settings);
		db = client.getDatabase(config.getDatabaseName());
		
		users = JacksonMongoCollection.builder()
				.withObjectMapper(mapper)
				.build(db.getCollection("users", User.class), User.class, UuidRepresentation.JAVA_LEGACY);
		sheets = JacksonMongoCollection.builder()
				.withObjectMapper(mapper)
				.build(db.getCollection("sheets", Sheet.class), Sheet.class, UuidRepresentation.JAVA_LEGACY);
	
		replaceUpsertOption = new ReplaceOptions().upsert(true);
	}
	
	public void disconnect() {
		client.close();
	}
	
	/**
	 * @param id the id of the user to find
	 * @return the user
	 */
	public User findUser(long id) {
		return users.find(Filters.eq("_id", id)).first();
	}
	
	/**
	 * updates or replace the provided user into the db
	 * @param user the user to update
	 */
	public void updateUser(User user) {
		users.replaceOne(Filters.eq("_id", user.getId()), user, replaceUpsertOption);
	}
	
	
	/**
	 * @param id the uuid of the sheet to find
	 * @return the sheet
	 */
	public Sheet findSheet(UUID id) {
		return sheets.find(Filters.eq("_id", id)).first();
	}
	
	/**
	 * @return all existing character sheets
	 */
	public Set<Sheet> listSheets() {
		Set<Sheet> sheets = new HashSet<>();
		this.sheets.find()
			.projection(Projections.fields(
					Projections.include("_id")))
			.forEach((Sheet sheet) -> sheets.add(sheet));
		
		return sheets;
	}
	
	/**
	 * updates or replace the provided sheet into the db
	 * @param sheet the sheet to update
	 */
	public void updateSheet(Sheet sheet) {
		sheets.replaceOne(Filters.eq("_id", sheet.getId()), sheet, replaceUpsertOption);
	}
}

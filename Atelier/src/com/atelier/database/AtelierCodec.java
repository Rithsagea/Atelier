package com.atelier.database;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import com.atelier.database.annotations.Constructor;
import com.atelier.database.annotations.Id;
import com.rithsagea.util.DataUtil;

public class AtelierCodec<T> implements Codec<T> {

	private Class<T> type;
	
	public AtelierCodec(Class<T> type) {
		this.type = type;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
		TypeRegistry types = TypeRegistry.getInstance();
		CodecRegistry codecs = types.getCodecRegistry();
		
		writer.writeStartDocument();
		
		for(Field field : DataUtil.getFields(type)) {
			
			if(field.isAnnotationPresent(Id.class)) continue;
			
			try {
				field.setAccessible(true);
				
				// write to the following to bson
				String name = field.getName();
				Object obj = field.get(value);
				//TODO: nonetype check
				
				writer.writeName(name);
				Codec<Object> codec = (Codec<Object>) codecs.get(obj.getClass());
				codec.encode(writer, obj, encoderContext);
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		writer.writeEndDocument();
	}

	@Override
	public Class<T> getEncoderClass() {
		return type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T decode(BsonReader reader, DecoderContext decoderContext) {
		
		T value = null;
		Object id = null;
		TypeRegistry types = TypeRegistry.getInstance();
		CodecRegistry codecs = types.getCodecRegistry();
		
		Map<String, Field> fieldMap = new HashMap<>(); // map out all fields in type to serialize
		for(Field field : DataUtil.getFields(type)) {
			if(Modifier.isTransient(field.getModifiers())) continue;
			fieldMap.put(field.isAnnotationPresent(Id.class) ? "_id" : field.getName(), field);
		}
		
		reader.readStartDocument();
		
		while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
			String name = reader.readName();
			Field field = fieldMap.get(name);
			
			field.setAccessible(true);
			Class<?> fieldType = field.getType(); // wrap primitive types
			if(fieldType.isPrimitive()) fieldType = DataUtil.getWrapper(fieldType);
			
			if(name.equals("_id")) { id = codecs.get(fieldType).decode(reader, decoderContext); continue; }
			
			// TODO subtypes go here
			try {
				if(value == null) {
					Constructor construct = type.getAnnotation(Constructor.class);
					if(construct != null) value = (T) construct.value().newInstance().build();
					else value = type.newInstance(); // TODO factory class
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			// actually serialize field
			try {
				Codec<?> codec = codecs.get(fieldType);
				Object obj = codec.decode(reader, decoderContext);
				field.set(value, obj);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		reader.readEndDocument();
		
		// handle id
		Field field = fieldMap.get("_id");
		field.setAccessible(true);
		try {
			field.set(value, id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return value;
	}

}

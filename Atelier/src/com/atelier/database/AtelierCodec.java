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

import com.rithsagea.util.DataUtil;

public class AtelierCodec<T> implements Codec<T> {
	
	private Class<T> encoderClass;
	
	public AtelierCodec(Class<T> encoderClass) {
		this.encoderClass = encoderClass;
	}
	
	@Override
	public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
		TypeRegistry types = TypeRegistry.getInstance();
		CodecRegistry codecRegistry = types.getCodecRegistry();
		
		writer.writeStartDocument();
		
		Subtype ann = value.getClass().getAnnotation(Subtype.class);
		if(ann != null) writer.writeString("_type", ann.value());
		
		for(Field field : DataUtil.getFields(encoderClass)) {
			
			if(field.isAnnotationPresent(Id.class)) continue;
			if(Modifier.isTransient(field.getModifiers())) continue;
				
			try {
				field.setAccessible(true);
				Object obj = field.get(value);
				String name = field.getName();
				if(DataUtil.isNoneType(obj)) continue;
				
				writer.writeName(name);
				@SuppressWarnings("unchecked")
				Codec<Object> codec = (Codec<Object>) codecRegistry.get(obj.getClass());
				codec.encode(writer, obj, encoderContext);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		writer.writeEndDocument();
	}

	@Override
	public T decode(BsonReader reader, DecoderContext decoderContext) {
		
		T obj = null;
		TypeRegistry types = TypeRegistry.getInstance();
		CodecRegistry codecRegistry = types.getCodecRegistry();
		
		Map<String, Field> fieldMap = new HashMap<>();
		for(Field field : DataUtil.getFields(encoderClass)) {
			if(Modifier.isTransient(field.getModifiers())) continue;
			
			if(field.isAnnotationPresent(Id.class))
				fieldMap.put("_id", field);
			else
				fieldMap.put(field.getName(), field);
		}
		
		Object id = null;
		
		reader.readStartDocument();
		while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
			String name = reader.readName();
			Field field = fieldMap.get(name);
			
			if(name.equals("_type")) { // handle subtypes
				Class<? extends T> type = types.getSubtype(encoderClass, reader.readString());
				obj = types.getFactory(type).build();
				continue;
			}
			
			if(field == null) continue; // TODO: handle unknown case for id
			
			field.setAccessible(true);
			Class<?> fieldType = field.getType();
			if(fieldType.isPrimitive()) fieldType = DataUtil.getWrapper(fieldType);
			
			if(name.equals("_id")) {
				id = codecRegistry.get(fieldType).decode(reader, decoderContext);
				continue;
			}
			
			if(obj == null) obj = types.getFactory(encoderClass).build();
			
			try {
				Codec<?> codec = codecRegistry.get(fieldType);
				Object res = codec.decode(reader, decoderContext);
				field.set(obj, res);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//TODO handle subtypes
		}
		
		reader.readEndDocument();
		
		Field idField = fieldMap.get("_id");
		idField.setAccessible(true);
		try {
			idField.set(obj, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}

	@Override
	public Class<T> getEncoderClass() {
		return encoderClass;
	}
}

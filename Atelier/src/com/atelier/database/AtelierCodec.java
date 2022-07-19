package com.atelier.database;

import java.lang.reflect.Field;
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
		
		T obj = TypeRegistry.getInstance().getFactory(encoderClass).build();
		
		CodecRegistry codecRegistry = TypeRegistry.getInstance().getCodecRegistry();
		
		Map<String, Field> fieldMap = new HashMap<>();
		for(Field field : DataUtil.getFields(encoderClass)) {
			if(field.isAnnotationPresent(Id.class))
				fieldMap.put("_id", field);
			else
				fieldMap.put(field.getName(), field);
		}
		
		reader.readStartDocument();
		while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
			String name = reader.readName();
			Field field = fieldMap.get(name);
			if(name.equals("_id"))
			if(field == null) continue; // TODO: handle unknown case
			
			//TODO handle subtypes
			
			field.setAccessible(true);
			Class<?> fieldType = field.getType();
			if(fieldType.isPrimitive()) fieldType = DataUtil.getWrapper(fieldType);
			Codec<?> codec = codecRegistry.get(fieldType);
			try {
				field.set(obj, codec.decode(reader, decoderContext));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		reader.readEndDocument();
		
		return obj;
	}

	@Override
	public Class<T> getEncoderClass() {
		return encoderClass;
	}
}

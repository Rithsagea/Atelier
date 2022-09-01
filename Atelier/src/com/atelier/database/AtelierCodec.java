package com.atelier.database;

import java.io.IOException;
import java.util.Map.Entry;

import org.bson.BsonBinary;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class AtelierCodec<T> implements Codec<T> {

	private Class<T> type;
	private ObjectMapper mapper;
	
	public AtelierCodec(Class<T> type, ObjectMapper mapper) {
		this.type = type;
		this.mapper = mapper;
	}
	
	@Override
	public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
		ObjectNode node = mapper.valueToTree(value);
		writeNode(writer, node);
	}
	
	private void writeNode(BsonWriter writer, JsonNode node) {
		switch(node.getNodeType()) {
			case BINARY:
				try {
					writer.writeBinaryData(new BsonBinary(node.binaryValue()));
				} catch(Exception e) {
					e.printStackTrace();
					writer.writeNull();
				}
				break;
			case BOOLEAN:
				writer.writeBoolean(node.booleanValue());
				break;
			case NUMBER:
				switch(node.numberType()) {
					case DOUBLE:
					case FLOAT:
						writer.writeDouble(node.doubleValue());
						break;
					case INT:
						writer.writeInt32(node.intValue());
						break;
					case LONG:
						writer.writeInt64(node.longValue());
						break;
					case BIG_DECIMAL:
					case BIG_INTEGER:
					default:
						break;
				}
				break;
			case STRING:
				writer.writeString(node.textValue());
				break;
			case ARRAY:
				writer.writeStartArray();
				node.forEach(e -> writeNode(writer, e));
				writer.writeEndArray();
				break;
			case OBJECT:
				writer.writeStartDocument();
				node.fields().forEachRemaining((Entry<String, JsonNode> entry) -> {
					if(entry.getKey().equals("_id")) return; // don't serialize id
					writer.writeName(entry.getKey());
					writeNode(writer, entry.getValue());
				});
				writer.writeEndDocument();
				break;
			case MISSING:
			case POJO:
			case NULL:
			default:
				writer.writeNull();
				break;
		}
	}

	@Override
	public Class<T> getEncoderClass() {
		return type;
	}
	
	@Override
	public T decode(BsonReader reader, DecoderContext decoderContext) {
		JsonNode node = readNode(reader, BsonType.DOCUMENT);
		
		try {
			return mapper.readerFor(type).readValue(node);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private JsonNode readNode(BsonReader reader, BsonType type) {
		
		//TODO add more
		switch(type) {
			case INT32:
				return new IntNode(reader.readInt32());
			case INT64:
				return new LongNode(reader.readInt64());
			case STRING:
				return new TextNode(reader.readString());
			case BINARY:
				return new BinaryNode(reader.readBinaryData().getData());
			case ARRAY:
				reader.readStartArray();
				ArrayNode arr = mapper.createArrayNode();
				BsonType valueType;
				while((valueType = reader.readBsonType()) != BsonType.END_OF_DOCUMENT)
					arr.add(readNode(reader, valueType));
				reader.readEndArray();
				return arr;
			case DOCUMENT:
				reader.readStartDocument();
				ObjectNode obj = mapper.createObjectNode();
				while((valueType = reader.readBsonType()) != BsonType.END_OF_DOCUMENT)
					obj.set(reader.readName(), readNode(reader, valueType));
				reader.readEndDocument();
				return obj;
			default:
				throw new RuntimeException("Unsupported Type: " + type);
		}
	}
}

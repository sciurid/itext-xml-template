package me.chenqiang.pdf.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SerializableCloning {
	private  SerializableCloning() {
	}
	public static final Logger LOGGER = LoggerFactory.getLogger(SerializableCloning.class);
	public static void toOutputStream(Serializable obj, OutputStream bos) {
		try(ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(obj);
		} catch (IOException e) {
			LOGGER.error("Serialization Error", e);			
		} 
	}
	
	public static byte [] toBytes(Serializable obj) {
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			toOutputStream(obj, bos);
			return bos.toByteArray();
		} catch (IOException e) {
			LOGGER.error("Serialization Error", e);			
			throw new IllegalStateException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T fromInputStream(InputStream bis) {
		try(ObjectInputStream ois = new ObjectInputStream(bis)) {
			return (T)ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			LOGGER.error("De-serialization Error", e);				
			throw new IllegalStateException(e);		
		}
	}
	
	public static <T> T fromBytes(byte [] data) {
		try(ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
			return fromInputStream(bis);
		} catch (IOException e) {
			LOGGER.error("De-serialization Error", e);			
			throw new IllegalStateException(e);
		}
	}
}

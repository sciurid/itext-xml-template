package me.chenqiang.pdf.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface SerializableCloning {
	public static byte [] writeObject(Serializable obj) {
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			try(ObjectOutputStream oos = new ObjectOutputStream(bos)) {
				oos.writeObject(obj);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T readObject(byte [] data) {
		try(ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
			try(ObjectInputStream ois = new ObjectInputStream(bis)) {
				return (T)ois.readObject();
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}

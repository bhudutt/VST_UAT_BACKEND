package com.hitech.dms.app.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serializable tools (JDK)
 */
public class SerializableUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(SerializableUtil.class);

	/**
	 * Serialization
	 *
	 * @param object
	 * @return
	 */
	public static byte[] serializable(Object object) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			return baos.toByteArray();
		} catch (IOException e) {
			LOGGER.error("serializable: " + e.getMessage());
			throw new RuntimeException("serializable: " + e.getMessage());
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				LOGGER.error("serializable: " + e.getMessage());
				throw new RuntimeException("serializable: " + e.getMessage());
			}
		}
	}

	/**
	 * deserialize
	 *
	 * @param bytes
	 * @return
	 */
	public static Object unserializable(byte[] bytes) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException("unserializable: " + e.getMessage());
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (bais != null) {
					bais.close();
				}
			} catch (IOException e) {
				LOGGER.error("unserializable: " + e.getMessage());
				throw new RuntimeException("unserializable: " + e.getMessage());
			}
		}
	}

}

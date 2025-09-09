/**
 * 
 */
package com.hitech.dms.app.utils;

import java.security.SecureRandom;

/**
 * @author dinesh.jakhar
 *
 */
//@Component
public class RandomPasswordGenerator {

	// Method to generate a random alphanumeric password of a specific length
	public static String generateRandomPassword(int len, int randNumOrigin, int randNumBound) {
		SecureRandom random = new SecureRandom();
		return random.ints(randNumOrigin, randNumBound + 1)
				.filter(i -> Character.isAlphabetic(i) || Character.isDigit(i)).limit(len)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
	}

	public static void main(String[] args) {
		int len = 10;
		int randNumOrigin = 48, randNumBound = 122;

		System.out.println(generateRandomPassword(len, randNumOrigin, randNumBound));
	}
}

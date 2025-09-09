/**
 * 
 */
package com.hitech.dms.app.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.io.Files;

/**
 * @author dinesh.jakhar
 *
 */
public class CommonUtils {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	
	public static String getExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int extensionPos = filename.lastIndexOf("\'.\'");
		int lastUnixPos = filename.lastIndexOf("\'/\'");
		int lastWindowsPos = filename.lastIndexOf("\'\\\'");
		int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);

		int index = lastSeparator > extensionPos ? -1 : extensionPos;
		if (index == -1) {
			return "";
		} else {
			return filename.substring(index + 1);
		}
	}

	public static String extractExtension(String fileName) {
		return Arrays.stream(fileName.split("\\.")).reduce((a, b) -> b).orElse(null);
	}

	private static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}

	public static String drawDigitsFromString(String strValue) {
		String str = strValue.trim();
		String digits = "";
		for (int i = 0; i < str.length(); i++) {
			char chrs = str.charAt(i);
			if (Character.isDigit(chrs) && i < 1) {
				digits = digits + chrs;
				break;
			}
		}
		return digits;
	}

	private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		return pattern.matcher(strNum).matches();
	}

	public static String encodeHTML(String s) {
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if ((c >= 32 && c <= 46) || (c >= 58 && c <= 64) || (c >= 91 && c <= 96) || (c >= 123 && c <= 126)) {
				out.append("&#" + (int) c + ";");
			} else {
				out.append(c);
			}
		}
		return out.toString();
	}

	public static String GenerateRandomNumber(int charLength) {
		return String.valueOf(charLength < 1 ? 0
				: new Random().nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
						+ (int) Math.pow(10, charLength - 1));
	}

	public static <T> List<List<T>> chopped(List<T> list, final int L) {
		List<List<T>> parts = new ArrayList<List<T>>();
		final int N = list.size();
		for (int i = 0; i < N; i += L) {
			parts.add(new ArrayList<T>(list.subList(i, Math.min(N, i + L))));
		}
		return parts;
	}

	public static boolean isValidPassword(String password) {
		// Regex to check valid password.
		String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";

		// Compile the ReGex
		Pattern p = Pattern.compile(regex);

		// If the password is empty
		// return false
		if (password == null) {
			return false;
		}

		// Pattern class contains matcher() method
		// to find matching between given password
		// and regular expression.
		Matcher m = p.matcher(password);

		// Return if the password
		// matched the ReGex
		return m.matches();
	}

	public static String replaceValue(char oldVal, char newVal, String strVal) {
		if (strVal != null && !strVal.equals("")) {
			strVal = strVal.replace(oldVal, newVal);
		}
		return strVal;
	}

	public static void copyFile(String from, String to) throws IOException {
		Path src = Paths.get(from);
		Path dest = Paths.get(to);
		Files.copy(src.toFile(), dest.toFile());
	}

	public static void main(String[] args) {
		File file = new File("C:\\Users\\dinesh.jakhar\\Desktop\\ZgifjRoNRp.pdf");
		String ext = FilenameUtils.getExtension("C:\\Users\\dinesh.jakhar\\Desktop\\ZgifjRoNRp.pdf");
		System.out.println(ext);
		System.out.println("abs : " + getFileExtension(file));
		System.out.println(getExtension(file.getName()));
	}
	
	public static <T>List<List<T>> chopIntoParts( final List<T> ls, final int iParts ) {
	    final List<List<T>> lsParts = new ArrayList<List<T>>();
	    final int iChunkSize = ls.size() / iParts;
	    int iLeftOver = ls.size() % iParts;
	    int iTake = iChunkSize;
	    for( int i = 0, iT = ls.size(); i < iT; i += iTake ) {
	        if( iLeftOver > 0 ) {
	            iLeftOver--;
	            iTake = iChunkSize + 1;
	        } else {
	            iTake = iChunkSize;
	        }
	        lsParts.add( new ArrayList<T>( ls.subList( i, Math.min( iT, i + iTake ) ) ) );
	    }
	    return lsParts;
	}
	public static <T> List<T> jsonArrayToObjectList(String json, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
		List<T> ts = mapper.readValue(json, listType);
		logger.debug("class name: {}", ts.get(0).getClass().getName());
		return ts;
	}
}

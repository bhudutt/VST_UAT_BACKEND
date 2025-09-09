/**
 * 
 */
package com.hitech.dms.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;

import com.hitech.dms.app.security.util.AesUtil;
import com.hitech.dms.constants.AppConstants;

/**
 * @author dinesh.jakhar
 *
 */
public class CommonUtils {
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
		return Arrays.stream(fileName.split("\\.")).reduce((a,b) -> b).orElse(null);
	}

	private static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}

	public static String getFileExtensionFromString(String fileName) {
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

	public static boolean findNumberBetween(int i, int minValueInclusive, int maxValueInclusive) {
		return (i >= minValueInclusive && i <= maxValueInclusive);
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
//		com.google.common.io.Files.copy(src.toFile(), dest.toFile());
	}

	public static String decodeEncodedValue(String value) {
		if (value != null) {
			try {
				value = URLDecoder.decode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static String decryptValue(String encryptedValue) {
		String decryptedValue = null;
//		try {
//			encryptedValue = URLDecoder.decode(encryptedValue, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		encryptedValue = encryptedValue.toString();
		try {
			decryptedValue = new String(java.util.Base64.getDecoder().decode(encryptedValue));
			AesUtil aesUtil = new AesUtil(128, 1000);
			if (decryptedValue != null && decryptedValue.split("::").length == 3) {
				try {
					decryptedValue = aesUtil.decrypt(decryptedValue.split("::")[1], decryptedValue.split("::")[0],
							AppConstants.SECRET_KEY, decryptedValue.split("::")[2]);
				} catch (IllegalArgumentException ex) {
					ex.printStackTrace();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return decryptedValue;
	}

	public static <T> List<List<T>> chopIntoParts(final List<T> ls, final int iParts) {
		final List<List<T>> lsParts = new ArrayList<List<T>>();
		final int iChunkSize = ls.size() / iParts;
		int iLeftOver = ls.size() % iParts;
		int iTake = iChunkSize;
		for (int i = 0, iT = ls.size(); i < iT; i += iTake) {
			if (iLeftOver > 0) {
				iLeftOver--;
				iTake = iChunkSize + 1;
			} else {
				iTake = iChunkSize;
			}
			lsParts.add(new ArrayList<T>(ls.subList(i, Math.min(iT, i + iTake))));
		}
		return lsParts;
	}

	public static void copyDirectoryJavaNIO(Path source, Path target) throws IOException {

		// is this a directory?
		if (Files.isDirectory(source)) {

			// if target directory exist?
			if (Files.notExists(target)) {
				// create it
				Files.createDirectories(target);
				System.out.println("Directory created : " + target);
			}
			// list all files or folders from the source, Java 1.8, returns a stream
			// doc said need try-with-resources, auto-close stream
			try (Stream<Path> paths = Files.list(source)) {

				// recursive loop
				paths.forEach(p -> copyDirectoryJavaNIOWrapper(p, target.resolve(source.relativize(p))));

			}
		} else {
			// if file exists, replace it
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
			System.out.println(String.format("Copy File from \t'%s' to \t'%s'", source, target));
		}
	}

	// extract method to handle exception in lambda
	public static void copyDirectoryJavaNIOWrapper(Path source, Path target) {

		try {
			copyDirectoryJavaNIO(source, target);
		} catch (IOException e) {
			System.err.println("IO errors : " + e.getMessage());
		}

	}

	public static Date stringToDate(String value, String Format) {
		Date date = null;
		try {
			// "dd/MM/yyyy"
			date = new SimpleDateFormat(Format).parse(value);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return date;
	}

	public static <T> List<T> removeElements(List<T> l, Predicate<T> p) {
		// Removing nulls using Java Stream
		// using Predicate condition in lambda expression
		l = l.stream().filter(p).collect(Collectors.toList());

		// Return the list
		return l;
	}

	// Generic function to remove elements using Predicate
	public static <T> List<T> removeIfElements(List<T> l, Predicate<T> p) {

		// Removing nulls using Java Stream
		// using Predicate condition in removeIf()
		l.removeIf(x -> p.test(x));

		// Return the list
		return l;
	}

	// Generic function to remove elements using Predicate
	public static <T> List<T> removeElementsOtherThanPredicate(List<T> l, Predicate<T> p) {

		// Removing nulls or other conditions using Java Stream
		// using Predicate condition in removeIf()
		l.removeIf(x -> !p.test(x));

		// Return the list
		return l;
	}

	// Generic function to remove elements which are not start with Some Letter like
	// "A", "B" etc. using Predicate
	public static <T> List<T> filterElementsOtherThanPredicate(List<T> l, Predicate<T> p) {

		l.stream().filter(p.negate()).collect(Collectors.toList());
		// Return the list
		return l;
	}

	public static Calendar getCalendarForNow() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date());
		return calendar;
	}

	public static void setTimeToBeginningOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	public static void setTimeToEndofDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
	}

	public static Date getFirstDateOfCurrentMonth(Calendar cal) {
		if (cal == null) {
			cal = Calendar.getInstance();
		}
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	public static void deleteFilesOlderThanNDays(int days, String dirPath) throws IOException {
	    long cutOff = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000);
	    Files.list(Paths.get(dirPath))
	    .filter(path -> {
	        try {
	            return Files.isRegularFile(path) && Files.getLastModifiedTime(path).to(TimeUnit.MILLISECONDS) < cutOff;
	        } catch (IOException ex) {
	            // log here and move on
	            return false;
	        }
	    })
	    .forEach(path -> {
	        try {
	            Files.delete(path);
	        } catch (IOException ex) {
	            // log here and move on
	        }
	    });
	}
	
	public static void recursiveDeleteFilesOlderThanNDays(int days, String dirPath) throws IOException {
	    long cutOff = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000);
	    Files.list(Paths.get(dirPath))
	    .forEach(path -> {
	        if (Files.isDirectory(path)) {
	            try {
	                recursiveDeleteFilesOlderThanNDays(days, path.toString());
	            } catch (IOException e) {
	                // log here and move on
	            }
	        } else {
	            try {
	                if (Files.getLastModifiedTime(path).to(TimeUnit.MILLISECONDS) < cutOff) {
	                    Files.delete(path);
	                }
	            } catch (IOException ex) {
	                // log here and move on
	            }
	        }
	    });
	}

	public static void main(String[] args) {
		File file = new File("C:\\Users\\dinesh.jakhar\\Desktop\\ZgifjRoNRp.pdf");
		String ext = FilenameUtils.getExtension("C:\\Users\\dinesh.jakhar\\Desktop\\ZgifjRoNRp.pdf");
		System.out.println(ext);
		System.out.println("abs : " + getFileExtension(file));
		System.out.println(getExtension(file.getName()));
	}
}

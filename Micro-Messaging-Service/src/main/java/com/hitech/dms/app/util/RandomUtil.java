package com.hitech.dms.app.util;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {

    public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String numberChar = "0123456789";

    public static String UUID32() {
        String str = UUID.randomUUID().toString();
        return str.replaceAll("-", "");
    }

    public static String UUID36() {
        return UUID.randomUUID().toString();
    }

    /**
     * 
     *
     * @param length
     * @return 
     */
    public static String generateStr(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sb.toString();
    }

    /**
     * 
     *
     * @param length
     * @return 
     */
    public static String generateDigitalStr(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
        return sb.toString();
    }

    /**
     * 
     *
     * @param length
     * @return XetrWaYc
     */
    public static String generateLetterStr(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(letterChar.charAt(random.nextInt(letterChar.length())));
        }
        return sb.toString();
    }

    /**
     * 
     *
     * @param length
     * @return 如: nzcaunmk
     */
    public static String generateLowerStr(int length) {
        return generateLetterStr(length).toLowerCase();
    }

    /**
     * 
     *
     * @param length
     * @return  KZMQXSXW
     */
    public static String generateUpperStr(int length) {
        return generateLetterStr(length).toUpperCase();
    }

    /**
     * 
     *
     * @param length
     * @return  00000000
     */
    public static String generateZeroStr(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    /**
     * 
     *
     * @param num       
     * @param strLength 
     * @return  00000099
     */
    public static String generateStrWithZero(int num, int strLength) {
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if (strLength - strNum.length() >= 0) {
            sb.append(generateZeroStr(strLength - strNum.length()));
        } else {
            throw new RuntimeException("generateStrWithZero " + num + ", " + strLength + " !");
        }
        sb.append(strNum);
        return sb.toString();
    }

}

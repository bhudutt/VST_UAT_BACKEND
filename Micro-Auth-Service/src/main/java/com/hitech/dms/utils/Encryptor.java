package com.hitech.dms.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

//import sun.misc.BASE64Decoder;
/**
 * 
 * @author dinesh.jakhar
 *
 */
public class Encryptor {
	/**
     * 1. Generate a plain text for encryption
     * 2. Get a secret key (printed in hexadecimal form). In actual use this must 
     * by encrypted and kept safe. The same key is required for decryption.
     * 3. 
     */
    public static void main(String[] args) throws Exception {
        String plainText = "bhudutt";
        SecretKey secKey = getSecretEncryptionKey();
        byte[] cipherText = encryptText(plainText, secKey);
        String decryptedText = decryptText(cipherText, secKey);
        
        System.out.println("Original Text:" + plainText);
        System.out.println("AES Key (Hex Form):"+bytesToHex(secKey.getEncoded()));
        System.out.println("Encrypted Text (Hex Form):"+bytesToHex(cipherText));
        System.out.println("Descrypted Text:"+decryptedText);
        System.out.println(" : "+decryptText(hexTobytesx(bytesToHex(cipherText)), secKey));
        
    }
    
    /**
     * gets the AES encryption key. In your actual programs, this should be safely
     * stored.
     * @return secKey
     * @throws Exception 
     */
    public static SecretKey getSecretEncryptionKey() throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        return secKey;
    }
    
    /**
     * Encrypts plainText in AES using the secret key
     * @param plainText
     * @param secKey
     * @return byteCipherText
     * @throws Exception 
     */
    public static byte[] encryptText(String plainText,SecretKey secKey) throws Exception{
		// AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }
    
    /**
     * Decrypts encrypted byte array using the key used for encryption.
     * @param byteCipherText
     * @param secKey
     * @return string 
     * @throws Exception 
     */
    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
		// AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }
    
    /**
     * Decrypts encrypted byte array using the key used for encryption.
     * @param secKey
     * @return string 
     * @throws Exception 
     */
//    public static String decryptText(String encrptedText, SecretKey secKey) throws Exception {
//		// AES defaults to AES/ECB/PKCS5Padding in Java 7
//        Cipher aesCipher = Cipher.getInstance("AES");
//        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
//        BASE64Decoder base64decoder = new BASE64Decoder();
//        byte[] encrypedTextBytes = base64decoder.decodeBuffer(encrptedText);
//        byte[] bytePlainText = aesCipher.doFinal(encrypedTextBytes);
//        return new String(bytePlainText);
//    }
    
    /**
     * Convert a binary byte array into readable hex form
     * @param hash
     * @return hash
     */
    public static String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }
    
    public static byte[]  hexTobytesx(String hash) {
        return DatatypeConverter.parseHexBinary(hash);
    }
}

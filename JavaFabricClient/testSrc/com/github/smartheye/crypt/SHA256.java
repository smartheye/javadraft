package com.github.smartheye.crypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class SHA256 {

	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	public static byte[] getSalt() throws NoSuchAlgorithmException {
		// Always use a SecureRandom generator
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		// Create array for salt
		byte[] salt = new byte[16];
		// Get a random salt
		sr.nextBytes(salt);
		// return salt
		return salt;
	}

	public static byte[] hash(String str, byte[] salt) throws NoSuchAlgorithmException, NoSuchProviderException {
		byte[] src = null;
		try {
			src = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// unhandle it because UTF-8 always exists
			e.printStackTrace();
		}
		return hash(src, salt);
	}

	public static byte[] hash(byte[] src) {
		return hash(src, null);
	}

	public static byte[] hash(byte[] src, byte[] salt) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA256", "BC");
		} catch (NoSuchProviderException e) {
			try {
				md = MessageDigest.getInstance("SHA256");
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (salt != null && salt.length > 0) {
			md.update(salt);
		}
		md.update(src);
		return md.digest();
	}

}

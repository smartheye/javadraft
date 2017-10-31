package smart.github.com.cipher;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import com.lambdaworks.crypto.SCrypt;
import com.lambdaworks.crypto.SCryptUtil;

public class EthereumAccountTest {

	/**
	 * 将16进制字符串转换为byte[]
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] toBytes(String str) {
		if (str == null || str.trim().equals("")) {
			return new byte[0];
		}

		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr, 16);
		}

		return bytes;
	}
	
	public static String toHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		// Get complete hashed password in hex format
		return sb.toString();
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, GeneralSecurityException {
		String password = "testaccount";
		String strMac = "da136314eff398ce3231d5356113dcd7cb269a812beba0529c057d8f24cf2f2c";
		BigInteger mac = new BigInteger(strMac ,16);
		System.out.println("mac="+mac);
		String strIv = "a2a0eee875f6755fdd4c0347f02f4856";
		BigInteger iv = new BigInteger(strIv ,16);
		System.out.println("iv="+iv);
		String cipherText = "fa7948f7600d45040ba01010e0cfa599df9d910ca0ab378396f00f7547771d8c";
		BigInteger cipher = new BigInteger(cipherText ,16);
		System.out.println("cipher="+cipher);
		
		String strSalt = "7a651d5fc1bcc45a199ac943ace27b2ff4ab6c877147d97199d8bdfe33e9172b";
		BigInteger salt = new BigInteger(strSalt ,16);
		byte[] saltBytes = toBytes(strSalt);
		System.out.println("salt="+salt);
		
		BigInteger dkLen = BigInteger.valueOf(32L);
		String kdf = "scrypt";
		
		int n = 262144;
		int p = 1;
		int r = 8;
		
		//String hash = SCryptUtil.scrypt(password, n, r, p);
		
		//System.out.println(hash);
		
		byte[] hash = SCrypt.scrypt(password.getBytes("UTF-8"), saltBytes, n, r, p, dkLen.intValue());
		// exptected:fa7948f7600d45040ba01010e0cfa599df9d910ca0ab378396f00f7547771d8c
		// but was  :e09332fb0a5692c6475c507489a2f317d110d0bb1c2b15e3642931aa4bbc9d34
		System.out.println(toHex(hash));
	}
}

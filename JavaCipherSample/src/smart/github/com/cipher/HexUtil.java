package smart.github.com.cipher;

import java.math.BigInteger;

public class HexUtil {

	public static void main(String[] args) {
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
		System.out.println("salt="+salt);
		
		BigInteger dkLen = BigInteger.valueOf(32L);
		String kdf = "scrypt";
		
		int n = 262144;
		int p = 1;
		int r = 8;
	}
}

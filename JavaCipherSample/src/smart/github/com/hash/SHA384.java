package smart.github.com.hash;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

import org.spongycastle.jce.provider.BouncyCastleProvider;

public class SHA384 {

	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	static {
		if(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)==null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}
	
	private byte[] getSalt() throws NoSuchAlgorithmException
	{
	    //Always use a SecureRandom generator
	    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
	    //Create array for salt
	    byte[] salt = new byte[16];
	    //Get a random salt
	    sr.nextBytes(salt);
	    //return salt
	    return salt;
	}
	
	
	public byte[] hash(String str, byte[] salt) throws NoSuchAlgorithmException, NoSuchProviderException {
		byte[] src = null;
		try {
			src = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// unhandle it because UTF-8 always exists
			e.printStackTrace();
		}
		return hash(src, salt);
	}

	public byte[] hash(byte[] src, byte[] salt) throws NoSuchAlgorithmException, NoSuchProviderException {
		MessageDigest md = MessageDigest.getInstance("SHA384","SC");
		if(salt!=null && salt.length>0) {
			md.update(salt);
		}
		md.update(src);
		return md.digest();
	}
	
	public String toHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		// Get complete hashed password in hex format
		return sb.toString();
	}

    /** 
     * 方式一 
     *  
     * @param bytes 
     * @return
     * @deprecated 
     */
    public static String bytesToHexFun1(byte[] bytes)  
    {  
    	// 有问题的代码不要用
        /** 
         * 第一个参数的解释，记得一定要设置为1 
         *  signum of the number (-1 for negative, 0 for zero, 1 for positive). 
         */  
        BigInteger bigInteger = new BigInteger(1, bytes);  
        return bigInteger.toString(16);  
    }  
    
	/**
	 * 方法二： byte[] to hex string
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToHexFun2(byte[] bytes) {
		char[] buf = new char[bytes.length * 2];
		int index = 0;
		for (byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
			buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
			buf[index++] = HEX_CHAR[b & 0xf];
		}
		return new String(buf);
	}

	/**
	 * 方法三： byte[] to hex string
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToHexFun3(byte[] bytes) {
		StringBuilder buf = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) { // 使用String的format方法进行转换
			buf.append(String.format("%02x", new Integer(b & 0xff)));
		}
		return buf.toString();
	}

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

	public static void main(String[] args) throws Exception {
		String srcText = "password";
		SHA384 sha = new SHA384();
		byte[] salt = sha.getSalt();

		byte[] bytes = sha.hash(srcText,salt);
		System.out.println(sha.toHex(bytes));

		System.out.println(bytesToHexFun1(bytes));
		System.out.println(bytesToHexFun2(bytes));
		System.out.println(bytesToHexFun3(bytes));
		
		System.out.println("salt = "+bytesToHexFun2(salt));
	}
}

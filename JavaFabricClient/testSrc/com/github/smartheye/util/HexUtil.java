package com.github.smartheye.util;

public class HexUtil {

	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
	'f' };
	
	private HexUtil() {
		
	}
	
	/**
	 * 把字节数组转换为Hex字符�?
	 * 
	 * @param bytes 字节数组
	 * @return Hex字符�?
	 */
	public static String hexString(byte[] bytes) {
		char[] buf = new char[bytes.length * 2];
		int index = 0;
		for (byte b : bytes) { // 利用位运算进行转换，可以看作方法�?的变�?
			buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
			buf[index++] = HEX_CHAR[b & 0xf];
		}
		return new String(buf);
	}
}

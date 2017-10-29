package com.github.smartheye.bitcoin;

import java.math.BigInteger;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;

public class ECKeyGenMain2 {

	public static byte BITCOIN_VERSION=(byte)128; // 当前比特币version信息：128
	
	public static void main(String[] args) {

		ECKey eckey = new ECKey();
		String privateKeyAsHex = eckey.getPrivateKeyAsHex();
		BigInteger privateKeyInt = eckey.getPrivKey();
		byte[] privateKey = eckey.getPrivKeyBytes(); // 32位数组（支持压缩）

		String privateKeyStr = Base58.encode(privateKey);
		System.out.println("PrivateKey ByteLength is :" + privateKey.length);
		System.out.println("PrivateKey is :" + privateKeyStr);
		
		byte[] publicKey = eckey.getPubKey(); // 33位数组(压缩公钥)
		System.out.println("PublicKey ByteLength is :" + publicKey.length);
		System.out.println(eckey.getPublicKeyAsHex());
		
		byte[] hash160 = Utils.sha256hash160(publicKey);
		System.out.println(hash160.length);
		String address = toBase58(0,hash160);
		System.out.println("address:"+address);
	}

    /**
     * Returns the base-58 encoded String representation of this
     * object, including version and checksum bytes.
     */
    public final static String toBase58(int version, byte[] bytes) {
        // A stringified buffer is:
        //   1 byte version + data bytes + 4 bytes check code (a truncated hash)
        byte[] addressBytes = new byte[1 + bytes.length + 4];
        addressBytes[0] = (byte) version;
        System.arraycopy(bytes, 0, addressBytes, 1, bytes.length);
        byte[] checksum = Sha256Hash.hashTwice(addressBytes, 0, bytes.length + 1);
        System.arraycopy(checksum, 0, addressBytes, bytes.length + 1, 4);
        return Base58.encode(addressBytes);
    }
}

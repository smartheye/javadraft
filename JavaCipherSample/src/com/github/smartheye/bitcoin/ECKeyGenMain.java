package com.github.smartheye.bitcoin;


import org.bitcoinj.core.ECKey;
import org.spongycastle.math.ec.ECPoint;

public class ECKeyGenMain {

	public static byte BITCOIN_VERSION=(byte)128; // 当前比特币version信息：128
	
	public static void main(String[] args) {
		ECKey eckey = new ECKey();
		ECPoint point = eckey.getPubKeyPoint();
	}

}

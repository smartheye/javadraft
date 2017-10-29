package com.github.smartheye.bitcoin;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.Wallet;

public class BitCoinTest {

	public static void main(String[] args) {
		BriefLogFormatter.init();
		// testnet
		NetworkParameters params =TestNet3Params.get();//Network ID:0x6f
		// regnet
		// params = RegTestParams.get(); //Network ID:0x34
		// main net
		params = MainNetParams.get(); //Network ID:0x00
		ECKey key = new ECKey();
		System.out.println("We created a new key:\n" + key);

		Address addressFromKey = key.toAddress(params);
		System.out.println("Public Address generated: " + addressFromKey);

		System.out.println("Private key is: " + key.getPrivateKeyEncoded(params).toString());

		Wallet wallet = new Wallet(params);
		wallet.importKey(key);
		
		System.out.println("version="+params.getAddressHeader());
		
		// params.getAddressHeader() : 0
	}

}

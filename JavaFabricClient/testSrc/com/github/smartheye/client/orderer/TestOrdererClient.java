package com.github.smartheye.client.orderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.junit.Test;

import com.github.smartheye.client.orgnization.OrgnizationAdmin;
import com.github.smartheye.common.configtx.channel.CreateChannelRequest;
import com.google.common.io.Files;

public class TestOrdererClient {

	public JcaPEMKeyConverter newJcaPEMKeyConverter() {
		return new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
	}

	public PrivateKey privateKeyFromPEMFile(String privateKey) throws IOException {
		PEMParser pemParser = new PEMParser(new StringReader(privateKey));
		try {
			Object o = pemParser.readObject();
			if (o == null) {
				throw new IOException("Not an OpenSSL key");
			}
			if (o instanceof PEMKeyPair) {
				KeyPair kp = newJcaPEMKeyConverter().getKeyPair((PEMKeyPair) o);
				return kp.getPrivate();
			} else if (o instanceof PrivateKeyInfo) {
				// JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
				// .setProvider(SpongyCastleProvider.getInstance().getName());
				return newJcaPEMKeyConverter().getPrivateKey((PrivateKeyInfo) o);
			}
			throw new IOException("Not an OpenSSL key" + o);
		} finally {
			pemParser.close();
		}
	}

	public PrivateKey loadPeerAdmin() throws Exception {
		String privateKeyFile = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\users\\Admin@localhost.com\\msp\\keystore\\71a908e6ceb7f0cff8399db66b485f9cee2cceea7e17bd0d8931a1a31e7d27f3_sk";
		String signcertPemFile = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\users\\Admin@localhost.com\\msp\\signcerts\\Admin@localhost.com-cert.pem";

		OrgnizationAdmin admin = new OrgnizationAdmin();
		String privateKeyStr = new String(IOUtils.toByteArray(new FileInputStream(signcertPemFile)), "UTF-8");
		String certificateStr = new String(IOUtils.toByteArray(new FileInputStream(signcertPemFile)), "UTF-8");

		byte[] privateKeyBytes = Files.toByteArray(new File(privateKeyFile));
		PEMParser pemParser = new PEMParser(new StringReader(signcertPemFile));
		Object o = pemParser.readObject();
		if (o == null) {
			throw new IOException("Not an OpenSSL key");
		}
		if (o instanceof PEMKeyPair) {
			KeyPair kp = new JcaPEMKeyConverter().setProvider("SC").getKeyPair((PEMKeyPair) o);
			return kp.getPrivate();
		} else if (o instanceof PrivateKeyInfo) {
			// JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
			// .setProvider(SpongyCastleProvider.getInstance().getName());
			// return converter.getPrivateKey((PrivateKeyInfo) o);
		}
		return null;
	}

	@Test
	public void testConnectOrderer() throws Exception {
		// ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",
		// 17050).usePlaintext(true).build();
		// CallOptions callOpts = CallOptions.DEFAULT;
		HFClient hfcClient = HFClient.createNewInstance();

		hfcClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

		Properties ordererProperties = new Properties();
		ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[] { 5L, TimeUnit.MINUTES });
		ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[] { 8L, TimeUnit.SECONDS });
		Orderer solo = hfcClient.newOrderer("solo-orderer", "grpc://localhost:17050", ordererProperties);

		CreateChannelRequest createChannelRequest = new CreateChannelRequest("mychannel", "SampleConsortium",
				new String[] { "Org1MSP" });
		createChannelRequest.initialize();

		Channel newChannel = hfcClient.newChannel("mychannel", solo, createChannelRequest.getChannelConfiguration());

	}

}

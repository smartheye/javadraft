package com.github.smartheye.client.orderer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.ChannelConfiguration;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.junit.Test;

import com.github.smartheye.client.orgnization.OrgnizationAdmin;
import com.github.smartheye.client.orgnization.UserEnrollment;
import com.github.smartheye.common.configtx.channel.CreateChannelRequest;
import com.github.smartheye.crypt.PEMUtil;

public class TestGetChannel {

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

	private Set<String> getAdminRoles() {
		HashSet<String> roles = new HashSet<String>();
		roles.add("Admin");
		return Collections.unmodifiableSet(roles);
	}

	public OrgnizationAdmin loadPeerAdmin() throws Exception {
		String privateKeyFile = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\users\\Admin@localhost.com\\msp\\keystore\\71a908e6ceb7f0cff8399db66b485f9cee2cceea7e17bd0d8931a1a31e7d27f3_sk";
		String signcertPemFile = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\users\\Admin@localhost.com\\msp\\signcerts\\Admin@localhost.com-cert.pem";

		String privateKeyStr = new String(IOUtils.toByteArray(new FileInputStream(privateKeyFile)), "UTF-8");
		String certificateStr = new String(IOUtils.toByteArray(new FileInputStream(signcertPemFile)), "UTF-8");

		PrivateKey privateKey = PEMUtil.privateKeyFromPEM(privateKeyStr);

		UserEnrollment enrollment = new UserEnrollment(privateKey, certificateStr);

		OrgnizationAdmin admin = new OrgnizationAdmin();
		admin.setAccount("localhost.com.Admin");
		admin.setMspId("Org1MSP");
		admin.setEnrollment(enrollment);
		admin.setName("localhost.com.Admin");
		admin.setRoles(getAdminRoles());
		return admin;
	}

	@Test
	public void testConnectOrderer() throws Exception {
		HFClient hfcClient = HFClient.createNewInstance();
		hfcClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

		User peerAdmin = loadPeerAdmin();

		hfcClient.setUserContext(peerAdmin);

		Properties ordererProperties = new Properties();
		ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[] { 5L, TimeUnit.MINUTES });
		ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[] { 8L, TimeUnit.SECONDS });
		Orderer solo = hfcClient.newOrderer("solo-orderer", "grpc://localhost:17050", ordererProperties);

		CreateChannelRequest createChannelRequest = new CreateChannelRequest("mychannel", "SampleConsortium",
				new String[] { "Org1MSP" });
		createChannelRequest.initialize();
		ChannelConfiguration createChannelConfiguration = createChannelRequest.getChannelConfiguration();
		byte[] peerAdminSignature = hfcClient.getChannelConfigurationSignature(createChannelConfiguration, peerAdmin);

		// Channel newChannel = hfcClient.newChannel("mychannel", solo, createChannelRequest.getChannelConfiguration(),
		//		peerAdminSignature);
		Channel existChannel = hfcClient.newChannel("mychannel");
		existChannel.addOrderer(solo);
		existChannel.initialize();

		System.out.println(String.format("fetch existing channel: %s", existChannel.getName()));

	}

	@Test
	public void test1() {
		String str = String.format("create channel %s:", "abc");
		System.out.println(str);
	}
}

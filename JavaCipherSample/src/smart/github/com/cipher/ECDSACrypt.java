package smart.github.com.cipher;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Enumeration;

import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ParametersWithRandom;
import org.spongycastle.crypto.signers.ECDSASigner;
import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.jce.spec.ECParameterSpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class ECDSACrypt {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * PKCS1 -----BEGIN RSA PUBLIC KEY----- BASE64 ENCODED DATA -----END RSA PUBLIC
	 * KEY-----
	 * 
	 * PKCS8 -----BEGIN PUBLIC KEY----- BASE64 ENCODED DATA -----END PUBLIC KEY-----
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		KeyFactory fact = KeyFactory.getInstance("ECDSA", "SC");
		/*
		 * c2pnb272w1 c2tnb191v3 c2pnb208w1 c2tnb191v2 c2tnb191v1 prime192v3 c2tnb359v1
		 * prime192v2 prime192v1 c2tnb239v3 c2pnb163v3 c2tnb239v2 c2pnb163v2 c2tnb239v1
		 * c2pnb163v1 c2pnb176w1 prime256v1 c2pnb304w1 c2pnb368w1 c2tnb431r1 prime239v3
		 * prime239v2 prime239v1 sect283r1 sect283k1 sect163r2 secp256k1 secp160k1
		 * secp160r1 secp112r2 secp112r1 sect113r2 sect113r1 sect239k1 secp128r2
		 * sect163r1 secp128r1 sect233r1 sect163k1 sect233k1 sect193r2 sect193r1
		 * sect131r2 sect131r1 secp256r1 sect571r1 sect571k1 secp192r1 sect409r1
		 * sect409k1 secp521r1 secp384r1 secp224r1 secp224k1 secp192k1 secp160r2 B-163
		 * P-521 P-256 K-163 B-233 P-224 P-384 K-233 B-409 B-283 B-571 K-409 K-283 P-192
		 * K-571 brainpoolp512r1 brainpoolp384t1 brainpoolp256r1 brainpoolp192r1
		 * brainpoolp512t1 brainpoolp256t1 brainpoolp224r1 brainpoolp320r1
		 * brainpoolp192t1 brainpoolp160r1 brainpoolp224t1 brainpoolp384r1
		 * brainpoolp320t1 brainpoolp160t1 FRP256v1 wapip192v1 sm2p256v1
		 */
		Enumeration enums = ECNamedCurveTable.getNames();
		while (enums.hasMoreElements()) {
			String key = (String) enums.nextElement();
			// System.out.println(key);
		}
		// ECParameterSpec p256Curve = ECNamedCurveTable.getParameterSpec("P-256");
		ECParameterSpec p256Curve = ECNamedCurveTable.getParameterSpec("secp256k1");
		KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "SC");
		g.initialize(p256Curve, new SecureRandom());
		KeyPair pair = g.generateKeyPair();
		PublicKey publicKey = pair.getPublic();
		PrivateKey privateKey = pair.getPrivate();

		// PKCS8格式
		byte[] encodedPrivateKey = privateKey.getEncoded();
		// EC Private Key是PKCS1格式的私钥
		// PEMWriter class from BouncyCastle stores private key in PKCS#1 format
		String privKeyPem = PEMUtil.privateKeyToPEM(privateKey);
		System.out.println(privKeyPem);
		checkRecover(privateKey, privKeyPem);
		// privateKey.getEncoded() 是一个unencrypted PKCS#8 private key，所以不能加EC Private
		// Key而是Private Key
		String privKeyPemCustom = PEMUtil.generatePem("PRIVATE KEY", encodedPrivateKey);
		System.out.println(privKeyPemCustom);
		checkRecover(privateKey, privKeyPemCustom);
		// System.out.println("PrivateKey PEM is equal? " +
		// (privKeyPem.equals(privKeyPemCustom)));

		// 转换成PKCS#8格式的Private Key
		/*
		 * PKCS8EncodedKeySpec pkcs8EncodedPrivateKey= new
		 * PKCS8EncodedKeySpec(encodedPrivateKey); byte[]
		 * pkcs8EncodedPrivateKeyByte=pkcs8EncodedPrivateKey.getEncoded(); String
		 * privKeyPemECCustom = PEMUtil.generatePem("EC PRIVATE KEY",
		 * pkcs8EncodedPrivateKeyByte); System.out.println(privKeyPemECCustom);
		 * checkRecover(privateKey,privKeyPemECCustom);
		 */
		PKCS8EncodedKeySpec pkcs8EncodedPrivateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
		PrivateKey pkcs8PrivateKey = fact.generatePrivate(pkcs8EncodedPrivateKeySpec);
		String privKeyPem2 = PEMUtil.privateKeyToPEM(pkcs8PrivateKey);
		System.out.println(privKeyPem2);

		// Sign

		// JcaContentSignerBuilder csBuilder = new
		// JcaContentSignerBuilder("SHA256withECDSA").setProvider("SC");
		// ContentSigner signer = csBuilder.build(privateKey);

		Signature signer = Signature.getInstance("SHA256withECDSA","SC");
		signer.initSign(privateKey, new SecureRandom());
		signer.update("heyemessage".getBytes("UTF-8"));
		byte[] signature = signer.sign();
		String signatureHex = Base64.encode(signature);
		System.out.println(signatureHex);
		
		
		Signature verifier = Signature.getInstance("SHA256withECDSA","SC");
		verifier.initVerify(publicKey);
		verifier.update("heyemessage".getBytes("UTF-8"));
		boolean result = verifier.verify(Base64.decode(signatureHex));
		System.out.println("result is "+result);
		
		String pubKeyPEM = PEMUtil.publicKeyToPEM(publicKey);
		System.out.println(pubKeyPEM);
	}

	private static void checkRecover(PrivateKey privateKey, String pem) throws IOException {
		PrivateKey recoveredKey = PEMUtil.privateKeyFromPEM(pem);
		System.out.println("privateKey=" + privateKey);
		System.out.println("privateKey.getAlgorithm()=" + privateKey.getAlgorithm());
		System.out.println("\nrecoveredKey=" + privateKey);
		System.out.println("recoveredKey.getAlgorithm()=" + recoveredKey.getAlgorithm());
		System.out.println();
		if (privateKey.equals(recoveredKey)) {
			System.out.println("Key recovery ok");
		} else {
			System.err.println("Private key recovery failed");
		}
		if (privateKey.getAlgorithm().equals(recoveredKey.getAlgorithm())) {
			System.out.println("Key algorithm ok");
		} else {
			System.err.println("Key algorithms do not match");
		}
		System.out.println("\n\n\n");
	}
}

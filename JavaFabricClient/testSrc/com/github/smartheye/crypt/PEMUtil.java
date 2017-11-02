package com.github.smartheye.crypt;

import java.io.IOException;
import java.io.StringReader;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;

public class PEMUtil {

	public static PublicKey publickKeyFromPEM(String pemStr) throws IOException {
		StringReader reader = new StringReader(pemStr);
		PEMParser pemParser = new PEMParser(reader);
		try {
			Object o = pemParser.readObject();
			if (o == null) {
				throw new IOException("Not an OpenSSL key");
			}
			if (o instanceof PEMKeyPair) {
				KeyPair kp = SecurityManager.newJcaPEMKeyConverter().getKeyPair((PEMKeyPair) o);
				return kp.getPublic();
			}else if(o instanceof X509CertificateHolder) {
				X509CertificateHolder x509 = (X509CertificateHolder)o;
				return SecurityManager.newJcaPEMKeyConverter().getPublicKey(x509.getSubjectPublicKeyInfo());
			}
			throw new IOException("Not an OpenSSL key" + o);
		} finally {
			pemParser.close();
		}
	}

	public static PrivateKey privateKeyFromPEM(String pemStr) throws IOException {
		StringReader reader = new StringReader(pemStr);
		PEMParser pemParser = new PEMParser(reader);
		try {
			Object o = pemParser.readObject();
			if (o == null) {
				throw new IOException("Not an OpenSSL key");
			}
			if (o instanceof PEMKeyPair) {
				KeyPair kp = SecurityManager.newJcaPEMKeyConverter().getKeyPair((PEMKeyPair) o);
				return kp.getPrivate();
			} else if (o instanceof PrivateKeyInfo) {
				JcaPEMKeyConverter converter = SecurityManager.newJcaPEMKeyConverter();
				return converter.getPrivateKey((PrivateKeyInfo) o);
			} else if (o instanceof PKCS8EncryptedPrivateKeyInfo) {
				PKCS8EncryptedPrivateKeyInfo eki = (PKCS8EncryptedPrivateKeyInfo) o;
				return privateKeyFromEncryptedPEM(eki.getEncoded(), "1234");
			}
			throw new IOException("Not an OpenSSL key" + o);
		} finally {
			pemParser.close();
		}
	}

	public static PrivateKey privateKeyFromEncryptedPEM(byte[] keyBytes, String password) throws IOException {
		EncryptedPrivateKeyInfo encryptPKInfo = new EncryptedPrivateKeyInfo(keyBytes);
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(encryptPKInfo.getAlgName());
			PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
			SecretKeyFactory secFac = SecretKeyFactory.getInstance(encryptPKInfo.getAlgName());
			Key pbeKey = secFac.generateSecret(pbeKeySpec);
			AlgorithmParameters algParams = encryptPKInfo.getAlgParameters();
			cipher.init(Cipher.DECRYPT_MODE, pbeKey, algParams);
			KeySpec pkcs8KeySpec = encryptPKInfo.getKeySpec(cipher);
			KeyFactory kf = KeyFactory.getInstance("ECDSA");
			return kf.generatePrivate(pkcs8KeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
}

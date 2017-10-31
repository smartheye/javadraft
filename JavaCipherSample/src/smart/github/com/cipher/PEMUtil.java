package smart.github.com.cipher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

import org.ethereum.crypto.jce.SpongyCastleProvider;
import org.spongycastle.asn1.pkcs.PrivateKeyInfo;
import org.spongycastle.openssl.PEMKeyPair;
import org.spongycastle.openssl.PEMParser;
import org.spongycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.spongycastle.openssl.jcajce.JcaPEMWriter;
import org.spongycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.spongycastle.util.encoders.Base64;

import com.google.common.base.Splitter;

public class PEMUtil {

	private PEMUtil() {

	}

	public static String publicKeyToPEM(PublicKey publicKey) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));
		pemWriter.writeObject(publicKey);
		pemWriter.close();
		return new String(bos.toByteArray());
	}


	public static String privateKeyToPEM(PrivateKey privateKey) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));
		pemWriter.writeObject(privateKey);
		pemWriter.close();
		return new String(bos.toByteArray());
	}

	public static String generatePem(String type, byte[] encoded) {
		// CUSTOM Generate PEM
		String code = "-----BEGIN " + type + "-----\n";
		String codenew = new String(Base64.encode((encoded)));
		String myOutput = "";
		for (String substring : Splitter.fixedLength(64).split(codenew)) {
			myOutput += substring + "\n";
		}
		code += myOutput.substring(0, myOutput.length() - 1);
		code += "\n-----END " + type + "-----";
		return new String(code.getBytes());
	}

	public static PrivateKey privateKeyFromPEM(String der) throws IOException {
		StringReader reader = new StringReader(der);
		PEMParser pemParser = new PEMParser(reader);
		try {
			Object o = pemParser.readObject();
			if (o == null) {
				throw new IOException("Not an OpenSSL key");
			}
			if (o instanceof PEMKeyPair) {
				KeyPair kp = new JcaPEMKeyConverter().setProvider("SC").getKeyPair((PEMKeyPair) o);
				return kp.getPrivate();
			} else if (o instanceof PrivateKeyInfo) {
				JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
						.setProvider(SpongyCastleProvider.getInstance().getName());
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

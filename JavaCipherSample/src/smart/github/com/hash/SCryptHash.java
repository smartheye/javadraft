package smart.github.com.hash;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import com.lambdaworks.crypto.SCryptUtil;

public class SCryptHash {

	static {
		if(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)==null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}
	
	public String hash(String str) throws NoSuchAlgorithmException, NoSuchProviderException {
		// N : 16
		// r : 16
		// p : 16
		String hash = SCryptUtil.scrypt(str, 16, 16, 16);
		return hash;
	}

	public static void main(String[] args) throws Exception {
		String srcText = "helloworld";
		SCryptHash md5 = new SCryptHash();
		
		String result = md5.hash(srcText);
		System.out.println(result);
		
		boolean matched = SCryptUtil.check("helloworld", result);
        System.out.println(matched);
	}
}

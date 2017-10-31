package smart.github.com.hash;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import org.mindrot.jbcrypt.BCrypt;
import org.spongycastle.jce.provider.BouncyCastleProvider;

public class BCryptHash {

	static {
		if(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)==null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}
	
	public String hash(String str) throws NoSuchAlgorithmException, NoSuchProviderException {
		String hash = BCrypt.hashpw(str, BCrypt.gensalt());
		return hash;
	}

	public static void main(String[] args) throws Exception {
		String srcText = "helloworld";
		BCryptHash md5 = new BCryptHash();
		
		String result = md5.hash(srcText);
		System.out.println(result);
	}
}

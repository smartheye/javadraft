package com.github.smartheye.crypt;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

public class SecurityManager {

	static {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	public static final String getProviderName() {
		return BouncyCastleProvider.PROVIDER_NAME;
	}

	public static final JcaPEMKeyConverter newJcaPEMKeyConverter() {
		JcaPEMKeyConverter keyConverter = new JcaPEMKeyConverter();
		keyConverter.setProvider(getProviderName());
		return keyConverter;
	}
}

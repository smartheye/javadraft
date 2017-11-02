package com.github.smartheye.client.orgnization;

import java.io.Serializable;
import java.security.PrivateKey;

import org.hyperledger.fabric.sdk.Enrollment;

public class UserEnrollment implements Enrollment, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6677326291426893127L;

	private final PrivateKey key;
	private final String cert;

	public UserEnrollment(PrivateKey key, String cert) {
		super();
		this.key = key;
		this.cert = cert;
	}

	@Override
	public PrivateKey getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public String getCert() {
		// TODO Auto-generated method stub
		return cert;
	}

}

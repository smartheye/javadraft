package com.github.smartheye.client.orgnization;

import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

public class OrgnizationAdmin implements User{

	private String name;
	
	private String mspId;
	
    private String affiliation;
    
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	private String privateKey;
	
	private String cert;
	
	private boolean isPeerOrg;
	
	private boolean isOrdererOrg;

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}

	public boolean isPeerOrg() {
		return isPeerOrg;
	}

	public void setPeerOrg(boolean isPeerOrg) {
		this.isPeerOrg = isPeerOrg;
	}

	public boolean isOrdererOrg() {
		return isOrdererOrg;
	}

	public void setOrdererOrg(boolean isOrdererOrg) {
		this.isOrdererOrg = isOrdererOrg;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMspId(String mspId) {
		this.mspId = mspId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Set<String> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAffiliation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enrollment getEnrollment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMspId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

package com.github.smartheye.client.orgnization;

import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

public class OrgnizationAdmin implements User {

	private String name;

	private Set<String> roles;

	private String account;

	private Enrollment enrollment;

	private String mspId;

	public void setName(String name) {
		this.name = name;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	public void setMspId(String mspId) {
		this.mspId = mspId;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public Set<String> getRoles() {
		// TODO Auto-generated method stub
		return roles;
	}

	@Override
	public String getAccount() {
		// TODO Auto-generated method stub
		return account;
	}

	@Override
	public String getAffiliation() {
		// TODO Auto-generated method stub
		return mspId;
	}

	@Override
	public Enrollment getEnrollment() {
		// TODO Auto-generated method stub
		return enrollment;
	}

	@Override
	public String getMspId() {
		// TODO Auto-generated method stub
		return mspId;
	}

}

package com.github.smartheye.common.policies;

import org.hyperledger.fabric.protos.common.Configtx.ConfigPolicy;
import org.hyperledger.fabric.protos.common.Policies.ImplicitMetaPolicy;
import org.hyperledger.fabric.protos.common.Policies.Policy;
import org.hyperledger.fabric.protos.common.Policies.Policy.PolicyType;

public class ImplicitMetaPolicyUtil {

	//private ImplicitMetaPolicyUtil() {}
	

	/**
	 * build a config policy
	 * @param subPolicyName
	 * @param rule
	 * @return
	 */
	public static ConfigPolicy implicitConfigPolicy(String subPolicyName, ImplicitMetaPolicy.Rule rule, String modPolicy){
		Policy implicitMetaPolicy = implicitMetaPolicyWithSubPolicy(subPolicyName, rule);
		return ConfigPolicy.newBuilder().setModPolicy(modPolicy).setPolicy(implicitMetaPolicy).build();
	}
	
	/**
	 * ImplicitMetaPolicyWithSubPolicy creates an implicitmeta policy
	 * @param subPolicyName
	 * @param rule
	 * @return
	 */
	public static Policy implicitMetaPolicyWithSubPolicy(String subPolicyName, ImplicitMetaPolicy.Rule rule){
		ImplicitMetaPolicy implicitMetaPolicy = ImplicitMetaPolicy.newBuilder().setRule(rule).setSubPolicy(subPolicyName).build();
		return Policy.newBuilder().setType(PolicyType.IMPLICIT_META_VALUE).setValue(implicitMetaPolicy.toByteString()).build();
	}
	
}

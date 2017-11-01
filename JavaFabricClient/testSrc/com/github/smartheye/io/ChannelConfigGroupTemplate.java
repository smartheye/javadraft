package com.github.smartheye.io;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.hyperledger.fabric.protos.common.Common.ChannelHeader;
import org.hyperledger.fabric.protos.common.Common.Envelope;
import org.hyperledger.fabric.protos.common.Common.Header;
import org.hyperledger.fabric.protos.common.Common.HeaderType;
import org.hyperledger.fabric.protos.common.Common.Payload;
import org.hyperledger.fabric.protos.common.Common.SignatureHeader;
import org.hyperledger.fabric.protos.common.Configtx.ConfigGroup;
import org.hyperledger.fabric.protos.common.Configtx.ConfigPolicy;
import org.hyperledger.fabric.protos.common.Configtx.ConfigUpdate;
import org.hyperledger.fabric.protos.common.Configtx.ConfigUpdateEnvelope;
import org.hyperledger.fabric.protos.common.Configtx.ConfigValue;
import org.hyperledger.fabric.protos.common.Configuration.Consortium;
import org.hyperledger.fabric.protos.common.Policies.ImplicitMetaPolicy;
import org.hyperledger.fabric.protos.common.Policies.Policy;
import org.hyperledger.fabric.protos.common.Policies.Policy.PolicyType;

import com.github.smartheye.common.policies.ImplicitMetaPolicyUtil;
import com.github.smartheye.util.HexUtil;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;


public class ChannelConfigGroupTemplate {
	
	
	public Consortium newConsortium(String name){
		return Consortium.newBuilder().setName(name).build();
	}
	
	public ConfigPolicy implicitMetaPolicyWithSubPolicy(String subPolicyName, ImplicitMetaPolicy.Rule rule){
		ImplicitMetaPolicy implicitMetaPolicy = ImplicitMetaPolicy.newBuilder().setRule(rule).setSubPolicy(subPolicyName).build();
		Policy policy = Policy.newBuilder().setType(PolicyType.IMPLICIT_META_VALUE).setValue(implicitMetaPolicy.toByteString()).build();
		
		return ConfigPolicy.newBuilder().setPolicy(policy).build();
	}

	
	public Envelope newChannelCreateEnvelope() throws NoSuchAlgorithmException, NoSuchProviderException {
		java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
		long seconds = now.getTime()/1000;
		int nanos = now.getNanos();
		String txId = createTxID(new byte[0], new byte[0]);
		
		ChannelHeader createChannelHeader = ChannelHeader.newBuilder().setType(HeaderType.CONFIG_UPDATE_VALUE)
				.setVersion(0)
				.setTimestamp(Timestamp.newBuilder().setSeconds(seconds).setNanos(0).build())
				.setChannelId("mychannel")
				.setTxId(txId)
				.setEpoch(0L).build();

		SignatureHeader createChannelSignatureHeader = SignatureHeader.newBuilder().setCreator(ByteString.copyFrom(new byte[0]))
				.setNonce(ByteString.copyFrom(new byte[0]))
				.build();
		
		Header payloadHeader = Header.newBuilder()
				.setChannelHeader(createChannelHeader.toByteString())
				.setSignatureHeader(createChannelSignatureHeader.toByteString())
				.build();
		
		Payload payload = Payload.newBuilder().setHeader(payloadHeader)
		.setData(newChannelCreateConfigUpdateEnvelope().toByteString()).build();
		return Envelope.newBuilder().setPayload(payload.toByteString()).build();
	}
	
	public ConfigUpdateEnvelope newChannelCreateConfigUpdateEnvelope() {
		ConfigUpdate configUpdate = ConfigUpdate.newBuilder()
				.setChannelId("mychannel")
				.setReadSet(newDefaultReadSet())
				.setWriteSet(newDefaultWriteSet()).build();

		return ConfigUpdateEnvelope.newBuilder().setConfigUpdate(configUpdate.toByteString()).build();
	}
	
	public ConfigGroup newDefaultWriteSet(){
		
		ConfigPolicy adminsPolicy = ImplicitMetaPolicyUtil.implicitConfigPolicy("Admins", ImplicitMetaPolicy.Rule.MAJORITY,"Admins");
		ConfigPolicy writersPolicy = ImplicitMetaPolicyUtil.implicitConfigPolicy("Writers", ImplicitMetaPolicy.Rule.ANY,"Admins");
		ConfigPolicy readersPolicy = ImplicitMetaPolicyUtil.implicitConfigPolicy("Readers", ImplicitMetaPolicy.Rule.ANY,"Admins");
		
		ConfigValue consortiumValue = ConfigValue.newBuilder().setValue(newConsortium("SampleConsortium").toByteString()).build();
		ConfigGroup applicationConfigGroup = ConfigGroup.newBuilder()
				.setVersion(1)
				.setModPolicy("Admins")
				.putGroups("Org1MSP", ConfigGroup.getDefaultInstance())
				.putPolicies("Admins", adminsPolicy)
				.putPolicies("Writers", writersPolicy)
				.putPolicies("Readers", readersPolicy).build();
		
		ConfigGroup writeset = ConfigGroup.newBuilder().putGroups("Application", applicationConfigGroup)
				.putValues("Consortium", consortiumValue).build();
		return writeset;
	}
	
	public ConfigGroup newDefaultReadSet(){

		ConfigValue consortiumValue = ConfigValue.newBuilder().setValue(newConsortium("SampleConsortium").toByteString()).build();
		ConfigGroup applicationConfigGroup = ConfigGroup.newBuilder()
				.putGroups("Org1MSP", ConfigGroup.getDefaultInstance()).build();
		
		ConfigGroup writeset = ConfigGroup.newBuilder().putGroups("Application", applicationConfigGroup)
				.putValues("Consortium", consortiumValue).build();
		return writeset;
	}

//	@Test
//	public void testNewChannelCreateEnvelope() throws Exception{
//		Envelope envelop = newChannelCreateEnvelope();
//		Payload payload = Common.Payload.parseFrom(envelop.getPayload());
//		System.out.println("=============Envelop===========");
//		System.out.println("payload.size "+payload.toByteString().size());
//		System.out.println("payload.header.size "+payload.getHeader().toByteString().size());
//		System.out.println("payload.data.size "+payload.getData().size());
//		System.out.println(envelop);
//	}
	
	public String createTxID(byte[] nonce, byte[] creator) throws NoSuchAlgorithmException, NoSuchProviderException {
		ByteString nonceByteStr = ByteString.copyFrom(nonce);
		ByteString createorByteStr = ByteString.copyFrom(creator);
		ByteString message = nonceByteStr.concat(createorByteStr);
		MessageDigest md = MessageDigest.getInstance("SHA256");
		md.update(message.toByteArray());
		byte[] results = md.digest();
		// hex:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855
		return HexUtil.hexString(results);
	}
}

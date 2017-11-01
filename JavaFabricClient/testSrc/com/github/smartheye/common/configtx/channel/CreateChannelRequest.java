package com.github.smartheye.common.configtx.channel;

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
import org.hyperledger.fabric.sdk.ChannelConfiguration;

import com.github.smartheye.common.policies.ImplicitMetaPolicyUtil;
import com.github.smartheye.crypt.SHA256;
import com.github.smartheye.util.HexUtil;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;

public class CreateChannelRequest {

	private String channelId;

	private String consortiumName;

	private String[] orgNames;
	
	private ChannelConfiguration channelConfiguration;
	
	public CreateChannelRequest(String channelId, String consortium, String[] orgNames) {
		this.channelId = channelId;
		this.consortiumName = consortium;
		this.orgNames = orgNames;
	}
	
	public void initialize() {
		channelConfiguration = new ChannelConfiguration();
		Envelope channelCreateEnvelope = newChannelCreateEnvelope(channelId, consortiumName,orgNames);
		channelConfiguration.setChannelConfiguration(channelCreateEnvelope.toByteArray());
	}
	
	
	public ChannelConfiguration getChannelConfiguration(){
		return channelConfiguration;
	}
	
	public Envelope newChannelCreateEnvelope(String channelId,String consortiumName,String[] orgNames) {
		java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
		long seconds = now.getTime()/1000;
		// int nanos = now.getNanos();
		// no sigature
		String txId = createTxID(new byte[0], new byte[0]);
		
		ChannelHeader createChannelHeader = ChannelHeader.newBuilder().setType(HeaderType.CONFIG_UPDATE_VALUE)
				.setVersion(0)
				.setTimestamp(Timestamp.newBuilder().setSeconds(seconds).setNanos(0).build())
				.setChannelId(channelId)
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
		.setData(newChannelCreateConfigUpdateEnvelope(channelId,orgNames).toByteString()).build();
		return Envelope.newBuilder().setPayload(payload.toByteString()).build();
	}
	
	public ConfigUpdateEnvelope newChannelCreateConfigUpdateEnvelope(String channelId,String[] orgNames) {
		ConfigUpdate configUpdate = ConfigUpdate.newBuilder()
				.setChannelId(channelId)
				.setReadSet(newDefaultReadSet(channelId,orgNames))
				.setWriteSet(newDefaultWriteSet(channelId,orgNames)).build();

		return ConfigUpdateEnvelope.newBuilder().setConfigUpdate(configUpdate.toByteString()).build();
	}
	
	public ConfigGroup newDefaultWriteSet(String consortiumName,String[] orgNames){
		
		ConfigPolicy adminsPolicy = ImplicitMetaPolicyUtil.implicitConfigPolicy("Admins", ImplicitMetaPolicy.Rule.MAJORITY,"Admins");
		ConfigPolicy writersPolicy = ImplicitMetaPolicyUtil.implicitConfigPolicy("Writers", ImplicitMetaPolicy.Rule.ANY,"Admins");
		ConfigPolicy readersPolicy = ImplicitMetaPolicyUtil.implicitConfigPolicy("Readers", ImplicitMetaPolicy.Rule.ANY,"Admins");
		
		ConfigValue consortiumValue = ConfigValue.newBuilder().setValue(newConsortium(consortiumName).toByteString()).build();
		ConfigGroup.Builder applicationConfigGroupBuild = ConfigGroup.newBuilder()
				.setVersion(1)
				.setModPolicy("Admins")
				.putPolicies("Admins", adminsPolicy)
				.putPolicies("Writers", writersPolicy)
				.putPolicies("Readers", readersPolicy);
		for(String orgName:orgNames) {
			applicationConfigGroupBuild.putGroups(orgName, ConfigGroup.getDefaultInstance());
		}
		ConfigGroup applicationConfigGroup = applicationConfigGroupBuild.build();
		ConfigGroup writeset = ConfigGroup.newBuilder()
				.putGroups("Application", applicationConfigGroup)
				.putValues("Consortium", consortiumValue).build();
		return writeset;
	}
	
	public ConfigGroup newDefaultReadSet(String consortiumName,String[] orgNames){

		ConfigValue consortiumValue = ConfigValue.newBuilder().setValue(newConsortium(consortiumName).toByteString()).build();
		ConfigGroup.Builder applicationConfigGroupBuild = ConfigGroup.newBuilder();
		for(String orgName:orgNames) {
			applicationConfigGroupBuild.putGroups(orgName, ConfigGroup.getDefaultInstance());
		}
		ConfigGroup applicationConfigGroup = applicationConfigGroupBuild.build();
		ConfigGroup writeset = ConfigGroup.newBuilder().putGroups("Application", applicationConfigGroup)
				.putValues("Consortium", consortiumValue).build();
		return writeset;
	}
	
	public String createTxID(byte[] nonce, byte[] creator) {
		ByteString nonceByteStr = ByteString.copyFrom(nonce);
		ByteString createorByteStr = ByteString.copyFrom(creator);
		ByteString message = nonceByteStr.concat(createorByteStr);
		// hex:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855
		return HexUtil.hexString(SHA256.hash(message.toByteArray()));
	}
	
	
	public Consortium newConsortium(String name){
		return Consortium.newBuilder().setName(name).build();
	}
}

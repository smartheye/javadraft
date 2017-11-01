package com.github.smartheye.io;

import java.security.MessageDigest;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.common.Common.ChannelHeader;
import org.hyperledger.fabric.protos.common.Common.Envelope;
import org.hyperledger.fabric.protos.common.Common.Header;
import org.hyperledger.fabric.protos.common.Common.Payload;
import org.hyperledger.fabric.protos.common.Common.SignatureHeader;
import org.hyperledger.fabric.protos.common.Configtx.ConfigUpdate;
import org.hyperledger.fabric.protos.common.Configtx.ConfigUpdateEnvelope;
import org.hyperledger.fabric.protos.common.Configtx.ConfigValue;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.smartheye.util.HexUtil;
import com.google.common.io.ByteStreams;
import com.google.protobuf.ByteString;


public class GrpcMarshalTest {

	@BeforeClass
	public static void classSetup() throws Exception{
		Security.addProvider(new BouncyCastleProvider());
	}
	
	@Test
	public void testMarshalChannel() throws Exception {
		byte[] bytes = ByteStreams.toByteArray(GrpcMarshalTest.class.getResourceAsStream("channel.tx"));
		Envelope channeltx = Common.Envelope.parseFrom(bytes);
		ByteString payloadBytes = channeltx.getPayload();
		ByteString signatureBytes = channeltx.getSignature(); // signature is a zero byte array.
		
		// skip verify signature
		Payload payload = Common.Payload.parseFrom(payloadBytes);
		Header payloadHeader = payload.getHeader();
		
		ChannelHeader header = ChannelHeader.parseFrom(payload.getHeader().getChannelHeader());
		System.out.println(header.toByteArray().length);
		
		SignatureHeader signatureHeader = SignatureHeader.parseFrom(payload.getHeader().getSignatureHeader());
		
		System.out.println(header);
		System.out.println("version="+header.getVersion());
		System.out.println("epoch="+header.getEpoch());

		ConfigUpdateEnvelope configUpdateEnv = ConfigUpdateEnvelope.parseFrom(payload.getData());
		
		ConfigUpdate configUpdate = ConfigUpdate.parseFrom(configUpdateEnv.getConfigUpdate());
		System.out.println(configUpdate);
		//System.out.println(configUpdate.getChannelId());
		//System.out.println(configUpdate.getWriteSet());
		ConfigValue consortiumByte = configUpdate.getWriteSet().getValuesMap().get("Consortium");
		//System.out.println(Consortium.parseFrom(consortiumByte.getValue()));
		
		
		System.out.println("=============Envelop===========");
		System.out.println("payload.size "+payload.toByteString().size());
		System.out.println("payload.header.size "+payload.getHeader().toByteString().size());
		System.out.println("payload.data.size "+payload.getData().size());
		
		System.out.println("=============ConfigUpdateEnvelope===========");
		System.out.println(configUpdateEnv);
	}

	@Test
	public void testSHA256Nonce() throws Exception{
		ByteString nonce = ByteString.copyFrom(new byte[0]);
		ByteString createor = ByteString.copyFrom(new byte[0]);
		ByteString message = nonce.concat(createor);
		MessageDigest md = MessageDigest.getInstance("SHA256","BC");
		md.update(message.toByteArray());
		byte[] results = md.digest();
		// hex:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855
		System.out.println(HexUtil.hexString(results));
	}
}

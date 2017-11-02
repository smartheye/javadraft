package com.github.smartheye.crypt;

import java.io.FileInputStream;
import java.security.PublicKey;
import java.util.Date;

import javax.security.cert.X509Certificate;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import sun.misc.BASE64Encoder;

public class X509Cert {

	
	@Test
	public void testVerifyMSPCaCert() throws Exception {
		String caPEM = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\ca\\ca.localhost.com-cert.pem";
		String caCertificateStr = new String(IOUtils.toByteArray(new FileInputStream(caPEM)), "UTF-8");
		PublicKey caPublicKey = PEMUtil.publickKeyFromPEM(caCertificateStr);
		
		String folder = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\msp\\cacerts\\ca.localhost.com-cert.pem";
		X509Certificate cert = X509Certificate.getInstance(new FileInputStream(folder));  
		PublicKey publicKey = cert.getPublicKey();  
		BASE64Encoder base64Encoder=new BASE64Encoder();  
		String publicKeyString = base64Encoder.encode(publicKey.getEncoded());  
		System.out.println("-----------------提取出来的公钥--------------------");  
		System.out.println(publicKeyString);  
		System.out.println("-----------------提取出来的公钥--------------------");
		
		System.out.println("-----------------CA公钥--------------------");  
		System.out.println(base64Encoder.encode(caPublicKey.getEncoded()));  
		System.out.println("-----------------CA公钥--------------------");  
		
		cert.verify(caPublicKey);
		cert.checkValidity(new Date());
		System.out.println(cert);
	}
	
	@Test
	public void testVerifyMSPAdminCert() throws Exception {
		String caPEM = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\ca\\ca.localhost.com-cert.pem";
		String caCertificateStr = new String(IOUtils.toByteArray(new FileInputStream(caPEM)), "UTF-8");
		PublicKey caPublicKey = PEMUtil.publickKeyFromPEM(caCertificateStr);
		
		String folder = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\msp\\admincerts\\Admin@localhost.com-cert.pem";
		X509Certificate cert = X509Certificate.getInstance(new FileInputStream(folder));  
		PublicKey publicKey = cert.getPublicKey();  
		BASE64Encoder base64Encoder=new BASE64Encoder();  
		String publicKeyString = base64Encoder.encode(publicKey.getEncoded());  
		System.out.println("-----------------提取出来的公钥--------------------");  
		System.out.println(publicKeyString);  
		System.out.println("-----------------提取出来的公钥--------------------");
		
		System.out.println("-----------------CA公钥--------------------");  
		System.out.println(base64Encoder.encode(caPublicKey.getEncoded()));  
		System.out.println("-----------------CA公钥--------------------");  
		
		cert.verify(caPublicKey);
		cert.checkValidity(new Date());
		System.out.println(cert.getSubjectDN().getName());
		System.out.println(cert);
	}
	
	@Test
	public void testVerifyTLSCert() throws Exception {
		String caPEM = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\tlsca\\tlsca.localhost.com-cert.pem";
		String caCertificateStr = new String(IOUtils.toByteArray(new FileInputStream(caPEM)), "UTF-8");
		PublicKey caPublicKey = PEMUtil.publickKeyFromPEM(caCertificateStr);
		
		String folder = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\msp\\tlscacerts\\tlsca.localhost.com-cert.pem";
		X509Certificate cert = X509Certificate.getInstance(new FileInputStream(folder));  
		PublicKey publicKey = cert.getPublicKey();  
		BASE64Encoder base64Encoder=new BASE64Encoder();  
		String publicKeyString = base64Encoder.encode(publicKey.getEncoded());  
		System.out.println("-----------------提取出来的公钥--------------------");  
		System.out.println(publicKeyString);  
		System.out.println("-----------------提取出来的公钥--------------------");
		
		System.out.println("-----------------CA公钥--------------------");  
		System.out.println(base64Encoder.encode(caPublicKey.getEncoded()));  
		System.out.println("-----------------CA公钥--------------------");  
		
		cert.verify(caPublicKey);
		cert.checkValidity(new Date());
		System.out.println(cert.getSubjectDN().getName());
		//System.out.println(cert);
	}
	
	@Test
	public void testVerifyPeerOrg1MSPSignCert() throws Exception {
		//String caPEM = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\ca\\ca.localhost.com-cert.pem";
		String caPEM = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\msp\\cacerts\\ca.localhost.com-cert.pem";
		String caCertificateStr = new String(IOUtils.toByteArray(new FileInputStream(caPEM)), "UTF-8");
		PublicKey caPublicKey = PEMUtil.publickKeyFromPEM(caCertificateStr);
		
		String folder = "C:\\etc\\hyperledger\\fabric\\crypto-config\\peerOrganizations\\localhost.com\\peers\\peer.localhost.com\\msp\\cacerts\\ca.localhost.com-cert.pem";
		X509Certificate cert = X509Certificate.getInstance(new FileInputStream(folder));  
		PublicKey publicKey = cert.getPublicKey();  
		BASE64Encoder base64Encoder=new BASE64Encoder();  
		String publicKeyString = base64Encoder.encode(publicKey.getEncoded());  
		System.out.println("-----------------提取出来的公钥--------------------");  
		System.out.println(publicKeyString);  
		System.out.println("-----------------提取出来的公钥--------------------");
		
		System.out.println("-----------------CA公钥--------------------");  
		System.out.println(base64Encoder.encode(caPublicKey.getEncoded()));  
		System.out.println("-----------------CA公钥--------------------");  
		
		cert.verify(caPublicKey);
		cert.checkValidity(new Date());
		System.out.println(cert.getSubjectDN().getName());
		//System.out.println(cert);
	}
}

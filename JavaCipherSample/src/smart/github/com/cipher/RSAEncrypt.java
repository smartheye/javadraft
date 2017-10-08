package smart.github.com.cipher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;

// http://cache.baiducontent.com/c?m=9d78d513d99b12eb0bfa950e1a67a471695f97153dc0a11468d5e315d13307000638f4bb56356101c4b6777070ae5e289ae47132690c7af1dd8a9f4baea68f7871d57223706bd700448f0eafbc1a72873dd811acac12edbff33091abcf809f12089b0d5267d6a7cd095b4a8b72f6103ae0a59f4c10&p=977ac54ad6c345f546b5c7710f7abb&newp=882a9545d2855fe90be2962b490589231610db2151d6d61e6b82c825d7331b001c3bbfb42324140fd1c57c6300a44d5bebf13c7837092ba3dda5c91d9fb4c57479ce3972&user=baidu&fm=sc&query=java%CA%B9%D3%C3openssl+%C7%A9%C3%FB&qid=9a76178f000082dc&p1=1
public class RSAEncrypt {

    /**
     * �ֽ�����ת�ַ���ר�ü���
     */
    private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    
	/*
DEFAULT_PUBLIC_KEY Ϊrsa_public_key.pem���ݡ���ȥ���ո�ͻ��з���ͷβ�ġ��CPUBLIC KEY���C�� 
DEFAULT_PRIVATE_KEY ����Ϊpkcs8_rsa_private_key.pem�����ݣ���ʽ����ͬ�ϡ�
	 */
	private final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8GpTZDi7ZdmhdGRk/LrpfuYCPFYg+0dgqM8cI/ExzmVsvi4TdTrt5HHXtJ3BozFKEdLTxnd15XjGihOWCfZPIGs42vGCOM0J9vl72viB9pZNSWS2kaBx8HbwGIGk6Y7bhJBSmZQWyyADOAFIKLqNGeF30MedINRkosaIB47NgUQIDAQAB";
	//private final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxrbbBo9Zkd4uQ16/cx0LJwUJpUHAYHRnSwM8uPgoRkPEXXRFR9sWvZ3WAFdjSB/3KSd8LAK34P8sq49BouUxCJw0h6o2uv0wKIy70k3mTc4KVF12iClPfZR1fQ3FmJrOPr35AiuZTz+YsMp24WageIVz3VZ7qYmzMZ8dH0Ez+ZQIDAQAB";

	
	private final String DEFAULT_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALwalNkOLtl2aF0ZGT8uul+5gI8ViD7R2Cozxwj8THOZWy+LhN1Ou3kcde0ncGjMUoR0tPGd3XleMaKE5YJ9k8gazja8YI4zQn2+Xva+IH2lk1JZLaRoHHwdvAYgaTpjtuEkFKZlBbLIAM4AUgouo0Z4XfQx50g1GSixogHjs2BRAgMBAAECgYEAoMD2xCji0OXoeuegM+bzUCtwOtfkAAvF+QsW5g74JzLAQvmxKpDGltJD1dEGuAkl3B0BJhwL0lU1hfadTc+UIs/mryPdacgLxn/7S/V+68rMj5M/lwlu7uLcucTAgR2vkG4sUgf1C3T/zg/vUfbMzaqJ3/NcGKsgYvkvhng0PiECQQDe4675AAG2qUw94ZvW6kgV3miJ2/BOIphni0oyDzN9oOFHDML4kXtB4Qpamfyjjtiufbd24ElNN7TrLS/IUHRHAkEA2AwHg4xdqDJNfpR7xcbusC0Fv06VvzEHpebiwfMtC6xCNHBbahTWLOM7sgRk+06fMUmJWXM/9erSZsBLgg1KpwJAAtp5V/i3DOaX8kbcHuE68QS64+Dr76QjvVecKI3nQ9l2aNlx+YqrQEmoP0avZEyk238ChUQTzChFaalXbkIRawJACphcbfkeRrdg0Ypan5Jn9j/e/kjOUzgGVf6CtFxGo0HpZUT1Vrh64GysasiIY0Kxz+r5T2e1hUvk0aEwksowFQJATcloiJk1w/e2IpbvTUqRlWxZqh2YA0txLD5+b+1BplDE7MaoU7eXTdKAD623qnnrhohIxHaSareA94IgK2mDzg==";
	//private final String DEFAULT_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALGttsGj1mR3i5DXr9zHQsnBQmlQcBgdGdLAzy4+ChGQ8RddEVH2xa9ndYAV2NIH/cpJ3wsArfg/yyrj0Gi5TEInDSHqja6/TAojLvSTeZNzgpUXXaIKU99lHV9DcWYms4+vfkCK5lPP5iwynbhZqB4hXPdVnupibMxnx0fQTP5lAgMBAAECgYBkxQH+pt4kEszb9Afwri5J4Mq6kf1RH8gSiHm4X0U0m0CxIH5/Pvb1OTch0sIqtI+Sl/zg75y7S+r9Q0Fi85y0ghkbLFmmZgv8bGzh4k34WWFCyPMRmexOhsi+IGpTOYkbRnJbycxuY0TXJHHYvEcS0I14fUvI8gbflf12l6DCwQJBANoPXAjHF4TQm0h+qTyFmB4NoGlV3DNH2yk2D2UlKJ+9o7YqjMmw/jNQyeNb65Dj62ij57FQrbn4yfHLBWlkxnECQQDQl7g4kNtDzh0eMMcZYVKU7W/eEkd/G8WeldH26RWE1kZzYBIHeQJiujASDFodM3pqbJ3ssOCizoN1Acy+LPk1AkBvBXjvQyZXSbp1238gwEflrTxpxPw646/SuKJ39cZMJkpu2hUaL1INIGnZpxg1icV2XlT9cz0wPVncEGit81ARAkBkhrnhiO4i31JpGljZgm2qGEOxYl3ShC/ZHZryw6H7QsjxEXue3lKoAdLo38sOB6EXaQEy8ItR6vSvjw59GG+BAkBWwm0ObgH9LJ4eF3ZLCLvViScML3QJr2vAOh3GBsa1avWu/ffD5e3xZGdpmkfEMxWW8nTErFAL46xn8XBLsZqs";

    /**
     * ���������Կ��
     */
    public void genKeyPair() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //this.publicKey = (RSAPublicKey) keyPair.getPublic();
    }
    
    /**
     * rsaǩ��
     * 
     * @param content
     *            ��ǩ�����ַ���
     * @param privateKey
     *            rsa˽Կ�ַ���
     * @param charset
     *            �ַ�����
     * @return ǩ�����
     * @throws Exception
     *             ǩ��ʧ�����׳��쳣
     */
    public byte[] rsaSign(String content, RSAPrivateKey priKey)
            throws SignatureException {
        try {

            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(priKey);
            signature.update(content.getBytes("utf-8"));

            byte[] signed = signature.sign();
            return signed;
        } catch (Exception e) {
            throw new SignatureException("RSAcontent = " + content
                    + "; charset = ", e);
        }
    }
    
    /**
     * rsa��ǩ
     * 
     * @param content
     *            ��ǩ��������
     * @param sign
     *            ǩ����Ľ��
     * @param publicKey
     *            rsa��Կ
     * @param charset
     *            �ַ���
     * @return ��ǩ���
     * @throws SignatureException
     *             ��ǩʧ�ܣ������쳣
     */
    boolean doCheck(String content, byte[] sign, RSAPublicKey pubKey)
            throws SignatureException {
        try {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));
            return signature.verify((sign));
        } catch (Exception e) {
            throw new SignatureException("RSA��֤ǩ��[content = " + content
                    + "; charset = " + "; signature = " + sign + "]�����쳣!", e);
        }
    }

    
	/**
     * ���ܹ���
     * 
     * @param publicKey
     *            ��Կ
     * @param plainTextData
     *            ��������
     * @return
     * @throws Exception
     *             ���ܹ����е��쳣��Ϣ
     */
    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData)
            throws Exception {
        if (publicKey == null) {
            throw new Exception("���ܹ�ԿΪ��, ������");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("�޴˼����㷨");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("���ܹ�Կ�Ƿ�,����");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("���ĳ��ȷǷ�");
        } catch (BadPaddingException e) {
            throw new Exception("������������");
        }
    }

    /**
     * ���ܹ���
     * 
     * @param privateKey
     *            ˽Կ
     * @param cipherData
     *            ��������
     * @return ����
     * @throws Exception
     *             ���ܹ����е��쳣��Ϣ
     */
    public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData)
            throws Exception {
        if (privateKey == null) {
            throw new Exception("����˽ԿΪ��, ������");
        }
        Cipher cipher = null;
        try {
            // , new BouncyCastleProvider()
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("�޴˽����㷨");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("����˽Կ�Ƿ�,����");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("���ĳ��ȷǷ�");
        } catch (BadPaddingException e) {
            throw new Exception("������������");
        }
    }
	
	public RSAPublicKey loadPublicKey(String pubKey) throws Exception{
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = base64Decoder.decodeBuffer(pubKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("�޴��㷨");
        } catch (InvalidKeySpecException e) {
            throw new Exception("��Կ�Ƿ�");
        } catch (IOException e) {
            throw new Exception("��Կ�������ݶ�ȡ����");
        } catch (NullPointerException e) {
            throw new Exception("��Կ����Ϊ��");
        }
	}
	
    public RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory
                    .generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("�޴��㷨");
        } catch (InvalidKeySpecException e) {
            throw new Exception("˽Կ�Ƿ�");
        } catch (IOException e) {
            throw new Exception("˽Կ�������ݶ�ȡ����");
        } catch (NullPointerException e) {
            throw new Exception("˽Կ����Ϊ��");
        }
    }
	
    /**
     * �ֽ�����תʮ�������ַ���
     * 
     * @param data
     *            ��������
     * @return ʮ����������
     */
    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            // ȡ���ֽڵĸ���λ ��Ϊ�����õ���Ӧ��ʮ�����Ʊ�ʶ�� ע���޷�������
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            // ȡ���ֽڵĵ���λ ��Ϊ�����õ���Ӧ��ʮ�����Ʊ�ʶ��
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1) {
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }

    // btyeת��hex����
    public static String ByteToHex(byte[] byteArray) {
        StringBuffer StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            } else {
                StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return StrBuff.toString();
    }

    /**
     * ���ֽ�Ϊ��λ��ȡ�ļ��������ڶ��������ļ�����ͼƬ��������Ӱ����ļ���
     */
    public static byte[] readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        byte[] txt = new byte[(int) file.length()];
        try {
            // һ�ζ�һ���ֽ�
            in = new FileInputStream(file);
            int tempbyte;
            int i = 0;

            while ((tempbyte = in.read()) != -1) {
                txt[i] = (byte) tempbyte;
                i++;
            }
            in.close();
            return txt;
        } catch (IOException e) {
            e.printStackTrace();
            return txt;
        }
    }
    
	public static void main(String[] args) {
		RSAEncrypt rsaEncrypt = new RSAEncrypt();
        // RsaEncrypt.genKeyPair();
		RSAPublicKey publicKey = null;
		RSAPrivateKey privateKey = null;
        // ���ع�Կ
        try {
        	publicKey = rsaEncrypt.loadPublicKey(rsaEncrypt.DEFAULT_PUBLIC_KEY);
            System.out.println("���ع�Կ�ɹ�");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("���ع�Կʧ��");
        }

        // ����˽Կ
        try {
        	privateKey=rsaEncrypt.loadPrivateKey(rsaEncrypt.DEFAULT_PRIVATE_KEY);
            System.out.println("����˽Կ�ɹ�");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("����˽Կʧ��");
        }

     // �����ַ���
        String encryptStr = "1234567890";
        
        try {

            System.out.println(new Date());
            // ����
            byte[] cipher = rsaEncrypt.encrypt(publicKey,
                    encryptStr.getBytes());
            // ����
            byte[] plainText = rsaEncrypt.decrypt(privateKey,
                    cipher);
            System.out.println(new Date());
            // System.out.println("���ĳ���:"+ cipher.length);
            // System.out.println(RsaEncrypt.byteArrayToString(cipher));
            // System.out.println("���ĳ���:"+ plainText.length);
            // System.out.println(RsaEncrypt.byteArrayToString(plainText));
            System.out.println(new String(plainText));

            // ǩ����֤
            byte[] signbyte = rsaEncrypt.rsaSign("helloadsfdsfaasdfasffffffffffffdsafdasfasdfsafzxvxzvasfs",
            		privateKey);
            System.out.println("ǩ��-----��" + ByteToHex(signbyte));
            Boolean isok = rsaEncrypt.doCheck("helloadsfdsfaasdfasffffffffffffdsafdasfasdfsafzxvxzvasfs", signbyte,
            		publicKey);
            System.out.println("��֤��" + isok);

            // ��ȡ��֤�ļ�
            byte[] read = readFileByBytes("C:/openssl-1.0.2c-static-x86/bin/rsasign.bin");
            System.out.println("��ȡǩ���ļ���" + ByteToHex(read));
            Boolean isfok = rsaEncrypt.doCheck("helloadsfdsfaasdfasffffffffffffdsafdasfasdfsafzxvxzvasfs", read,
            		publicKey);
            System.out.println("�ļ���֤��" + isfok);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
	}

}

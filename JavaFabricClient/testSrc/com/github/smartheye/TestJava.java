package com.github.smartheye;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestJava {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOsDetectedArch() {
		// ${os.detected.classifier} ��ֵ���� ${os.detected.name}-${os.detected.arch}  ���ɵ�
		/* ${os.detected.name}��ֵ����${os.name}ת�������ġ�ת��������������
		   ${os.detected.arch}��ֵ����${os.arch}ת�������ģ�ת������Ҳ��������
		   https://github.com/trustin/os-maven-plugin
		   
		   */
		System.out.println(System.getProperty("os.name"));
		System.out.println(System.getProperty("os.arch"));
		// Windows 7    amd64  ��Ҫ��ת��Ϊ��windows-x86_64
	}

}

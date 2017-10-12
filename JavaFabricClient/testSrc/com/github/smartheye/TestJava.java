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
		// ${os.detected.classifier} 的值是由 ${os.detected.name}-${os.detected.arch}  构成的
		/* ${os.detected.name}的值是由${os.name}转化而来的。转化规则在链接中
		   ${os.detected.arch}的值是由${os.arch}转化而来的，转化规则也在链接中
		   https://github.com/trustin/os-maven-plugin
		   
		   */
		System.out.println(System.getProperty("os.name"));
		System.out.println(System.getProperty("os.arch"));
		// Windows 7    amd64  需要被转换为：windows-x86_64
	}

}

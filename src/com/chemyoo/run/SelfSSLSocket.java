package com.chemyoo.run;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;

/** 
 * @author Author : jianqing.liu
 * @version version : created time��2018��12��20�� ����9:32:35 
 * @since since from 2018��12��20�� ����9:32:35 to now.
 * @description class description
 */
public class SelfSSLSocket {
	
	private SelfSSLSocket() {}
	
	
	public static SSLSocketFactory getSSLSocketFactory() {
		SSLSocketFactory factory = null;
		try {
			TrustManager[] trust = { new IgnoreX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			// ������SSLContext�����еõ�SSLSocketFactory����
			sslContext.init(null, trust, new SecureRandom());
			factory = sslContext.getSocketFactory();
		} catch (KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException e) {
			Logger.getLogger(SelfSSLSocket.class).error(e.getMessage(), e);
		}
		return factory;
	}
	
}

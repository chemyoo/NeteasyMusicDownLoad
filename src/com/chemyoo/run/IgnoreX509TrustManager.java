package com.chemyoo.run;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/** 
 * @author Author : jianqing.liu
 * @version version : created time：2018年12月20日 上午9:28:17 
 * @since since from 2018年12月20日 上午9:28:17 to now.
 * @description class description
 */
public class IgnoreX509TrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		// ignore
	}

	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		// ignore
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// ignore
		return new X509Certificate[]{};
	}

}

package com.gedoumi.wx.bean;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 *  证书信任管理器（用于https请求）
 * @ClassName: MyX509TrustManager  
 * @Description: TODO 
 * @author: zengmin 
 * @date:2017年3月29日 上午10:35:52
 */
public class MyX509TrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}
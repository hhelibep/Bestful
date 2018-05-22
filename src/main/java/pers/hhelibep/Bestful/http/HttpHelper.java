package pers.hhelibep.Bestful.http;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pers.hhelibep.Bestful.core.AuthType;
import pers.hhelibep.Bestful.util.CommonUtils;
import pers.hhelibep.Bestful.util.Properties;

public class HttpHelper {

	private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);
	private static HttpClientBuilder builder = HttpClientBuilder.create();
	private static CloseableHttpClient client = null;
	// set default request config
	static {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(Integer.parseInt(Properties.getValue("connectionRequestTimeout")))
				.setSocketTimeout(Integer.parseInt(Properties.getValue("socketTimeout")))
				.setConnectTimeout(Integer.parseInt(Properties.getValue("connectTimeout"))).build();
		builder.setDefaultRequestConfig(requestConfig);
	}

	/**
	 * @return the standalone httpclient
	 */
	public static CloseableHttpClient getClient() {
		if (null == client) {
			KeyStore keyStore = null;
			try {
				keyStore = SSLCertsTool.prepareDefaultCert();
			} catch (KeyStoreException e1) {
				logger.warn("error while feching keystore");
			}
			return getSSLClient(keyStore);
		} else {
			return client;
		}
	}

	private static CloseableHttpClient getSSLClient(KeyStore keystore) {
		try {
			SSLContext sslc = SSLContexts.createDefault();
			if (null != keystore) {
				sslc = SSLContexts.custom()
						.loadKeyMaterial(keystore, Properties.getValue("defaultJREKeystorePassword").toCharArray())
						.build();
			}
			sslc.init(null, new TrustManager[] { new SavingAndTrustAllManager() }, null);
			HostnameVerifier verifier = new NoopHostnameVerifier();
			SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(sslc, verifier);
			Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("https", scsf).build();
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(reg);
			connectionManager.setMaxTotal(Integer.parseInt(Properties.getValue("maxConnectionInPool")));
			connectionManager.setDefaultMaxPerRoute(Integer.parseInt(Properties.getValue("maxConnectionPerRoute")));
			client = builder.setConnectionManager(connectionManager).setSSLSocketFactory(scsf)
					.setRetryHandler(new DefaultHttpRequestRetryHandler())
					.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()).build();
		} catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException | UnrecoverableKeyException e) {
			e.printStackTrace();
			return null;
		}
		return client;
	}

	public static Header getAuthHeader(AuthType authType) {
		String auth = "";
		switch (authType) {
		case Magic:
			auth = "";
			break;

		default:
			auth = CommonUtils.getBase64Encoded(Properties.getValue("user"), Properties.getValue("password"));
			break;
		}
		return new BasicHeader("authorization", "Basic " + auth);
	}

	public static List<Header> getDefaultHeaders() {
		ArrayList<Header> defaultHeaders = new ArrayList<>();
		defaultHeaders.add(HttpHelper.getAuthHeader(AuthType.Base64));
		return defaultHeaders;
	}
}

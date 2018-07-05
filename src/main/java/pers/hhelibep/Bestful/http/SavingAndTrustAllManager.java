package pers.hhelibep.Bestful.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class SavingAndTrustAllManager implements X509TrustManager {
    private X509Certificate[] chain;

    public X509Certificate[] getCertificateChain() {
        return chain;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // no need to check client certification
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        this.chain = chain;
    }

}

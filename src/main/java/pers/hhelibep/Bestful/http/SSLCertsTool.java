package pers.hhelibep.Bestful.http;

import java.io.*;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pers.hhelibep.Bestful.util.Properties;

public class SSLCertsTool {
    private static final int SOCKET_CONNECTION_TIME_OUT_SECONDS = 15;
    private static final Logger logger = LoggerFactory.getLogger(SSLCertsTool.class);
    private final static ArrayList<String> TARGET_URLS = Properties.getHosts();

    /**
     * @param hosts
     *            which hosts do you want to fetch certificates
     * @return system keystore with target hosts' certificates
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws KeyManagementException
     */
    public static KeyStore prepareCert(List<String> hosts) throws KeyStoreException, NoSuchAlgorithmException,
            CertificateException, IOException, KeyManagementException {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        for (String url : hosts) {
            logger.debug("connecting {}", url);
            String host;
            int port;
            char[] passphrase;
            String[] c = url.split(":");
            if (c.length == 3) {
                host = c[1].replaceAll("/", "");
                port = Integer.parseInt(c[2].split("/")[0]);
            } else if (c.length == 2) {
                if (c[0].equalsIgnoreCase("https")) {
                    host = c[1].split("/")[2];
                    port = 443;
                } else {
                    host = c[0];
                    port = Integer.parseInt(c[1].split("/")[0]);
                }

            } else {
                logger.error("wrong url format, return null!");
                return null;
            }
            passphrase = Properties.getValue("defaultJREKeystorePassword").toCharArray();
            File file;
            char SEP = File.separatorChar;
            File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
            file = new File(dir, "jssecacerts");
            if (file.isFile() == false) {
                file = new File(dir, "cacerts");
            }
            try (InputStream in = new FileInputStream(file);) {
                ks.load(in, passphrase);
                SSLContext context = SSLContext.getInstance("SSL");
                SavingAndTrustAllManager tm = new SavingAndTrustAllManager();
                context.init(null, new TrustManager[] { tm }, null);
                SSLSocketFactory factory = context.getSocketFactory();
                SSLSocket socket = (SSLSocket) factory.createSocket();
                socket.connect(new InetSocketAddress(host, port), SOCKET_CONNECTION_TIME_OUT_SECONDS * 1000);
                try {
                    logger.debug("Starting SSL handshake...");
                    socket.startHandshake();
                    socket.close();
                } catch (SSLException e) {
                    logger.warn("unable to do handshake with url {}, now regist cert and keystore", url);
                }

                X509Certificate[] chain = tm.getCertificateChain();
                if (chain == null) {
                    logger.error("Could not obtain server certificate chain");
                    return null;
                }
                logger.debug("Server sent " + chain.length + " certificate(s):");
                for (int k = 0; k < chain.length; ++k) {
                    X509Certificate cert = chain[k];
                    String alias = host + "-" + (k + 1);
                    ks.setCertificateEntry(alias, cert);
                    logger.debug("Added  certificate to java keystore using alias '" + alias + "'");
                }
            } catch (FileNotFoundException e1) {
                logger.warn("unable to locate java cacerts file, exit!");
            } catch (IOException e1) {
                logger.warn("connection time out, ignore " + url);
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            } catch (CertificateException e1) {
                e1.printStackTrace();
            } catch (KeyManagementException e1) {
                e1.printStackTrace();
            }
        }
        return ks;
    }

    public static KeyStore prepareDefaultCert() throws KeyStoreException {
        try {
            return prepareCert(TARGET_URLS);
        } catch (KeyManagementException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

/**
 * @author Bo (bwang@lattice-engines.com)
 * @date : Feb 5, 2018
 */

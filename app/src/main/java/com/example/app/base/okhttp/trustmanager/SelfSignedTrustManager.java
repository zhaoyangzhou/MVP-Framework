package com.example.app.base.okhttp.trustmanager;

import com.example.app.AppApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Package: com.example.app.base.okhttp.trustmanager
 * Class: SelfSignedTrustManager
 * Description: 自签名TrustManager实现类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2018/2/7 16:05
 */
public class SelfSignedTrustManager implements IATrustManager {
    private final String ASSETS_PATH = "cer/";//证书文件所在目录
    private X509TrustManager defaultTrustManager;
    private X509TrustManager localTrustManager;
    private KeyManager[] keyManagers;

    public SelfSignedTrustManager(String[] certificateNames, String bksFileName, String password) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManager[] trustManagers = prepareTrustManager(certificateNames);
        this.localTrustManager = chooseTrustManager(trustManagers);

        TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        var4.init((KeyStore) null);
        defaultTrustManager = chooseTrustManager(var4.getTrustManagers());

        keyManagers = prepareKeyManager(bksFileName, password);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        try {
            localTrustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException ce) {
            defaultTrustManager.checkServerTrusted(chain, authType);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[]{};
    }

    public TrustManager[] prepareTrustManager(String[] certificateNames) {
        if (certificateNames == null || certificateNames.length <= 0) return null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            for (String certificateName : certificateNames) {
                InputStream certificate = null;
                try {
                    certificate = AppApplication.getInstance().getAssets().open(ASSETS_PATH + certificateName);
                    keyStore.setCertificateEntry(certificateName, certificateFactory.generateCertificate(certificate));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (certificate != null)
                        certificate.close();
                }
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            return trustManagers;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeyManager[] prepareKeyManager(String bksFileName, String password) {
        try {
            if (bksFileName == null || password == null) return null;

            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            InputStream bksFile = null;
            try {
                bksFile = AppApplication.getInstance().getAssets().open(bksFileName);
                clientKeyStore.load(bksFile, password.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bksFile != null) bksFile.close();
            }
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    @Override
    public KeyManager[] getKeyManagers() {
        return keyManagers;
    }
}

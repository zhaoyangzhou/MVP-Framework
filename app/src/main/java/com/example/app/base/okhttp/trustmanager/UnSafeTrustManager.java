package com.example.app.base.okhttp.trustmanager;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;

/**
 * Package: com.example.app.base.okhttp.trustmanager
 * Class: UnSafeTrustManager
 * Description: 忽略证书TrustManager实现类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2018/2/7 16:09
 */
public class UnSafeTrustManager implements IATrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public KeyManager[] getKeyManagers() {
        return null;
    }
}

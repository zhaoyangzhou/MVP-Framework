package com.example.app.base.okhttp;

import com.example.app.base.okhttp.trustmanager.IATrustManager;
import com.example.app.base.okhttp.trustmanager.SelfSignedTrustManager;
import com.example.app.base.okhttp.trustmanager.UnSafeTrustManager;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Package: com.example.app.base.okhttp
 * Class: HttpsUtils
 * Description:
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2018/2/8 11:23
 */
public class HttpsUtils {
    public static class SSLParams {
        public SSLSocketFactory sSLSocketFactory;
        public X509TrustManager trustManager;
    }

    /**获取SSL连接
     * @param certificateNames 服务器公钥证书名称列表
     * @param bksFileName 客户端私钥BKS证书文件名
     * @param password 客户端私钥BKS证书密码
     * @return
     */
    public static SSLParams getSslSocketFactory(String[] certificateNames, String bksFileName, String password) {
        SSLParams sslParams = new SSLParams();
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            IATrustManager trustManager = null;
            if (certificateNames != null && certificateNames.length > 0) {
                trustManager = new SelfSignedTrustManager(certificateNames, bksFileName, password);
            } else {
                trustManager = new UnSafeTrustManager();
            }
            sslContext.init(trustManager.getKeyManagers(), new TrustManager[]{trustManager}, null);
            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = trustManager;
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        } catch (KeyManagementException e) {
            throw new AssertionError(e);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return sslParams;
    }
}

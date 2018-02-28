package com.example.app.base.okhttp.trustmanager;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Package: com.example.app.base.okhttp.trustmanager
 * Class: SelfSignedHostnameVerifier
 * Description: 自签名域名验证
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2018/2/8 9:31
 */
public class SelfSignedHostnameVerifier implements javax.net.ssl.HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        String[] hostNameArray = new String[]{"www.12306.cn"};
        javax.net.ssl.HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
        boolean result = false;
        for (String hostName : hostNameArray) {
            result = hv.verify(hostName, session);
            if (result == true) break;
        }
        return result;
    }
}
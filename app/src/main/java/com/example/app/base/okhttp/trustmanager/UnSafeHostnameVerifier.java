package com.example.app.base.okhttp.trustmanager;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Package: com.example.app.base.okhttp.trustmanager
 * Class: UnSafeHostnameVerifier
 * Description: 忽略证书域名验证
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2018/2/7 16:14
 */
public class UnSafeHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}

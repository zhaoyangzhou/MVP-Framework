package com.example.app.base.okhttp.trustmanager;

import javax.net.ssl.KeyManager;
import javax.net.ssl.X509TrustManager;

/**
 * Package: com.example.app.base.okhttp.trustmanager
 * Class: BaseTrustManager
 * Description: TrustManager接口类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2018/2/8 14:13
 */
public interface IATrustManager extends X509TrustManager {
    KeyManager[] getKeyManagers();
}

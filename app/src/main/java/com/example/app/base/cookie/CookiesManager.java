package com.example.app.base.cookie;

import com.example.app.AppApplication;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Package: com.example.app.base.cookie
 * Class: CookiesManager
 * Description: Cookies管理类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class CookiesManager implements CookieJar {
    private final PersistentCookieStore cookieStore = new PersistentCookieStore(AppApplication.getInstance().getApplicationContext());

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }
}
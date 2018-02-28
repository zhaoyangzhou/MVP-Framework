#MVP-Framework
##项目采用MVP架构，即MODEL + VIEW + PRESENTER模式，可以很好地将视图和业务相分离。

###采用的开源框架有：

```
1、RXJava2 + RxAndroid（响应式编程框架）

2、Retrofit2（网络请求和数据解析）

3、DBFlow（数据库，支持数据库文件加密）

4、OKHttp
```

项目支持Https证书单向认证：
```
1、将证书文件拷贝至app module的assets/cer目录下

2、SelfSignedHostnameVerifier类中hostNameArray增加域名地址
```

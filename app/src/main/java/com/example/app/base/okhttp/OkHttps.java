package com.example.app.base.okhttp;

import com.example.app.AppApplication;
import com.example.app.base.Constants;
import com.example.app.base.okhttp.trustmanager.SelfSignedHostnameVerifier;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by linlong on 2016/9/1.
 */
public class OkHttps implements OkHttpInterface {
    private static OkHttps instance;
    private static OkHttpClient client;
    private static final String APPDIR = Constants.APPDIR + "cache/audio/";
    /**
     * okHttp time out 定义（单位：秒）
     */
    public static final long OKHTTP_READ_TIME_OUT = 5;
    public static final long OKHTTP_CONNECT_TIME_OUT = 5;
    public static final long OKHTTP_WRITE_TIME_OUT = 5;

    public static OkHttps getInstance() {
        if(instance == null) {
            synchronized (OkHttps.class) {
                if(instance == null) {
                    instance = new OkHttps();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化HTTPS,添加信任证书
     */
    public OkHttps() {
        try {
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(AppApplication.getInstance().getAssets().list("cer"), null, null);
            client = new OkHttpClient.Builder()
//            .addInterceptor(new HttpLoggingInterceptor())
//            .cache(new Cache(cacheDir, cacheSize))
                    .readTimeout(OKHTTP_READ_TIME_OUT, TimeUnit.SECONDS)
                    .connectTimeout(OKHTTP_CONNECT_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(OKHTTP_WRITE_TIME_OUT, TimeUnit.SECONDS)
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                    .hostnameVerifier(new SelfSignedHostnameVerifier())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get请求方式，异步回调方式返回请求数据
     *
     * @param url      请求的URL地址
     * @param cls      javabean(json转成javabean)
     * @param callback 返回请求结果的回调函数
     */
    @Override
    public void getAsyn(String url, final Class cls, final OkHttpCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if(cls == null) {
                    callback.onResponse(str);
                } else {
                    Object obj = new Gson().fromJson(str, cls);
                    callback.onResponse(obj);
                }
            }
        });
    }

    /**
     * get请求方式，同步阻塞方式请求数据
     *
     * @param url 请求的URL地址
     */
    public Response get(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * post请求方式，异步回调方式返回请求数据
     *
     * @param url      请求的URL地址
     * @param cls      javabean(json转成javabean)
     * @param callback 返回请求结果的回调函数
     */
    @Override
    public void postAsyn(String url, RequestBody requestBody, final Class cls, final OkHttpCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                callback.onResponse(str);
            }
        });
    }

    public void postAsynInpuStream(String url, RequestBody requestBody, final Class cls, final OkHttpCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                if ("OK".equals(response.message())){
//                    InputStream str = response.body().byteStream();
//                    callback.onResponse(str);
//                }else{
//                    String str1 = response.body().string();
//                    callback.onResponse(str1);

//                }
                callback.onResponse(response);
            }
        });
    }

    /**
     * post请求方式，同步阻塞方式请求数据
     *
     * @param url 请求的URL地址
     */
    @Override
    public Response post(String url, RequestBody requestBody) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**上传音频流，服务端解析成文字
     * @param url
     * @param audioFileName
     * @param callback
     */
    public void postAudio(String url, String audioFileName, final OkHttpCallback callback) {
        final MediaType mediaType = MediaType.parse("application/octet-stream;");
        byte[] fileBytes = fileToBytes(audioFileName, "amr");
        if(fileBytes != null) {
            RequestBody requestBody = RequestBody.create(mediaType, fileBytes);
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onFailure(call, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    callback.onResponse(str);
                }
            });
        } else {
            callback.onFailure(null, new IOException());
        }
    }

    /**获取文件字节流
     * @param fileName 文件名
     * @param fileName 扩展名
     * @return
     */
    private byte[] fileToBytes(String fileName, String extension) {
        StringBuilder filesDir = new StringBuilder();
        filesDir.append(APPDIR).append(fileName).append(".").append(extension);
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            File file = new File(filesDir.toString());
            if (!file.exists()) {
                return null;
            } else {
                fis = new FileInputStream(file);
                bos = new ByteArrayOutputStream(1000);
                byte[] b = new byte[1000];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bos.toByteArray();
    }
}

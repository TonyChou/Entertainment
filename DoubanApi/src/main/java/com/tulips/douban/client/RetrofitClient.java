package com.tulips.douban.client;

import com.tulips.douban.Logger;
import com.tulips.douban.service.CookieManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by zhouxiaming on 2017/4/11.
 */
public class RetrofitClient {
    private String TAG = RetrofitClient.class.getSimpleName();
    private String host;
    private static final int RETRY_COUNT = 1;
    private static final int RETRY_WAIT_TIME = 2000;
    private boolean isDebug = false;
    private boolean isRetry = false;
    private Retrofit retrofit;

    public RetrofitClient(String host, boolean isDebug, boolean retry) {
        this.host = host;
        this.isDebug = isDebug;
        this.isRetry = retry;
        createRetrofit(createHttpClient());
    }


    private void createRetrofit(OkHttpClient httpclient) {
        retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .client(httpclient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T createApi(Class<T> apiClass) {
        return retrofit.create(apiClass);
    }

    private OkHttpClient createHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (isDebug) {
            clientBuilder.addInterceptor(new LogInterceptor());
        }

        if (isRetry) {
            clientBuilder.addInterceptor(new RetryInterceptor());
        }

        clientBuilder.addInterceptor(new CookieInterceptor());

        if (host != null && (host.startsWith("https") || host.startsWith("HTTPS"))) {
            supportHttps(clientBuilder);
        }

        return clientBuilder.build();
    }

    /**
     * 忽略Https验证
     *
     * @param builder
     */
    private void supportHttps(OkHttpClient.Builder builder) {
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            builder.sslSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析并缓存返回的cookie信息
     */
    class CookieInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            if (response != null) {
                CookieManager.updateCookieCache(response.headers());
            }
            return response;
        }
    }

    /**
     * 重试拦截器
     */
    class RetryInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            // 当前在网络请求的子线程中

            Logger.print(TAG, "intercept: getId " + Thread.currentThread().getId());
            okhttp3.Response response = null;
            int retryCount = 0;
            boolean retry = true;

            while (retry && retryCount++ < RETRY_COUNT) {
                try {
                    response = chain.proceed(request);

                    // 没有产生Exception
                    retry = shouldRetry(response, null);
                } catch (ConnectException e) {
                    Logger.print(TAG, "ConnectException : " + e);
                    retry = shouldRetry(response, e);
                    if (!retry) {
                        throw e;
                    }
                } catch (SocketException e) {
                    Logger.print(TAG, "SocketException : " + e);
                    retry = shouldRetry(response, e);
                    if (!retry) {
                        throw e;
                    }
                } catch (UnknownHostException e) {
                    Logger.print(TAG, "UnknownHostException : " + e);
                    retry = shouldRetry(response, e);
                    if (!retry) {
                        throw e;
                    }
                } catch (SocketTimeoutException e) {
                    Logger.print(TAG, "SocketTimeoutException : " + e);
                    retry = shouldRetry(response, e);
                    if (!retry) {
                        throw e;
                    }
                } catch (Exception e) {
                    Logger.print(TAG, "Exception : " + e);
                    retry = shouldRetry(response, e);
                    if (!retry) {
                        throw e;
                    }
                }

                if (retry) {
                    Logger.print(TAG, "RetryInterceptor tryCount: " + retryCount);

                    try {
                        Thread.sleep(Math.min(retryCount * RETRY_WAIT_TIME, 1000 * 60));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (response == null) {//important ,should throw an exception here
                throw new IOException();
            }
            return response;
        }
    }

    // 默认网络不可用的情况下不会重试，如果需要重试的话需，重载该函数。
    protected static boolean shouldRetry(okhttp3.Response response, Exception e) {
        if (response != null && response.isSuccessful()) {
            return false;
        }
        return true;
    }

    /**
     * 打印Log
     */
    class LogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Logger.print(TAG, String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            okhttp3.Response response = chain.proceed(request);
            if (response != null) {
                long t2 = System.nanoTime();
                Logger.print(TAG, String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            }
            return response;
        }
    }
}

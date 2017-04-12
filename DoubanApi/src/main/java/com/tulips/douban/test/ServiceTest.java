package com.tulips.douban.test;

import com.tulips.douban.model.ChannelsPage;
import com.tulips.douban.service.DoubanService;
import com.tulips.douban.service.DoubanUrl;
import com.union.net.RetrofitClient;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

/**
 * Created by zhouxiaming on 2017/4/11.
 */
public class ServiceTest {
    public static void main(String[] args) {
        RetrofitClient client = new RetrofitClient(DoubanUrl.API_HOST, true, true, true);
        DoubanService douBanService = client.createApi(DoubanService.class);

        try {
            douBanService.appChannels(appChannelsMap())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<ChannelsPage>() {
                        @Override
                        public void onNext(ChannelsPage value) {
                            System.out.println("onNext");
                        }

                        @Override
                        public void onError(Throwable e) {
                            System.out.println("onError");
                        }

                        @Override
                        public void onComplete() {
                            System.out.println("onComplete");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> appChannelsMap() {
        Map<String, String> params = new HashMap<>();
        params.put("app_name", "radio_android");
        params.put("apikey", "02f7751a55066bcb08e65f4eff134361");
        params.put("client", "s:mobile|v:4.6.7|y:android 6.0.1|f:647|m:Xiaomi|d:6aba7f108504dd47509e335b0b6335c5fee027a3|e:xiaomi_mi_note_lte");
        params.put("udid", "6aba7f108504dd47509e335b0b6335c5fee027a3");
        params.put("push_device_id", "95a76957764225b195bcbc8121b66559fd64b3e5");
        params.put("version", "647");
        return params;
    }

    private static OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
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
                return new java.security.cert.X509Certificate[] {};
            }
        } };
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            builder.sslSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.build();
    }

    /**
     * @return
     */
    protected static SSLSocketFactory getSSLSocketFactory() {
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}

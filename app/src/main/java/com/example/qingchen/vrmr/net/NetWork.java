package com.example.qingchen.vrmr.net;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author qingchen
 * @date 17-11-10
 */

public class NetWork {
    private static OkHttpClient okHttpClient=new OkHttpClient();
    private static Retrofit getRetrofit(String url, OkHttpClient client){
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static InfoApi getInfo(){
        return getRetrofit("http://api.tianapi.com",okHttpClient.newBuilder().build()).create(InfoApi.class);
    }
}

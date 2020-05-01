package com.procialize.mrgeApp20;

/**
 * Created by Naushad on 10/30/2017.
 */

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TenorClient {

    static ConnectionPool pool = new ConnectionPool(15, 10, TimeUnit.MINUTES);
    static OkHttpClient client = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectionPool(pool)
//            .connectTimeout(5, TimeUnit.MINUTES)
//            .readTimeout(5, TimeUnit.MINUTES)
//            .writeTimeout(5, TimeUnit.MINUTES)
            .build();
    static OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

package com.gliesereum.karma.data.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

//                private static final String ROOT_URL = "http://192.168.0.102:8200/api/";  //Ura
//    private static final String ROOT_URL = "http://192.168.0.100:8200/api/";  //Vetal

    private static final String ROOT_URL = "http://207.154.239.122:8200/api/";

    //        private static final String ROOT_URL = "https://karma.gliesereum.com/api/";
    public static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }
}

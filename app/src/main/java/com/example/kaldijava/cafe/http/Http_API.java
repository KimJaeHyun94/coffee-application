package com.example.kaldijava.cafe.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class Http_API {


    private final String base_url = "http://ru6300.cafe24.com/kaldi/";

    private Retrofit retro;

    public OkHttpClient getclient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.connectTimeout(5, TimeUnit.SECONDS); // connect timeout
        builder.addInterceptor(interceptor);
        return builder.build();
    }


    public Http_API() {
        Gson gson = new GsonBuilder().setLenient().create();
        retro = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getclient( ))
                .build();
    }




    public Apis getApis() {
        return retro.create(Apis.class);
    }


    public interface Apis{
        // Http 통신 인터페이스   POST 방식을 사용함
        @POST("m/api/auth/add-fcm.php")
        @FormUrlEncoded
        Call<RES<String>> sendToken(@Field("fcm_key") String token);

        // Http 통신 인터페이스   GET 방식을 사용함
        @GET("m/api/cafes/list.php")
        Call<RES<ArrayList<Menu>>> getMenus();

        @GET("m/api/push.php")
        Call<RES<Object>> sendPush(@Query("coffee") String coffee);
    }
}

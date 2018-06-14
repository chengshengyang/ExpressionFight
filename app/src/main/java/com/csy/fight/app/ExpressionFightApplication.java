package com.csy.fight.app;

import android.app.Application;

import com.csy.fight.constant.TagStatic;
import com.csy.fight.http.HttpService;
import com.csy.fight.http.LoggerInterceptor;
import com.csy.fight.http.NullOrEmptyConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mob.MobSDK;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chengshengyang on 2018/4/28.
 *
 * @author chengshengyang
 */
public class ExpressionFightApplication extends Application {

    /**
     * 测试服务器地址
     */
    public static final String BASE_URL = "http://192.168.4.154:8080/demo/";
    /**
     * Http请求响应超时时间10秒
     */
    public static final int HTTP_TIMEOUT = 10;
    /**
     * 是否调试模式
     */
    public static boolean mIsDebug = true;

    private HttpService mHttpService;
    private static ExpressionFightApplication mApplication;

    public String sToken;
    public String sUserId;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;
        initRetrofit();

        // 初始化shareSDK
        MobSDK.init(this);
    }

    public static synchronized ExpressionFightApplication getInstance() {
        return mApplication;
    }

    /**
     * 初始化Retrofit2
     */
    public void initRetrofit() {
        //checkUserIdAndToken();

        //LoggerInterceptor是打印log
        //TokenInterceptor为每次请求都添加token
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor(TagStatic.TAG_LOGGER, mIsDebug))
                //.addInterceptor(new TokenInterceptor(sToken, sUserId))
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //添加Rxjava
                .addConverterFactory(new NullOrEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))//解析方法
                .baseUrl(BASE_URL)//主机地址
                .build();
        mHttpService = retrofit.create(HttpService.class);
    }

    public HttpService getHttpService() {
        return mHttpService;
    }

}

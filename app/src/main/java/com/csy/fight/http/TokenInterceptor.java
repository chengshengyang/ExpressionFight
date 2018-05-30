package com.csy.fight.http;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author chengshengyang
 * @date 2017年4月10日11:05:50
 *
 * 为每一条请求插入userID和token信息，服务器会验证。
 */
public class TokenInterceptor implements Interceptor {
    String userid;
    String token;

    public TokenInterceptor(String token, String userid) {
        this.userid = userid;
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String url = originalRequest.url().toString();
        if (!TextUtils.isEmpty(userid) && !TextUtils.isEmpty(token)) {
            if (url.contains("?")) {
                url += "&userid=" + userid + "&token=" + token;
            } else {
                url += "?userid=" + userid + "&token=" + token;
            }
        }
        Request authorised = originalRequest.newBuilder()
                .url(url)
                .build();
        return chain.proceed(authorised);
    }
}

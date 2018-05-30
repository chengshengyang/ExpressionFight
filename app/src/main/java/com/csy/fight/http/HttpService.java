package com.csy.fight.http;


import com.csy.fight.data.model.StringResult;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by chengshengyang on 2018/4/28.
 *
 * @author chengshengyang
 */
public interface HttpService {

    @Headers("Content-Type: application/json")
    @POST("springboot/getuserbyjson")
    Call<StringResult> getUser(@Body HashMap<String, Object> body);
}

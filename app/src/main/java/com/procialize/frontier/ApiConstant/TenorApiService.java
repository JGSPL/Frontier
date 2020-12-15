package com.procialize.frontier.ApiConstant;

import com.procialize.frontier.GetterSetter.GifId;
import com.procialize.frontier.GetterSetter.response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Naushad on 1/30/2018.
 */

public interface TenorApiService {

    @POST("anonid?")
    @FormUrlEncoded
    Call<GifId> GifIdPost(@Field("key") String key);

    @POST("trending?")
    @FormUrlEncoded
    Call<response> Result(@Field("key") String key,
                          @Field("anon_id") String anon_id);


    @POST("search?")
    @FormUrlEncoded
    Call<response> search(@Field("q") String query,
                          @Field("key") String key,
                          @Field("anon_id") String anon_id);
}

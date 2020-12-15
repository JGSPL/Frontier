package com.procialize.frontier.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.GetterSetter.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserActivityReport {
    public String api_access_token;
    public String event_id;
    public String event_type;
    public String page_id;
    public String file_id;
    public String contentType;
    Context context;
    APIService mAPIService;

    public GetUserActivityReport(Context _context,
                                 String _api_access_token,
                                 String _event_id,
                                 String _event_type,
                                 String _page_id,
                                 String _file_id
    ) {
        context = _context;
        api_access_token = _api_access_token;
        event_id = _event_id;
        event_type = _event_type;
        page_id = _page_id;
        file_id = _file_id;
        mAPIService = ApiUtils.getAPIService();
    }


    public void userActivityReport() {
        mAPIService.getUserActivityReport(api_access_token,
                event_id,
                event_type,
                page_id,
                file_id).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("success")) {
                            SharedPreferences pref = context.getSharedPreferences("Page", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear();
                        }
                    }
                } else {
                    Log.e("Message", response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Log.e("Error Message", t.getMessage());

            }
        });
        /*RestClient.getInstance().getWebServices().getUserActivityReport(eventId, accessToken, eventType, pageId, fileId, contentType)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("success")) {
                                SharedPreferences pref = context.getSharedPreferences("Page", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                            }
                        } else {
                            Log.e("Message", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Error Message", t.getMessage());
                    }
                });*/
    }
}

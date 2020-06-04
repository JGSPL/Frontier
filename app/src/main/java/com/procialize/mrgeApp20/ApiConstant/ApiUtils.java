package com.procialize.mrgeApp20.ApiConstant;

import com.procialize.mrgeApp20.BuddyClient;
import com.procialize.mrgeApp20.RetrofitClient;
import com.procialize.mrgeApp20.TenorClient;

/**
 * Created by Naushad on 10/30/2017.
 */

public class ApiUtils {

    private ApiUtils() {
    }


    public static APIService getAPIService() {

        return RetrofitClient.getClient(ApiConstant.baseUrl).create(APIService.class);
    }

    public static APIService getBuddyAPIService() {

        return BuddyClient.getClientbuddy(ApiConstant.buddybaseUrl).create(APIService.class);
    }

    public static TenorApiService getTenorAPIService() {

        return TenorClient.getClient(ApiConstant.tenorUrl).create(TenorApiService.class);
    }
}

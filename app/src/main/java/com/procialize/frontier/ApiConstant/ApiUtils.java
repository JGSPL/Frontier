package com.procialize.frontier.ApiConstant;

import com.procialize.frontier.BuddyClient;
import com.procialize.frontier.RetrofitClient;
import com.procialize.frontier.TenorClient;

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
    public static APIService getGalleryAPIService() {

        return BuddyClient.getClientbuddy(ApiConstant.gallerycontestUrl).create(APIService.class);
    }

    public static TenorApiService getTenorAPIService() {

        return TenorClient.getClient(ApiConstant.tenorUrl).create(TenorApiService.class);
    }
}

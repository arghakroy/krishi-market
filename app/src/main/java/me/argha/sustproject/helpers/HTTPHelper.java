package me.argha.sustproject.helpers;

import com.loopj.android.http.AsyncHttpClient;

import me.argha.sustproject.utils.AppConst;

/**
 * Created by ARGHA K ROY on 5/19/2015.
 */
public class HTTPHelper {

    public static AsyncHttpClient getHTTPClient(){
        AsyncHttpClient httpClient=new AsyncHttpClient();
        httpClient.setMaxRetriesAndTimeout(AppConst.MAX_CONNECTION_TRY, AppConst.TIME_OUT);
        return httpClient;
    }
}

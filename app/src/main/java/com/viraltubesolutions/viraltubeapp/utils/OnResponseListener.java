package com.viraltubesolutions.viraltubeapp.utils;

/**
 * Created by Shashi on 10/9/2017.
 */

public interface OnResponseListener<T> {

    void onResponse(T response, WebServices.ApiType URL, boolean isSucces);

}
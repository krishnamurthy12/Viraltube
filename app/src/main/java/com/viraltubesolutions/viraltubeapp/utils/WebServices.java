package com.viraltubesolutions.viraltubeapp.utils;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.widget.Adapter;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shashi on 10/9/2017.
 */

public class WebServices<T> {
    T t;
    Call<T> call=null;
    public T getT() {
        return t;
    }

    public void setT(T t) {

        this.t = t;
    }
    ApiType apiTypeVariable;
    Context context;
    OnResponseListener<T> onResponseListner;
    private static OkHttpClient.Builder builder;

    public enum ApiType {
        getOriginalVideos,Getvideos,selfuploadVideo,uploadVideofromGallery,
        userlogin,usersignup,getViews,getvotes,getContacts,getSelfVotecount,
        contactShare,updateFCMKey
    }
    public static final String BASE_URL="https://viraltube.co.in/viral/";
    public static final String SELF_UPLOAD_URL="https://viraltube.co.in/vt/";

    public WebServices(OnResponseListener<T> onResponseListner) {
        this.onResponseListner = onResponseListner;

        if (onResponseListner instanceof Activity) {
            this.context = (Context) onResponseListner;
        } else if (onResponseListner instanceof IntentService) {
            this.context = (Context) onResponseListner;
        } else if (onResponseListner instanceof android.app.DialogFragment) {
            android.app.DialogFragment dialogFragment = (android.app.DialogFragment) onResponseListner;
            this.context = dialogFragment.getActivity();
        }else if (onResponseListner instanceof android.app.Fragment) {
            android.app.Fragment fragment = (android.app.Fragment) onResponseListner;
            this.context = fragment.getActivity();
        }
         else if (onResponseListner instanceof Adapter) {

            this.context = (Context) onResponseListner;
        }
        else if (onResponseListner instanceof Adapter) {
            this.context = (Context) onResponseListner;
        }
            else {
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) onResponseListner;
            this.context = fragment.getActivity();
        }

        builder = getHttpClient();
    }

    public WebServices(Context context, OnResponseListener<T> onResponseListner) {
        this.onResponseListner = onResponseListner;
        this.context = context;
        builder = getHttpClient();
    }


    public OkHttpClient.Builder getHttpClient() {

        if (builder == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.connectTimeout(10000, TimeUnit.SECONDS);
            client.readTimeout(10000, TimeUnit.SECONDS).build();
            client.addInterceptor(loggingInterceptor);
            return client;
        }
        return builder;
    }
    public void userLogIn(String api,ApiType apiTypes,String email,String password)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(api)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ViralTubeAPI viralTubeAPI=retrofit.create(ViralTubeAPI.class);
        call=(Call<T>)viralTubeAPI.userLogIn(email,password);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }
    public void userSignUp(String api, final ApiType apiTypes, String userName, String email, String mobile, String password,
                           String age, String gender, String location)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        ViralTubeAPI viralTubeAPI=retrofit.create(ViralTubeAPI.class);
        call=(Call<T>) viralTubeAPI.userSignUp(userName,email,mobile,password,age,gender,location);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t,apiTypes,true);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null,apiTypes,false);

            }
        });


    }

    public Call<T> getVideos(String api, ApiType apiTypes, String userid) {

        apiTypeVariable = apiTypes;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

        call = (Call<T>) viralTubeAPI.getVideos(userid,"1");
        call.enqueue(new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t = (T) response.body();
               onResponseListner.onResponse(t, apiTypeVariable, true);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {

                onResponseListner.onResponse(null, apiTypeVariable, false);
            }

        });
        return call;
    }


    public Call<T> getOriginalVideos(String api, ApiType apiTypes, String userid) {

        apiTypeVariable = apiTypes;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

        call = (Call<T>) viralTubeAPI.getOriginalVideos(userid,"1");
        call.enqueue(new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t = (T) response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {

                onResponseListner.onResponse(null, apiTypeVariable, false);
            }

        });
        return call;
    }

    public Call<T> selfVideoUpload(String api, ApiType apiType, RequestBody title, RequestBody ftag, MultipartBody.Part filePath, RequestBody userid, RequestBody type) {
        apiTypeVariable=apiType;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

        call = (Call<T>) viralTubeAPI.selfVideoUpload(title,filePath,ftag,userid,type);
        call.enqueue(new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t = (T) response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Toast.makeText(context, "Failed to uploaded your video", Toast.LENGTH_SHORT).show();
               onResponseListner.onResponse(null, apiTypeVariable, false);
            }
        });
        return call;
    }

    public Call<T> uploadFromGallery(String api, ApiType apiType, RequestBody title, MultipartBody.Part filePath,
                                     RequestBody tag, RequestBody userid, RequestBody uploadtype) {
        apiTypeVariable=apiType;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

        call = (Call<T>) viralTubeAPI.uploadFromgallery(title,filePath,tag,userid,uploadtype);
        call.enqueue(new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t = (T) response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Toast.makeText(context, "Failed to uploaded your video", Toast.LENGTH_SHORT).show();
                onResponseListner.onResponse(null, apiTypeVariable, false);
            }
        });
        return call;
    }

    /*public Call<T> getViews(String api,ApiType apiType) {
        apiTypeVariable=apiType;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

        call = (Call<T>) viralTubeAPI.getViews("1","2");
        call.enqueue(new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t = (T) response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);
            }
        });
        return call;
    }*/

    public void getVotes(String api,ApiType apiType,String userId,String videoId) {
        apiTypeVariable=apiType;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

        call = (Call<T>) viralTubeAPI.getVotes(userId,videoId);
        call.enqueue(new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t = (T) response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);
            }
        });
    }

    public void getContacts(String api, ApiType apiType, JSONObject data) {
        apiTypeVariable=apiType;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

        call = (Call<T>) viralTubeAPI.getContacts(data);
        call.enqueue(new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t = (T) response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);
            }
        });
    }

    public void getSelfVoteCount(String api, ApiType apiType, String userid) {
        apiTypeVariable=apiType;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

        call = (Call<T>) viralTubeAPI.getSelfVoteCount(userid);
        call.enqueue(new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t = (T) response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);
            }
        });
    }

    public void share(String api, ApiType apiType,String vid_id, String userid) {
        apiTypeVariable=apiType;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

        call = (Call<T>) viralTubeAPI.share(vid_id,userid);
        call.enqueue(new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t = (T) response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);
            }
        });
    }

    public void updateFCM(String api, ApiType apiType,String fcm_id, String userid) {
        apiTypeVariable=apiType;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

        call = (Call<T>) viralTubeAPI.updateFCM(fcm_id,userid);
        call.enqueue(new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t = (T) response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);
            }
        });
    }



}

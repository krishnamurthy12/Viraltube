package com.viraltubesolutions.viraltubeapp.utils;

import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.Abhi.AbhiDemo;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.SelfUploadResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.afterPayment.AfterPaymentResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.callus.CallUs;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.catagories.Catagories;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.checkUserPayment.CheckUserPayment;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.contactsResponse.ContactsResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.fcmResponse.FCMResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.forgotpassword.ForgotPassword;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.forgotpassword.UpdatePassword;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.numberOfViews.NumOfViewsResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.numberOfVotes.NumOfVotesResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.originalVideos.OriginalVideos;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.paymentresponse.PaymentResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.promotevideo.PromoteResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.searchResults.Datum;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.selfVoteCount.SelfVoteCountResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.shareResponse.ShareResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.subCatagories.SubCatagories;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadFromGallery.UploadFromGallery;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadedVideoResponse.AllVideosByLimit;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadedVideoResponse.UploadedVideosResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadlimitcheck.UploadLimitCheck;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadprofilepic.UploadProfilePic;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.userLogIn.UserLoginResults;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.userSignUp.UserSignUpResults;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Shashi on 10/9/2017.
 */

public interface ViralTubeAPI {
    @FormUrlEncoded
    @POST("register-api.php")
    Call<UserSignUpResults> userSignUp(@Field("userName") String userName, @Field("email") String email,
                                       @Field("mobile") String mobile, @Field("password") String password,
                                       @Field("age") String age, @Field("gender") String gender, @Field("location") String location);

    @FormUrlEncoded
    @POST("login.php")
    Call<UserLoginResults> userLogIn(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("viral_videos.php")
    Call<OriginalVideos> getOriginalVideos(@Field("user_id") String user_id, @Field("limit") String limit);


    @FormUrlEncoded
    @POST("all.php")
    Call<UploadedVideosResponse> getVideos(@Field("user_id") String user_id, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("all_videos.php")
    Call<AllVideosByLimit> getvideosWithLimit(@Field("user_id") String name, @Field("limit") String limit);

    @Multipart
    @POST("self_video_upload.php")
    Call<SelfUploadResponse> selfVideoUpload(@Part("video_name") RequestBody video_name,
                                             @Part MultipartBody.Part myFile,
                                             @Part("user_id") RequestBody user_id,
                                             @Part("tag_line") RequestBody video_tag,
                                             @Part("upload_type") RequestBody upload_type
    );

    @Multipart
    @POST("video_upload.php")
    Call<UploadFromGallery> uploadFromgallery(@Part("video_name") RequestBody video_name,
                                              @Part MultipartBody.Part myFile,
                                              @Part("user_id") RequestBody user_id,
                                              @Part("tag_line") RequestBody video_tag,
                                              @Part("upload_type") RequestBody upload_type);
    @FormUrlEncoded
    @POST("view_video.php")
    Call<NumOfViewsResponse> getViews(@Field("user_id") String userId, @Field("vid_id") String VideoId);

    @FormUrlEncoded
    @POST("vote_video.php")
    Call<NumOfVotesResponse> getVotes(@Field("user_id") String userId, @Field("vid_id") String VideoId);

    @FormUrlEncoded
    @POST("promote_video.php")
    Call<PromoteResponse> promote(@Field("user_id") String userId, @Field("vid_id") String VideoId);

    @FormUrlEncoded
    @POST("contacts.php")
    Call<ContactsResponse> getContacts(@Field("data") JSONObject data);

    @FormUrlEncoded
    @POST("self_video_count.php")
    Call<SelfVoteCountResponse> getSelfVoteCount(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("search.php")
    Call<Datum> searchVideo(@Field("search") String searchresults, @Field("user_id") String id);

    @FormUrlEncoded
    @POST("update_fcm_id.php")
    Call<FCMResponse> updateFCM(@Field("fcm_id") String tokenID, @Field("user_id") String id);

    @FormUrlEncoded
    @POST("share.php")
    Call<ShareResponse> share(@Field("vid_id") String videoID, @Field("user_id") String id);

    @FormUrlEncoded
    @POST("pay.php")
    Call<PaymentResponse> payment(@Field("data") JSONObject data);

    @FormUrlEncoded
    @POST("pay_check.php")
    Call<CheckUserPayment> checkUserPayment(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("fee.php")
    Call<AfterPaymentResponse> afterPayment();

    @POST("categories.php")
    Call<Catagories> getCatagories();

    @FormUrlEncoded
    @POST("sub_categories.php")
    Call<SubCatagories> getSubcatagories(@Field("cat_id") String catID);

    @Multipart
    @POST("upload_picture.php")
    Call<UploadProfilePic> updateProfilepicture(@Part("user_id") RequestBody user_id,
                                                @Part MultipartBody.Part myFile);

    @FormUrlEncoded
    @POST("video_check.php")
    Call<UploadLimitCheck> checkUploadlimit(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("forgot_password.php")
    Call<ForgotPassword> forgotpassword(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("update_password.php")
    Call<UpdatePassword> updatePassword(@Field("mobile") String mobile, @Field("password") String password);

    @POST("call.php")
    Call<CallUs> callUs();

    @GET("getMenu")
    Call<AbhiDemo> abhiApi();





}

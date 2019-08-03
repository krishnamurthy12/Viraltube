package com.viraltubesolutions.viraltubeapp.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.numberOfViews.NumOfViewsResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.numberOfVotes.NumOfVotesResponse;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.activities.HomePageActivity;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomTextView;
import com.viraltubesolutions.viraltubeapp.likeanimation.LikeButton;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeAPI;

import java.io.ByteArrayOutputStream;

import cn.jzvd.JZVideoPlayerStandard;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shashi on 11/17/2017.
 */

public class SharedVideoAdapter extends RecyclerView.Adapter<SharedVideoAdapter.MyHolder> {

    String mUserID;
    int numOfvotes;
    int numOfViews;
    Context context;
    static String finalvideourl;
    static  String finalthumburl;
    //ArrayList<ShareResponse> arrayList;
    String[] z;

    public SharedVideoAdapter(Context context, String userID,String[] z, SpotsDialog loadingDialog) {
        this.context=context;
        this.mUserID=userID;
        this.z=z;
        if(loadingDialog.isShowing())
        {
            loadingDialog.dismiss();
        }

    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public MyCustomTextView title, views, votes;
        ImageView /*mVotes,*/mPromote,mShare,mContact;
        JZVideoPlayerStandard jzVideoPlayerStandard;
        LinearLayout mVoteLayout, mShareLayout, mPromoteLayout, mContactLayout;
        WebView webview;
        LikeButton animatedLike;
        TextView txt;


        public MyHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.vT_ssa_title);
            views = itemView.findViewById(R.id.vT_ssa_views);
            votes = itemView.findViewById(R.id.vT_ssa_votes);
            jzVideoPlayerStandard =itemView.findViewById(R.id.videoplayer_ssa);

            //mVotes=itemView.findViewById(R.id.vI_ssa_vote);
            mPromote=itemView.findViewById(R.id.vI_ssa_promote);
            mShare=itemView.findViewById(R.id.vI_ssa_share);
            mContact=itemView.findViewById(R.id.vI_ssa_contact);

            animatedLike=(LikeButton) itemView.findViewById(R.id.vl_likebtn);
            txt= (TextView) itemView.findViewById(R.id.textclick1);
            webview = itemView.findViewById(R.id.help_webview1);

            mVoteLayout=itemView.findViewById(R.id.vote_layout);
            mPromoteLayout=itemView.findViewById(R.id.promote_layout);
            mShareLayout=itemView.findViewById(R.id.share_layout);
            mContactLayout=itemView.findViewById(R.id.contact_layout);

        }
    }
    private void shareDeepLink(String deepLink) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Firebase Deep Link");
        intent.putExtra(Intent.EXTRA_TEXT, deepLink);

        context.startActivity(intent);
    }
    public void shortlinkBuild(@NonNull String videouri, @NonNull String imageuri, @NonNull String vidtitle, int minVersion)  {

        Uri video=Uri.parse(videouri);
        Log.d("sharingparameters",video.toString()+"\n");
        Log.d("sharingparameters",imageuri);
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(videouri))
                .setDynamicLinkDomain("jv5e5.app.goo.gl")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.viraltubesolutions.viraltubeapp")
                                .setMinimumVersion(minVersion)
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(vidtitle)
                                .setDescription("This link works whether the app is installed or not!")
                                .setImageUrl(Uri.parse(imageuri))
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.d("generatedshortlink",shortLink.toString());
                            shareDeepLink(shortLink.toString());
                        } else {
                            // Error
                            // ...
                            Log.d("generatedshortlink","failed to generate short link");
                        }
                    }
                });
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharedvideo_singlerow_appearence, parent, false);
        SharedVideoAdapter.MyHolder holder = new SharedVideoAdapter.MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

       /* String thumburl=String.valueOf(z[8]).replaceAll("\\\\", File.separator);
        String videourl=String.valueOf(z[3]).replaceAll("\\\\",File.separator);*/
        String thumburl = String.valueOf(z[8]).replace('\\', '/');
        String videourl = String.valueOf(z[3]).replace('\\', '/');
        finalvideourl = videourl.replaceAll("////", "//");
        finalthumburl = thumburl.replaceAll("////", "//");


        Log.d("replacedurl", finalvideourl + "   " + finalthumburl);


        if (z[1].equalsIgnoreCase("1")) {
            holder.animatedLike.setLiked(true);
            //holder.mVotes.setImageResource(R.drawable.heart_shape_blued);
            holder.votes.setText(z[6] + " Votes");

        } else {
            holder.animatedLike.setLiked(false);
            ///holder.mVotes.setImageResource(R.drawable.vote);
            holder.votes.setText(z[6] + " Votes");

        }

        numOfViews = Integer.parseInt(String.valueOf(z[7]));
        numOfvotes = Integer.parseInt(String.valueOf(z[6]));

        holder.title.setText(z[5]);
        holder.views.setText(z[7] + " Views");
        holder.votes.setText(z[6] + " Votes");


        holder.jzVideoPlayerStandard.setUp(finalvideourl.trim(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, " ");
        holder.jzVideoPlayerStandard.progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("Here", progress + " ");

                if (progress > 35) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://viraltube.co.in/vt/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

                    Call<NumOfViewsResponse> call = viralTubeAPI.getViews(mUserID, z[4]);
                    //Call<NumOfViewsResponse> call =  viralTubeAPI.getViews(mUserID,mlist.get(position).getVideoId());
                    call.enqueue(new Callback<NumOfViewsResponse>() {
                        @Override
                        public void onResponse(Call<NumOfViewsResponse> call, Response<NumOfViewsResponse> response) {

                            if (response.body().getrESPONSECODE().equalsIgnoreCase("200")) {
                                numOfViews = Integer.parseInt(String.valueOf(z[7])) + 1;
                                holder.views.setText(numOfViews + " Views");

                            } else if (response.body().getrESPONSECODE().equalsIgnoreCase("405")) {
                                holder.views.setText(z[7] + " Views");
                            }

                        }

                        @Override
                        public void onFailure(Call<NumOfViewsResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Glide.with(context)
                .load(finalthumburl.trim())
                //.load(mlist.get(position).getVideoThumb())
                .into(holder.jzVideoPlayerStandard.thumbImageView);


        holder.mVoteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.animatedLike.check(1);
                if(!holder.animatedLike.isLiked())
                {
                    holder.animatedLike.callOnClick();
                }


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://viraltube.co.in/vt/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

                Call<NumOfVotesResponse> call = viralTubeAPI.getVotes(mUserID, z[4]);
                //Call<NumOfVotesResponse> call = viralTubeAPI.getVotes(mUserID, mlist.get(position).getVideoId());
                call.enqueue(new Callback<NumOfVotesResponse>() {
                    @Override
                    public void onResponse(Call<NumOfVotesResponse> call, Response<NumOfVotesResponse> response) {
                        holder.animatedLike.check(0);
                        if (response.body().getRESPONSECODE().equalsIgnoreCase("200")) {

                            numOfvotes = Integer.parseInt(String.valueOf(z[6])) + 1;
                            //holder.mVotes.setImageResource(R.drawable.heart_shape_blued);
                            holder.votes.setText(numOfvotes + " Votes");
                            holder.animatedLike.setLiked(true);

                        } else if (response.body().getRESPONSECODE().equalsIgnoreCase("405")) {
                            holder.animatedLike.setLiked(true);
                            Snackbar.make(holder.mVoteLayout, "Sorry you can vote only once", Snackbar.LENGTH_SHORT).show();
                            //holder.mVotes.setImageResource(R.drawable.heart_shape_blued);
                            holder.votes.setText(z[6] + " Votes");

                        }
                        else {
                            holder.animatedLike.setLiked(false);
                        }

                    }

                    @Override
                    public void onFailure(Call<NumOfVotesResponse> call, Throwable t) {

                    }
                });

            }
        });
        holder.mShareLayout.setOnClickListener(new View.OnClickListener() {
            String videoUrl = finalvideourl.trim();
            String vidTitle = z[5];
            String thumburi = finalthumburl;

            @Override
            public void onClick(View v) {
                holder.webview.loadUrl(thumburi);
                holder.webview.setWebViewClient(new WebViewClient() {
                    public void onPageFinished(WebView view, String url) {
                        String myResult = holder.webview.getUrl();
                        shortlinkBuild(videoUrl, myResult, vidTitle, 1);
                    }
                });

               /* Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image");
                //shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(context, getBitmapFromView(holder.jzVideoPlayerStandard.thumbImageView)));
                shareIntent.putExtra(Intent.EXTRA_TEXT, "\n" + context.getPackageName() + "\n" +
                        z[5] + "\n file:" + z[3]);

                if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(Intent.createChooser(shareIntent, "Share using"));*/
            }
        });

        holder.mContactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom);
                holder.mContact.startAnimation(animation);
                ((HomePageActivity) context).viewPager.setCurrentItem(3);
                SharedPreferences videoIdPreference = context.getSharedPreferences("VideoIDPreference", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = videoIdPreference.edit();
                editor.putString("videoID",z[4]);
                editor.apply();
            }
        });

        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mVoteLayout.callOnClick();
            }
        });
    }

        @TargetApi(Build.VERSION_CODES.M)
        public static Bitmap getBitmapFromView(View view) {
            //Define a bitmap with the same size as the view
            Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(returnedBitmap,(int)(returnedBitmap.getWidth()*0.4),
                (int)(returnedBitmap.getHeight()*0.4), true);
            //Bind a canvas to it
            Canvas canvas = new Canvas(returnedBitmap);
            //Get the view's background
            Drawable bgDrawable = view.getForeground();
            if (bgDrawable != null)
                //has background drawable, then draw it on the canvas
                bgDrawable.draw(canvas);
            else
                //does not have background drawable, then draw white background on the canvas
                canvas.drawColor(Color.WHITE);
            // draw the view on the canvas
            view.draw(canvas);
            //return the bitmap
            return returnedBitmap;
        }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

}

package com.viraltubesolutions.viraltubeapp.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.viraltubesolutions.videoplayerlibrary.customview.JZVideoPlayerStandard;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.numberOfViews.NumOfViewsResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.numberOfVotes.NumOfVotesResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadedVideoResponse.Datum;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeAPI;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shashi on 11/17/2017.
 */

public class SharedVideoAdapter extends RecyclerView.Adapter<SharedVideoAdapter.MyHolder> {

    List<Datum> mlist = new ArrayList<Datum>();
    String mUserID;
    int numOfvotes;
    int numOfViews;
    Context context;
    RecyclerView mRecyclerview;

    public SharedVideoAdapter(Context context,List<Datum> list, RecyclerView recyclerView, String userID) {
        this.context=context;
        this.mlist=list;
        this.mUserID=userID;
        this.mRecyclerview=recyclerView;

    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView title, views, votes;
        ImageView mVotes,mPromote,mShare,mContact;
        JZVideoPlayerStandard jzVideoPlayerStandard;


        public MyHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.vT_ssa_title);
            views = itemView.findViewById(R.id.vT_ssa_views);
            votes = itemView.findViewById(R.id.vT_ssa_votes);
            jzVideoPlayerStandard =itemView.findViewById(R.id.videoplayer_ssa);

            mVotes=itemView.findViewById(R.id.vI_ssa_vote);
            mPromote=itemView.findViewById(R.id.vI_ssa_promote);
            mShare=itemView.findViewById(R.id.vI_ssa_share);
            mContact=itemView.findViewById(R.id.vI_ssa_contact);
        }
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharedvideo_singlerow_appearence, parent, false);
        SharedVideoAdapter.MyHolder holder = new SharedVideoAdapter.MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        if (mlist.get(position).getUserVote() == 1) {
            holder.mVotes.setImageResource(R.drawable.heart_shape_red);
            holder.votes.setText(mlist.get(position).getVoteCount() + " Votes");

        } else {
            holder.mVotes.setImageResource(R.drawable.vote);
            holder.votes.setText(mlist.get(position).getVoteCount() + " Votes");

        }

        numOfViews = mlist.get(position).getViewCount();
        numOfvotes = mlist.get(position).getVoteCount();

        holder.title.setText(mlist.get(position).getTitle());
        holder.views.setText(mlist.get(position).getViewCount() + " Views");
        holder.votes.setText(mlist.get(position).getVoteCount() + " Votes");


        holder.jzVideoPlayerStandard.setUp(mlist.get(position).getVideoUrl(), JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, " ");
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

                    Call<NumOfViewsResponse> call = viralTubeAPI.getViews(mUserID, mlist.get(position).getId());
                    //Call<NumOfViewsResponse> call =  viralTubeAPI.getViews(mUserID,mlist.get(position).getVideoId());
                    call.enqueue(new Callback<NumOfViewsResponse>() {
                        @Override
                        public void onResponse(Call<NumOfViewsResponse> call, Response<NumOfViewsResponse> response) {

                            if (response.body().getrESPONSECODE().equalsIgnoreCase("200")) {
                                numOfViews = mlist.get(position).getViewCount() + 1;
                                holder.views.setText(numOfViews + " Views");

                            } else if (response.body().getrESPONSECODE().equalsIgnoreCase("405")) {
                                holder.views.setText(mlist.get(position).getViewCount() + " Views");
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
                .load(mlist.get(position).getThumbnailUrl())
                //.load(mlist.get(position).getVideoThumb())
                .into(holder.jzVideoPlayerStandard.thumbImageView);


        holder.mVotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://viraltube.co.in/vt/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

                Call<NumOfVotesResponse> call = viralTubeAPI.getVotes(mUserID, mlist.get(position).getId());
                //Call<NumOfVotesResponse> call = viralTubeAPI.getVotes(mUserID, mlist.get(position).getVideoId());
                call.enqueue(new Callback<NumOfVotesResponse>() {
                    @Override
                    public void onResponse(Call<NumOfVotesResponse> call, Response<NumOfVotesResponse> response) {
                        if (response.body().getRESPONSECODE().equalsIgnoreCase("200")) {
                            numOfvotes = mlist.get(position).getVoteCount() + 1;
                            holder.mVotes.setImageResource(R.drawable.heart_shape_red);
                            holder.votes.setText(numOfvotes + " Votes");
                        } else if (response.body().getRESPONSECODE().equalsIgnoreCase("405")) {
                            Snackbar.make(holder.mVotes, "Sorry you can vote only once", Snackbar.LENGTH_SHORT).show();
                            holder.mVotes.setImageResource(R.drawable.heart_shape_red);
                            holder.votes.setText(mlist.get(position).getVoteCount() + " Votes");

                        }

                    }

                    @Override
                    public void onFailure(Call<NumOfVotesResponse> call, Throwable t) {

                    }
                });

            }
        });
        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                //shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(context, getBitmapFromView(holder.jzVideoPlayerStandard.thumbImageView)));
                shareIntent.putExtra(Intent.EXTRA_TEXT, "\n" + context.getPackageName() + "\n" +
                        mlist.get(position).getTitle() + "\n file:" + mlist.get(position).getVideoUrl());
               /* shareIntent.putExtra(Intent.EXTRA_TEXT, "\n"+context.getPackageName()+"\n"+
                        mlist.get(position).getVideoTitle()+"\n file:"+mlist.get(position).getVideoUrl());*/

                if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(Intent.createChooser(shareIntent, "Share using"));
                }
            }
        });
    }
        @TargetApi(Build.VERSION_CODES.M)
        public static Bitmap getBitmapFromView(View view) {
            //Define a bitmap with the same size as the view
            Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);

       /* Bitmap resizedBitmap = Bitmap.createScaledBitmap(returnedBitmap,(int)(returnedBitmap.getWidth()*0.4),
                (int)(returnedBitmap.getHeight()*0.4), true);*/
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
        return mlist.size();
    }

}

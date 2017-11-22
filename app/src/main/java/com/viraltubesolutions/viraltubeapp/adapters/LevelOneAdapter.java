package com.viraltubesolutions.viraltubeapp.adapters;

import android.annotation.SuppressLint;
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
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.viraltubesolutions.videoplayerlibrary.customview.JZVideoPlayerStandard;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.numberOfViews.NumOfViewsResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.numberOfVotes.NumOfVotesResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.promotevideo.PromoteResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadedVideoResponse.Datum;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.activities.HomePageActivity;
import com.viraltubesolutions.viraltubeapp.fragments.LevelOneFragment;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeAPI;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Shashi on 10/5/2017.
 */

public class LevelOneAdapter extends RecyclerView.Adapter<LevelOneAdapter.MyHolder> implements Filterable {


    Context context;
    List<Datum> mlist = new ArrayList<Datum>();
    List<Datum> mArrayList = new ArrayList<Datum>();
    List<Datum> mFilteredList = new ArrayList<Datum>();
    LinearLayout noMatchFoundlayout;

  /*  List<Video> mlist = new ArrayList<Video>();
    List<Video> mArrayList = new ArrayList<Video>();
    List<Video> mFilteredList = new ArrayList<Video>();*/
    RecyclerView recyclerView;
    String mUserID;
    int numOfvotes;
    int numOfViews;
    SpotsDialog refreshdialog;
    LevelOneFragment levelOneFragment;
    String fcmKey;
    public LevelOneAdapter(Context context, List<Datum> list, RecyclerView recyclerView, String userID, LevelOneFragment levelOneFragment, String refreshedToken, LinearLayout novideosLayout) {
        this.mlist = list;
        this.mArrayList = list;
        this.mFilteredList = list;
        this.context = context;
        this.recyclerView=recyclerView;
        this.mUserID=userID;
        this.levelOneFragment=levelOneFragment;
        this.fcmKey=refreshedToken;
        this.noMatchFoundlayout=novideosLayout;

    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().trim().toLowerCase();

                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<Datum> filteredList = new ArrayList<>();
                    //ArrayList<Video> filteredList = new ArrayList<>();

                   /* for(int i=0;i<=mArrayList.size();i++)
                    {
                        //Log.d("filter_list",mArrayList.get(i).getTitle());
                        if(mArrayList.get(i).getTitle().toLowerCase().contains(charString))
                        {
                            Log.d("filter_list","search success");
                            filteredList.add(mArrayList.get(i));
                        }
                    }*/

                    for (Datum datum : mArrayList) {

                       if (datum.getTitle().toLowerCase().contains(charString) ||
                               datum.getTag().toLowerCase().contains(charString))
                       {
                           filteredList.add(datum);

                       }


                    }
                    /*for (Video datum : mArrayList) {

                        if (datum.getVideoTitle().toLowerCase().contains(charString) || datum.getVideoUrl().toLowerCase().contains(charString)) {
                            filteredList.add(datum);
                        }

                    }*/
                    mFilteredList = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredList = (ArrayList<Datum>) results.values;
                //mFilteredList = (ArrayList<Video>) results.values;

                    noMatchFoundlayout.setVisibility(View.GONE);
                    SearchResultsAdapter adapter = new SearchResultsAdapter(context, mFilteredList, recyclerView, mUserID, levelOneFragment);
                    recyclerView.setAdapter(adapter);
                    notifyDataSetChanged();


            }
        };
    }


    public class MyHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener*/ {
         public TextView title, views, votes;
        ImageView mVotes,mPromote,mShare,mContact;
        JZVideoPlayerStandard jzVideoPlayerStandard;


        public MyHolder(View view) {
            super(view);

            title = view.findViewById(R.id.videoTitle);
            views = view.findViewById(R.id.numberOfViews);
            votes = view.findViewById(R.id.numberOfVotes);
            jzVideoPlayerStandard = view.findViewById(R.id.videoView_level1);

            mVotes=view.findViewById(R.id.vI_los_like);
            mPromote=view.findViewById(R.id.vI_los_promote);
            mShare=view.findViewById(R.id.vI_los_share);
            mContact=view.findViewById(R.id.vI_los_contact);

        }

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_one_singlerow_appearence, parent, false);
        MyHolder holder = new MyHolder(v);
        return holder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

       /* numOfViews=mlist.get(position).getViewCount();
        numOfvotes=mlist.get(position).getVoteCount();

        holder.title.setText(mlist.get(position).getVideoTitle());
        holder.views.setText(mlist.get(position).getViewCount()+" Views");
        holder.votes.setText(mlist.get(position).getVoteCount()+" Votes");


        holder.jzVideoPlayerStandard.setUp(mlist.get(position).getVideoUrl(), JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, " ");
*/
        if(mlist.get(position).getUserVote()==1)
        {
            holder.mVotes.setImageResource(R.drawable.heart_shape_blued);
            holder.votes.setText(mlist.get(position).getVoteCount()+" Votes");

        }
        else
        {
            holder.mVotes.setImageResource(R.drawable.vote);
            holder.votes.setText(mlist.get(position).getVoteCount()+" Votes");

        }

        numOfViews=mlist.get(position).getViewCount();
        numOfvotes=mlist.get(position).getVoteCount();

        holder.title.setText(mlist.get(position).getTitle());
        holder.views.setText(mlist.get(position).getViewCount()+" Views");
        holder.votes.setText(mlist.get(position).getVoteCount()+" Votes");


        holder.jzVideoPlayerStandard.setUp(mlist.get(position).getVideoUrl(), JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, " ");
        //holder.jzVideoPlayerStandard.thumbImageView.setImageResource(R.drawable.macbook);
        holder.jzVideoPlayerStandard.progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("Here",progress+" ");
                if(progress>35)
                {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://viraltube.co.in/vt/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

                    Call<NumOfViewsResponse> call =  viralTubeAPI.getViews(mUserID,mlist.get(position).getId());
                    //Call<NumOfViewsResponse> call =  viralTubeAPI.getViews(mUserID,mlist.get(position).getVideoId());
                    call.enqueue(new Callback<NumOfViewsResponse>() {
                        @Override
                        public void onResponse(Call<NumOfViewsResponse> call, Response<NumOfViewsResponse> response) {

                            if(response.body().getrESPONSECODE().equalsIgnoreCase("200")) {
                                numOfViews=mlist.get(position).getViewCount()+1;
                                holder.views.setText(numOfViews+" Views");

                            }
                            else if(response.body().getrESPONSECODE().equalsIgnoreCase("405"))
                            {
                                holder.views.setText(mlist.get(position).getViewCount()+" Views");
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
                                holder.mVotes.setImageResource(R.drawable.heart_shape_blued);
                                holder.votes.setText(numOfvotes + " Votes");
                            } else if (response.body().getRESPONSECODE().equalsIgnoreCase("405")) {
                                Snackbar.make(holder.mVotes,"Sorry you can vote only once", Snackbar.LENGTH_SHORT).show();
                                holder.mVotes.setImageResource(R.drawable.heart_shape_blued);
                                holder.votes.setText(mlist.get(position).getVoteCount() + " Votes");

                            }

                        }

                        @Override
                        public void onFailure(Call<NumOfVotesResponse> call, Throwable t) {

                        }
                    });

            }
        });
        holder.mPromote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://viraltube.co.in/vt/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

                Call<PromoteResponse> call = viralTubeAPI.promote(mUserID, mlist.get(position).getId());
                //Call<PromoteResponse> call = viralTubeAPI.promote(mUserID, mlist.get(position).getVideoId());
                call.enqueue(new Callback<PromoteResponse>() {
                    @Override
                    public void onResponse(Call<PromoteResponse> call, Response<PromoteResponse> response) {
                        if (response.body().getRESPONSECODE().equalsIgnoreCase("200")) {
                            levelOneFragment.onRefresh();

                            /*Snackbar.make(holder.mPromote,"Your video promoted please refresh the page", Snackbar.LENGTH_SHORT)
                                    .show();
*/
                        } else if (response.body().getRESPONSECODE().equalsIgnoreCase("403")) {

                            Snackbar.make(holder.mPromote,"can't promote Your video", Snackbar.LENGTH_SHORT)
                                    .show();
                        }

                    }

                    @Override
                    public void onFailure(Call<PromoteResponse> call, Throwable t) {

                    }
                });

            }
        });
        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent=new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                //shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_STREAM,getImageUri(context, getBitmapFromView(holder.jzVideoPlayerStandard.thumbImageView)));
                shareIntent.putExtra(Intent.EXTRA_TEXT, "\n"+context.getPackageName()+"\n"+
                        mlist.get(position).getTitle()+"\n file:"+mlist.get(position).getVideoUrl());
               /* shareIntent.putExtra(Intent.EXTRA_TEXT, "\n"+context.getPackageName()+"\n"+
                        mlist.get(position).getVideoTitle()+"\n file:"+mlist.get(position).getVideoUrl());*/

                if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(Intent.createChooser(shareIntent, "Share using"));
                }
            }
        });
        holder.mContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomePageActivity)context).viewPager.setCurrentItem(3);
                SharedPreferences videoIdPreference=context.getSharedPreferences("VideoIDPreference",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=videoIdPreference.edit();
                editor.putString("videoID",mlist.get(position).getId());
                editor.apply();
            }
        });

        holder.mVotes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom);
                holder.mVotes.startAnimation(animation);
                return false;

            }
        });
        holder.mPromote.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom);
                holder.mPromote.startAnimation(animation);
                return false;

            }
        });
        holder.mShare.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom);
                holder.mShare.startAnimation(animation);
                return false;

            }
        });
        holder.mContact.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom);
                holder.mContact.startAnimation(animation);
                return false;

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

   /* public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight,View view) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(output, m, new Paint());

        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        return output;
    }*/
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

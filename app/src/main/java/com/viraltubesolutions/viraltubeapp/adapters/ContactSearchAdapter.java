package com.viraltubesolutions.viraltubeapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.contactsResponse.Contact;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.shareResponse.ShareResponse;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.customs.TextDrawable;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shashi on 11/15/2017.
 */

public class ContactSearchAdapter extends RecyclerView.Adapter<ContactSearchAdapter.MyHolder> {

    List<Contact> mList=new ArrayList<Contact>();
    Context context;
    RecyclerView recyclerView;
    String mVideoid,muserID;
    LinearLayout noMatchLaytout;

    public ContactSearchAdapter(Context context, List<Contact> mList, RecyclerView recyclerView, String muserID, String mVideoid) {
        this.mList = mList;
        this.context = context;
        this.recyclerView = recyclerView;
        this.mVideoid=mVideoid;
        this.muserID=muserID;
    }

    public  void callSendAPI(int position)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viraltube.co.in/vt/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

        Call<ShareResponse> call = viralTubeAPI.share(mVideoid,muserID);
        call.enqueue(new Callback<ShareResponse>() {
            @Override
            public void onResponse(Call<ShareResponse> call, Response<ShareResponse> response) {
                ShareResponse shareResponse=response.body();
                if (response.body().getRESPONSECODE().equalsIgnoreCase("200")) {

                    ArrayList<ShareResponse> share=new ArrayList<>();
                    share.add(new ShareResponse(response.body().getId(),response.body().getTitle(),response.body().getThumbnailUrl(),response.body().getVideoUrl(),
                            response.body().getViewCount(), response.body().getVoteCount(), response.body().getUserVote(),
                            response.body().getAdsUrl(),response.body().getRESPONSECODE()));
                    Gson gson = new Gson();

                    String json = gson.toJson(share);
                    SharedPreferences preferences=context.getSharedPreferences("ShareData",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("Data",json);
                    editor.apply();

                    Snackbar.make(recyclerView,"Sent Successfully", Snackbar.LENGTH_SHORT).show();

                } else if (response.body().getRESPONSECODE().equalsIgnoreCase("403")) {
                    Snackbar.make(recyclerView,"Database error", Snackbar.LENGTH_SHORT).show();
                }
                else if (response.body().getRESPONSECODE().equalsIgnoreCase("409")) {
                    Snackbar.make(recyclerView,"Please select a video to send", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShareResponse> call, Throwable t) {
                Snackbar.make(recyclerView,"Fail to send", Snackbar.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactlist_singlerow_appearence, parent, false);
        ContactSearchAdapter.MyHolder holder = new ContactSearchAdapter.MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContactSearchAdapter.MyHolder holder, int position) {
        Random rnd = new Random();
        int color= Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        TextDrawable drawable= TextDrawable.builder().buildRound(mList.get(position).getContactPic()+"",color);
        holder.contactChar.setImageDrawable(drawable);
        //holder.contactChar.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        holder.contactChar.setTag(mList.get(position).getContactPic());
        holder.name.setText(mList.get(position).getName());
        holder.mobileNumber.setText(mList.get(position).getMobile());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,mobileNumber;
        ImageView contactChar;

        public MyHolder(View view) {
            super(view);
            contactChar= (ImageView) view.findViewById(R.id.contact_pic);
            name = (TextView)view.findViewById(R.id.contact_name);
            mobileNumber =(TextView)view.findViewById(R.id.contact_number);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            callSendAPI(getAdapterPosition());

        }
    }
}

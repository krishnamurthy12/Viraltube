package com.viraltubesolutions.viraltubeapp.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * Created by Shashi on 11/13/2017.
 */

public class Tab4adapter extends RecyclerView.Adapter<Tab4adapter.MyHolder> implements Filterable
{
    List<Contact> contactList=new ArrayList<Contact>();
    //LayoutInflater inflater;
    Context context;
    List<Contact> mlist = new ArrayList<Contact>();
    List<Contact> mArrayList = new ArrayList<Contact>();
    List<Contact> mFilteredList = new ArrayList<Contact>();
    RecyclerView recyclerView;
    String muserID,mVideoid;
    LinearLayout nomatchLayout;
    ProgressDialog pd;

    public Tab4adapter(Context context, List<Contact> contactList, RecyclerView mRecyclerView, String userID, LinearLayout nomatchLayout) {
        this.contactList = contactList;
        this.mArrayList=contactList;
        this.mFilteredList=contactList;
        this.context = context;
        this.recyclerView=mRecyclerView;
        this.muserID=userID;
        this.nomatchLayout=nomatchLayout;
        pd=new ProgressDialog(context);
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().trim().toLowerCase();

                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                }
                else {
                    ArrayList<Contact> filteredList = new ArrayList<>();
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

                    for (Contact datum : mArrayList) {

                        if (datum.getName().toLowerCase().contains(charString) || datum.getMobile().toLowerCase().contains(charString)) {
                            filteredList.add(datum);
                        }


                    }

                    mFilteredList = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredList = (ArrayList<Contact>) results.values;
                ContactSearchAdapter adapter=new ContactSearchAdapter(context,mFilteredList,recyclerView,muserID,mVideoid);
                recyclerView.setAdapter(adapter);
                notifyDataSetChanged();

            }
        };
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
            SharedPreferences videoid=context.getSharedPreferences("VideoIDPreference",Context.MODE_PRIVATE);
            mVideoid=videoid.getString("videoID",null);
            if(mVideoid!=null){
                pd.setMessage("Please Wait");
                pd.show();
                callSendAPI(getAdapterPosition());
            SharedPreferences videoid1=context.getSharedPreferences("VideoIDPreference",Context.MODE_PRIVATE);
            videoid1.edit().clear().apply();
            mVideoid=null;
            }
            else
            {
                Snackbar.make(recyclerView,"Please select a video to share", Snackbar.LENGTH_SHORT).show();

            }


        }
        public  void callSendAPI(int position)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://viraltube.co.in/vt/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ViralTubeAPI viralTubeAPI = retrofit.create(ViralTubeAPI.class);

            Call<ShareResponse> call = viralTubeAPI.share(mVideoid,contactList.get(position).getId());
            call.enqueue(new Callback<ShareResponse>() {
                @Override
                public void onResponse(Call<ShareResponse> call, Response<ShareResponse> response) {
                    if (response.body().getRESPONSECODE().equalsIgnoreCase("200")) {
                        if(pd.isShowing())
                        {
                            pd.dismiss();
                        }

                       /* ArrayList<ShareResponse> share=new ArrayList<>();
                        share.add(new ShareResponse(response.body().getId(),response.body().getTitle(),response.body().getThumbnailUrl(),response.body().getVideoUrl(),
                                response.body().getViewCount(), response.body().getVoteCount(), response.body().getUserVote(),
                                response.body().getAdsUrl(),response.body().getRESPONSECODE()));
                        Gson gson = new Gson();

                        String json = gson.toJson(share);
                        SharedPreferences preferences=context.getSharedPreferences("ShareData",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("Data",json);
                        editor.apply();*/

                        Snackbar.make(recyclerView,"Sent Successfully", Snackbar.LENGTH_SHORT).show();

                        SharedPreferences deletePreference=context.getSharedPreferences("VideoIDPreference",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1=deletePreference.edit();
                        editor1.clear();
                        editor1.apply();

                    } else if (response.body().getRESPONSECODE().equalsIgnoreCase("403")) {
                        if(pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Snackbar.make(recyclerView,"The user is currently not using ViralTube app", Snackbar.LENGTH_SHORT).show();
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
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactlist_singlerow_appearence, parent, false);
        Tab4adapter.MyHolder holder = new Tab4adapter.MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        Random rnd = new Random();
        int color= Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        TextDrawable drawable= TextDrawable.builder().buildRound(contactList.get(position).getContactPic()+"",color);
        holder.contactChar.setImageDrawable(drawable);
        //holder.contactChar.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        holder.contactChar.setTag(contactList.get(position).getContactPic());
        holder.name.setText(contactList.get(position).getName());
        holder.mobileNumber.setText(contactList.get(position).getMobile());

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


}

package com.viraltubesolutions.viraltubeapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.subCatagories.Datum;
import com.viraltubesolutions.viraltubeapp.R;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Shashi on 11/28/2017.
 */

public class SubCatagoriesAdapter extends RecyclerView.Adapter<SubCatagoriesAdapter.MyHolder>{
    private List<Datum> mList=new ArrayList<>();
    private Context context;
    String subCatagoryID;

    public SubCatagoriesAdapter(Context context,List<Datum> mList) {
        this.mList = mList;
        this.context=context;
    }

    @Override
    public SubCatagoriesAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcatagories_singlerow_appearence, parent, false);
        return new SubCatagoriesAdapter.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(SubCatagoriesAdapter.MyHolder holder, int position) {

        holder.jzVideoPlayerStandard.setUp(mList.get(position).getVideoUrl(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                mList.get(position).getCategoryName());

        Glide.with(context)
                .load(mList.get(position).getThumbnailUrl())
                .into(holder.jzVideoPlayerStandard.thumbImageView);

        //holder.mSubCatagoryName.setText(mList.get(position).getCategoryName());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       JZVideoPlayerStandard jzVideoPlayerStandard;

        public MyHolder(View itemView) {
            super(itemView);
           jzVideoPlayerStandard=itemView.findViewById(R.id.videoView_subcatagories);
            //itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            //subCatagoryID=mList.get(getAdapterPosition()).getSubCategoryId();

        }
    }
}

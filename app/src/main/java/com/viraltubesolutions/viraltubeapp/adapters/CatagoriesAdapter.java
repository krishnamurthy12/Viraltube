package com.viraltubesolutions.viraltubeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.catagories.Datum;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.activities.SubCatagoriesActivity;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shashi on 11/27/2017.
 */

public class CatagoriesAdapter extends RecyclerView.Adapter<CatagoriesAdapter.MyHolder>{
    private List<Datum> mList=new ArrayList<>();
    private Context context;
    String catagoryID;

    public CatagoriesAdapter(Context context,List<Datum> mList) {
        this.mList = mList;
        this.context=context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catagories_singlerow_appearence, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Glide.with(context)
                .load(mList.get(position).getImage())
                .into(holder.mImage);
        holder.mTitle.setText(mList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mImage;
        MyCustomTextView mTitle;

        public MyHolder(View itemView) {
            super(itemView);
            mImage=itemView.findViewById(R.id.vI_csa_image);
            mTitle=itemView.findViewById(R.id.vT_csa_title);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            catagoryID=mList.get(getAdapterPosition()).getId();
            Intent subCatagoryIntent=new Intent(context,SubCatagoriesActivity.class);
            subCatagoryIntent.putExtra("catagoryID",catagoryID);
            context.startActivity(subCatagoryIntent);

        }
    }
}

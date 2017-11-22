package com.viraltubesolutions.viraltubeapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.SearchView;

import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadedVideoResponse.Datum;
import com.viraltubesolutions.viraltubeapp.fragments.LevelOneFragment;
import com.viraltubesolutions.viraltubeapp.fragments.LevelTwoFragment;
import com.viraltubesolutions.viraltubeapp.fragments.Tab4;
import com.viraltubesolutions.viraltubeapp.fragments.UploadsFragment;

import java.util.List;

/**
 * Created by Shashi on 10/5/2017.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int no_of_pages;
    SearchView.OnQueryTextListener context;
    public MyPagerAdapter(FragmentManager fm, int numofPages) {
        super(fm);
        this.no_of_pages=numofPages;
    }

    public MyPagerAdapter(FragmentManager fm, SearchView.OnQueryTextListener context, List<Datum> mArrayList) {
        super(fm);
        this.context = context;
           }


    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                LevelOneFragment level1=new LevelOneFragment();
                return level1;
            case 1:
                UploadsFragment uploads=new UploadsFragment();
                return uploads;

            case 2:
                LevelTwoFragment level2=new LevelTwoFragment();
                return level2;
            case 3:
                /*ContactsFragment contacts=new ContactsFragment();
                return contacts;*/
                Tab4 tab4=new Tab4();
                return tab4;

        }
        return null;
    }

    @Override
    public int getCount() {
        return no_of_pages;
    }
 /*   @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<Datum> filteredList = new ArrayList<>();

                    for (Datum datum : mArrayList) {

                        if (datum.getTitle().toLowerCase().contains(charString)) {

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
                mFilteredList = (ArrayList<Datum>) results.values;
                notifyDataSetChanged();

            }
        };
    }*/
}

package com.viraltubesolutions.viraltubeapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.beanclasses.ContactsBeanClass;
import com.viraltubesolutions.viraltubeapp.customs.TextDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Shashi on 10/5/2017.
 */

public class ContactsAdapter extends ArrayAdapter<ContactsBeanClass> {
    List<ContactsBeanClass> contactList=new ArrayList<ContactsBeanClass>();
    LayoutInflater inflater;
    Context context;


    public ContactsAdapter(Context context,int resources, List<ContactsBeanClass> contactList) {
        super(context,resources,contactList);
        this.contactList=contactList;
        this.context=context;
    }


    public class ViewHolder
    {
        //ImageView image;
        TextView name,mobileNumber;
        ImageView contactChar;


        public ViewHolder(View view) {
            contactChar= (ImageView) view.findViewById(R.id.contact_pic);
            name = (TextView)view.findViewById(R.id.contact_name);
            mobileNumber =(TextView)view.findViewById(R.id.contact_number);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        ViewHolder holder=null;
        if(v==null)
        {
            inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.contactlist_singlerow_appearence,parent,false);
            holder=new ViewHolder(v);
            v.setTag(holder);

        }
        else
        {
            holder= (ViewHolder) v.getTag();
        }
        Random rnd = new Random();
        int color= Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        TextDrawable drawable= TextDrawable.builder().buildRound(contactList.get(position).getContactPic()+"",color);
        holder.contactChar.setImageDrawable(drawable);
        //holder.contactChar.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        //holder.contactChar.setBackground(contactList.get(position).getContactPic()+"");
        holder.name.setText(contactList.get(position).getContactName());
        holder.mobileNumber.setText(contactList.get(position).getContactnumber());
        return v;
    }
}


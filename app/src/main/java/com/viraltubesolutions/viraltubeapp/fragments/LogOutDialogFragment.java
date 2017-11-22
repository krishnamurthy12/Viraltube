package com.viraltubesolutions.viraltubeapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.activities.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogOutDialogFragment extends DialogFragment implements View.OnClickListener {
    Context context;
    Button mcancel,mlogout;
    View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_log_out_dialog, container, false);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        getDialog().setCancelable(false);
        init();
        return view;
    }

    private void init() {
        mcancel=view.findViewById(R.id.vB_flo_cancel);
        mlogout=view.findViewById(R.id.vB_flo_logout);

        AssetManager assetManager = getActivity().getAssets();
        Typeface candaraTypeface = Typeface.createFromAsset(assetManager, "Fonts/candara.ttf");
        mcancel.setTypeface(candaraTypeface);
        mlogout.setTypeface(candaraTypeface);

        mcancel.setOnClickListener(this);
        mlogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id== R.id.vB_flo_logout)
        {
            SharedPreferences pref=getActivity().getSharedPreferences("LogIn",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=pref.edit();
            editor.putBoolean("isLoggedin",false);
            editor.apply();
            Intent logout=new Intent(context,LoginActivity.class);
            startActivity(logout);

        }
        if(id== R.id.vB_flo_cancel)
        {
            dismiss();
        }

    }
}

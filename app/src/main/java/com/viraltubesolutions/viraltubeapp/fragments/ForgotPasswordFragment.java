package com.viraltubesolutions.viraltubeapp.fragments;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomEditText;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomTextView;

public class ForgotPasswordFragment extends DialogFragment {


    MyCustomEditText mFpEmail;
    Button mFpSubmit;
    MyCustomTextView mFpForgotPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.forgotpassword_dialog,container,false);

        mFpEmail=view.findViewById(R.id.vE_fp_email);
        mFpSubmit=view.findViewById(R.id.vB_fp_submit);
        mFpForgotPassword=view.findViewById(R.id.vT_fp_forgotpassword_txt);

        AssetManager assetManager=getActivity().getAssets();
        Typeface candaraTypeface = Typeface.createFromAsset(assetManager , "Fonts/candara.ttf");
        mFpSubmit.setTypeface(candaraTypeface);

        mFpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }


}

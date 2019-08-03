package com.viraltubesolutions.viraltubeapp.fragments;

import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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
                callConfirmPasswordDialog();
                callPasswordSentDialog();

                /*ConfirmpasswordDialogFragment fragment=new ConfirmpasswordDialogFragment();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
                fragment.show(ft, "confirmpassword");
                fragment.setCancelable(false);*/
            }
        });

        return view;
    }
    void callPasswordSentDialog()
    {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.userupload_limit_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        MyCustomTextView dialogtext=dialogView.findViewById(R.id.vT_uld_text);
        Button btn=dialogView.findViewById(R.id.vB_uld_ok);
        dialogtext.setText(R.string.password_sent);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //callConfirmPasswordDialog();

            }
        });

    }
    void callConfirmPasswordDialog()
    {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_confirmpassword_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        MyCustomTextView skip=dialogView.findViewById(R.id.vT_fcp_skip);
        skip.setPaintFlags(skip.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        MyCustomEditText receivedPassword=dialogView.findViewById(R.id.vE_fcp_receivedpassword);
        MyCustomEditText newPassword=dialogView.findViewById(R.id.vE_fcp_currentpassword);
        MyCustomEditText confirmPassword=dialogView.findViewById(R.id.vE_fcp_confirmpassword);
        Button btn=dialogView.findViewById(R.id.vB_fcp_update);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //callPasswordSentDialog();
                alertDialog.dismiss();
            }
        });


    }



}

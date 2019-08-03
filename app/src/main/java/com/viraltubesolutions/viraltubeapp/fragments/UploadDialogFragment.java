package com.viraltubesolutions.viraltubeapp.fragments;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomEditText;


public class UploadDialogFragment extends DialogFragment implements View.OnClickListener {
    MyCustomEditText mVideotitle,mOptionaltag;
    Button mUpload,mCancel;
    View baseView;
    Context context;
    uploadDialogInterface interfaceObj;
    String title="";
    String tag=" ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_upload_dialog, container, false);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        getDialog().setCancelable(false);

        mVideotitle = baseView.findViewById(R.id.vE_fud_title);
        mUpload = baseView.findViewById(R.id.vB_fud_upload);
        mCancel = baseView.findViewById(R.id.vB_fud_cancel);
        //mVideotitle = baseView.findViewById(R.id.vE_fud_title);
        mOptionaltag=baseView.findViewById(R.id.vE_fud_tag);

        AssetManager assetManager = getActivity().getAssets();
        Typeface candaraTypeface = Typeface.createFromAsset(assetManager, "Fonts/candara.ttf");
        mUpload.setTypeface(candaraTypeface);
        mCancel.setTypeface(candaraTypeface);

        mUpload.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        return baseView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        interfaceObj= (uploadDialogInterface) getTargetFragment();
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id== R.id.vB_fud_cancel)
        {
            dismiss();
        }
        else if(id== R.id.vB_fud_upload)
        {
            title=mVideotitle.getText().toString();
            tag=mOptionaltag.getText().toString();
            if(mVideotitle.getText().toString().isEmpty()) {
                Snackbar.make(mVideotitle,"Please enter the video title", Snackbar.LENGTH_SHORT).show();
           }else
            {
                interfaceObj.senddata(title,tag);
                dismiss();

            }

        }

    }
   public interface uploadDialogInterface

   {
       public void senddata(String title, String tag);
   }
}

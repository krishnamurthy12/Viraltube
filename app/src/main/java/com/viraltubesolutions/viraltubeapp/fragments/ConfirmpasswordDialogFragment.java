/*
package com.viraltubesolutions.viraltubeapp.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomEditText;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class ConfirmpasswordDialogFragment extends DialogFragment implements View.OnClickListener{

View view;
    MyCustomEditText mNewPassword,mConfirmPassword;
    Button mupdate,mCancel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_confirmpassword_dialog, container, false);
        initialize();
        return view;
    }
    void initialize()
    {
        mNewPassword=view.findViewById(R.id.vE_fcp_currentpassword);
        mConfirmPassword=view.findViewById(R.id.vE_fcp_confirmpassword);
        mCancel=view.findViewById(R.id.vB_fcp_cancel);
        mupdate=view.findViewById(R.id.vB_fcp_update);
        mCancel.setOnClickListener(this);
        mupdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==mupdate.getId())
        {
            String password=mNewPassword.getText().toString();
            String confirmpassword=mConfirmPassword.getText().toString();
            if(!password.isEmpty()&& !confirmpassword.isEmpty()) {
                if (password.equals(confirmpassword)) {
                    Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if(id==mCancel.getId()){
            dismiss();

        }

    }
}
*/

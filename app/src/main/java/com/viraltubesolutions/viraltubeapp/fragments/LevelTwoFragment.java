package com.viraltubesolutions.viraltubeapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.selfVoteCount.SelfVoteCountResponse;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.activities.HomePageActivity;
import com.viraltubesolutions.viraltubeapp.activities.MPSActivity;
import com.viraltubesolutions.viraltubeapp.utils.OnResponseListener;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeUtils;
import com.viraltubesolutions.viraltubeapp.utils.WebServices;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LevelTwoFragment extends Fragment implements OnResponseListener,View.OnClickListener {
    View view;
    LinearLayout mPaymentlayout,mShortageVoteslayout;
    RelativeLayout mMainlayout;
    Button mPay,mGotoLevelOne;
    Context context;
    String userID;


    public LevelTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_level_two, container, false);
        initializeViews();
        callSelfVoteCountAPI();
        return view;

    }
    private void initializeViews()
    {
        mPaymentlayout=view.findViewById(R.id.paymentlayout);
        mShortageVoteslayout=view.findViewById(R.id.shortagevoteslayout);
        mMainlayout=view.findViewById(R.id.mainlayout);

        mPay=view.findViewById(R.id.vB_flt_buypremium);
        mGotoLevelOne=view.findViewById(R.id.vB_flt_gotolevelone);

        SharedPreferences preferences = context.getSharedPreferences("LogIn", MODE_PRIVATE);
        userID=preferences.getString("userID",null);
    }
    public void callSelfVoteCountAPI()
    {
        if (ViralTubeUtils.isConnectingToInternet(getContext())) {

            WebServices<SelfVoteCountResponse> response=new WebServices<SelfVoteCountResponse>(LevelTwoFragment.this);
            response.getSelfVoteCount(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.getSelfVotecount,userID);
        } else {
            Snackbar.make(view, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
        switch (URL)
        {
            case getSelfVotecount:
                SelfVoteCountResponse voteCountResponse= (SelfVoteCountResponse) response;
                if(isSucces)
                {
                    if(voteCountResponse.getRESPONSECODE().equalsIgnoreCase("200"))
                    {
                        if(voteCountResponse.getVoteCount()>=1)
                        {
                            callPaymentGateway();

                        }
                        else
                        {
                            callShortageOfVotes();
                        }

                    }
                    else if(voteCountResponse.getRESPONSECODE().equalsIgnoreCase("409"))
                    {
                        Snackbar.make(view,"Invalid parameters", Snackbar.LENGTH_SHORT).show();

                    }
                    else if(voteCountResponse.getRESPONSECODE().equalsIgnoreCase("403"))
                    {
                        Snackbar.make(view,"database error", Snackbar.LENGTH_SHORT).show();

                    }
                    else if(voteCountResponse.getRESPONSECODE().equalsIgnoreCase("503"))
                    {
                        Snackbar.make(view,"Invalid method", Snackbar.LENGTH_SHORT).show();

                    }

                }
                break;
        }

    }
    public void callPaymentGateway()
    {
        mPaymentlayout.setVisibility(View.VISIBLE);
        mPay.setOnClickListener(this);

    }
    public void callShortageOfVotes()
    {
        mShortageVoteslayout.setVisibility(View.VISIBLE);
        mGotoLevelOne.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.vB_flt_buypremium:
                integrateGateway();
                break;
            case R.id.vB_flt_gotolevelone:
                gotoLevelOne();
                break;
        }

    }
    public  void gotoLevelOne()
    {
       /* Fragment fragment = new LevelOneFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.leveltwoMainlayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
        ((HomePageActivity)getActivity()).viewPager.setCurrentItem(0);
    }
    public void integrateGateway()
    {
        Toast.makeText(context, "Integrate gateway", Toast.LENGTH_SHORT).show();
        Intent paymentIntent=new Intent(context, MPSActivity.class);
        startActivity(paymentIntent);
        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_in_from_right);


    }

}

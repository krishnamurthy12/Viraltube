package com.viraltubesolutions.viraltubeapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.checkUserPayment.CheckUserPayment;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.selfVoteCount.SelfVoteCountResponse;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.activities.HomePageActivity;
import com.viraltubesolutions.viraltubeapp.activities.ViralTubePaymentActivity;
import com.viraltubesolutions.viraltubeapp.utils.ButtonClickEffect;
import com.viraltubesolutions.viraltubeapp.utils.OnResponseListener;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeUtils;
import com.viraltubesolutions.viraltubeapp.utils.WebServices;

import static android.content.Context.MODE_PRIVATE;

public class LevelTwoFragment extends Fragment implements OnResponseListener,View.OnClickListener {
    View view;
    LinearLayout mPaymentlayout,mShortageVoteslayout;
    RelativeLayout mMainlayout;
    Button mPay,mGotoLevelOne;
    Context context;
    String userID;
    private String txnid;
    private String user_name;
    private String user_phone;
    private String user_adress;
    String user_email;
    String amt = null;
    MenuItem filter;


/*Execution flow onattach=>oncreate=>oncreateview=>onactivitycreated=>onstart=>onresume*/
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
        user_name=preferences.getString("userName", null);
        user_phone=preferences.getString("userNumber", null);
        user_email=preferences.getString("userEmail", null);

        amt="610.000";
        //Double doubleAmt = Double.valueOf(amt);
        //amt = doubleAmt.toString();


    }

    private void callCheckUserPaymentAPI() {

        if (ViralTubeUtils.isConnectingToInternet(context)) {

            WebServices<CheckUserPayment> response=new WebServices<CheckUserPayment>(LevelTwoFragment.this);
            response.checkUserPayment(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.checkUserPayment,userID);
        } else {
            // Toast.makeText(context, ""+R.string.err_msg_nointernet, Toast.LENGTH_SHORT).show();
            Snackbar.make(mPay, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.leveltwomenu, menu);
        filter=menu.findItem(R.id.filter);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(filter);
        search(searchView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {
        // refresh.setVisible(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //levelOneAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void callPaymentGateway()
    {
        mPaymentlayout.setVisibility(View.VISIBLE);
        ButtonClickEffect.addClickEffect(mPay);
        mPay.setOnClickListener(this);

    }
    public void callShortageOfVotes()
    {
        mShortageVoteslayout.setVisibility(View.VISIBLE);
        ButtonClickEffect.addClickEffect(mGotoLevelOne);
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
        Intent paymentIntent = new Intent(context, ViralTubePaymentActivity.class);
        startActivity(paymentIntent);
        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_in_from_right);

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
                        if(voteCountResponse.getVoteCount()>=100)
                        {
                            callCheckUserPaymentAPI();
                            //callPaymentGateway();

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
            case checkUserPayment:
                CheckUserPayment paymentResponse= (CheckUserPayment) response;
                if(isSucces)
                {
                    if(paymentResponse.getRESPONSECODE().equalsIgnoreCase("200") &&
                            paymentResponse.getPayment().equalsIgnoreCase("1"))
                    {
                        Fragment fragment = new AfterPaymentLevelTwoFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.leveltwoMainlayout, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                            Snackbar.make(mPay, "Transaction Successul", Snackbar.LENGTH_SHORT).show();
                    }
                    else if(paymentResponse.getRESPONSECODE().equalsIgnoreCase("409"))
                    {
                        /*Fragment fragment = new AfterPaymentLevelTwoFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.leveltwoMainlayout, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();*/
                        callPaymentGateway();
                        //Snackbar.make(mPay,"Invalid UserID",Snackbar.LENGTH_SHORT).show();

                    }
                    else if(paymentResponse.getRESPONSECODE().equalsIgnoreCase("403"))
                    {
                        Snackbar.make(mPay,"Database error",Snackbar.LENGTH_SHORT).show();

                    }

                    else if(paymentResponse.getRESPONSECODE().equalsIgnoreCase("503"))
                    {
                        Snackbar.make(mPay,"Invalid method",Snackbar.LENGTH_SHORT).show();

                    }

                }
                else {
                    Snackbar.make(mPay,"Check user payment API call Failed",Snackbar.LENGTH_SHORT).show();

                }
        }

    }

}

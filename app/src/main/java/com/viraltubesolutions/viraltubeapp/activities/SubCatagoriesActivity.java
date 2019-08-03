package com.viraltubesolutions.viraltubeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.subCatagories.SubCatagories;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.adapters.SubCatagoriesAdapter;
import com.viraltubesolutions.viraltubeapp.utils.OnResponseListener;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeUtils;
import com.viraltubesolutions.viraltubeapp.utils.WebServices;

import dmax.dialog.SpotsDialog;

public class SubCatagoriesActivity extends AppCompatActivity implements OnResponseListener {
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    SubCatagoriesAdapter mAdapter;
    Toolbar toolbar;
    String catagoryId;
    SpotsDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_catagories);
        Intent in=getIntent();
        Bundle bn=in.getExtras();
        if(bn!=null)
        {
           catagoryId= bn.getString("catagoryID");
        }
        initializeViews();
        callSubCatagoriesAPI();
    }
    private void initializeViews()
    {
        toolbar= (Toolbar) findViewById(R.id.toolbar_subcatagories);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CatagoriesActivity.class));
            }
        });*/
        //getActionBar().setHomeButtonEnabled(true);
        linearLayoutManager= new LinearLayoutManager(this);
        mRecyclerView= (RecyclerView) findViewById(R.id.vR_asc_recyclerview);
         //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void callSubCatagoriesAPI()
    {
        loadingDialog=new SpotsDialog(this,"Loading...");
        loadingDialog.show();

        if (ViralTubeUtils.isConnectingToInternet(SubCatagoriesActivity.this)) {
            WebServices<SubCatagories> response=new WebServices<SubCatagories>(SubCatagoriesActivity.this);
            response.getSubCatagories(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.subCatagories,catagoryId);
        } else {
            // Toast.makeText(context, ""+R.string.err_msg_nointernet, Toast.LENGTH_SHORT).show();
            Snackbar.make(mRecyclerView, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

        switch (URL) {
            case subCatagories:
                if(loadingDialog.isShowing())
                {
                    loadingDialog.dismiss();
                }

                SubCatagories catagoriesResponse = (SubCatagories) response;
                if (isSucces) {
                    if(catagoriesResponse.getRESPONSECODE().equalsIgnoreCase("200"))
                    {
                        if (catagoriesResponse.getData() != null) {
                            // searchingList=catagoriesResponse.getData();
                            mAdapter = new SubCatagoriesAdapter(this,catagoriesResponse.getData());
                            mRecyclerView.setAdapter(mAdapter);
                        }

                    }
                    else if(catagoriesResponse.getRESPONSECODE().equalsIgnoreCase("409"))
                    {
                        Snackbar.make(mRecyclerView,"Invalid parameters",Snackbar.LENGTH_SHORT);

                    }
                    else if(catagoriesResponse.getRESPONSECODE().equalsIgnoreCase("403"))
                    {
                        Snackbar.make(mRecyclerView,"Database error",Snackbar.LENGTH_SHORT);

                    }
                    else if(catagoriesResponse.getRESPONSECODE().equalsIgnoreCase("503"))
                    {
                        Snackbar.make(mRecyclerView," Invalid method",Snackbar.LENGTH_SHORT);

                    }

                }
                else {
                    Snackbar.make(mRecyclerView," API Call Failed ",Snackbar.LENGTH_SHORT);

                }

                break;
        }

    }

}

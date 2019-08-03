package com.viraltubesolutions.viraltubeapp.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.catagories.Catagories;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.adapters.CatagoriesAdapter;
import com.viraltubesolutions.viraltubeapp.utils.OnResponseListener;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeUtils;
import com.viraltubesolutions.viraltubeapp.utils.WebServices;

import dmax.dialog.SpotsDialog;

public class CatagoriesActivity extends AppCompatActivity implements OnResponseListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    CatagoriesAdapter mAdapter;
    Toolbar toolbar;
    SpotsDialog loadingDialog,refreshdialog;
    SwipeRefreshLayout refreshPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagories);
        initializeViews();
    }
    private void initializeViews()
    {
        toolbar= (Toolbar) findViewById(R.id.toolbar_catagories);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.jz_back_normal);


       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
            }
        });*/
        //getActionBar().setHomeButtonEnabled(true);
        refreshPage= (SwipeRefreshLayout) findViewById(R.id.vS_ac_swiperefreshlayout);
        refreshPage.setOnRefreshListener(this);

        mGridLayoutManager=new GridLayoutManager(this,2);
        mRecyclerView= (RecyclerView) findViewById(R.id.vR_ac_recyclerview);
       // mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        callCatagoriesAPI();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void callCatagoriesAPI()
    {
        loadingDialog=new SpotsDialog(this,"Loading...");
        loadingDialog.show();

            if (ViralTubeUtils.isConnectingToInternet(CatagoriesActivity.this)) {
                WebServices<Catagories> response=new WebServices<Catagories>(CatagoriesActivity.this);
                response.getcatagories(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.catagories);
            } else {
                // Toast.makeText(context, ""+R.string.err_msg_nointernet, Toast.LENGTH_SHORT).show();
                Snackbar.make(mRecyclerView, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

        switch (URL) {
            case catagories:
                if(loadingDialog.isShowing())
                {
                    loadingDialog.dismiss();
                }
                if(refreshPage.isRefreshing()) {
                    refreshPage.setRefreshing(false);
                }

                Catagories catagoriesResponse = (Catagories) response;
                if (isSucces) {
                    if(catagoriesResponse.getRESPONSECODE().equalsIgnoreCase("200"))
                    {
                        Snackbar.make(mRecyclerView,"Call adapter",Snackbar.LENGTH_SHORT);
                        if (catagoriesResponse.getData() != null) {
                           // searchingList=catagoriesResponse.getData();
                            mAdapter = new CatagoriesAdapter(this,catagoriesResponse.getData());
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

    @Override
    public void onRefresh() {
        refreshPage.setColorSchemeColors(getResources().getColor(R.color.appBlue),getResources().getColor(R.color.green));
        refreshContent();
        refreshdialog = new SpotsDialog(this,"Refreshing Please wait...");
        refreshdialog.show();

    }

    public void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callCatagoriesAPI();
                //callgetOriginalVideosAPI();
                //refreshPage.setBackgroundColor(getResources().getColor(R.color.gray));
                refreshPage.setRefreshing(false);
                refreshdialog.dismiss();

            }
        },3000);
    }

}

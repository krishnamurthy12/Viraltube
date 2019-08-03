package com.viraltubesolutions.viraltubeapp.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadedVideoResponse.UploadedVideosResponse;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.activities.CatagoriesActivity;
import com.viraltubesolutions.viraltubeapp.adapters.LevelTwoAfterPaymentAdapter;
import com.viraltubesolutions.viraltubeapp.utils.EndlessRecyclerOnScrollListener;
import com.viraltubesolutions.viraltubeapp.utils.OnResponseListener;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeUtils;
import com.viraltubesolutions.viraltubeapp.utils.WebServices;

import cn.jzvd.JZVideoPlayer;
import dmax.dialog.SpotsDialog;

import static android.content.Context.MODE_PRIVATE;


public class AfterPaymentLevelTwoFragment extends Fragment implements View.OnClickListener,
        OnResponseListener, SwipeRefreshLayout.OnRefreshListener
{

    RecyclerView mRecyclerView;
    LinearLayout novideosLayout;
    FloatingActionButton fab;
    public LevelTwoAfterPaymentAdapter levelTwoAfterPaymentAdapter;
    SwipeRefreshLayout refreshPage;
    View view;
    Context context;
    String userID;
    SpotsDialog dialog;
    SpotsDialog refreshdialog;
    MenuItem refresh;
    LinearLayoutManager mLayoutManager;

    final int PERMISSION_ALL = 100;
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getContext().getTheme().applyStyle(R.style.colorControlHighlight_blue, true);
        this.view = inflater.inflate(R.layout.fragment_after_payment_level_two, container, false);
        init();
        return view;
    }

    // The request code used in ActivityCompat.requestPermissions()
// and returned in the Activity's onRequestPermissionsResult()


  /*  public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }*/

    private void init() {
       /* if(!hasPermissions(context, PERMISSIONS))
        {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        }
*/
        //checkPermissionForStorage();
        SharedPreferences preferences = context.getSharedPreferences("LogIn", MODE_PRIVATE);
        userID=preferences.getString("userID",null);
        refreshPage=view.findViewById(R.id.vS_fap_swiperefresh);
        refreshPage.setOnRefreshListener(this);

        Log.d("userid",userID);

        mRecyclerView = view.findViewById(R.id.vR_fap_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        novideosLayout=view.findViewById(R.id.nomatch_layout);

        fab = (FloatingActionButton) view.findViewById(R.id.vF_fap_floatingbtn);
        fab.setOnClickListener(this);

        //refreshPage=view.findViewById(R.id.swipeRefreshLayout);
        //refreshPage.setOnRefreshListener(this);
        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //JZVideoPlayer.releaseAllVideos();
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>90)
                    JZVideoPlayer.releaseAllVideos();
            }

        });
        CallGetVideosAPI();
        //callgetOriginalVideosAPI();
    }

    private void CallGetVideosAPI() {

        if (ViralTubeUtils.isConnectingToInternet(context)) {
            dialog=new SpotsDialog(context,"Loading your Videos please wait...");
            dialog.setCancelable(false);
            dialog.show();

            WebServices<UploadedVideosResponse> response=new WebServices<UploadedVideosResponse>(AfterPaymentLevelTwoFragment.this);
            response.getVideos(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.Getvideos,userID, 1);
        } else {
            // Toast.makeText(context, ""+R.string.err_msg_nointernet, Toast.LENGTH_SHORT).show();
            Snackbar.make(fab, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.vF_fap_floatingbtn) {
            {
                loadCatagories();

            }
        }
    }
    private void loadCatagories()
    {
        Intent catagoriesIntent=new Intent(context, CatagoriesActivity.class);
        startActivity(catagoriesIntent);
        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_in_from_right);
    }

 /*   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.leveltwomenu, menu);
        MenuItem filter=menu.findItem(R.id.filter);
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
    }*/
    @Override
    public void onRefresh() {
        refreshPage.setColorSchemeColors(getResources().getColor(R.color.appBlue),getResources().getColor(R.color.green));
        refreshContent();
        refreshdialog = new SpotsDialog(context,"Refreshing Please wait...");
        refreshdialog.show();

    }

    public void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CallGetVideosAPI();
                refreshPage.setRefreshing(false);
                refreshdialog.dismiss();

            }
        },3000);
    }




/*

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
*/

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
        switch (URL)
        {
            case Getvideos:
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }
                if(isSucces)
                {
                    UploadedVideosResponse details= (UploadedVideosResponse) response;

                    if(details!=null)
                    {
                        novideosLayout.setVisibility(View.GONE);
                        if(details.getRESPONSECODE().equalsIgnoreCase("200")) {
                            if(refreshPage.isRefreshing()) {
                                refreshPage.setRefreshing(false);
                            }
                            if (details.getData() != null) {

                                levelTwoAfterPaymentAdapter = new LevelTwoAfterPaymentAdapter(context,details.getData(),mRecyclerView,userID,
                                        AfterPaymentLevelTwoFragment.this,novideosLayout);
                                mRecyclerView.setAdapter(levelTwoAfterPaymentAdapter);
                            }
                        }
                    }
                }
                else{
                    novideosLayout.setVisibility(View.VISIBLE);
                    Snackbar.make(mRecyclerView,"Failed to get videos", Snackbar.LENGTH_SHORT).show();
                }
                break;

        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    return true;
                }
                return false;
            }
        });
    }

}


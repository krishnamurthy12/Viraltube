package com.viraltubesolutions.viraltubeapp.fragments;


import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.SelfUploadResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.fcmResponse.FCMResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.numberOfVotes.NumOfVotesResponse;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.originalVideos.OriginalVideos;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadedVideoResponse.Datum;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadedVideoResponse.UploadedVideosResponse;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.adapters.LevelOneAdapter;
import com.viraltubesolutions.viraltubeapp.utils.OnResponseListener;
import com.viraltubesolutions.viraltubeapp.utils.ProgressRequestBody;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeUtils;
import com.viraltubesolutions.viraltubeapp.utils.WebServices;
import com.viraltubesolutions.viraltubeapp.videocompression.MediaController;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class LevelOneFragment extends Fragment implements View.OnClickListener,OnResponseListener,
        SwipeRefreshLayout.OnRefreshListener,
        UploadDialogFragment.uploadDialogInterface,ProgressRequestBody.UploadCallbacks {

    RecyclerView recyclerView;
    LinearLayout novideosLayout;
    FloatingActionButton fab;
    public LevelOneAdapter levelOneAdapter;
    private static final int REQUEST_VIDEO_CAPTURE = 1,REQUEST_STORAGE_PERMISSION=2;
    SwipeRefreshLayout refreshPage;
    VideoView mVideoView;
    View view;
    String filePath,videoTitle,compressedPath,optionalTag;
    Context context;
    ProgressDialog progressDialog;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    String userID,refreshedToken;
    int votes;
    List<Datum> searchingList;
    SpotsDialog dialog;
    SpotsDialog refreshdialog;
    Button mVotes;
    MenuItem refresh;
    private boolean _hasLoadedOnce= false;

    private boolean isCameraPermissionGranted = false,isStoragePermissionGranted = false;;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getContext().getTheme().applyStyle(R.style.colorControlHighlight_blue, true);
        this.view = inflater.inflate(R.layout.fragment_level_one, container, false);
        init();
        //hasOptionsMenu();
        return view;
    }

    private void init() {
        checkPermissionForStorage();
        SharedPreferences preferences = context.getSharedPreferences("LogIn", MODE_PRIVATE);
        userID=preferences.getString("userID",null);

        Log.d("userid",userID);

        SharedPreferences fcmokenPreference = context.getSharedPreferences("FCM", MODE_PRIVATE);
        refreshedToken=fcmokenPreference.getString("RefreshedToken",null);

        callUpdateFCMAPI();

        recyclerView = view.findViewById(R.id.recyclerView_level1);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        searchingList=new ArrayList<>();
        novideosLayout=view.findViewById(R.id.nomatch_layout);


        progressDialog=new ProgressDialog(context);
        notificationManager=(NotificationManager)getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE );

        fab = (FloatingActionButton) view.findViewById(R.id.fab_level1);
        mVideoView=view.findViewById(R.id.video_view);
        fab.setOnClickListener(this);

        refreshPage=view.findViewById(R.id.swipeRefreshLayout);
        refreshPage.setOnRefreshListener(this);
       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
            }
        });
*/
        CallGetVideosAPI();
        //callgetOriginalVideosAPI();
    }
    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
            if (isFragmentVisible_ && !_hasLoadedOnce) {
                _hasLoadedOnce = true;
                //  getContacts1();
                CallGetVideosAPI();

            }
            _hasLoadedOnce = false;
        }
    }
    private void callUpdateFCMAPI()
    {
        if (ViralTubeUtils.isConnectingToInternet(context)) {
            WebServices<FCMResponse> response=new WebServices<FCMResponse>(LevelOneFragment.this);
            response.updateFCM(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.updateFCMKey,refreshedToken,userID);
        } else {
            // Toast.makeText(context, ""+R.string.err_msg_nointernet, Toast.LENGTH_SHORT).show();
            Snackbar.make(fab, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }

    }

    private void CallGetVideosAPI() {

        if (ViralTubeUtils.isConnectingToInternet(context)) {
            dialog=new SpotsDialog(context,"Loading your Videos please wait...");
            dialog.setCancelable(false);
            dialog.show();

            WebServices<UploadedVideosResponse> response=new WebServices<UploadedVideosResponse>(LevelOneFragment.this);
            response.getVideos(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.Getvideos,userID);
        } else {
           // Toast.makeText(context, ""+R.string.err_msg_nointernet, Toast.LENGTH_SHORT).show();
           Snackbar.make(fab, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void callgetOriginalVideosAPI() {

        if (ViralTubeUtils.isConnectingToInternet(context)) {
            dialog=new SpotsDialog(context,"Loading your Videos please wait...");
            dialog.show();

            WebServices<OriginalVideos> response=new WebServices<OriginalVideos>(LevelOneFragment.this);
            response.getOriginalVideos(WebServices.BASE_URL, WebServices.ApiType.getOriginalVideos,userID);
        } else {
            // Toast.makeText(context, ""+R.string.err_msg_nointernet, Toast.LENGTH_SHORT).show();
            Snackbar.make(fab, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void callSelfUploadAPI() {
        compressedPath= MediaController.cachedFile.getPath(); // will give string path;
        File compressedFile=new File(compressedPath);
        String uploadtype="self";

        RequestBody ftitle=RequestBody.create(MediaType.parse("multipart/form-data"), videoTitle);
        RequestBody ftag=RequestBody.create(MediaType.parse("multipart/form-data"), optionalTag);
        RequestBody ftype=RequestBody.create(MediaType.parse("text/plain"), uploadtype);
        RequestBody fid = RequestBody.create(MediaType.parse("text/plain"),userID);

        RequestBody fpath = RequestBody.create(MediaType.parse("video/*"), compressedFile);
        ProgressRequestBody progresssBody=new ProgressRequestBody(compressedFile,this);
        MultipartBody.Part myFile=MultipartBody.Part.createFormData("myFile", compressedFile.getName(), progresssBody);

        if (ViralTubeUtils.isConnectingToInternet(getContext())) {

            WebServices response = new WebServices(LevelOneFragment.this);
            response.selfVideoUpload(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.selfuploadVideo,ftitle,ftag,myFile,fid,ftype);
        } else {
            Snackbar.make(recyclerView, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }
    }
    @Override
    public void senddata(String title,String optionaltag) {
        this.videoTitle=title;
        this.optionalTag=optionaltag;
        new VideoCompressor().execute();
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fab_level1) {
            {
                checkPermissionsForcamera();
                if (isCameraPermissionGranted) {
                    Log.d("permissionCheck", "granted");
                    takeVideo();
                    }

            }
        }
    }

    private void takeVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }

    }
    private void checkPermissionForStorage()
    {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            isStoragePermissionGranted = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d("permissionCheck", "marshmellow device");
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);

            }
        }
        else{
            isStoragePermissionGranted=true;
        }

    }

    private void checkPermissionsForcamera() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            isCameraPermissionGranted = false;

                Log.d("permissionCheck", "permissions not granted");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    Log.d("permissionCheck", "marshmellow device");
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_VIDEO_CAPTURE);

                }
                else{
                    isCameraPermissionGranted = true;
                }
        }
       else{
            isCameraPermissionGranted = true;
           }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try

        {
            if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) ;
            {
                Uri videoUri = data.getData();
                Log.d("videoUri",videoUri+"");
                try {
                    filePath = getFilePath(context, videoUri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                if (filePath != null) {
                    File file = new File(filePath);
                    Log.d("beforecompression", "" + file.getAbsolutePath());
                    callUploadDialog();
                    //new VideoCompressor().execute();
                }

            }
            if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode != RESULT_OK) {
                Toast.makeText(getActivity(), "Recording cancelled", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            if ((permissions[0].equals(Manifest.permission.CAMERA)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                isCameraPermissionGranted = true;
                takeVideo();

            } else {

                Toast.makeText(context, "you dont have permission to access camera features", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == REQUEST_STORAGE_PERMISSION) {
                if((permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    isStoragePermissionGranted=true;
                }
                else
                {

                    Toast.makeText(context, "you dont have permission for Storage", Toast.LENGTH_SHORT).show();
                }

            }

         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
//to get swipe refreshing of page
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
                //callgetOriginalVideosAPI();
                //refreshPage.setBackgroundColor(getResources().getColor(R.color.gray));
                refreshPage.setRefreshing(false);
                refreshdialog.dismiss();

            }
    },3000);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_items, menu);
        MenuItem search=menu.findItem(R.id.action_search);
        refresh=menu.findItem(R.id.action_refresh);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_refresh)
        {
            refreshContent();
            refreshdialog = new SpotsDialog(context,"Refreshing Please wait...");
            refreshdialog.show();
            return true;

        }

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

                levelOneAdapter.getFilter().filter(newText);
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
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
        switch (URL)
        {
            /*case getOriginalVideos:
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }
                OriginalVideos getallVideos= (OriginalVideos) response;
                if(isSucces)
                {
                    if (getallVideos.getVideos() != null) {
                        searchingList=getallVideos.getVideos();
                        levelOneAdapter = new LevelOneAdapter(context,getallVideos.getVideos(),recyclerView,userID,LevelOneFragment.this);
                        recyclerView.setAdapter(levelOneAdapter);
                    }


                }
                break;*/

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
                                searchingList=details.getData();
                                levelOneAdapter = new LevelOneAdapter(context,details.getData(),recyclerView,userID,
                                        LevelOneFragment.this,refreshedToken,novideosLayout);
                                recyclerView.setAdapter(levelOneAdapter);
                            }
                        }
                    }
                }
                else{
                    novideosLayout.setVisibility(View.VISIBLE);
                    Snackbar.make(recyclerView,"Failed to get videos", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case selfuploadVideo:
                SelfUploadResponse selfUpload= (SelfUploadResponse) response;
                if(isSucces) {
                    if (selfUpload.getRESPONSECODE().equalsIgnoreCase("200")) {

                        Snackbar.make(fab, videoTitle+".mp4 Uploaded successfully", Snackbar.LENGTH_SHORT).show();
                        if(progressDialog.isShowing())
                        {
                            progressDialog.setProgress(100);
                            progressDialog.dismiss();
                            Snackbar.make(fab, "Uploaded successfully", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case updateFCMKey:
                FCMResponse fcmResponse= (FCMResponse) response;
                if(isSucces)
                {
                    if(fcmResponse.getRESPONSECODE().equalsIgnoreCase("200"))
                    {
                        Log.d("FCMKey",refreshedToken);
                    }
                }
                break;
            case getvotes:
                NumOfVotesResponse votesResponse= (NumOfVotesResponse) response;
                if(isSucces) {
                    if (votesResponse.getRESPONSECODE().equalsIgnoreCase("200")) {
                        votes++;
                        mVotes.setBackgroundResource(R.drawable.heart_shape_blued);

                    }


                }

        }
    }

    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void onProgressUpdate(int percentage) {
        // set current progress
        /*progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Uploading...");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(percentage);
        progressDialog.show();*/
        builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Uploading Video..")
                .setContentText(videoTitle+".mp4")
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setProgress(100,percentage,true)
                .setAutoCancel(false);
        try{
            notificationManager.notify(0, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError() {
        //Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFinish() {
        builder.setContentTitle(videoTitle+".mp4 Uploaded successfully")
                .setSmallIcon(android.R.drawable.stat_sys_upload_done)
                .setProgress(100, 100, false)
                .setAutoCancel(false);
        try{
            notificationManager.notify(0, builder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }

       // progressDialog.setProgress(100);
        //progressDialog.dismiss();
    }
    private void callUploadDialog()
    {
        UploadDialogFragment fragment = new UploadDialogFragment();
        fragment.setTargetFragment(this, 0);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
        fragment.show(ft, "UploadDialogFragment");
        fragment.setCancelable(false);
    }


    public class VideoCompressor extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*compressionDialog=new ProgressDialog(context);
            compressionDialog.setTitle("Compressing your video");
            compressionDialog.setMessage("please wait...");
            compressionDialog.setCancelable(false);
            compressionDialog.setIndeterminate(false);
            compressionDialog.show();*/
            Snackbar.make(recyclerView,"Compressing your video in background", Snackbar.LENGTH_SHORT).show();
            notificationManager=(NotificationManager)getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE );
            builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("Compressing  your Video..")
                    .setContentText(videoTitle+".mp4")
                    .setSmallIcon(android.R.drawable.stat_sys_upload)
                    .setProgress(0, 0, true)
                    .setAutoCancel(false);
            try{
                notificationManager.notify(0, builder.build());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return MediaController.getInstance().convertVideo(filePath);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            if (compressed) {
                //compressionDialog.dismiss();
                notificationManager=(NotificationManager)getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE );
                builder = new NotificationCompat.Builder(context);
                builder.setContentTitle("Compression done")
                        .setContentText(compressedPath+".mp4")
                        .setSmallIcon(android.R.drawable.stat_sys_upload)
                        .setProgress(100,100, true);
               // Snackbar.make(recyclerView,"Compression successful",Snackbar.LENGTH_SHORT).show();
                builder.setContentTitle(videoTitle+".MP4 Compressed Suceessfully")
                        .setSmallIcon(android.R.drawable.stat_sys_upload)
                        .setProgress(100, 100, false)
                        .setAutoCancel(false);
                try{
                    notificationManager.notify(0, builder.build());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                callSelfUploadAPI();
                Log.d("after_compression", "Compression successfully!");
                Log.d("after_compression", "Compressed File Path" + MediaController.cachedFile.getPath());

            }
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

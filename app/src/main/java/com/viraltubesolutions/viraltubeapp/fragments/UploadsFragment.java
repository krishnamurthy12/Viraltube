package com.viraltubesolutions.viraltubeapp.fragments;


import android.Manifest;
import android.app.NotificationManager;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.SelfUploadResponse;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.customs.CircleImageView;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomEditText;
import com.viraltubesolutions.viraltubeapp.utils.OnResponseListener;
import com.viraltubesolutions.viraltubeapp.utils.ProgressRequestBody;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeUtils;
import com.viraltubesolutions.viraltubeapp.utils.WebServices;
import com.viraltubesolutions.viraltubeapp.videocompression.MediaController;

import java.io.File;
import java.net.URISyntaxException;

import cn.jzvd.JZVideoPlayerStandard;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class UploadsFragment extends Fragment implements View.OnClickListener,ProgressRequestBody.UploadCallbacks,
        OnResponseListener {

     ImageView mBtnUpload,mBtnCancel,mBtnConfirmUpload;
    LinearLayout layout1;
    RelativeLayout layout2;
    CircleImageView mImageThumb;
    MyCustomEditText mvideoTitle,mtag;
    private static final int SELECT_VIDEO=100,REQUEST_STORAGE_PERMISSION=2;
    JZVideoPlayerStandard jzVideoPlayerStandard;
    String selectedFilePath,compressedPath;
    Context context;
    public String videoTitle;
    public String optionalTag;
    View view;
    File compressedFile;
    String userID;
    boolean isUploading=false,isStoragePermissionGranted = false;


    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    //VideoView video;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override

        public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            this.view = inflater.inflate(R.layout.fragment_uploads, container, false);
            initialize();
            return view;
        }
        public void initialize()
        {
            mBtnUpload = view.findViewById(R.id.uploadVideo_Btn);
            layout1 = view.findViewById(R.id.layout_upload_videos);
            layout2 = view.findViewById(R.id.layout_containing_uploadbutton);
            mBtnCancel = view.findViewById(R.id.imageButton_cancel_upload);
            mBtnConfirmUpload=view.findViewById(R.id.imageButton_continue_upload);
            mImageThumb=view.findViewById(R.id.imageview_account);
            mvideoTitle=view.findViewById(R.id.vE_uf_videotitle);
            mtag=view.findViewById(R.id.vE_uf_optionaltag);

            SharedPreferences preferences = context.getSharedPreferences("LogIn", MODE_PRIVATE);
            userID=preferences.getString("userID",null);
            setdata();

        }
        public void setdata()
        {
            JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) view.findViewById(R.id.videoView);
            jzVideoPlayerStandard.setUp("http://viraltube.co.in/viral/Viraltube_Video.mp4"
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
           // jzVideoPlayerStandard.thumbImageView.setImageResource(R.drawable.age);
           // Glide.with(context).load("http://viraltube.co.in/viral/kt_talent.jpeg").into(jzVideoPlayerStandard);
            Glide.with(context)
                    .load("http://viraltube.co.in/viral/kt_talent.jpeg")
                    .into(jzVideoPlayerStandard.thumbImageView);

            mBtnUpload.setOnClickListener(this);

            mBtnCancel.setOnClickListener(this);

            mBtnConfirmUpload.setOnClickListener(this);

        }
        private void callUploadFromGalleryAPI()
        {
            //compressedPath= MediaController.cachedFile.getPath(); // will give string path;
            //compressedFile=new File(compressedPath);
            File compressedFile=new File(selectedFilePath);
            String upload_type="normal";

            RequestBody ftitle=RequestBody.create(MediaType.parse("multipart/form-data"), videoTitle);
            RequestBody ftag=RequestBody.create(MediaType.parse("multipart/form-data"), optionalTag);
            RequestBody fid = RequestBody.create(MediaType.parse("text/plain"),userID);
            RequestBody ftype=RequestBody.create(MediaType.parse("text/plain"), upload_type);

            RequestBody fpath = RequestBody.create(MediaType.parse("video/*"), compressedFile);
            ProgressRequestBody progresssBody=new ProgressRequestBody(compressedFile,this);
            MultipartBody.Part myFile=MultipartBody.Part.createFormData("myFile", compressedFile.getName(), progresssBody);

            if (ViralTubeUtils.isConnectingToInternet(getContext())) {

               WebServices response = new WebServices(UploadsFragment.this);
              response.uploadFromGallery(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.uploadVideofromGallery,ftitle,myFile,ftag,fid,ftype);
            } else {
                Snackbar.make(mBtnConfirmUpload, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
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
            else{
                isStoragePermissionGranted=true;
            }
        }
        else{
            isStoragePermissionGranted=true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if ((permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
               selectVideo();

            } else {

                Toast.makeText(context, "you dont have permission for Storage", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SELECT_VIDEO) {
            Uri selectedUri = data.getData();
            Glide.with(this).load(selectedUri).into(mImageThumb);

            try {
                selectedFilePath = getFilePath(getContext(), selectedUri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (selectedFilePath == null) {
//                Snackbar.make(mBtnConfirmUpload,"Please select a video",Snackbar.LENGTH_SHORT).show();
//                layout1.setVisibility(View.VISIBLE);
//                layout2.setVisibility(View.GONE);
                //File file = new File(selectedFilePath);
                //Log.d("beforecompression", "" + file.getAbsolutePath());
            }


        }
        else if(requestCode==SELECT_VIDEO && resultCode==RESULT_CANCELED)
        {
            layout2.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.GONE);
            //Snackbar.make(mBtnConfirmUpload,"Please select a video",Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.uploadVideo_Btn:
                if(!isUploading) {
                    checkPermissionForStorage();
                    if(isStoragePermissionGranted)
                    {
                        selectVideo();

                    }

                }
                else{
                    Snackbar.make(mBtnUpload,"Another video is uploading Plese wait", Snackbar.LENGTH_SHORT).show();
                }
                    break;

            case R.id.imageButton_continue_upload:

                try {
                    //InputMethodManager is used to hide the virtual keyboard from the user after finishing the user input
                    final InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
                 catch (NullPointerException e) {
                    Log.e("Exception", e.getMessage() + ">>");
                }
                //if(builder.getWhenIfShowing()==100)

                //Toast.makeText(getActivity(), "uploaded successfully", Toast.LENGTH_SHORT).show();
                this.videoTitle=mvideoTitle.getText().toString();
                this.optionalTag=mtag.getText().toString();
                if (selectedFilePath == null) {
                    Snackbar.make(mBtnConfirmUpload,"Please select a video",Snackbar.LENGTH_SHORT).show();
                }
                else if(videoTitle.isEmpty())
                {
                    Snackbar.make(mBtnUpload,"Plese provide the Video title", Snackbar.LENGTH_SHORT).show();
                    break;
                }
                else {
                    isUploading=true;
                    //new VideoCompressor().execute();
                    callUploadFromGalleryAPI();

                    layout2.setVisibility(View.VISIBLE);
                    layout1.setVisibility(View.GONE);
                    break;
                }

            case R.id.imageButton_cancel_upload:
                try {
                    //InputMethodManager is used to hide the virtual keyboard from the user after finishing the user input
                    final InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
                catch (NullPointerException e) {
                    Log.e("Exception", e.getMessage() + ">>");
                }
                mvideoTitle.setText("");
                mtag.setText("");

                layout2.setVisibility(View.VISIBLE);
                layout1.setVisibility(View.GONE);
                break;

            default:
                Toast.makeText(getActivity(), "wrong choice", Toast.LENGTH_SHORT).show();
        }

    }
    private void selectVideo()
    {
        Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
        mediaChooser.setType("video/*");
        startActivityForResult(mediaChooser, SELECT_VIDEO);

        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.GONE);
    }


    @Override
    public void onProgressUpdate(int percentage) {
        notificationManager=(NotificationManager)getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE );
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

    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
        switch (URL)
        {
            case uploadVideofromGallery:
                SelfUploadResponse selfUpload= (SelfUploadResponse) response;
                if(isSucces) {
                    if (selfUpload.getRESPONSECODE().equalsIgnoreCase("200")) {
                        isUploading=false;
                        Snackbar.make(mBtnConfirmUpload, videoTitle+".mp4 Uploaded successfully", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    isUploading=false;
                    Snackbar.make(mBtnConfirmUpload, videoTitle+"Failed to Uploaded your video please try again", Snackbar.LENGTH_SHORT).show();
                }
                break;
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
    public class VideoCompressor extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Snackbar.make(mImageThumb,"Compressing your video in background", Snackbar.LENGTH_SHORT).show();
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
            return MediaController.getInstance().convertVideo(selectedFilePath);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            if (compressed) {
                builder.setContentTitle(videoTitle+".MP4 Compressed Suceessfully")
                        .setSmallIcon(android.R.drawable.stat_sys_upload_done)
                        .setProgress(100, 100, false)
                        .setAutoCancel(false);
                try{
                    notificationManager.notify(0, builder.build());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                callUploadFromGalleryAPI();
                Log.d("after_compression", "Compression successfully!");
                Log.d("after_compression", "Compressed File Path" + MediaController.cachedFile.getPath());

            }
        }
    }
}

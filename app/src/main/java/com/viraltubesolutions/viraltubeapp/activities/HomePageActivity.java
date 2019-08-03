package com.viraltubesolutions.viraltubeapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.Abhi.AbhiDemo;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.callus.CallUs;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadprofilepic.UploadProfilePic;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.userLogIn.UserLoginResults;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.adapters.MyPagerAdapter;
import com.viraltubesolutions.viraltubeapp.customs.CircleImageView;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomTextView;
import com.viraltubesolutions.viraltubeapp.fragments.LogOutDialogFragment;
import com.viraltubesolutions.viraltubeapp.utils.OnResponseListener;
import com.viraltubesolutions.viraltubeapp.utils.ProgressRequestBody;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeUtils;
import com.viraltubesolutions.viraltubeapp.utils.WebServices;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.jzvd.JZVideoPlayer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.viraltubesolutions.viraltubeapp.app_controller.AppController.context;

public class HomePageActivity extends AppCompatActivity implements OnResponseListener, ProgressRequestBody.UploadCallbacks, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static TabLayout tabLayout1;
    Toolbar toolbar;
    MyPagerAdapter adapter;
    public TabLayout tabLayout;
    public boolean doubleBackToExitPressedOnce = false;
    public ViewPager viewPager;
    NavigationView navigationview;
    CircleImageView userPic;
    MyCustomTextView userName;
    FloatingActionButton uploadBtn;
    String logInName, logInEmail, imageUrl, password, userID;
    String userChoosenTask;
    String selectedFilePath;
    ProgressDialog uploadingDialog, loginDialog;
    private static final int SELECT_PICTURE = 100, CAPTURE_PICTURE = 101;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private Uri fileUri;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getLoginSharedPreferences();
        initializetoolBar();
    }

    private void getLoginSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("LogIn", MODE_PRIVATE);
        logInName = preferences.getString("userName", "No name found");
        logInEmail = preferences.getString("userEmail", null);
        password = preferences.getString("password", null);
        userID = preferences.getString("userID", null);
        imageUrl = preferences.getString("image", null);
    }

    private void initializetoolBar() {
        navigationview = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationview.getHeaderView(0);
        userPic = headerView.findViewById(R.id.vI_nh_userpic);
        userName = headerView.findViewById(R.id.vT_nh_username);
        uploadBtn = headerView.findViewById(R.id.vF_nh_upload);
        navigationview.setNavigationItemSelectedListener(this);
        uploadBtn.setOnClickListener(this);
        userName.setText(logInName);
        if (imageUrl == null) {
            Glide.with(this)
                    .load("http://viraltube.co.in/viral/kt_talent.jpeg")
                    .into(userPic);
        } else {
            Glide.with(this)
                    .load(imageUrl)
                    .into(userPic);

        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout1 = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);

        tabLayout.addTab(tabLayout.newTab().setText("Level1"));
        tabLayout.addTab(tabLayout.newTab().setText("Uploads"));
        tabLayout.addTab(tabLayout.newTab().setText("Level2"));
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        adapter = new MyPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    void callUserLoginAPI() {
        if (ViralTubeUtils.isConnectingToInternet(getApplicationContext())) {
            WebServices<UserLoginResults> webServices = new WebServices<>(HomePageActivity.this);
            webServices.userLogIn(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.userlogin,
                    logInEmail, password);
        } else {
            Snackbar.make(tabLayout, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }

    }

    void callUploadProfilePic(String filepath) {
        uploadingDialog = new ProgressDialog(this);
        uploadingDialog.setCancelable(false);
        uploadingDialog.setTitle("Uploading profile picture..");
        uploadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        uploadingDialog.show();

        //String compressedPath= MediaController.cachedFile.getPath(); // will give string path;
        File file = new File(filepath);
        Log.d("inside apicall", file.getAbsolutePath());

        //To encode/serialize plain userId into  RequestBody type
        RequestBody fid = RequestBody.create(MediaType.parse("text/plain"), userID);

        RequestBody fpath = RequestBody.create(MediaType.parse("image/*"), file);
        ProgressRequestBody progresssBody = new ProgressRequestBody(file, this);
        MultipartBody.Part myFile = MultipartBody.Part.createFormData("myFile", file.getName(), progresssBody);

        Log.d("inside apicall", myFile.toString());
        //MultipartBody.Part myFile=MultipartBody.Part.createFormData("myFile", compressedFile.getName(), fpath);
        if (ViralTubeUtils.isConnectingToInternet(getApplicationContext())) {

            WebServices<UploadProfilePic> webServices = new WebServices<UploadProfilePic>(HomePageActivity.this);
            webServices.uploadProfilePic(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.uploadProfilePic, fid, myFile);
        } else {
            Snackbar.make(tabLayout, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }

    }

    void callContactUsAPI() {
        if (ViralTubeUtils.isConnectingToInternet(getApplicationContext())) {
            WebServices<CallUs> webServices = new WebServices<>(HomePageActivity.this);
            webServices.callUs(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.callUs);
        } else {
            Snackbar.make(tabLayout, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_events) {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            Toast.makeText(this, "Currently no Events navailable", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_aboutus) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
//            Intent aboutus=new Intent(HomePageActivity.this,AboutUsActivity.class);
//            startActivity(aboutus);

           /* String url = "http://viraltube.co.in/vt/about-us.html";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);*/
            Intent aboutus = new Intent(HomePageActivity.this, WebViewActivity.class);
            aboutus.putExtra("aboutusurl", "http://viraltube.co.in/vt/about-us.html");
            startActivity(aboutus);

        } else if (id == R.id.nav_rateus) {
            Intent play = new Intent(Intent.ACTION_VIEW);
            play.setData(Uri.parse("market://details?id=" + getPackageName()));
            startActivity(play);
        } else if (id == R.id.nav_feedback) {
        /*    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/html");
            sharingIntent.putExtra(Intent.EXTRA_TEXT,
                    "Type your expirence");
            sharingIntent.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{"info@viraltube.co.in"});
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
                    "Feedback for Viral Tube App");
            startActivity(Intent.createChooser(sharingIntent, "Share using"));*/


            String[] to = {"info@viraltube.co.in"};
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setData(Uri.parse("mailto:"));
            sharingIntent.setType("message/rfc822");
            sharingIntent.putExtra(Intent.EXTRA_EMAIL, to);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
                    "Feedback for Viral Tube App");
            sharingIntent.putExtra(Intent.EXTRA_TEXT,
                    "Type your expirence");

            try {
                startActivity(Intent.createChooser(sharingIntent,
                        "Share using"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this,
                        "No email clients installed.",
                        Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_callus) {
            callContactUsAPI();
        } else if (id == R.id.nav_logout) {
            callLogoutdialog();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void callLogoutdialog() {
        LogOutDialogFragment logout = new LogOutDialogFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
        logout.setCancelable(false);
        logout.show(ft, "logoutFragment");
    }

    public static void changePage() {
        tabLayout1.getTabAt(3).select();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (doubleBackToExitPressedOnce) {
            this.finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(toolbar, R.string.back_press_to_exit, Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }


    @Override
    protected void onStop() {
        super.onStop();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onClick(View v) {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result=Utility.checkPermission(HomePageActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    //if(result)
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    //if(result)
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    private void cameraIntent() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(intent, CAPTURE_PICTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(Intent.createChooser(intent, "Complete by using"), CAPTURE_PICTURE);

        }
    }

    private void galleryIntent() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_PICTURE);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ALL) {
            if ((permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) &&
                    (permissions[1].equals(Manifest.permission.CAMERA)
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                selectImage();

            }
        }
    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
        switch (URL) {
            case uploadProfilePic:
                if (uploadingDialog.isShowing()) {
                    uploadingDialog.dismiss();
                }
                UploadProfilePic uploadProfilePic = (UploadProfilePic) response;
                if (isSucces) {
                    if (uploadProfilePic.getRESPONSECODE().equalsIgnoreCase("200")) {
                        Glide.with(this)
                                .load(selectedFilePath)
                                .into(userPic);
                        //callUserLoginAPI();
                        Snackbar.make(tabLayout, "Successfully updated Profile picture", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(tabLayout, "Failed to update", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(tabLayout, "API call Failed", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case userlogin:
                UserLoginResults res = (UserLoginResults) response;
                String userID = res.getId();
               /* if(loginDialog.isShowing())
                {
                    loginDialog.dismiss();
                }*/

                if (isSucces) {
                    if (res != null) {
                        if (!userID.isEmpty()) {
                            if (res.getRESPONSECODE().equalsIgnoreCase("200")) {
                                imageUrl = res.getPicture();
                                String id = res.getId();
                                String name = res.getName();
                                String mob = res.getMobile();
                                String mail = res.getEmail();
                                String image = res.getPicture();

                                SharedPreferences loginPref = getSharedPreferences("LogIn", MODE_PRIVATE);
                                SharedPreferences.Editor editor = loginPref.edit();
                                editor.putBoolean("isLoggedin", true);
                                editor.putString("userID", id);
                                editor.putString("userName", name);
                                editor.putString("userNumber", mob);
                                editor.putString("userEmail", mail);
                                editor.putString("password", password);
                                editor.putString("image", image);
                                editor.apply();

                                Glide.with(this)
                                        .load(imageUrl)
                                        .into(userPic);
                                //Snackbar.make(tabLayout, "LogIn Suceess", Snackbar.LENGTH_SHORT).show();
                            } else if (res.getRESPONSECODE().equalsIgnoreCase("409")) {

                                Snackbar.make(tabLayout, R.string.err_parameter, Snackbar.LENGTH_SHORT).show();

                            } else if (res.getRESPONSECODE().equalsIgnoreCase("503")) {
                                Snackbar.make(tabLayout, R.string.err_server, Snackbar.LENGTH_SHORT).show();

                            } else if (res.getRESPONSECODE().equalsIgnoreCase("402")) {

                                //wrong password
                                Snackbar.make(tabLayout, "Wrong Password", Snackbar.LENGTH_SHORT).show();
                                //RJSnackBar.makeText(LoginActivity.this, "Wrong Password", RJSnackBar.LENGTH_SHORT).show();


                            }
                        }
                    }
                    break;
                }
            case callUs:
                if (isSucces) {
                    CallUs callUs = (CallUs) response;
                    String number = callUs.getNumber();
                    String responsecode = callUs.getRESPONSECODE();
                    if (responsecode.equalsIgnoreCase("200")) {
                        if (number != null) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
                            startActivity(intent);

                           /* Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "Your Phone_number"));
                            startActivity(intent);*/

                        } else {
                            Snackbar.make(tabLayout, "Something went wromng, Try again", Snackbar.LENGTH_SHORT).show();

                        }

                    } else {
                        Snackbar.make(tabLayout, "Something went wromng, Try again", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(tabLayout, "Something went wromng, Try again", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
                Uri selectedUri = data.getData();
                //Glide.with(this).load(selectedUri).into(userPic);
                try {
                    selectedFilePath = getFilePath(this, selectedUri);
                    Log.d("selectedfilepath", "" + selectedFilePath);
                    if (selectedFilePath != null) {
                        Glide.with(this).load(selectedFilePath).into(userPic);
                        File file = new File(selectedFilePath);
                        callUploadProfilePic(selectedFilePath);
                        Log.d("selectedfile", "" + file.getAbsolutePath());
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAPTURE_PICTURE && resultCode == RESULT_OK) {
                //Uri imageUri = data.getData();

                try {
                    if (fileUri != null) {
                        Glide.with(this).load(fileUri.toString()).into(userPic);
                        callUploadProfilePic(fileUri.getPath());
                    }

                    selectedFilePath = getFilePath(context, fileUri);
                   /* //Toast.makeText(this, "file path"+imageUri, Toast.LENGTH_LONG).show();
                    if (selectedFilePath != null) {

                        //callUploadProfilePic(selectedFilePath);
                    } else {

                        Snackbar.make(tabLayout, "file is empty", Snackbar.LENGTH_SHORT).show();
                    }*/
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAPTURE_PICTURE && resultCode == RESULT_CANCELED) {
                Log.d("LogCheck", "inside result code cancel block");
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        /*uploadingDialog = new ProgressDialog(this);
        uploadingDialog.setCancelable(false);
        uploadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        uploadingDialog.setProgress(percentage);
        uploadingDialog.getProgress();
        uploadingDialog.show();
*/
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        callUserLoginAPI();

    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}


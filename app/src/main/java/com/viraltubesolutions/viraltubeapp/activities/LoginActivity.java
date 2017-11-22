package com.viraltubesolutions.viraltubeapp.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.userLogIn.UserLoginResults;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomEditText;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomTextView;
import com.viraltubesolutions.viraltubeapp.fragments.ForgotPasswordFragment;
import com.viraltubesolutions.viraltubeapp.utils.OnResponseListener;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeUtils;
import com.viraltubesolutions.viraltubeapp.utils.WebServices;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener ,OnResponseListener {

    private MyCustomEditText mInputEmail, mInputPassword;
    MyCustomTextView mSignup,mForgotPassword,mDonthaveText;
    CheckBox mShowPassword;
    String email,password;
    String logInResponseCode=null;
    ProgressDialog progressDialog=null;
    boolean doubleBackToExitPressedOnce=false;
    SpotsDialog dialog;
    String possibleEmail;
    boolean isPermissionGranted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        checkPermissions();
        if (isPermissionGranted)
        {
            accessAccounts();
        }
    }

    private void init() {



        mInputEmail = (MyCustomEditText) findViewById(R.id.vE_al_email);
        mInputPassword = (MyCustomEditText) findViewById(R.id.vE_al_password);
        Button mBtnLogin = (Button) findViewById(R.id.vB_al_login);
        mSignup= (MyCustomTextView) findViewById(R.id.vT_al_signup_from_login);
        mForgotPassword= (MyCustomTextView) findViewById(R.id.vT_al_forgotpassword);
        mShowPassword= (CheckBox)findViewById(R.id.vC_al_showpassword);
        mDonthaveText= (MyCustomTextView) findViewById(R.id.vT_al_dont_have_an_account);

        email=mInputEmail.getText().toString();
        password=mInputPassword.getText().toString();

        AssetManager assetManager = getAssets();
        Typeface candaraTypeface = Typeface.createFromAsset( assetManager, "Fonts/candara.ttf");
        mShowPassword.setTypeface(candaraTypeface);
        mBtnLogin.setTypeface(candaraTypeface);

        mSignup.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);

        mShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int start,end;
                if(!b){
                    start=mInputPassword.getSelectionStart();
                    end=mInputPassword.getSelectionEnd();
                    mInputPassword.setTransformationMethod(new PasswordTransformationMethod());;
                    mInputPassword.setSelection(start,end);
                }else{
                    start=mInputPassword.getSelectionStart();
                    end=mInputPassword.getSelectionEnd();
                    mInputPassword.setTransformationMethod(null);
                    mInputPassword.setSelection(start,end);
                }
            }
        });

    }
    private void checkPermissions() {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_CONTACTS}, 100);

                }
            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if ((permissions.equals(Manifest.permission.WRITE_CONTACTS)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED ))
            {
                isPermissionGranted=true;

            }
     }
    }
    private void accessAccounts()
    {
        AccountManager am = AccountManager.get(this); // "this" references the current Context
        Account[] accounts = am.getAccountsByType("com.google");
        //String name=accounts[0].name.toString();
        for (Account account : accounts)
        {
            possibleEmail = account.name;
            mInputEmail.setText(possibleEmail);

        }

        //mInputEmail.setText(possibleEmail);


    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id)
        {
            case R.id.vT_al_signup_from_login :
                try {
                    //InputMethodManager is used to hide the virtual keyboard from the user after finishing the user input
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isAcceptingText()) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (NullPointerException e) {
                    Log.e("Exception", e.getMessage() + ">>");
                }
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();

            case R.id.vB_al_login :
                try {
                    //InputMethodManager is used to hide the virtual keyboard from the user after finishing the user input
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isAcceptingText()) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (NullPointerException e) {
                    Log.e("Exception", e.getMessage() + ">>");
                }
                submitForm();
                break;

            case R.id.vT_al_forgotpassword:
                try {
                    //InputMethodManager is used to hide the virtual keyboard from the user after finishing the user input
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isAcceptingText()) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (NullPointerException e) {
                    Log.e("Exception", e.getMessage() + ">>");
                }
                ForgotPasswordFragment fragment=new ForgotPasswordFragment();
                FragmentManager manager=getSupportFragmentManager();
                FragmentTransaction ft=manager.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in,R.anim.fade_in);
                fragment.show(ft,"forgotpassword");
                break;

        }

    }

    //Validating form

    private void submitForm() {

        if (!validateEmail()) {
            return;
        }
        else if (!validatePassword()) {
            return;
        }

        callUserLoginAPI();

    }
    void callUserLoginAPI()
    {
        if (ViralTubeUtils.isConnectingToInternet(getApplicationContext())) {
           /* progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Logging in Please wait...");
            progressDialog.show();*/
            dialog = new SpotsDialog(LoginActivity.this,"Logging in Please wait...");
            dialog.show();


            WebServices<UserLoginResults> webServices = new WebServices<>(LoginActivity.this);
            webServices.userLogIn(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.userlogin,
                    mInputEmail.getText().toString(),mInputPassword.getText().toString());
        } else {
           Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }

    }

    private boolean validateEmail() {
        String email = mInputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            Snackbar.make(mInputPassword,R.string.err_msg_email, Snackbar.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        if (mInputPassword.getText().toString().trim().isEmpty()) {
            Snackbar.make(mInputPassword,R.string.err_msg_password, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else  if(mInputPassword.getText().toString().trim().length()<8){
            Snackbar.make(mInputPassword,R.string.err_msg_password_length, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(mSignup, R.string.back_press_to_exit, Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
        switch (URL)
        {
            case userlogin:
                UserLoginResults res= (UserLoginResults) response;
                String userID=res.getId();
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }

                if(isSucces)
                {
                    email="";
                    password="";
                    if(res!=null) {
                        if(!userID.isEmpty()) {
                            if (res.getRESPONSECODE().equalsIgnoreCase("200")) {
                                logInResponseCode = "200";
                                Snackbar.make(mInputPassword, "LogIn Suceess", Snackbar.LENGTH_SHORT).show();

                                SharedPreferences preferences = getSharedPreferences("LogIn", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isLoggedin", true);
                                editor.putString("userID", userID);
                                editor.apply();
                                Intent login = new Intent(LoginActivity.this, HomePageActivity.class);
                                startActivity(login);
                                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_in_from_right);

                            } else if (res.getRESPONSECODE().equalsIgnoreCase("409")) {
                                logInResponseCode = "409";
                                Snackbar.make(mInputPassword, R.string.err_parameter, Snackbar.LENGTH_SHORT).show();

                            } else if (res.getRESPONSECODE().equalsIgnoreCase("503")) {
                                logInResponseCode = "503";
                                Snackbar.make(mInputPassword, R.string.err_server, Snackbar.LENGTH_SHORT).show();

                            } else if (res.getRESPONSECODE().equalsIgnoreCase("402")) {

                                logInResponseCode = "402";
                                //wrong password
                                Snackbar.make(mInputPassword, "Wrong Password", Snackbar.LENGTH_SHORT).show();
                                //RJSnackBar.makeText(LoginActivity.this, "Wrong Password", RJSnackBar.LENGTH_SHORT).show();


                            } else if (res.getRESPONSECODE().equalsIgnoreCase("211")) {

                                logInResponseCode = "211";
                                startActivity(new Intent(LoginActivity.this, SignupActivity.class));

                            } else if (res.getRESPONSECODE().equalsIgnoreCase("301")) {

                                logInResponseCode = "301";
                                //wrong password
                                Snackbar.make(mSignup, "Account blocked please contact admin", Snackbar.LENGTH_SHORT).show();

                            } else {
                                Snackbar.make(mInputPassword, "You should Register First", Snackbar.LENGTH_SHORT).show();
                                Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                                startActivity(signupIntent);
                                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_in_from_right);
                            }

                        }else{
                            Snackbar.make(mSignup, "Cant get user id", Snackbar.LENGTH_SHORT).show();

                        }
                    }else {
                        Snackbar.make(mSignup, "Response is null", Snackbar.LENGTH_SHORT).show();
                    }

                }else
                {
                    Snackbar.make(mSignup, "Login Failure", Snackbar.LENGTH_SHORT).show();
                }

        }

    }
}
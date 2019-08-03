package com.viraltubesolutions.viraltubeapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Paint;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.forgotpassword.ForgotPassword;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.forgotpassword.UpdatePassword;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.userLogIn.UserLoginResults;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomEditText;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomTextView;
import com.viraltubesolutions.viraltubeapp.fragments.ForgotPasswordFragment;
import com.viraltubesolutions.viraltubeapp.utils.ButtonClickEffect;
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
    AlertDialog.Builder fpBuilder ,msgBuilder,upBuilder;
    AlertDialog fpDialog,msgDialog,upDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        //checkPermissions();
    }

    private void init() {

        mInputEmail = (MyCustomEditText) findViewById(R.id.vE_al_email);
        mInputPassword = (MyCustomEditText) findViewById(R.id.vE_al_password);
        Button mBtnLogin = (Button) findViewById(R.id.vB_al_login);
        mSignup= (MyCustomTextView) findViewById(R.id.vT_al_signup_from_login);
        mForgotPassword= (MyCustomTextView) findViewById(R.id.vT_al_forgotpassword);
        mShowPassword= (CheckBox)findViewById(R.id.vC_al_showpassword);
        mDonthaveText= (MyCustomTextView) findViewById(R.id.vT_al_dont_have_an_account);

        fpBuilder=new AlertDialog.Builder(this);
        msgBuilder=new AlertDialog.Builder(this);
        upBuilder=new AlertDialog.Builder(this);



        AssetManager assetManager = getAssets();
        Typeface candaraTypeface = Typeface.createFromAsset( assetManager, "Fonts/candara.ttf");
        mShowPassword.setTypeface(candaraTypeface);
        mBtnLogin.setTypeface(candaraTypeface);

        ButtonClickEffect.addClickEffect(mBtnLogin);

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
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS}, 100);

                }
                else {
                    isPermissionGranted=true;
                }
            }
            else {
                isPermissionGranted=true;
            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if ((permissions.equals(Manifest.permission.READ_CONTACTS)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED ))
            {
                isPermissionGranted=true;

            }
     }
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
               /* ForgotPasswordFragment fragment=new ForgotPasswordFragment();
                FragmentManager manager=getSupportFragmentManager();
                FragmentTransaction ft=manager.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in,R.anim.fade_in);
                fragment.show(ft,"forgotpassword");
                fragment.setCancelable(false);*/
                showForgotPasswordDialog();
                break;

        }

    }
    private void showForgotPasswordDialog()
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.forgotpassword_alert, null);
        final MyCustomEditText phonenumber=dialogView.findViewById(R.id.vE_fp_mobile);
        Button sendPassword=dialogView.findViewById(R.id.vB_fp_sendpassword);
        fpBuilder.setView(dialogView);
        fpBuilder.setCancelable(true);
        fpDialog = fpBuilder.create();
        fpDialog.show();
        ButtonClickEffect.addClickEffect(sendPassword);

        sendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //InputMethodManager is used to hide the virtual keyboard from the user after finishing the user input
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isAcceptingText()) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (NullPointerException e) {
                    Log.e("Exception", e.getMessage() + ">>");
                }

                if (phonenumber.getText().toString().trim().isEmpty()||phonenumber.getText().toString().trim().length()<10)
                {
                    Snackbar.make(mSignup,R.string.err_msg_phonenumber, Snackbar.LENGTH_SHORT).show();
                }
                else{
                    callForgotAPI(phonenumber.getText().toString());
                }


            }
        });


    }
    private void showMessageSentDialog()
    {
        //should call from on success of forgor password Api

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.messagesent_alert, null);
        msgBuilder.setView(dialogView);
        msgBuilder.setCancelable(false);
        msgDialog = msgBuilder.create();
        msgDialog.show();


        MyCustomTextView dialogtext=dialogView.findViewById(R.id.vT_msa_text);
        Button btn=dialogView.findViewById(R.id.vB_msa_ok);
        dialogtext.setText(R.string.password_sent);
        ButtonClickEffect.addClickEffect(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgDialog.dismiss();
                showUpdatePasswordDialog();

            }
        });
    }
    private void showUpdatePasswordDialog()
    {
        LayoutInflater inflater =getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.updatepassword_alert, null);
        upBuilder.setView(dialogView);
        upBuilder.setCancelable(false);
        upDialog= upBuilder.create();
        upDialog.show();

        MyCustomTextView skip=dialogView.findViewById(R.id.vT_fcp_skip);
        skip.setPaintFlags(skip.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        final MyCustomEditText receivedPassword=dialogView.findViewById(R.id.vE_fcp_receivedpassword);
        final MyCustomEditText newPassword=dialogView.findViewById(R.id.vE_fcp_currentpassword);
        final MyCustomEditText confirmPassword=dialogView.findViewById(R.id.vE_fcp_confirmpassword);
        final Button btn=dialogView.findViewById(R.id.vB_fcp_update);
        ButtonClickEffect.addClickEffect(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //InputMethodManager is used to hide the virtual keyboard from the user after finishing the user input
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isAcceptingText()) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (NullPointerException e) {
                    Log.e("Exception", e.getMessage() + ">>");
                }
                String newpass=newPassword.getText().toString();
                String confirmpass=confirmPassword.getText().toString();
                String enteredOTP=receivedPassword.getText().toString();

                SharedPreferences myPref=getSharedPreferences("otpPref",MODE_PRIVATE);
                String receivedOTP=myPref.getString("otp",null);
                String mobilenumber=myPref.getString("mobile",null);


                if(enteredOTP.trim().isEmpty()||enteredOTP.trim().length()<6)
                {
                    Snackbar.make(mSignup,"enter a valid OTP",2000).show();
                }
                else if (newpass.trim().isEmpty() || confirmpass.trim().isEmpty()) {
                    Snackbar.make(mSignup,R.string.err_msg_password, Snackbar.LENGTH_SHORT).show();
                }
                else  if(newpass.trim().length()<6 || confirmpass.trim().length()<6){
                    Snackbar.make(mSignup,R.string.err_msg_password_length, Snackbar.LENGTH_SHORT).show();
                }
                else if(!enteredOTP.equals(receivedOTP))
                {
                    Snackbar.make(mSignup,"entered OTP is invalid, please try again",2000).show();
                }
                else if(!newpass.equals(confirmpass))
                {
                    Snackbar.make(mSignup,"Password didnot matched",2000).show();
                }
                else
                {
                    callUpdatePasswordAPI(mobilenumber,receivedOTP);

                }
                //alertDialog.dismiss();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SharedPreferences myPref=getSharedPreferences("otpPref",MODE_PRIVATE);
                String receivedOTP=myPref.getString("otp",null);
                String mobilenumber=myPref.getString("mobile",null);
                callUpdatePasswordAPI(mobilenumber,receivedOTP);*/
                upDialog.dismiss();
            }
        });
    }


    //Validating form

    private void submitForm() {

        if (!validateEmail()) {
            return;
        }
        else if (!validatePassword()) {
            return;
        }
        email=mInputEmail.getText().toString();
        password=mInputPassword.getText().toString();
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
    void callForgotAPI(String mobile)
    {
        if (ViralTubeUtils.isConnectingToInternet(getApplicationContext())) {
           /* progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Logging in Please wait...");
            progressDialog.show();*/
            dialog = new SpotsDialog(LoginActivity.this,"message is being sending...");
            dialog.show();

            WebServices<ForgotPassword> webServices = new WebServices<ForgotPassword>(LoginActivity.this);
            webServices.forgotPassword(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.forgotPassword,mobile);
        } else {
            Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }

    }
    void callUpdatePasswordAPI(String mobile,String password)
    {
        if (ViralTubeUtils.isConnectingToInternet(getApplicationContext())) {
           /* progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Logging in Please wait...");
            progressDialog.show();*/
            dialog = new SpotsDialog(LoginActivity.this,"Updating new password...");
            dialog.show();

            WebServices<UpdatePassword> webServices = new WebServices<UpdatePassword>(LoginActivity.this);
            webServices.updatePassword(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.updatePassword,mobile,password);
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
        else  if(mInputPassword.getText().toString().trim().length()<6){
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
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(mSignup, R.string.back_press_to_exit, Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
       // finish();

    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
        switch (URL)
        {
            case userlogin:
                UserLoginResults res= (UserLoginResults) response;
                String userID=res.getId();
                String name=res.getName();
                String mob=res.getMobile();
                String mail=res.getEmail();
                String image=res.getPicture();
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }

                if(isSucces)
                {

                    if(res!=null) {
                        if(!userID.isEmpty()) {
                            if (res.getRESPONSECODE().equalsIgnoreCase("200")) {
                                mInputEmail.setText("");
                                mInputPassword.setText("");
                                logInResponseCode = "200";
                                Snackbar.make(mInputPassword, "LogIn Suceess", Snackbar.LENGTH_SHORT).show();

                                SharedPreferences preferences = getSharedPreferences("LogIn", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isLoggedin", true);
                                editor.putString("userID", userID);
                                editor.putString("userName", name);
                                editor.putString("userNumber", mob);
                                editor.putString("userEmail", mail);
                                editor.putString("password",password);
                                editor.putString("image",image);
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
                                Snackbar.make(mInputPassword, "Wrong email or Password", Snackbar.LENGTH_SHORT).show();
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
                break;

            case forgotPassword:
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }

                if(isSucces)
                {
                    ForgotPassword fp= (ForgotPassword) response;

                    if(fp.getRESPONSECODE().equalsIgnoreCase("200"))
                    {
                        if(fpDialog.isShowing())
                        {
                            fpDialog.dismiss();
                        }
                        String otp=fp.getOtp().toString();
                        String mobole= fp.getMobile();
                        showMessageSentDialog();
                        if(otp!=null) {
                            SharedPreferences otpPreference = getSharedPreferences("otpPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = otpPreference.edit();
                            editor.putString("otp", otp);
                            editor.putString("mobile", mobole);
                            editor.apply();
                        }
                    }
                    else if(fp.getRESPONSECODE().equalsIgnoreCase("409"))
                    {
                        Snackbar.make(mSignup, "Enter a registered mobile number", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Snackbar.make(mSignup, "Enter a registered mobile number", Snackbar.LENGTH_SHORT).show();
                }

                }
                else
                {
                    Snackbar.make(mSignup, "Failed to get response, try again", Snackbar.LENGTH_SHORT).show();

                }
                break;

            case updatePassword:
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }

                if(isSucces)
                {
                    UpdatePassword up= (UpdatePassword) response;
                    if(up.getRESPONSECODE().equalsIgnoreCase("200"))
                    {
                        if(upDialog.isShowing())
                        {
                            upDialog.dismiss();
                        }
                        SharedPreferences otpPreference = getSharedPreferences("otpPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = otpPreference.edit();
                        editor.clear();
                        editor.apply();
                        Snackbar.make(mSignup, "password changed successfully", Snackbar.LENGTH_SHORT).show();
                        init();

                    } else {
                        Snackbar.make(mSignup, "Can't update your password try again", Snackbar.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Snackbar.make(mSignup, "Failed to update your password try again", Snackbar.LENGTH_SHORT).show();
                }
                break;

        }

    }
}
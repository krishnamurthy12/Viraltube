package com.viraltubesolutions.viraltubeapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.userSignUp.UserSignUpResults;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomEditText;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomTextView;
import com.viraltubesolutions.viraltubeapp.utils.OnResponseListener;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeUtils;
import com.viraltubesolutions.viraltubeapp.utils.WebServices;

import dmax.dialog.SpotsDialog;

public class SignupActivity extends AppCompatActivity implements OnResponseListener,View.OnClickListener,AdapterView.OnItemSelectedListener {
    MyCustomEditText mUserName, mEmail, mPhoneNum, mPassword, mAge, mLocation,mViral,mTube;
    MyCustomTextView mTermsAndConditionsText;
    Spinner mGender;
    RadioGroup mUserType;
    RadioButton mIndividual, mCollege;
    Button mRegister;
    CheckBox mTermsAndConditionsCheck;
    String userName, email, mobile, password,age,gender, location;
    ProgressDialog progressDialog=null;
    SpotsDialog dialog;
    String signUpResponse="";
    //create a list of items for the spinner.
    String[] items = new String[]{"Select gender", "male", "female", "other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }
    public void init() {
        //EditText fields
        mUserName = (MyCustomEditText) findViewById(R.id.vE_as_username);
        mEmail = (MyCustomEditText) findViewById(R.id.vE_as_email);
        mPhoneNum = (MyCustomEditText) findViewById(R.id.vE_as_phonenumber);
        mPassword = (MyCustomEditText) findViewById(R.id.vE_as_password);
        mAge = (MyCustomEditText) findViewById(R.id.vE_as_age);
        mLocation = (MyCustomEditText) findViewById(R.id.vE_as_location);

        //TextViews
        mTermsAndConditionsText = (MyCustomTextView) findViewById(R.id.vT_as_terms_condition);

        //Spinner
        mGender = (Spinner) findViewById(R.id.vS_as_gender);

        //RadioGroup and RadioButtons
        mUserType = (RadioGroup) findViewById(R.id.signup_radiogroup_layout);
        mIndividual = (RadioButton) findViewById(R.id.vR_as_individual);
        mCollege = (RadioButton) findViewById(R.id.vR_as_college);

        //Button
        mRegister = (Button) findViewById(R.id.vB_as_signup);

        //Checkbox
        mTermsAndConditionsCheck = (CheckBox) findViewById(R.id.vC_as_terms_condition);

        setvalues();

    }

    private void setvalues()
    {
        mTermsAndConditionsText.setPaintFlags(mTermsAndConditionsText.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        userName=mUserName.getText().toString();
        email=mEmail.getText().toString();
        mobile=mPhoneNum.getText().toString();
        password=mPassword.getText().toString();
        age=mAge.getText().toString();
        //gender=mGender.getSelectedItem().toString();
        location=mLocation.getText().toString();

        Typeface candaraTypeface = Typeface.createFromAsset( getAssets(), "Fonts/candara.ttf");
        mRegister.setTypeface(candaraTypeface);
        mIndividual.setTypeface(candaraTypeface);
        mCollege.setTypeface(candaraTypeface);

        mRegister.setOnClickListener(SignupActivity.this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGender.setAdapter(adapter);
        mGender.setOnItemSelectedListener(this);

    }
    public void register()
    {   if(!validateUserName()) {
        return;
        }
        else if(!validateEmail()){
            return;
        }
    else if (!validatePhoneNumber()) {
            return;
        }
    else if (!validatePassword()) {
            return;
        }
    else if (!validateGender()) {
            return;
        }
    else if (!validateAge()) {
            return;
        }
    else if (!validateLocation()) {
            return;
        }
    else if (!validateTermsAndconditions()) {
            return;
        }
        callSignUpAPI();
    }
    private void callSignUpAPI()
    {
        if (ViralTubeUtils.isConnectingToInternet(getApplicationContext())) {

         /*   progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait");
            progressDialog.show();

*/
            dialog = new SpotsDialog(SignupActivity.this,"Registering you Please wait..");
            dialog.show();
            WebServices<UserSignUpResults> webServices = new WebServices<>(SignupActivity.this);
            webServices.userSignUp(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.usersignup,
                    userName,email, mobile,password,age,gender,location);
        } else {
            Snackbar.make(mRegister,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }
    }
    private boolean validateUserName() {
        if (mUserName.getText().toString().trim().isEmpty()||mUserName.getText().toString().trim().startsWith("{0-9}")) {
            Snackbar.make(mRegister,R.string.err_msg_name, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private boolean validateEmail() {
        String email = mEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            Snackbar.make(mRegister,R.string.err_msg_email, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if (mPassword.getText().toString().trim().isEmpty()) {
            Snackbar.make(mRegister,R.string.err_msg_password, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else  if(mPassword.getText().toString().trim().length()<8){
            Snackbar.make(mRegister,R.string.err_msg_password_length, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private boolean validatePhoneNumber()
    {
        if (mPhoneNum.getText().toString().trim().isEmpty()||mPhoneNum.getText().toString().trim().length()<10) {
            Snackbar.make(mRegister,R.string.err_msg_phonenumber, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean validateAge()
    {
        if (mAge.getText().toString().trim().isEmpty()) {
            Snackbar.make(mRegister,R.string.err_msg_age, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
    private boolean validateGender()
    {

       if(mGender.getSelectedItem().toString().trim().equals("Select gender"))
       {
           Snackbar.make(mRegister,R.string.err_msg_gender, Snackbar.LENGTH_SHORT).show();
           return false;
       }
       return true;
    }

    private boolean validateLocation()
    {
        if (mLocation.getText().toString().trim().isEmpty()) {
            Snackbar.make(mRegister,R.string.err_msg_location, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
    private boolean validateTermsAndconditions()
    {
        if (!(mTermsAndConditionsCheck.isChecked())) {
            Snackbar.make(mTermsAndConditionsCheck,R.string.err_msg_termsandconditions, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_in_from_left);
        Intent back=new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(back);
    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
        if (URL == WebServices.ApiType.usersignup) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            UserSignUpResults results= (UserSignUpResults) response;
            String userID=results.getUserid();
            if (isSucces) {
                 /*success :200
                register failed: 302
                user number already existed: 405
                user email already existed: 411
                invalid parameter: 409
                Server error: 503*/
                if(results.getRESPONSECODE().equalsIgnoreCase("200"))
                {
                    signUpResponse="200";
                    Snackbar.make(mRegister,"Registered Successfully", Snackbar.LENGTH_SHORT).show();

                    SharedPreferences preferences = getSharedPreferences("LogIn", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isLoggedin", true);
                    editor.putString("userID", userID);
                    editor.apply();

                    Intent intent=new Intent(SignupActivity.this,HomePageActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_in_from_right);
                }
               else if(results.getRESPONSECODE().equalsIgnoreCase("302"))
                {
                    signUpResponse="302";
                    Snackbar.make(mRegister,R.string.err_registration, Snackbar.LENGTH_SHORT).show();
                }
                else if(results.getRESPONSECODE().equalsIgnoreCase("405"))
                {
                    signUpResponse="405";
                    Snackbar.make(mRegister,R.string.err_mob_regestered, Snackbar.LENGTH_SHORT).show();
                }
                else if(results.getRESPONSECODE().equalsIgnoreCase("411"))
                {
                    signUpResponse="411";
                    Snackbar.make(mRegister,R.string.err_registration, Snackbar.LENGTH_SHORT).show();
                }
                else if(results.getRESPONSECODE().equalsIgnoreCase("409"))
                {
                    signUpResponse="409";
                    Snackbar.make(mRegister,R.string.err_email_regestered, Snackbar.LENGTH_SHORT).show();
                }
                else if(results.getRESPONSECODE().equalsIgnoreCase("503"))
                {
                    signUpResponse="503";
                    Snackbar.make(mRegister,R.string.err_server, Snackbar.LENGTH_SHORT).show();
                }
            }
        }

    }

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
        register();
    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        if(pos>0)
        {
            gender=parent.getItemAtPosition(pos).toString();
        }


    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    }

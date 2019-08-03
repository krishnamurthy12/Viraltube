package com.viraltubesolutions.viraltubeapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.atom.mobilepaymentsdk.PayActivity;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.paymentresponse.PaymentResponse;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.utils.ButtonClickEffect;
import com.viraltubesolutions.viraltubeapp.utils.OnResponseListener;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeUtils;
import com.viraltubesolutions.viraltubeapp.utils.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class ViralTubePaymentActivity extends AppCompatActivity implements View.OnClickListener,OnResponseListener{
    Button mPay;
    Spinner bankName,bankCode;

    private String txnid;
    public String user_name;
    public String user_id;
    public String user_phone;
    public String user_bankId;
    String user_email;
    String amt = null;

    //hash generate
    public String hashCal(String type, String str) {
        byte[] hashSequence = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashSequence);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException NSAE) {
        }
        return hexString.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viraltube_payment_screen);
        initializeViews();
    }
    private void initializeViews() {
        mPay = (Button) findViewById(R.id.vB_vps_pay);
        bankName= (Spinner) findViewById(R.id.vS_vps_bank);
        bankCode=(Spinner) findViewById(R.id.vS_vps_bankcode);
        ButtonClickEffect.addClickEffect(mPay);

        mPay.setOnClickListener(this);
        Random rand = new Random();
        String randomString = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        txnid = hashCal("SHA-256", randomString).substring(0, 20);
        amt = "610";
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.vB_vps_pay:
                if(!validateBank())
                {
                    return;
                }
                proceedpayment();
                break;
        }
    }
    private boolean validateBank()
    {

        if(bankName.getSelectedItem().toString().trim().equals("Select Bank"))
        {
            Snackbar.make(mPay,"Please select a bank", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void proceedpayment()
    {
        int a=bankName.getSelectedItemPosition();
        String bank=bankCode.getItemAtPosition(a).toString();
        Log.d("Check---",bank+"  ");
        SharedPreferences preferences = getSharedPreferences("LogIn", MODE_PRIVATE);
        preferences.getString("userID", null);
        user_name=preferences.getString("userName", null);
        user_phone=preferences.getString("userNumber", null);
        user_email=preferences.getString("userEmail", null);
        user_id=preferences.getString("userID",null);

        Double doubleAmt = Double.valueOf(amt);
        amt = doubleAmt.toString();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = sdf.format(cal.getTime());
        Intent newPayIntent = new Intent(ViralTubePaymentActivity.this, PayActivity.class);

        newPayIntent.putExtra("merchantId", "19887");
        newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
        newPayIntent.putExtra("loginid", "19887");
        newPayIntent.putExtra("password", "VIRAL@123");
        newPayIntent.putExtra("prodid", "VIRAL");
        newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
        newPayIntent.putExtra("clientcode", "007");
        newPayIntent.putExtra("custacc", "020046519050");
        newPayIntent.putExtra("amt", amt);//Should be 3 decimal number i.e 1.000
        newPayIntent.putExtra("txnid", txnid);
        newPayIntent.putExtra("date", strDate);
        newPayIntent.putExtra("bankid", bank); //Should be in same format

        //use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
        newPayIntent.putExtra("ru", "https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production
        //  newPayIntent.putExtra("ru","https://sslpayment.atomtech.in/mobilesdk/param"); //ru FOR Production

        //use below UAT url only with UAT "Library-MobilePaymentSDK", Located inside UAT folder
        //   newPayIntent.putExtra("ru", "https://paynetzuat.atomtech.in/mobilesdk/param"); // FOR UAT (Testing)

        //Optinal Parameters
        newPayIntent.putExtra("customerName", user_name); //Only for Name
        newPayIntent.putExtra("customerEmailID", user_email);//Only for Email ID
        newPayIntent.putExtra("customerMobileNo", user_phone);//Only for Mobile Number
        newPayIntent.putExtra("billingAddress", user_id);//Only for Address
//        newPayIntent.putExtra("customerName", user_name); //Only for Name
//        newPayIntent.putExtra("customerMobileNo", user_phone);//Only for Mobile Number
//        newPayIntent.putExtra("billingAddress", user_adress);//Only for Address
        newPayIntent.putExtra("optionalUdf9", "NET");// Can pass any data

        startActivityForResult(newPayIntent, 1);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_in_from_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed here it is 2
        System.out.println("RESULTCODE--->" + resultCode);
        System.out.println("REQUESTCODE--->" + requestCode);
        System.out.println("RESULT_OK--->" + RESULT_OK);


        if (requestCode == 1) {
            System.out.println("---------INSIDE-------");
            if (data != null) {
                String message = data.getStringExtra("status");
                String[] resKey = data.getStringArrayExtra("responseKeyArray");
                String[] resValue = data.getStringArrayExtra("responseValueArray");
                try {
                    JSONObject jsonObject = new JSONObject();
                    if (resKey != null && resValue != null) {
                        for (int i = 0; i < resKey.length; i++) {
                            System.out.println("  " + i + " resKey : " + resKey[i] + " resValue : " + resValue[i]);
                            jsonObject.put(resKey[i], resValue[i]);
                        }
                    }
                    Log.d("BANK", jsonObject.toString());
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    System.out.println("RECEIVED BACK--->" + message);
                    callPaymentAPI(jsonObject);
                    if (jsonObject.get("udf9").equals("CARD")) {
                        //paymentService(jsonObject.toString(), "CARD");
                    }
                    else if (jsonObject.get("optionalUdf9").equals("NET")) {
                        //paymentService(jsonObject.toString(), "NET");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }
    private void callPaymentAPI(JSONObject jsonObject) {

        if (ViralTubeUtils.isConnectingToInternet(this)) {

            WebServices<PaymentResponse> response=new WebServices<PaymentResponse>(ViralTubePaymentActivity.this);
            response.payment(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.payment,jsonObject);
        } else {
            // Toast.makeText(context, ""+R.string.err_msg_nointernet, Toast.LENGTH_SHORT).show();
            Snackbar.make(mPay, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces)
    {
        switch (URL) {

         case payment:
                PaymentResponse paymentResponse= (PaymentResponse) response;
                if(isSucces)
                {
                    if(paymentResponse.getRESPONSECODE().equalsIgnoreCase("200"))
                    {
                        Snackbar.make(mPay,"Transaction Successul",Snackbar.LENGTH_SHORT).show();

                    }
                    else if(paymentResponse.getRESPONSECODE().equalsIgnoreCase("000"))
                    {
                        Snackbar.make(mPay,"Transaction Failed",Snackbar.LENGTH_SHORT).show();

                    }
                    else if(paymentResponse.getRESPONSECODE().equalsIgnoreCase("403"))
                    {
                        Snackbar.make(mPay,"Database error",Snackbar.LENGTH_SHORT).show();

                    }
                    else if(paymentResponse.getRESPONSECODE().equalsIgnoreCase("409"))
                    {
                        Snackbar.make(mPay,"Invalid parameters",Snackbar.LENGTH_SHORT).show();

                    }
                    else if(paymentResponse.getRESPONSECODE().equalsIgnoreCase("503"))
                    {
                        Snackbar.make(mPay,"Invalid method",Snackbar.LENGTH_SHORT).show();

                    }

                }
        }

    }
}

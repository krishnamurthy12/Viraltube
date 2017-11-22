package com.viraltubesolutions.viraltubeapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atom.mobilepaymentsdk.PayActivity;
import com.viraltubesolutions.viraltubeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MPSActivity extends Activity {

    public static String USERNAME = "user_name";
    public static String USERPHONE = "user_phonr";
    public static String AMOUNT = "amount";
    public static String REFERCODE = "refer_code";
    Button payMerchantNB;
    Button payMerchantDC;
    Spinner Bank;
    Spinner cardType;
    Spinner PaymentType;
    TextView et_nb_amt, et_card_amt;
    Dialog customDialog = null;
    private String txnid;
    private String user_name;
    private String user_phone;
    private String refer_code;
    private String amount;

    // font path
    String fontPathBold = "fonts/QuicksandBold.ttf";
    String fontPathRegular = "fonts/QuicksandRegular.ttf";
    // Loading Font Face
    Typeface typefaceBold, typefaceRegular;

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
        if (getIntent().getExtras() != null) {
            user_name = getIntent().getExtras().getString(MPSActivity.USERNAME);
            user_phone = getIntent().getExtras().getString(MPSActivity.USERPHONE);
            amount = getIntent().getExtras().getString(MPSActivity.AMOUNT);
            refer_code = getIntent().getExtras().getString(MPSActivity.REFERCODE);
        }
        Random rand = new Random();
        String randomString = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        txnid = hashCal("SHA-256", randomString).substring(0, 20);

        //typefaceBold = Typeface.createFromAsset(getAssets(), fontPathBold);
        //typefaceRegular = Typeface.createFromAsset(getAssets(), fontPathRegular);
    }


    @Override
    public void onResume() {
        super.onResume();
        System.out.println("In On Resume");
        setContentView(R.layout.paymentlayout);

        et_nb_amt = (TextView) findViewById(R.id.et_nb_amt);
        et_nb_amt.setText(amount);
        et_card_amt = (TextView) findViewById(R.id.et_card_amt);
        et_card_amt.setText(amount);
        cardType = (Spinner) findViewById(R.id.sp_cardType);
        PaymentType = (Spinner) findViewById(R.id.sp_paymentType);
        Bank = (Spinner) findViewById(R.id.sp_bank);

        et_nb_amt.setTypeface(typefaceBold);
        et_card_amt.setTypeface(typefaceBold);


        payMerchantNB = (Button) findViewById(R.id.btn_payMerchantNB);
        payMerchantNB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String amt = et_nb_amt.getText().toString();

                if (amt.equalsIgnoreCase("")) {
                    Toast.makeText(MPSActivity.this, "Please enter valid amount", Toast.LENGTH_LONG).show();
                } else if (Bank.getSelectedItemPosition() == 0) {
                    Toast.makeText(MPSActivity.this, "Please select valid bank", Toast.LENGTH_LONG).show();
                } else {

                    Double doubleAmt = Double.valueOf(amt);
                    amt = doubleAmt.toString();
                    Intent newPayIntent = new Intent(MPSActivity.this, PayActivity.class);

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
                    newPayIntent.putExtra("date", "25/08/2015 18:31:00");//Should be in same format
                    //newPayIntent.putExtra("bankid", getResources().getStringArray(R.array.bank_code)[Bank.getSelectedItemPosition()]); //Should be valid bank id

                    //use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
                    newPayIntent.putExtra("ru", "https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production
                    //  newPayIntent.putExtra("ru","https://sslpayment.atomtech.in/mobilesdk/param"); //ru FOR Production

                    //use below UAT url only with UAT "Library-MobilePaymentSDK", Located inside UAT folder
                    //   newPayIntent.putExtra("ru", "https://paynetzuat.atomtech.in/mobilesdk/param"); // FOR UAT (Testing)

                    //Optinal Parameters
                    newPayIntent.putExtra("customerName", user_name); //Only for Name
                    //newPayIntent.putExtra("customerEmailID", Utilities.getSharedString("userid", MPSActivity.this));//Only for Email ID
                    newPayIntent.putExtra("customerMobileNo", user_phone);//Only for Mobile Number
                    newPayIntent.putExtra("billingAddress", refer_code);//Only for Address
                    newPayIntent.putExtra("optionalUdf9", "NET");// Can pass any data

                    startActivityForResult(newPayIntent, 1);



                }
            }
        });

        payMerchantDC = (Button) findViewById(R.id.btn_payMerchantDC);
        payMerchantDC.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String amt = et_card_amt.getText().toString();
                String strPaymentMode = null, strCardType = null;
                int PaymentTypePos = PaymentType.getSelectedItemPosition();
                int cardTypePos = cardType.getSelectedItemPosition();

                if (amt.equalsIgnoreCase("")) {
                    Toast.makeText(MPSActivity.this, "Please enter valid amount", Toast.LENGTH_LONG).show();
                } else if (PaymentTypePos == 0) {
                    Toast.makeText(MPSActivity.this, "Please select valid Payment Mode", Toast.LENGTH_LONG).show();
                } else if (cardTypePos == 0) {
                    Toast.makeText(MPSActivity.this, "Please select valid Card Type", Toast.LENGTH_LONG).show();
                } else {
                    Double doubleAmt = Double.valueOf(amt);
                    amt = doubleAmt.toString();

                    switch (PaymentTypePos) {
                        case 1:
                            strPaymentMode = "CC";
                            break;
                        case 2:
                            strPaymentMode = "DC";
                            break;
                    }


                    switch (cardTypePos) {
                        case 1:
                            strCardType = "VISA";
                            break;
                        case 2:
                            strCardType = "MAESTRO";
                            break;
                        case 3:
                            strCardType = "MASTER";
                            break;
                    }

                    Intent newPayIntent = new Intent(MPSActivity.this, PayActivity.class);
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
                    newPayIntent.putExtra("date", "25/08/2015 18:31:00");//Should be in same format
                    newPayIntent.putExtra("cardtype", strPaymentMode);// CC or DC ONLY (value should be same as commented)
                    newPayIntent.putExtra("cardAssociate", strCardType);// VISA or MASTER or MAESTRO ONLY (value should be same as commented)

                    //use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
                    newPayIntent.putExtra("ru", "https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production
                    //    newPayIntent.putExtra("ru","https://sslpayment.atomtech.in/mobilesdk/param"); //ru FOR Production

                    //Optinal Parameters
                    newPayIntent.putExtra("customerName", user_name);//Only for Name
                    //newPayIntent.putExtra("customerEmailID", Utilities.getSharedString("userid", MPSActivity.this));//Only for Email ID
                    newPayIntent.putExtra("customerMobileNo", user_phone);//Only for Mobile Number
                    newPayIntent.putExtra("billingAddress", refer_code);//Only for Address
                    newPayIntent.putExtra("optionalUdf9", "CARD");// Can pass any data
                    startActivityForResult(newPayIntent, 1);

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed here it is 2
        System.out.println("RESULTCODE--->" + resultCode);
        System.out.println("REQUESTCODE--->" + requestCode);
        System.out.println("RESULT_OK--->" + RESULT_OK);

        if (requestCode == 1 && resultCode == 1) {
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


   /* public void paymentService(String data, String type) {
        ReferActivity.sProgressDialog.show();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(AppController.ROOT_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("PAYMENT"))
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        PaymentAPI api = adapter.create(PaymentAPI.class);


        api.post(data, type, new Callback<Response>() {

            @Override
            public void success(Response object, Response response) {

                String output = BufferReaderOutput.BufferReaderOutput(response);
                try {
                    JSONObject root = new JSONObject(output);
                    String ResponseCode = root.getString("responsecode");

                    if (ResponseCode.equals("200")) {
                        dialogdisplay();
                    } else if (ResponseCode.equals("400")) {
                        MainActivity.dialogdisplay(MPSActivity.this, "Alert", "Something wrong");
                    } else if (ResponseCode.equals("425")) {
                        MainActivity.dialogdisplay(MPSActivity.this, "Alert", "Please Contact Admin");
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                ReferActivity.sProgressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                ReferActivity.sProgressDialog.dismiss();
                MainActivity.dialogdisplay(MPSActivity.this, "Error",
                        " Sorry Something Wrong ");
            }
        });
    }


    public void dialogdisplay() {
        customDialog = new Dialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_view,
                null);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(dialogView);
        customDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView title = (TextView) dialogView.findViewById(R.id.tv_DialogTitle);
        TextView message = (TextView) dialogView.findViewById(R.id.tv_DialogMessage);
        title.setText("ALERT");
        message.setText("Please Login again for Premium");
        Button yes = (Button) dialogView.findViewById(R.id.btn_yes);
        yes.setText("OK");
        Button no = (Button) dialogView.findViewById(R.id.btn_no);
        no.setVisibility(View.GONE);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.removeSharedValueForKey("username", MPSActivity.this);
                Utilities.removeSharedValueForKey("userid", MPSActivity.this);
                Utilities.removeSharedValueForKey("ref_code", MPSActivity.this);
                Utilities.removeSharedValueForKey("Userstatus", MPSActivity.this);
                Intent intent = new Intent(MPSActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }


        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }
*/

}
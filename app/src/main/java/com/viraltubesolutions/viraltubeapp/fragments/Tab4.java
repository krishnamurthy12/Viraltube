package com.viraltubesolutions.viraltubeapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.contactsResponse.Contact;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.contactsResponse.ContactsResponse;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.adapters.Tab4adapter;
import com.viraltubesolutions.viraltubeapp.utils.OnResponseListener;
import com.viraltubesolutions.viraltubeapp.utils.ViralTubeUtils;
import com.viraltubesolutions.viraltubeapp.utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Shashi on 11/13/2017.
 */

public class Tab4 extends Fragment implements OnResponseListener,SwipeRefreshLayout.OnRefreshListener {
    Context context;
    boolean isPermissionGranted=false;
    private final int REQUEST_READ_CONTACTS=1;
    private boolean _hasLoadedOnce= false;
    String userID;
    LinearLayout nomatchLayout;

    public ListView contact_listview;
    public static List<Contact> contacts = new ArrayList<Contact>();
    public static List<Contact> newcontacts = new ArrayList<Contact>();
    private static ArrayList<String> contacts_names = new ArrayList<String>();
    private static ArrayList<String> contacts_number = new ArrayList<String>();
    //List<Contact> contactInfoList = new ArrayList<>();
    Contact contactObj;

    ArrayList <String> aa= new ArrayList<String>();

    private View v;
    SwipeRefreshLayout refreshPage;
    RecyclerView mRecyclerView;
    SpotsDialog dialog;
    SpotsDialog refreshdialog;
    Tab4adapter tab4adapter;
    JSONObject Mainobject=new JSONObject();
    JSONArray array = new JSONArray();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_contacts, container, false);

        initializeViews();
        return v;
    }
    public void initializeViews()
    {
        SharedPreferences preferences = context.getSharedPreferences("LogIn", MODE_PRIVATE);
        userID=preferences.getString("userID",null);
        //contact_listview = (ListView) v.findViewById(R.id.contacts_listview);
        mRecyclerView=v.findViewById(R.id.recyclerView_contacts);
        mRecyclerView.setHasFixedSize(true);

        nomatchLayout=v.findViewById(R.id.nomatch_layout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshPage=(SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout_contacts);
        if(newcontacts!=null)
        {
            newcontacts.clear();
        }
        refreshPage.setOnRefreshListener(this);

        /*if(refreshPage!=null) {
            refreshPage.setOnRefreshListener(this);
        }
*/
        checkPermissions();
        new GetContacts().execute();

        //new GetContacts().execute();
    }
   /* @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
            if (isFragmentVisible_ && !_hasLoadedOnce) {
                _hasLoadedOnce = true;
                  new GetContacts().execute();
                //  getContacts1();
            }
        }
    }
*/
    @Override
    public void onRefresh() {
        refreshPage.setColorSchemeColors(getResources().getColor(R.color.appBlue),getResources().getColor(R.color.green));
        refreshContent();
        refreshdialog = new SpotsDialog(context,"Refreshing Please wait...");
        refreshdialog.show();

    }

    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               callGetContactsAPI();
                refreshPage.setRefreshing(false);
                refreshdialog.dismiss();

            }
        },3000);
    }

    @SuppressLint("StaticFieldLeak")
    class GetContacts extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(isPermissionGranted) {
                dialog = new SpotsDialog(context, "Loading your Contacts please wait...");
                dialog.show();

            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (newcontacts.isEmpty()) {
                getContatcs();
            }
            //getContatcs();

            return null;
        }

        @Override
        protected void onPostExecute(Void success) {
            super.onPostExecute(success);
            callGetContactsAPI();
        }
    }
    private void getContatcs() {
        try {
            Cursor c = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            String name, number = "";
            String id;
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                if (Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
                            null);
                    assert pCur != null;
                    while (pCur.moveToNext()) {
                        number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contacts_number.add(number);
                        contacts_names.add(name);
                    }
                    pCur.close();
                }
                //contacts.add(new Contact(name, number));
                newcontacts.add(new Contact(name,number));
                JSONObject innerobj = new JSONObject();
                innerobj.put("name", name);
                innerobj.put("number", number);
                array.put(innerobj);
                c.moveToNext();
            }
            Mainobject.put("contacts", array);
            c.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void callGetContactsAPI()
    {
        if (ViralTubeUtils.isConnectingToInternet(getContext())) {

              Log.d("mainobject",String.valueOf(Mainobject));
            WebServices<ContactsResponse> response=new WebServices<ContactsResponse>(Tab4.this);
            response.getContacts(WebServices.SELF_UPLOAD_URL, WebServices.ApiType.getContacts,Mainobject);
        } else {
            Snackbar.make(contact_listview, R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        }

    }
    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            isPermissionGranted = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);

                }
            }else
            {
                isPermissionGranted = true;
            }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if ((permissions[0].equals(Manifest.permission.READ_CONTACTS)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED )) {
                   isPermissionGranted=true;

            } else {

                Toast.makeText(context, "you dont have permission to access camera features", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.contact_menu, menu);
        MenuItem search=menu.findItem(R.id.Search);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
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

                tab4adapter.getFilter().filter(newText);
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
            case getContacts:
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }
                ContactsResponse responsedata= (ContactsResponse) response;
                contacts=responsedata.getContacts();
               /* for(int i=0;i<contacts.size();i++) {
                    newcontacts.add(new Contact(contacts.get(i).getName(), contacts.get(i).getMobile()));
                }*/
               // Log.d("ResultContacts",String.valueOf(contacts)+"\n");
                if(isSucces)
                {
                    if(responsedata.getRESPONSECODE().equalsIgnoreCase("200"))
                    {
                        //nomatchLayout.setVisibility(View.GONE);
                        Log.d("====>",String.valueOf(contacts));
                        tab4adapter = new Tab4adapter(context, contacts,mRecyclerView,userID,nomatchLayout);
                        mRecyclerView.setAdapter(tab4adapter);


                    }
                    else if(responsedata.getRESPONSECODE().equalsIgnoreCase("403"))
                    {
                        //mRecyclerView.setVisibility(View.GONE);
                        nomatchLayout.setVisibility(View.VISIBLE);
                        Log.d("====>",String.valueOf(contacts));
                        //Toast.makeText(context, "Database error", Toast.LENGTH_LONG).show();

                    }



                }
                break;

        }
    }


}


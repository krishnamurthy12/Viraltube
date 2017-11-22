package com.viraltubesolutions.viraltubeapp.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.adapters.ContactsAdapter;
import com.viraltubesolutions.viraltubeapp.beanclasses.ContactsBeanClass;

import java.util.ArrayList;
import java.util.List;


public class ContactsFragment extends Fragment {

    List<ContactsBeanClass> contactList=new ArrayList<ContactsBeanClass>();

    String[] names;
    String[] numbers;
    String phoneNumber;
    String contactName;
    ListView lv;
    ArrayList <String> aa= new ArrayList<String>();
    boolean isPermissionGranted = false;
    Context context;
    private static final int REQUEST_READ_CONTACTS = 1;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_contacts, container, false);
        names=getActivity().getResources().getStringArray(R.array.ContactNames);
        numbers=getActivity().getResources().getStringArray(R.array.MobileNumbers);

        //initializing listview
       // lv=(ListView)view.findViewById(R.id.contacts_listview);

        //initializing list
        contactList=getList();
        checkPermissions();
        if(isPermissionGranted) {
           // getNumber(getContext().getContentResolver());
        }

        //initializing listview
        //lv=(ListView)view.findViewById(R.id.contacts_listview);
        lv.setAdapter(new ContactsAdapter(getActivity(), R.layout.contactlist_singlerow_appearence,contactList));
        return view;
    }

   /* public void getNumber(ContentResolver cr)
    {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            contactName=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //String contactpic=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.SEARCH_DISPLAY_NAME_KEY));

            System.out.println(".................."+contactName);
            System.out.println(".................."+phoneNumber);
            System.out.println("..............................");

            //aa.add(contactpic);
            aa.add(contactName);
            aa.add(phoneNumber);
        }
        phones.close();// close cursor
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,aa);
        lv.setAdapter(adapter);
        //display contact numbers in the list
    }*/
    private List<ContactsBeanClass> getList()
    {
        for (int i=0;i<numbers.length;i++){

            ContactsBeanClass object =new ContactsBeanClass(names[i].charAt(0), names[i],numbers[i]);
            contactList.add(object);

        }
        return contactList;
    }

   private void checkPermissions() {
       if (ActivityCompat.checkSelfPermission(context,
               Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
       {
           isPermissionGranted = false;

           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                   requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},REQUEST_READ_CONTACTS);

           }
       }
       else{
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

}

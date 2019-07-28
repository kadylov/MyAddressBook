package com.example.myaddressbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<Contact> listOfContacts;

    private EditText mSearchTxt;
    private FloatingActionButton fBtnAddContact;

    private ContactAdapter contactAdapter;
    private RecyclerView recyclerView;

    private DataManager dataManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfContacts = new ArrayList<Contact>();
        dataManager = new DataManager(this);


        mSearchTxt = findViewById(R.id.searchTxt);
        fBtnAddContact = findViewById(R.id.fBtnAddContact);

        recyclerView = findViewById(R.id.recycle_view);
        contactAdapter = new ContactAdapter(this, listOfContacts);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(contactAdapter);


        fBtnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "in float button listener");
                ContactDialog newContact = new NewContactDialog(MainActivity.this);
                newContact.show(getSupportFragmentManager(), "");

            }
        });

        Log.d(TAG, "onCreate started.");
        reloadData();

        ContactDialog cc = new NewContactDialog(this);
        cc.show(getSupportFragmentManager(), "");


    }

    public void showContact(int contactToShow) {
        ContactDialog dialog = new ViewContactDialog(MainActivity.this);
        ((ViewContactDialog) dialog).sendSelectedContact(listOfContacts.get(contactToShow));
        dialog.show(getSupportFragmentManager(), "");
    }

    public void updateContact(Contact contact, String contactOldName) {

        dataManager.update(contact,contactOldName);

        listOfContacts.clear();
        reloadData();

    }

    public void createNewContact(Contact contact) {
        if (contact != null) {
            String name = contact.getFullName();

            String phone = contact.getPrimaryPhoneNumber().getPhoneNumber();
            String phoneType = Integer.toString(contact.getPrimaryPhoneNumber().getPhoneNumberType());
            String email = contact.getEmail();

            String street = contact.getPrimaryAddress().getStreet();
            String city = contact.getPrimaryAddress().getCity();
            String state = contact.getPrimaryAddress().getState();
            String zip = contact.getPrimaryAddress().getZipCode();
            byte[] profileImage = contact.getProfileImage();

            dataManager.insert(name, phone, phoneType, email, street, city, state, zip, profileImage);

            reloadData();
        }


    }


    public void reloadData() {
        listOfContacts.clear();

        Cursor cursor = dataManager.selectAll();

        int contactCount = cursor.getCount();

        Log.i("info", "Number of contact " + contactCount);
        int i = 0;
        if (contactCount > 0) {
            listOfContacts.clear();

            while (cursor.moveToNext()) {
                i = 1;
                String name = cursor.getString(i++);
                String phone = cursor.getString(i++);
                String phoneType = cursor.getString(i++);
                String email = cursor.getString(i++);

                String street = cursor.getString(i++);
                String city = cursor.getString(i++);
                String state = cursor.getString(i++);
                String zip = cursor.getString(i++);
                byte[] profileImage = cursor.getBlob(i++);



                Contact contact = new Contact(name, new PhoneNumber(phone, Integer.parseInt(phoneType)), email, new Address(street, city, state, zip), profileImage);

                listOfContacts.add(contact);
            }
            contactAdapter.notifyDataSetChanged();

        }

    }
}

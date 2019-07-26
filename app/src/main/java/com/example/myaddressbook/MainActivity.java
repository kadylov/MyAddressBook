package com.example.myaddressbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private List<Contact> listOfContacts = new ArrayList<Contact>();
    ;
    private EditText mSearchTxt;
    private FloatingActionButton fBtnAddContact;


    private ContactAdapter contactAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
                NewContactDialog newContact = new NewContactDialog(MainActivity.this);
                newContact.show(getSupportFragmentManager(), "");

            }
        });

        Log.d(TAG, "onCreate started.");
        createNewContact(null);

    }

    public void showContact(int contactToShow) {
        ContactDialog dialog = new ViewContactDialog(this);
        ((ViewContactDialog) dialog).sendSelectedContact(listOfContacts.get(contactToShow));
        dialog.show(getSupportFragmentManager(), "");
    }

    public void updateContact(Contact contact) {

        listOfContacts.clear();
        listOfContacts.add(contact);
        contactAdapter.notifyDataSetChanged();
//        reloadData();

    }

    public void createNewContact(Contact contact) {
        Contact c = new Contact("Kamil Adylov", new PhoneNumber("3035064789", PhoneNumber.CELL));
        String s = "\"https:/i.redd.it/tpsnoz5bzo501.jpg\"";
        Uri uri = Uri.parse("https:/i.redd.it/tpsnoz5bzo501.jpg");


        c.setProfileImage(s);
        listOfContacts.add(c);
        if (contact != null) {
            listOfContacts.add(contact);
            contactAdapter.notifyDataSetChanged();
//            reloadData();

        }


    }


    public void reloadData() {
        listOfContacts.clear();

    }
}

package com.example.myaddressbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

        init();


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

//        ContactDialog cc = new NewContactDialog(this);
//        cc.show(getSupportFragmentManager(), "");


    }


    // displays context menu when user long press on on of the contacts from the lsit
    public void showContextMenu(final int selectedContact) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final int EDIT = 0, SHARE_CONTACT = 1, DELETE = 2;
        String name = listOfContacts.get(selectedContact).getFullName();
        if (name == null)
            name = "AAAAA";

        final String[] options = {"Edit", "Share Contact", "Delete", "Cancel"};
        builder.setTitle(name);
//        builder.se
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case EDIT:                // Edit contact
                        ContactDialog contactDialog = new ViewContactDialog(MainActivity.this, true);
                        ((ViewContactDialog) contactDialog).sendSelectedContact(listOfContacts.get(selectedContact));
                        ((ViewContactDialog) contactDialog).enableAll(true);
                        contactDialog.show(getSupportFragmentManager(), "");


                        break;

                    case SHARE_CONTACT:       // Share contact to available messengers on the phone
                        break;

                    case DELETE:             // Delete contact
                        Log.i(TAG, "in the showContextMenu");
                        deleteContact(selectedContact);
                        break;

                    default:                        // cancel
                        Log.i(TAG, "in the onClick...dismiss");
                        dialog.dismiss();
                }

                dialog.dismiss();

            }
        });
        builder.show();

    }

    public void deleteContact(int contactToDelete) {
        Contact contact = listOfContacts.get(contactToDelete);

//        startActivityForResult(photoPickerIntent, CHOOSE_FROM_GALLERY);

        dataManager.delete(contact);
        Log.i(TAG, "deleteContact()");
        reloadData();
    }

    private void init() {
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
    }

    public void showContact(int contactToShow) {
        ContactDialog dialog = new ViewContactDialog(MainActivity.this, false);
        ((ViewContactDialog) dialog).sendSelectedContact(listOfContacts.get(contactToShow));
        dialog.show(getSupportFragmentManager(), "");
    }

    public void updateContact(Contact contact, String contactOldName) {

        dataManager.update(contact, contactOldName);

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

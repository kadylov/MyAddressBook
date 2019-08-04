package com.example.myaddressbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.EditorInfo;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Contact> listOfContacts;

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
        fBtnAddContact = findViewById(R.id.fBtnAddContact);
        reloadData();
        initRecyclerView();
        contactAdapter.notifyDataSetChanged();

        fBtnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactDialog newContact = new NewContactDialog(MainActivity.this);
                newContact.show(getSupportFragmentManager(), "");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                contactAdapter.getFilter().filter(newText);
                contactAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    // displays context menu when user long press on on of the contacts
    public void showContextMenu(final int selectedContact) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final int EDIT = 0, SHARE_CONTACT = 1, DELETE = 2;
        String name = listOfContacts.get(selectedContact).getFullName();
        if (name == null)
            name = "";

        final String[] options = {"Edit", "Share Contact", "Delete", "Cancel"};
        builder.setTitle(name);
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                dialog.dismiss();

                switch (item) {
                    case EDIT:                // Edit contact
                        ContactDialog contactDialog = new ViewContactDialog(MainActivity.this, true);
                        ((ViewContactDialog) contactDialog).sendSelectedContact(listOfContacts.get(selectedContact));
                        ((ViewContactDialog) contactDialog).enableAll(true);

//                        dialog.dismiss();
                        contactDialog.show(getSupportFragmentManager(), "");
                        break;

                    case SHARE_CONTACT:       // Share contact to available messengers on the phone

                        shareContactInfo(selectedContact);
                        break;

                    case DELETE:             // Delete contact
                        deleteContact(selectedContact);
                        break;

                    default:                        // cancel
                        dialog.dismiss();
                }
            }
        });
        builder.show();

    }


    public void shareContactInfo(int contactToShare) {

        Contact contact = listOfContacts.get(contactToShare);
        String contactInfo = contact.getFullName() + "\n" + contact.getPrimaryPhoneNumber().getNumber();

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "That dude's phone number.");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, contactInfo);
        startActivity(Intent.createChooser(intent, "Choose sharing option"));
    }

    public void deleteContact(int contactToDelete) {
        Contact contact = listOfContacts.get(contactToDelete);

        dataManager.delete(contact);
        reloadData();
        contactAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
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

        reloadData();
        contactAdapter.notifyDataSetChanged();
    }

    public void createNewContact(Contact contact) {
        if (contact != null) {

            String name = contact.getFullName();

            // check whether contact with the given name exists in the database
            if (dataManager.findContactByName(name)) {
                String title = "Contact name '" + name + "' is already in use.";
                String message = "Please create contact with a different name";
                new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton(android.R.string.ok, null).show();

            } else {

                String phone = contact.getPrimaryPhoneNumber().getNumber();
                String phoneType = Integer.toString(contact.getPrimaryPhoneNumber().getPhoneNumberType());
                String email = contact.getEmail();

                String street = contact.getPrimaryAddress().getStreet();
                String city = contact.getPrimaryAddress().getCity();
                String state = contact.getPrimaryAddress().getState();
                String zip = contact.getPrimaryAddress().getZipCode();
                byte[] profileImage = contact.getProfileImage();

                dataManager.insert(name, phone, phoneType, email, street, city, state, zip, profileImage);

                reloadData();
                contactAdapter.notifyDataSetChanged();
            }
        }
    }


    public void reloadData() {
        listOfContacts.clear();

        Cursor cursor = dataManager.selectAll();
        int contactCount = cursor.getCount();

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

        }

    }
}

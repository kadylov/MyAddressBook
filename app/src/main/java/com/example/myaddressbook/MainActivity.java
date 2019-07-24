package com.example.myaddressbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    private List<String> mNames = new ArrayList<>();
    private List<String> mImageUrls = new ArrayList<>();

    private EditText mSearchTxt;

    private FloatingActionButton fBtnAddContact;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchTxt = findViewById(R.id.searchTxt);
        fBtnAddContact = findViewById(R.id.fBtnAddContact);

//        DialogViewContact viewContact = new DialogViewContact(MainActivity.this);
//        viewContact.show(getSupportFragmentManager(),"");

        fBtnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "in float button listener");
//                DialogNewContact newContact = new DialogNewContact();
//                newContact.show(getSupportFragmentManager(),"");

                Dialog dialog = new DialogContact(MainActivity.this);
//                ((DialogContact) dialog).disableAll();
                dialog.getEditTextFirstName().setText("asdasdasdsaasd");

                dialog.show(getSupportFragmentManager(),"");
            }
        });

        Log.d(TAG, "onCreate started.");
        initImageBitmaps();

    }

    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps preparing bitmaps.");

        mNames.add("Jason DeRulo");
        mImageUrls.add("https:/i.redd.it/tpsnoz5bzo501.jpg");
        initRecycleView();

    }

    private void initRecycleView() {
        Log.d(TAG, "initRecycleView: init initRecycleView");
        RecyclerView recyclerView = findViewById(R.id.recycle_view);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mImageUrls, this, mSearchTxt);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}

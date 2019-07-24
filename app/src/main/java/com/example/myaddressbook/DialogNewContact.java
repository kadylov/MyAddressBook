package com.example.myaddressbook;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class DialogNewContact extends DialogFragment {

    private static final String TAG = "DialogNewContact";

    final int TAKE_PHOTO = 0;
    final int CHOOSE_FROM_GALLERY = 1;


    private CircleImageView imgBtn;

    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtPhoneNumber;
    private Spinner spnrPhoneType;
    private EditText txtEmail;
    private EditText txtStreet;
    private EditText txtCity;
    private EditText txtState;
    private EditText txtZip;

    private Button btnCreate;
    private Button btnCancel;


    private AlertDialog.Builder builder;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);


        init();


        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageOptionDialog();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        MainActivity callingActivity = (MainActivity) getActivity();
//        callingActivity.createNewContact (contact);

//        dismiss();


        return builder.create();
    }

    private void init() {

        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);
        builder.setView(dialogView).setMessage(" ");

        imgBtn = dialogView.findViewById(R.id.imgBtn);

        txtFirstName = dialogView.findViewById(R.id.txtFirstName);
        txtLastName = dialogView.findViewById(R.id.txtLastName);
        txtPhoneNumber = dialogView.findViewById(R.id.txtPhoneNumber);
        spnrPhoneType = dialogView.findViewById(R.id.spnrPhoneType);
        txtEmail = dialogView.findViewById(R.id.txtEmail);
        txtStreet = dialogView.findViewById(R.id.txtStreet);
        txtCity = dialogView.findViewById(R.id.txtCity);
        txtState = dialogView.findViewById(R.id.txtState);
        txtZip = dialogView.findViewById(R.id.txtZip);

        btnCreate = dialogView.findViewById(R.id.btnEdit);
        btnCancel = dialogView.findViewById(R.id.btnMainMenu);

    }


    private void createContact() {

        String firstName = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String phone = txtPhoneNumber.getText().toString();
        String phoneType = spnrPhoneType.getSelectedItem().toString();
        String email = txtEmail.getText().toString();
        String street = txtStreet.getText().toString();
        String city = txtCity.getText().toString();
        String state = txtState.getText().toString();
        String zip = txtZip.getText().toString();
    }


    private void showImageOptionDialog() {

        final String[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case TAKE_PHOTO:                // Take Photo
                        takePictureFromCamera(TAKE_PHOTO);
                        break;

                    case CHOOSE_FROM_GALLERY:       // Choose from Gallery
                        pickPhotoFromGallery(CHOOSE_FROM_GALLERY);
                        break;

                    default:                        // cancel
                        dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    private void pickPhotoFromGallery(int requestCode) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, requestCode);

    }

    private void takePictureFromCamera(int requestCode) {

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, requestCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    if (resultCode == RESULT_OK) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");

                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), selectedImage);
                        imgBtn.setBackground(bitmapDrawable);
                    }
                    break;

                case CHOOSE_FROM_GALLERY:
                    if (resultCode == RESULT_OK) {

                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            imgBtn.setImageURI(selectedImage);
                        }

                    }
                    break;
            }
        }

    }
}

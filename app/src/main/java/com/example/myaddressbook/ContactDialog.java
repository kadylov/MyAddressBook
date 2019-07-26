package com.example.myaddressbook;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public abstract class ContactDialog extends DialogFragment {

    final int TAKE_PHOTO = 0;
    final int CHOOSE_FROM_GALLERY = 1;


    private CircleImageView imgBtn;

    private EditText txtFullName;
    private EditText txtPhoneNumber;
    private Spinner spnrPhoneType;
    private EditText txtEmail;
    private EditText txtStreet;
    private EditText txtCity;
    private EditText txtState;
    private EditText txtZip;


    private LinearLayout buttonLayout;

    private AlertDialog.Builder builder;
    private View dialogView;
    private Activity activity;

    private LayoutInflater inflater;

    public ContactDialog(Activity activity, int inflaterResource) {

        this.activity = activity;
        builder = new AlertDialog.Builder(activity);

        inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(inflaterResource, null);
        init();
        builder.setView(dialogView).setMessage("asasdsadsadasd ");

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageOptionDialog();
            }
        });
    }

    @Override
    public abstract android.app.Dialog onCreateDialog(Bundle savedInstanceState);


    private void init() {

        imgBtn = dialogView.findViewById(R.id.profileImageCircleView);

        txtFullName = dialogView.findViewById(R.id.txtFullName);
        txtPhoneNumber = dialogView.findViewById(R.id.txtPhoneNumber);
        spnrPhoneType = dialogView.findViewById(R.id.spnrPhoneType);
        txtEmail = dialogView.findViewById(R.id.txtEmail);
        txtStreet = dialogView.findViewById(R.id.txtStreet);
        txtCity = dialogView.findViewById(R.id.txtCity);
        txtState = dialogView.findViewById(R.id.txtState);
        txtZip = dialogView.findViewById(R.id.txtZip);

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

    public CircleImageView getImageButton() {
        return imgBtn;
    }

    public EditText getEditTextFulltName() {
        return txtFullName;
    }

    public EditText getEditTextPhoneNumber() {
        return txtPhoneNumber;
    }

    public Spinner getSpinnerPhoneType() {
        return spnrPhoneType;
    }

    public EditText getEditTextEmail() {
        return txtEmail;
    }

    public EditText getEditTextStreet() {
        return txtStreet;
    }

    public EditText getEditTextCity() {
        return txtCity;
    }

    public EditText getEditTextState() {
        return txtState;
    }

    public EditText getEditTextZip() {
        return txtZip;
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public View getDialogView() {
        return dialogView;
    }

    public LinearLayout getButtonLayout() {
        return buttonLayout;
    }

    public CircleImageView getProfileImage() {
        return imgBtn;
    }
}

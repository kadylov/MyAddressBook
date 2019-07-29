package com.example.myaddressbook;

import android.app.Activity;
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
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class NewContactDialog extends ContactDialog {

    private static final String TAG = "NewContactDialog";
    private static final int xmlResource = R.layout.dialog_add_contact;


    private Button btnAdd;
    private Button btnCancel;

    public NewContactDialog(Activity activity) {
        super(activity, xmlResource);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        btnAdd = getDialogView().findViewById(R.id.btnAdd);
        btnCancel = getDialogView().findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createContact();
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


//        Toolbar toolbar = getView().findViewById(R.id.toolbar);


        return getBuilder().create();
    }

    private void createContact() {

        String fullname = getEditTextFulltName().getText().toString();
        String email = getEditTextEmail().getText().toString();

        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber(getEditTextPhoneNumber().getText().toString());
        phoneNumber.setPhoneNumberType(getSpinnerPhoneType().getSelectedItemPosition());

        Address address = new Address();
        address.setStreet(getEditTextStreet().getText().toString());
        address.setCity(getEditTextStreet().getText().toString());
        address.setState(getEditTextStreet().getText().toString());
        address.setCity(getEditTextState().getText().toString());
        address.setZipCode(getEditTextStreet().getText().toString());

        byte[] profileImg = getProfileImageInBytes();

        MainActivity callingActivity = (MainActivity) getActivity();

        if (fullname == null || fullname.equals(""))
            callingActivity.createNewContact(null);
        else {


            callingActivity.createNewContact(new Contact(fullname, phoneNumber, email, address, profileImg));

        }
    }


}

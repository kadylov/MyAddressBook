package com.example.myaddressbook;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ViewContactDialog extends ContactDialog {

    private Button btnEdit;
    private Button btnMainMenu;
    private Button btnUpdate;

    private Contact contact;
    private String oldContactName;

    private boolean enableFlag;

    private static final int xmlResource = R.layout.dialog_view_contact;

    public ViewContactDialog(Activity activity, boolean enableFlag) {
        super(activity, xmlResource);
        this.enableFlag = enableFlag;

        btnEdit = getDialogView().findViewById(R.id.btnEdit);
        btnUpdate = getDialogView().findViewById(R.id.btnUpdate);
        btnMainMenu = getDialogView().findViewById(R.id.btnMainMenu);
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {



        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enableAll(true);
                btnEdit.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);

            }
        });

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateContactInfo();
                dismiss();
            }
        });


        loadData();
        enableAll(enableFlag);

        return getBuilder().create();
    }

    private void loadData() {
        String fullname = contact.getFullName();
        oldContactName = fullname;
        getEditTextFulltName().setText(fullname);

        if (contact.getNumberOfPhoneNumbers() > 0) {
            PhoneNumber phone = contact.getPrimaryPhoneNumber();
            getEditTextPhoneNumber().setText(phone.getNumber());
            getSpinnerPhoneType().setSelection(phone.getPhoneNumberType());
        }

        getEditTextEmail().setText(contact.getEmail());

        if (contact.getNumberOfAddresses() > 0) {
            Address address = contact.getlistOfAddresses().get(0);
            getEditTextStreet().setText(address.getStreet());
            getEditTextCity().setText(address.getCity());
            getEditTextState().setText(address.getState());
            getEditTextZip().setText(address.getZipCode());
        }

        byte[] profileImage = contact.getProfileImage();
        if (profileImage != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(profileImage, 0, profileImage.length);
            getCircleViewProfileImage().setImageBitmap(bitmap);
        }
    }


    public void enableAll(boolean flag) {

        // disable profile image Image view
        getCircleViewProfileImage().setClickable(flag);

        // disable full name text field
        getEditTextFulltName().setEnabled(flag);
        getEditTextFulltName().setTextColor(Color.BLACK);

        // disable phone text field
        getEditTextPhoneNumber().setEnabled(flag);
        getEditTextPhoneNumber().setTextColor(Color.BLACK);

        // disable phone type spinner
        getSpinnerPhoneType().setEnabled(flag);
//        View v = getSpinnerPhoneType().getSelectedView();
//        ((EditText)v).setTextColor(Color.BLACK);
//        ((EditText) getSpinnerPhoneType().getSelectedView()).setTextColor(Color.BLACK);

        // disable email text field
        getEditTextEmail().setEnabled(flag);
        getEditTextEmail().setTextColor(Color.BLACK);

        // disable street text field
        getEditTextStreet().setEnabled(flag);
        getEditTextStreet().setTextColor(Color.BLACK);


        // disable city text field
        getEditTextCity().setEnabled(flag);
        getEditTextCity().setTextColor(Color.BLACK);


        // disable state text field
        getEditTextState().setEnabled(flag);
        getEditTextState().setTextColor(Color.BLACK);


        // disable zip text field
        getEditTextZip().setEnabled(flag);
        getEditTextZip().setTextColor(Color.BLACK);

        if (enableFlag == true){
            btnEdit.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        }

    }

    public void sendSelectedContact(Contact contact) {
        this.contact = contact;
    }

    public void updateContactInfo() {

        PhoneNumber newPhoneNumber = new PhoneNumber();
        newPhoneNumber.setPhoneNumber(getEditTextPhoneNumber().getText().toString());
        newPhoneNumber.setPhoneNumberType(getSpinnerPhoneType().getSelectedItemPosition());

        Address newAddress = new Address();
        newAddress.setStreet(getEditTextStreet().getText().toString());
        newAddress.setCity(getEditTextCity().getText().toString());
        newAddress.setState(getEditTextStreet().getText().toString());
        newAddress.setZipCode(getEditTextZip().getText().toString());

        contact.setFullName(getEditTextFulltName().getText().toString());
        contact.setEmail(getEditTextEmail().getText().toString());
        contact.updateAddress(newAddress);
        contact.updatePhoneNumber(newPhoneNumber);

        byte[] profileImg = getProfileImageInBytes();

        contact.setProfileImage(profileImg);

        MainActivity callingActivity = (MainActivity) getActivity();
        callingActivity.updateContact(contact, oldContactName);
    }
}

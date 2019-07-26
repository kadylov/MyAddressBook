package com.example.myaddressbook;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ViewContactDialog extends ContactDialog {

    private Button btnEdit;
    private Button btnMainMenu;
    private Button btnUpdate;

    private Contact contact;

    private static final int xmlResource = R.layout.dialog_view_contact;

    public ViewContactDialog(Activity activity) {
        super(activity, xmlResource);
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        btnEdit = getDialogView().findViewById(R.id.btnEdit);
        btnUpdate = getDialogView().findViewById(R.id.btnUpdate);
        btnMainMenu = getDialogView().findViewById(R.id.btnMainMenu);

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
        enableAll(false);
        return getBuilder().create();
    }

    private void loadData() {
        getEditTextFulltName().setText(contact.getFullName());

        if (contact.getNumberOfPhoneNumbers() > 0) {
            PhoneNumber phone = contact.getPrimaryPhoneNumber();
            getEditTextPhoneNumber().setText(phone.getPhoneNumber());
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
    }


    public void enableAll(boolean flag) {

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

        MainActivity callingActivity = (MainActivity) getActivity();
        callingActivity.updateContact(contact);
    }
}

package com.example.myaddressbook;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewContactDialog extends ContactDialog {

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
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return getBuilder().create();
    }

    private void createContact() {

        String fullname = getEditTextFulltName().getText().toString();
        String email = getEditTextEmail().getText().toString();

        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setNumber(getEditTextPhoneNumber().getText().toString());
        phoneNumber.setPhoneNumberType(getSpinnerPhoneType().getSelectedItemPosition());

        Address address = new Address();
        address.setStreet(getEditTextStreet().getText().toString());
        address.setCity(getEditTextCity().getText().toString());
        address.setState(getEditTextState().getText().toString());
        address.setZipCode(getEditTextZip().getText().toString());

        byte[] profileImg = getProfileImageInBytes();



        if (fullname == null || fullname.equals("")) {

            getEditTextFulltName().setError("name is required");
        } else {
            MainActivity callingActivity = (MainActivity) getActivity();
            callingActivity.createNewContact(new Contact(fullname, phoneNumber, email, address, profileImg));
            dismiss();
        }
    }


}

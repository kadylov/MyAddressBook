package com.example.myaddressbook;

import android.app.Activity;
import android.graphics.Color;

public class DialogContact extends Dialog {
    public DialogContact(Activity activity) {
        super(activity);

        super.getEditTextFirstName().setText("asdasdsaads");
    }



    public void disableAll(){

        super.getEditTextFirstName().setFocusable(false);
//        super.getEditTextFirstName().setEnabled(false);
        super.getEditTextFirstName().setCursorVisible(false);
        super.getEditTextFirstName().setKeyListener(null);
        super.getEditTextFirstName().setBackgroundColor(Color.TRANSPARENT);

//        txtFirstName.setFocusable(false);
//        txtLastName.setEnabled(false);
//        txtPhoneNumber.setEnabled(false);
//        spnrPhoneType.setEnabled(false);
//        txtEmail.setEnabled(false);
//        txtStreet.setEnabled(false);
//        txtCity.setEnabled(false);
//        txtState.setEnabled(false);
//        txtZip.setEnabled(false);


    }
}

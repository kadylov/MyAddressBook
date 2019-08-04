package com.example.myaddressbook;

public class PhoneNumber implements Comparable<PhoneNumber> {

    public static final int CELL = 0;
    public static final int HOME = 1;
    public static final int WORK = 2;


    private String phoneNumber;
    private int phoneNumberType;


    public PhoneNumber() {
        phoneNumber = "";
        phoneNumberType = 0;
    }


    public PhoneNumber(String phoneNumber, int phoneNumberType) {
        this.phoneNumber = phoneNumber;
        this.phoneNumberType = phoneNumberType;
    }

    public String getNumber() {
        return phoneNumber;
    }

    public void setNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber newPhoneNumber) {
        this.phoneNumber=newPhoneNumber.getNumber();
        this.phoneNumberType=newPhoneNumber.phoneNumberType;
    }

    public int getPhoneNumberType() {
        return phoneNumberType;
    }

    public void setPhoneNumberType(int phoneNumberType) {
        this.phoneNumberType = phoneNumberType;
    }

    @Override
    public int compareTo(PhoneNumber otherNumber) {
        if (this.phoneNumber == otherNumber.getNumber())
            return 0;

        else return 1;
    }
}

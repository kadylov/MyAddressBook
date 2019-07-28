package com.example.myaddressbook;

import java.util.ArrayList;
import java.util.List;

public class Contact implements Comparable<Contact> {

    private String fullName = "";
    private String email = "";
    private byte[] profileImage = {0};

    private List<PhoneNumber> listOfPhoneNumbers = new ArrayList<PhoneNumber>();
    private List<Address> listOfAddresses = new ArrayList<Address>();

    public Contact(String fullName) {
        this.fullName = fullName;
    }

    public Contact(String fullName, PhoneNumber phoneNumber) {
        this.fullName = fullName;
        listOfPhoneNumbers.add(phoneNumber);
    }

    public Contact(String fullName, PhoneNumber phoneNumber, String email, Address address, byte[] profileImage) {
        this.fullName = fullName;
        listOfPhoneNumbers.add(phoneNumber);
        this.email = email;
        listOfAddresses.add(address);
        this.profileImage = profileImage;
    }

    public int getNumberOfPhoneNumbers() {
        return listOfPhoneNumbers.size();
    }

    public void addPhoneNumber(PhoneNumber newNumber) {
        listOfPhoneNumbers.add(newNumber);
    }

    public void updatePhoneNumber(PhoneNumber phoneNumber) {
        listOfPhoneNumbers.get(0).setPhoneNumber(phoneNumber.getPhoneNumber());
        listOfPhoneNumbers.get(0).setPhoneNumberType(phoneNumber.getPhoneNumberType());

    }

    public void deletePhoneNumber(PhoneNumber phoneNumber) {
        listOfPhoneNumbers.remove(phoneNumber);
    }

    public void addNewAddress(Address newAddress) {
        listOfAddresses.add(newAddress);
    }


    public void removeAddress(Address address) {
        listOfAddresses.remove(address);
    }

    public void updateAddress(Address newAddress) {
        listOfAddresses.clear();
        listOfAddresses.add(newAddress);
    }

    public int getNumberOfAddresses() {
        return listOfAddresses.size();
    }

    @Override
    public int compareTo(Contact contact) {
        return this.fullName.compareTo(contact.getFullName());
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<PhoneNumber> getlistOfPhoneNumbers() {
        return listOfPhoneNumbers;
    }

    public void setlistOfPhoneNumbers(List<PhoneNumber> listOfPhoneNumbers) {
        this.listOfPhoneNumbers = listOfPhoneNumbers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Address> getlistOfAddresses() {
        return listOfAddresses;
    }

    public void setlistOfAddresses(List<Address> listOfAddresses) {
        this.listOfAddresses = listOfAddresses;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
            this.profileImage = profileImage;
    }


    public PhoneNumber getPrimaryPhoneNumber() {
        return listOfPhoneNumbers.get(0);
    }

    public Address getPrimaryAddress() {
        return listOfAddresses.get(0);
    }


}

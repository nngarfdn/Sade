package com.udindev.sade.model;

public class Profile {
    private String address;
    private String phoneNumber;
    private String waNumber;

    public Profile(String address, String phoneNumber, String waNumber) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.waNumber = waNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWaNumber() {
        return waNumber;
    }

    public void setWaNumber(String waNumber) {
        this.waNumber = waNumber;
    }
}

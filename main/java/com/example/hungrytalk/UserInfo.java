package com.example.hungrytalk;

public class UserInfo  {
    private String name;
    private String phoneNumber;
    private String date;
    private String address;
    private String photoUrl;

    public UserInfo (String name, String number, String date, String address, String photoUrl) {
        this.name = name;
        this.phoneNumber = number;
        this.date = date;
        this.address = address;

        this.photoUrl = photoUrl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoUrl(){
        return this.photoUrl;
    }
    public void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }
}
package com.example.lifebeacon.Model;

public class ContactData {
    private String name;
    private String phonenumber;

    public ContactData() {

    }

    public ContactData(String name, String phonenumber) {
        this.name = name;
        this.phonenumber = phonenumber;
    }

    public String getName() {
        return name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }
}

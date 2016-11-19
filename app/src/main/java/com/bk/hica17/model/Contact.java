package com.bk.hica17.model;

import java.io.Serializable;

/**
 * Created by khanh on 17/11/2016.
 */
public class Contact implements Serializable {
    private String name;
    private String phoneNumber;
    private int role;

    public Contact() {}

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Contact(String name, String phone, int role) {
        this.name = name;
        this.phoneNumber = phone;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRole() {
        return role;
    }
}

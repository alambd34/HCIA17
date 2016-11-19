package com.bk.hica17.model;

import java.util.ArrayList;

/**
 * Created by trung on 17/11/2016.
 */
public class ContactGroup {

    ArrayList<Contact> contactList;
    String textHead;

    public ContactGroup(ArrayList<Contact> contactList, String textHead) {
        this.contactList = contactList;
        this.textHead = textHead;
    }

    public ArrayList<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<Contact> contactList) {
        this.contactList = contactList;
    }

    public String getTextHead() {
        return textHead;
    }

    public void setTextHead(String textHead) {
        this.textHead = textHead;
    }
}

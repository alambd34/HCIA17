package com.bk.hica17.model;

/**
 * Created by trung on 17/11/2016.
 */
public class CallLog {

    private Contact contact;
    private String time;
    private int catalog;
    private String status;

    public CallLog(String time, Contact contact, int catalog, String status) {
        this.time = time;
        this.contact = contact;
        this.catalog = catalog;
        this.status = status;
    }

    public int getCatalog() {
        return catalog;
    }

    public void setCatalog(int catalog) {
        this.catalog = catalog;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

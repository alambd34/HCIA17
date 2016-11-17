package com.bk.hica17.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.bk.hica17.BaseApplication;

/**
 * Created by khanh on 14/11/2016.
 */
public class ContactMatching {

    private String strNumInput;

    public ContactMatching(String strNumInput) {
        this.strNumInput = strNumInput;
    }


    public Contact getContactMatching() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Context context = BaseApplication.getContext();
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        Contact result = null;
        while (cursor.moveToNext()) {
            String colPhone = ContactsContract.CommonDataKinds.Phone.NUMBER;
            int colPhoneIndex = cursor.getColumnIndex(colPhone);
            String phone = cursor.getString(colPhoneIndex);
            if (phone.contains(strNumInput)) {
                String colName = ContactsContract.Contacts.DISPLAY_NAME;
                int colNameIndex = cursor.getColumnIndex(colName);
                String name = cursor.getString(colNameIndex);
                result = new Contact(name, phone);
                break;
            }
        }
        cursor.close();
        return result;
    }

    public String getInputString() {
        return strNumInput;
    }

}

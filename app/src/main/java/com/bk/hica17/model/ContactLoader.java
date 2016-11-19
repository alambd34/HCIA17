package com.bk.hica17.model;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created by Dell on 11-Nov-16.
 */
public class ContactLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    Context mContext;
    ArrayList<Contact> contacts;

    public static final String QUERY_KEY = "query";

    public static final String TAG = "ContactLoader";

    private OnLoadFinishCallback mLoadCallback;

    public interface OnLoadFinishCallback {

        void onLoadFinish(ArrayList<Contact> contacts);
    }

    public ContactLoader(Context context) {
        mContext = context;
    }


    /**
     * Where the Contact table excels is matching text queries
     * not just data dump from Contacts db. One search term is used to query
     * display name, email address and phone number. In this case, the query was
     * extracted from an incoming intent in the handleIntent() method via the
     * intent.getStringExtra() method
     *
     * @param i
     * @param bundle
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String query = bundle.getString(QUERY_KEY);
        Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Contactables.CONTENT_FILTER_URI, query);
        // Easy way to limit the query to contacts with phone numbers.
        String selection = ContactsContract.CommonDataKinds.Contactables.HAS_PHONE_NUMBER + " = " + 1;

        // sort results such that rows for the same contact stay together
        String sortBy = ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY;

        return new CursorLoader(
                mContext,   // Context
                uri,        // URI representing the table/resoursc to be queried
                null,       // projection - the list of columns to return. Null mean "all"
                selection,  // selection - Which rows to return (condition rows must match)
                null,       // selection args - can be provided separately and subbed into selection.
                sortBy);    // string specifying sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.getCount() == 0) {
            contacts = null;
        } else {
            contacts = new ArrayList<>();
            int phoneColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME);
            int lookupColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY);
            int typeColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.MIMETYPE);

            cursor.moveToFirst();
            // Lookup key is the easiest way to verify a row of data is for the same
            // contact as the previous row
            String lookupKey = "";
            Contact contact;
            do {

                String currentLookupKey = cursor.getString(lookupColumnIndex);
                if (!lookupKey.equals(currentLookupKey)) {
                    String displayName = cursor.getString(nameColumnIndex);
                    lookupKey = currentLookupKey;
                    String mineType = cursor.getString(typeColumnIndex);
                    if (mineType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                        String phoneNumber = cursor.getString(phoneColumnIndex);
                        contact = new Contact(displayName, phoneNumber);
                        contacts.add(contact);
                    }
                }
            } while (cursor.moveToNext());
        }

        if (mLoadCallback != null) {
            mLoadCallback.onLoadFinish(contacts);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        contacts = null;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setLoadCallback(OnLoadFinishCallback onLoadFinishCallback) {
        this.mLoadCallback = onLoadFinishCallback;
    }
}

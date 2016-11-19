package com.bk.hica17.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.adapter.ContactAdapter;
import com.bk.hica17.adapter.SearchContactAdapter;
import com.bk.hica17.callback.OnContactItemClickListener;
import com.bk.hica17.model.Contact;
import com.bk.hica17.model.ContactGroup;
import com.bk.hica17.ui.activity.PersonalActivity;
import com.bk.hica17.utils.SpaceItem;

import java.util.ArrayList;

/**
 * Created by trung on 17/11/2016.
 */
public class ContactFragment extends Fragment {

    View mRootView;
    EditText mEdSearch;
    RecyclerView mRVContact;

    LinearLayout llSearchContact;
    TextView txtSearchContact;
    RecyclerView mRVSearchContact;
    LinearLayout llNoSearchContact;

    ArrayList<ContactGroup> mContactGroupList;

    SearchContactAdapter searchContactAdapter;

    public static final int GUEST = 1;
    public static final int CUSTOMER = 2;
    public static final int STAFF = 3;
    public static final int FAMILY = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.contact_fragment, container, false);
        mEdSearch = (EditText) mRootView.findViewById(R.id.edSearch);
        mRVContact = (RecyclerView) mRootView.findViewById(R.id.rvContact);
        llSearchContact = (LinearLayout) mRootView.findViewById(R.id.llSearchContact);
        txtSearchContact = (TextView) mRootView.findViewById(R.id.txtSearchContact);
        mRVSearchContact = (RecyclerView) mRootView.findViewById(R.id.rvSearchContact);
        llNoSearchContact = (LinearLayout) mRootView.findViewById(R.id.llNoSearchContact);

        initData();
        initRV();

        addTextListener();
        return mRootView;
    }

    public void initData(){

        mContactGroupList = new ArrayList<ContactGroup>();

        ArrayList<Contact> contacts = new ArrayList<Contact>();
        contacts.add(new Contact("anh long", "01238485", GUEST));
        contacts.add(new Contact("anh minh", "0123456", CUSTOMER));
        mContactGroupList.add(new ContactGroup(contacts, "A"));

        contacts = new ArrayList<Contact>();
        contacts.add(new Contact("bach", "0123456", FAMILY));
        contacts.add(new Contact("bach khoa", "0123456", STAFF));
        mContactGroupList.add(new ContactGroup(contacts, "B"));

        contacts = new ArrayList<Contact>();
        contacts.add(new Contact("chi thao", "0123456", CUSTOMER));
        contacts.add(new Contact("chu minh", "0123456", GUEST));
        mContactGroupList.add(new ContactGroup(contacts, "C"));

        contacts = new ArrayList<Contact>();
        contacts.add(new Contact("di ha", "0123456", FAMILY));
        contacts.add(new Contact("di hien", "0123456", STAFF));
        contacts.add(new Contact("dung cntt", "0123456", CUSTOMER));
        mContactGroupList.add(new ContactGroup(contacts, "D"));

        contacts = new ArrayList<Contact>();
        contacts.add(new Contact("hai", "0123456", GUEST));
        contacts.add(new Contact("hai cntt", "0123456", GUEST));
        mContactGroupList.add(new ContactGroup(contacts, "H"));

        contacts = new ArrayList<Contact>();
        contacts.add(new Contact("trung", "0123456", CUSTOMER));
        contacts.add(new Contact("tu", "0123456", FAMILY));
        contacts.add(new Contact("tung", "0123456", FAMILY));
        mContactGroupList.add(new ContactGroup(contacts, "T"));

    }

    public void initRV(){

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRVContact.setLayoutManager(layoutManager);
        //mRVSearchContact.setLayoutManager(layoutManager);

        int space = 15;
        SpaceItem spaceItem = new SpaceItem(space, SpaceItem.VERTICAL_NORMAL);
        mRVContact.addItemDecoration(spaceItem);

        ContactAdapter contactAdapter = new ContactAdapter(getActivity(), mContactGroupList);
        mRVContact.setAdapter(contactAdapter);

        contactAdapter.setListener(new OnContactItemClickListener() {
            @Override
            public void OnClick(Contact contact) {
                Intent intent = new Intent(getActivity(), PersonalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("contact", contact);
                intent.putExtra("ContactFragment", bundle);
                startActivity(intent);
            }
        });
    }

    public void addTextListener(){

        mEdSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().toLowerCase();

                if (!text.equals("")){

                    ArrayList<Contact> contactList = searchContact(text);

                    if (contactList.size() == 0){

                        llNoSearchContact.setVisibility(View.VISIBLE);
                        mRVContact.setVisibility(View.INVISIBLE);
                        llSearchContact.setVisibility(View.INVISIBLE);
                    }
                    else {
                        txtSearchContact.setText(contactList.size() + " kết quả");
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mRVSearchContact.setLayoutManager(layoutManager);

                        mRVContact.setVisibility(View.INVISIBLE);
                        llSearchContact.setVisibility(View.VISIBLE);
                        llNoSearchContact.setVisibility(View.INVISIBLE);

                        searchContactAdapter = new SearchContactAdapter(getActivity(), contactList);
                        mRVSearchContact.setAdapter(searchContactAdapter);
                    }

                }
                else {

                    mRVContact.setVisibility(View.VISIBLE);
                    llSearchContact.setVisibility(View.INVISIBLE);
                    llNoSearchContact.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public ArrayList<Contact> searchContact(String text){

        ArrayList<Contact> contactList = new ArrayList<Contact>();
        int size = mContactGroupList.size();

        for (int i=0; i<size; i++){

            ContactGroup contactGroup = mContactGroupList.get(i);
            ArrayList<Contact> contacts = contactGroup.getContactList();
            for (int j=0; j<contacts.size(); j++){

                Contact contact = contacts.get(j);

                if (contact.getName().contains(text)){

                    contactList.add(contact);
                }
            }

        }

        return contactList;
    }
}

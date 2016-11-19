package com.bk.hica17.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bk.hica17.R;
import com.bk.hica17.adapter.viewholder.SeerchContactViewHolder;
import com.bk.hica17.callback.OnContactItemClickListener;
import com.bk.hica17.model.Contact;

import java.util.ArrayList;

/**
 * Created by trung on 19/11/2016.
 */
public class SearchContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    ArrayList<Contact> mContactList;
    OnContactItemClickListener mOnContactItemClickListener;

    public SearchContactAdapter(Activity activity, ArrayList<Contact> contactList){

        mActivity = activity;
        mContactList = contactList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = null;

        view = layoutInflater.inflate(R.layout.item_search_contact, parent, false);
        SeerchContactViewHolder holder = new SeerchContactViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Contact contact = mContactList.get(position);

        ((SeerchContactViewHolder)holder).populate(contact);
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }
}

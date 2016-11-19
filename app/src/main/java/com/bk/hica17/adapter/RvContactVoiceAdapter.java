package com.bk.hica17.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bk.hica17.R;
import com.bk.hica17.adapter.viewholder.ContactHolder;
import com.bk.hica17.callback.OnItemContactVoiceClickCallback;
import com.bk.hica17.model.Contact;

import java.util.ArrayList;

/**
 * Created by Dell on 19-Nov-16.
 */
public class RvContactVoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Contact> mContacts;
    Activity mActivity;
    OnItemContactVoiceClickCallback itemClickListener;

    public RvContactVoiceAdapter(Activity activity, ArrayList<Contact> contacts) {
        this.mActivity = activity;
        mContacts = new ArrayList<>();
        if (contacts != null) {
            mContacts.addAll(contacts);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mRootView = LayoutInflater.from(mActivity).inflate(R.layout.cardview_contact_voice, parent, false);
        ContactHolder holder = new ContactHolder(mRootView, mActivity);
        holder.setOnItemClickListener(itemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ContactHolder contactHolder = (ContactHolder)holder;
        contactHolder.populate(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public ArrayList<Contact> getContacts() {
        return mContacts;
    }

    public void removeAll() {
        mContacts.removeAll(mContacts);
        this.notifyDataSetChanged();
    }


    public void addAll(ArrayList<Contact> contacts) {

        if (contacts != null && contacts.size() > 0) {
            mContacts.addAll(contacts);
            this.notifyDataSetChanged();
        }
    }

    public void setItemClickListener(OnItemContactVoiceClickCallback itemClickListener) {
        this.itemClickListener = itemClickListener;
    }



}

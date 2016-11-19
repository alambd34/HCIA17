package com.bk.hica17.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bk.hica17.R;
import com.bk.hica17.adapter.viewholder.OneItemContactViewHolder;
import com.bk.hica17.adapter.viewholder.ThreeItemContactViewHolder;
import com.bk.hica17.adapter.viewholder.TwoItemContactViewHolder;
import com.bk.hica17.callback.OnContactItemClickListener;
import com.bk.hica17.model.Contact;
import com.bk.hica17.model.ContactGroup;

import java.util.ArrayList;

/**
 * Created by trung on 17/11/2016.
 */
public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    ArrayList<ContactGroup> mContactGroupList;
    OnContactItemClickListener mOnContactItemClickListener;

    public ContactAdapter(Activity activity, ArrayList<ContactGroup> contactGroupList){

        mActivity = activity;
        mContactGroupList = contactGroupList;
    }

    @Override
    public int getItemViewType(int position) {

        ContactGroup contactGroup = mContactGroupList.get(position);
        ArrayList<Contact> contacts = contactGroup.getContactList();

        return contacts.size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = null;

        if (viewType == 1) {

            view = layoutInflater.inflate(R.layout.one_item_contact_view,parent,false);
            OneItemContactViewHolder holder = new OneItemContactViewHolder(view);
            holder.setListener(mOnContactItemClickListener);
            return holder;
        }
        else if (viewType == 2) {

            view = layoutInflater.inflate(R.layout.two_item_contact_view,parent,false);
            TwoItemContactViewHolder holder = new TwoItemContactViewHolder(view);
            holder.setListener(mOnContactItemClickListener);
            return holder;
        }

        else {

            view = layoutInflater.inflate(R.layout.three_item_contact_view,parent,false);
            ThreeItemContactViewHolder holder = new ThreeItemContactViewHolder(view);
            holder.setListener(mOnContactItemClickListener);
            return holder;
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ContactGroup contactGroup = mContactGroupList.get(position);

        if (holder instanceof OneItemContactViewHolder){

            ((OneItemContactViewHolder)holder).populate(contactGroup);
        }
        else if (holder instanceof TwoItemContactViewHolder){

            ((TwoItemContactViewHolder)holder).populate(contactGroup);
        }
        else {

            ((ThreeItemContactViewHolder)holder).populate(contactGroup);
        }
    }

    public void setListener(OnContactItemClickListener onContactItemClickListener){

        mOnContactItemClickListener = onContactItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mContactGroupList.size();
    }
}

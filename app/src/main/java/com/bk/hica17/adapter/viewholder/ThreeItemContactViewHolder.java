package com.bk.hica17.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.callback.OnContactItemClickListener;
import com.bk.hica17.model.Contact;
import com.bk.hica17.model.ContactGroup;
import com.bk.hica17.ui.fragment.ContactFragment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by trung on 17/11/2016.
 */
public class ThreeItemContactViewHolder extends RecyclerView.ViewHolder {

    TextView txtTextHead;
    CircleImageView cirImgAvatar1;
    TextView txtNameContact1;
    LinearLayout llItemContact1;
    CircleImageView cirImgAvatar2;
    TextView txtNameContact2;
    LinearLayout llItemContact2;
    CircleImageView cirImgAvatar3;
    TextView txtNameContact3;
    LinearLayout llItemContact3;
    OnContactItemClickListener mOnContactItemClickListener;

    Contact contact1;
    Contact contact2;
    Contact contact3;

    public ThreeItemContactViewHolder(View itemView) {
        super(itemView);

        txtTextHead = (TextView) itemView.findViewById(R.id.txtTextHead);

        cirImgAvatar1 = (CircleImageView) itemView.findViewById(R.id.cirImgAvatar1);
        txtNameContact1 = (TextView) itemView.findViewById(R.id.txtNameContact1);
        llItemContact1 = (LinearLayout) itemView.findViewById(R.id.llItemContact1);

        cirImgAvatar2 = (CircleImageView) itemView.findViewById(R.id.cirImgAvatar2);
        txtNameContact2 = (TextView) itemView.findViewById(R.id.txtNameContact2);
        llItemContact2 = (LinearLayout) itemView.findViewById(R.id.llItemContact2);

        cirImgAvatar3 = (CircleImageView) itemView.findViewById(R.id.cirImgAvatar3);
        txtNameContact3 = (TextView) itemView.findViewById(R.id.txtNameContact3);
        llItemContact3 = (LinearLayout) itemView.findViewById(R.id.llItemContact3);

        initListener();
    }

    public void populate(ContactGroup contactGroup){

        ArrayList<Contact> contacts = contactGroup.getContactList();
        String text = contactGroup.getTextHead();
        txtTextHead.setText(text);

        contact1= contacts.get(0);
        txtNameContact1.setText(contact1.getName());
        setImageAvatar1(contact1);

        contact2= contacts.get(1);
        txtNameContact2.setText(contact2.getName());
        setImageAvatar2(contact2);

        contact3= contacts.get(2);
        txtNameContact3.setText(contact3.getName());
        setImageAvatar3(contact3);
    }

    public void setListener(OnContactItemClickListener onContactItemClickListener) {
        this.mOnContactItemClickListener = onContactItemClickListener;
    }

    public void initListener(){

        llItemContact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnContactItemClickListener.OnClick(contact1);
            }
        });

        llItemContact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnContactItemClickListener.OnClick(contact2);
            }
        });

        llItemContact3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnContactItemClickListener.OnClick(contact3);
            }
        });
    }

    public void setImageAvatar1(Contact contact) {

        switch (contact.getRole()) {

            case ContactFragment.GUEST:
                cirImgAvatar1.setImageResource(R.drawable.icon_guest);
                break;

            case ContactFragment.CUSTOMER:
                cirImgAvatar1.setImageResource(R.drawable.icon_customer);
                break;

            case ContactFragment.STAFF:
                cirImgAvatar1.setImageResource(R.drawable.icon_staff);
                break;

            case ContactFragment.FAMILY:
                cirImgAvatar1.setImageResource(R.drawable.icon_family);
                break;
        }

    }

    public void setImageAvatar2(Contact contact) {

        switch (contact.getRole()) {

            case ContactFragment.GUEST:
                cirImgAvatar2.setImageResource(R.drawable.icon_guest);
                break;

            case ContactFragment.CUSTOMER:
                cirImgAvatar2.setImageResource(R.drawable.icon_customer);
                break;

            case ContactFragment.STAFF:
                cirImgAvatar2.setImageResource(R.drawable.icon_staff);
                break;

            case ContactFragment.FAMILY:
                cirImgAvatar2.setImageResource(R.drawable.icon_family);
                break;
        }

    }

    public void setImageAvatar3(Contact contact) {
        switch (contact.getRole()) {

            case ContactFragment.GUEST:
                cirImgAvatar3.setImageResource(R.drawable.icon_guest);
                break;

            case ContactFragment.CUSTOMER:
                cirImgAvatar3.setImageResource(R.drawable.icon_customer);
                break;

            case ContactFragment.STAFF:
                cirImgAvatar3.setImageResource(R.drawable.icon_staff);
                break;

            case ContactFragment.FAMILY:
                cirImgAvatar3.setImageResource(R.drawable.icon_family);
                break;
        }

    }
}

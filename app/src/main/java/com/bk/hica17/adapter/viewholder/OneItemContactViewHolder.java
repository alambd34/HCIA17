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
public class OneItemContactViewHolder extends RecyclerView.ViewHolder {

    CircleImageView cirImgAvatar1;
    TextView txtNameContact1;
    TextView txtTextHead;
    LinearLayout llItemContact1;
    OnContactItemClickListener mOnContactItemClickListener;

    Contact contact;

    public OneItemContactViewHolder(View itemView) {
        super(itemView);

        cirImgAvatar1 = (CircleImageView) itemView.findViewById(R.id.cirImgAvatar1);
        txtNameContact1 = (TextView) itemView.findViewById(R.id.txtNameContact1);
        txtTextHead = (TextView) itemView.findViewById(R.id.txtTextHead);
        llItemContact1 = (LinearLayout) itemView.findViewById(R.id.llItemContact1);

        initListener();

    }

    public void populate(ContactGroup contactGroup){

        ArrayList<Contact> contacts = contactGroup.getContactList();
        contact= contacts.get(0);
        String text = contactGroup.getTextHead();

        txtTextHead.setText(text);
        txtNameContact1.setText(contact.getName());

        switch (contact.getRole()){

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

    public void setListener(OnContactItemClickListener onContactItemClickListener) {
        this.mOnContactItemClickListener = onContactItemClickListener;
    }

    public void initListener(){

        llItemContact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnContactItemClickListener.OnClick(contact);
            }
        });
    }
}

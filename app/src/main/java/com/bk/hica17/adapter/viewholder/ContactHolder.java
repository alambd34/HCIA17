package com.bk.hica17.adapter.viewholder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.callback.OnItemContactVoiceClickCallback;
import com.bk.hica17.model.Contact;
import com.bk.hica17.model.ContactIcon;
import com.bk.hica17.utils.Util;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dell on 19-Nov-16.
 */
public class ContactHolder extends RecyclerView.ViewHolder{

    Activity mActivity;
    @Bind(R.id.cir_img_contact_icon)
    CircleImageView cirImgContact;

    @Bind(R.id.txt_contact_name)
    TextView txtContactName;

    @Bind(R.id.txt_contact_number)
    TextView txtContactNumber;

    OnItemContactVoiceClickCallback mOnItemClickListener;


    public ContactHolder(View itemView, Activity activity) {
        super(itemView);
        this.mActivity = activity;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(getLayoutPosition());
                }
            }
        });
        Util.applyCustomFont(txtContactName, Util.faceRegular);
        Util.applyCustomFont(txtContactNumber, Util.faceRegular);
    }

    public void populate(Contact contact) {

        txtContactName.setText(contact.getName());
        txtContactNumber.setText(contact.getPhoneNumber());

        Random rand = new Random();
        int iconIndex = rand.nextInt(ContactIcon.contactIcons.length);
        cirImgContact.setImageResource(ContactIcon.contactIcons[iconIndex]);
    }

    public void setOnItemClickListener(OnItemContactVoiceClickCallback onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}

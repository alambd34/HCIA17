package com.bk.hica17.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.model.CallLog;
import com.bk.hica17.model.Contact;
import com.bk.hica17.ui.fragment.CallLogFragment;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by trung on 18/11/2016.
 */
public class CallLogViewHolder extends RecyclerView.ViewHolder {

    LinearLayout llItemCallLog;
    CircleImageView cirImgAvatar;
    TextView txtNameContact;
    ImageView imgStatus;
    TextView txtPhoneNumber;
    TextView txtTime;
    TextView txtStatus;

    public CallLogViewHolder(View itemView) {
        super(itemView);

        llItemCallLog = (LinearLayout) itemView.findViewById(R.id.llItemCallLog);
        cirImgAvatar = (CircleImageView) itemView.findViewById(R.id.cirImgAvatar);
        txtNameContact = (TextView) itemView.findViewById(R.id.txtNameContact);
        imgStatus = (ImageView) itemView.findViewById(R.id.imgStatus);
        txtPhoneNumber = (TextView) itemView.findViewById(R.id.txtPhoneNumber);
        txtTime = (TextView) itemView.findViewById(R.id.txtTime);
        txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
    }

    public void populate(CallLog callLog){

        Contact contact = callLog.getContact();
        cirImgAvatar.setImageResource(R.drawable.icon_guest);
        txtNameContact.setText(contact.getName());
        txtPhoneNumber.setText(contact.getPhoneNumber());
        txtTime.setText(callLog.getTime());
        txtStatus.setText(callLog.getStatus());

        if (callLog.getCatalog() == CallLogFragment.CATALOG_GO){
            imgStatus.setImageResource(R.drawable.call_go);
        }
        else {
            imgStatus.setImageResource(R.drawable.call_back);
        }
    }
}

package com.bk.hica17.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.model.Contact;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by trung on 19/11/2016.
 */
public class SeerchContactViewHolder extends RecyclerView.ViewHolder {

    LinearLayout llItemContact;
    CircleImageView cirImgAvatar;
    TextView txtNameContact;

    public SeerchContactViewHolder(View itemView) {
        super(itemView);

        llItemContact = (LinearLayout) itemView.findViewById(R.id.llItemContact);
        cirImgAvatar = (CircleImageView) itemView.findViewById(R.id.cirImgAvatar);
        txtNameContact = (TextView) itemView.findViewById(R.id.txtNameContact);

    }

    public void populate(Contact contact){
        Log.d("trung", contact.getName());
        cirImgAvatar.setImageResource(R.drawable.icon_guest);
        txtNameContact.setText(contact.getName());
    }
}

package com.bk.hica17.adapter;

/**
 * Created by khanh on 20/11/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.model.Contact;
import com.bk.hica17.model.ContactMatching;

import java.util.List;

public class FindContactAdapter extends ArrayAdapter<Contact> {
    private List<Contact> listContact;
    private LayoutInflater inflater;
    private int resource;
    private Context context;

    public FindContactAdapter(Context context, int resource, List<Contact> listContact) {
        super(context, resource, listContact);
        this.listContact = listContact;
        this.inflater = inflater.from(context);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ContactHolder holder;
        if (row == null) {
//            row = inflater.inflate(R.layout.row_listview_dialog, parent, false);
            row = inflater.inflate(resource, null);
            holder = new ContactHolder(row);
            row.setTag(holder);
        }
        else {
            holder = (ContactHolder) row.getTag();
        }
        Contact contact = getItem(position);
        holder.populateFrom(contact);
        return(row);
    }

    static class ContactHolder {
        private ImageView imgView;
        private TextView name;
        private TextView phone;
        private TextView type;

        ContactHolder(View row) {
            imgView = (ImageView) row.findViewById(R.id.profile_image);
            name = (TextView) row.findViewById(R.id.txt_name);
            phone = (TextView) row.findViewById(R.id.txt_phone);
            type = (TextView) row.findViewById(R.id.txt_type);
        }

        void populateFrom(Contact contact) {
            name.setText(contact.getName());
            setTextForPhone(contact);
        }

        private void setTextForPhone(Contact contact) {
            String str = contact.getPhoneNumber();
            SpannableStringBuilder builder = new SpannableStringBuilder();
            String match = ContactMatching.strNumInput;
            int indexSub = str.indexOf(match);

            SpannableString str1 = new SpannableString(str.substring(0, indexSub));
            str1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str1.length(), 0);
            builder.append(str1);

            SpannableString str2 = new SpannableString(str.substring(indexSub, indexSub + match.length()));
            str2.setSpan(new ForegroundColorSpan(Color.RED), 0, str2.length(), 0);
            builder.append(str2);

            SpannableString str3 = new SpannableString(str.substring(indexSub + match.length(), str.length()));
            str3.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str3.length(), 0);
            builder.append(str3);
            phone.setText(builder);
        }
    }
}

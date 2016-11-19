package com.bk.hica17.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.model.Contact;
import com.bk.hica17.ui.fragment.ContactFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalActivity extends AppCompatActivity {

    CircleImageView cirImgAvatar;
    TextView txtNameContact;
    TextView txtPhoneNumber;
    TextView txtRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        cirImgAvatar = (CircleImageView) findViewById(R.id.cirImgAvatar);
        txtNameContact = (TextView) findViewById(R.id.txtNameContact);
        txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        txtRole = (TextView) findViewById(R.id.txtRole);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("ContactFragment");
        Contact contact = (Contact) bundle.getSerializable("contact");

        txtNameContact.setText(contact.getName());
        txtPhoneNumber.setText(contact.getPhoneNumber());
        switch (contact.getRole()){

            case ContactFragment.GUEST:
                txtRole.setText("Khách hàng");
                break;

            case ContactFragment.CUSTOMER:
                txtRole.setText("Khách hàng thân quen");
                cirImgAvatar.setImageResource(R.drawable.icon_customer);
                break;

            case ContactFragment.STAFF:
                txtRole.setText("Nhân viên");
                cirImgAvatar.setImageResource(R.drawable.icon_staff);
                break;

            case ContactFragment.FAMILY:
                txtRole.setText("Người thân");
                cirImgAvatar.setImageResource(R.drawable.icon_family);
                break;
        }
    }

    public void onClickBack(View view) {
        finish();
    }
}

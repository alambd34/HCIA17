package com.bk.hica17.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.dialog.CustomDiaglog;
import com.bk.hica17.model.Contact;
import com.bk.hica17.model.ContactMatching;
import com.bk.hica17.ui.activity.VoiceActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by khanh on 13/11/2016.
 */
public class HomeFragment extends Fragment {

    View mRootView;
    EditText editPhoneNumber;
    ImageButton[] btn;
    ImageButton btnStar;
    ImageButton btnHash;
    ImageView imgVoice;
    ImageButton btnCall;
    ImageButton btnDel;
    StringBuilder stringBuilder;
    TextView txtName, txtPhone;

    CircleImageView profileImage;
    TextView txtAddContact;
    LinearLayout listContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.tab_fragment_1, container, false);
        initDialer();
        initVoice();
        return mRootView;
    }

    public void initDialer() {
        editPhoneNumber = (EditText) mRootView.findViewById(R.id.txt_phone_number);

        btn = new ImageButton[10];

        btn[0] = (ImageButton) mRootView.findViewById(R.id.btn_0);
        btn[1] = (ImageButton) mRootView.findViewById(R.id.btn_1);
        btn[2] = (ImageButton) mRootView.findViewById(R.id.btn_2);
        btn[3] = (ImageButton) mRootView.findViewById(R.id.btn_3);
        btn[4] = (ImageButton) mRootView.findViewById(R.id.btn_4);
        btn[5] = (ImageButton) mRootView.findViewById(R.id.btn_5);
        btn[6] = (ImageButton) mRootView.findViewById(R.id.btn_6);
        btn[7] = (ImageButton) mRootView.findViewById(R.id.btn_7);
        btn[8] = (ImageButton) mRootView.findViewById(R.id.btn_8);
        btn[9] = (ImageButton) mRootView.findViewById(R.id.btn_9);
        btnStar = (ImageButton) mRootView.findViewById(R.id.btn_star);
        btnHash = (ImageButton) mRootView.findViewById(R.id.btn_hash);
        stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; ++i) {
            btn[i].setOnClickListener(new ImageButtonListener(stringBuilder, String.valueOf(i), editPhoneNumber));
        }
        btnStar.setOnClickListener(new ImageButtonListener(stringBuilder, "*", editPhoneNumber));
        btnHash.setOnClickListener(new ImageButtonListener(stringBuilder, "#", editPhoneNumber));

        btnDel = (ImageButton) mRootView.findViewById(R.id.btn_del);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editPhoneNumber.hasFocus()) {
                    int position = editPhoneNumber.getSelectionEnd();
                    if (stringBuilder.length() > 0) {
                        editPhoneNumber.setCursorVisible(true);
                        if (position > 0) {
                            stringBuilder.deleteCharAt(position - 1);
                            editPhoneNumber.setText(stringBuilder);
                            editPhoneNumber.setSelection(position - 1);
                        }
                    }
                    if (stringBuilder.length() == 0) {
                        editPhoneNumber.setCursorVisible(false);
                    }
                } else {
                    int len = stringBuilder.length();
                    if (len > 0) {
                        stringBuilder.deleteCharAt(len - 1);
                        editPhoneNumber.setText(stringBuilder);
                    }
                }
                findContact();
            }
        });

        if (Build.VERSION.SDK_INT >= 11) {
            editPhoneNumber.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editPhoneNumber.setTextIsSelectable(true);
        } else {
            editPhoneNumber.setRawInputType(InputType.TYPE_NULL);
            editPhoneNumber.setFocusable(true);
        }

        editPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (stringBuilder.length() == 0 || !b) {
                    editPhoneNumber.setCursorVisible(false);
                } else {
                    editPhoneNumber.setCursorVisible(true);
                }
            }
        });

        txtName = (TextView) mRootView.findViewById(R.id.name);
        txtPhone = (TextView) mRootView.findViewById(R.id.phone);

        btnCall = (ImageButton) mRootView.findViewById(R.id.btn_call);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Uri call = Uri.parse("tel:" + stringBuilder);
//                Intent surf = new Intent(Intent.ACTION_DIAL, call);
//                startActivity(surf);


                Uri call = Uri.parse("tel:" + stringBuilder);
                Intent surf = new Intent(Intent.ACTION_CALL, call);
                startActivity(surf);


            }
        });

        profileImage = (CircleImageView) mRootView.findViewById(R.id.profile_image);
        txtAddContact = (TextView) mRootView.findViewById(R.id.add_contact);

        profileImage.setVisibility(View.INVISIBLE);
        txtAddContact.setVisibility(View.INVISIBLE);

        listContact = (LinearLayout) mRootView.findViewById(R.id.list_contact);
        listContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomDiaglog();
            }
        });


    }

    public void initVoice() {
        imgVoice = (ImageView)mRootView.findViewById(R.id.btn_voice);
        imgVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVoiceActivity();
            }
        });
    }

    public void startVoiceActivity() {

        Intent voiceIntent = new Intent(getActivity(), VoiceActivity.class);
        getActivity().startActivity(voiceIntent);
    }


    private class ImageButtonListener implements View.OnClickListener {

        private StringBuilder stringBuilder;
        private String value;
        private EditText editPhoneNumber;

        public ImageButtonListener(StringBuilder stringBuilder, String value, EditText editPhoneNumber) {
            this.stringBuilder = stringBuilder;
            this.value = value;
            this.editPhoneNumber = editPhoneNumber;
        }

        @Override
        public void onClick(View view) {

            if (editPhoneNumber.hasFocus()) {
                int position = editPhoneNumber.getSelectionStart();
                stringBuilder.insert(position, value);
                editPhoneNumber.setText(stringBuilder);
                editPhoneNumber.setSelection(position + 1);
            } else {
                stringBuilder.append(value);
                editPhoneNumber.setText(stringBuilder);
            }

            findContact();

        }
    }

    private void findContact() {
        String inputStr = stringBuilder.toString();
        if (inputStr.length() > 0) {
            ContactMatching contactMatching = new ContactMatching(inputStr);
            Contact contact = contactMatching.getContactMatching();
            if (contact != null) {
                txtAddContact.setVisibility(View.INVISIBLE);
                profileImage.setVisibility(View.VISIBLE);
                txtPhone.setVisibility(View.VISIBLE);
                txtName.setVisibility(View.VISIBLE);
                txtName.setText(contact.getName());
                String str = contact.getPhoneNumber();

                txtPhone.setText(contact.getPhoneNumber());


                SpannableStringBuilder builder = new SpannableStringBuilder();
                String match = contactMatching.getInputString();
                int indexSub = str.indexOf(match);

                SpannableString str1 = new SpannableString(str.substring(0, indexSub));
                str1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, str1.length(), 0);
                builder.append(str1);

                SpannableString str2 = new SpannableString(str.substring(indexSub, indexSub + match.length()));
                str2.setSpan(new ForegroundColorSpan(Color.RED), 0, str2.length(), 0);
                builder.append(str2);

                SpannableString str3 = new SpannableString(str.substring(indexSub + match.length(), str.length()));
                str3.setSpan(new ForegroundColorSpan(Color.WHITE), 0, str3.length(), 0);
                builder.append(str3);

                txtPhone.setText(builder);
            } else {
                profileImage.setVisibility(View.INVISIBLE);
                txtAddContact.setVisibility(View.VISIBLE);
                txtName.setVisibility(View.INVISIBLE);
                txtPhone.setVisibility(View.INVISIBLE);
            }
        } else {
            profileImage.setVisibility(View.INVISIBLE);
            txtAddContact.setVisibility(View.INVISIBLE);
            txtName.setVisibility(View.INVISIBLE);
            txtPhone.setVisibility(View.INVISIBLE);
        }

    }

}

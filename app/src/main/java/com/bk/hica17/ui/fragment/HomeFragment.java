package com.bk.hica17.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.adapter.FindContactAdapter;
import com.bk.hica17.model.Contact;
import com.bk.hica17.model.ContactMatching;
import com.bk.hica17.ui.activity.VoiceActivity;

import java.util.List;

/**
 * Created by khanh on 13/11/2016.
 */
public class HomeFragment extends Fragment {

    View viewVoice;
    View viewCall;
    View viewDelete;
    View mRootView;
    EditText editPhoneNumber;
    ImageButton[] btn;
    ImageButton btnStar;
    ImageButton btnHash;
    StringBuilder stringBuilder;
    TextView txtName, txtPhone;
    LinearLayout layoutContactInfo;
    LinearLayout nameAndContact;
    ImageView profileImage;
    RelativeLayout addContact;
    LinearLayout linearContact;
    List<Contact> listContact;
    TextView txtNumberResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.home_fragment, container, false);
        initDialer();
        initVoice();
        return mRootView;
    }

    public void initDialer() {
        editPhoneNumber = (EditText) mRootView.findViewById(R.id.edit_phone_number);
        txtNumberResult = (TextView) mRootView.findViewById(R.id.number_result);
        txtName = (TextView) mRootView.findViewById(R.id.name);
        txtPhone = (TextView) mRootView.findViewById(R.id.phone);
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

        viewDelete =  mRootView.findViewById(R.id.img_delete_number);
        viewDelete.setOnClickListener(new View.OnClickListener() {
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
                        editPhoneNumber.setFocusable(false);
                        editPhoneNumber.setCursorVisible(false);
                    }
                }
                findContact();
            }
        });

        viewDelete.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                int len = stringBuilder.length();
                if (len > 0) {
                    if (editPhoneNumber.hasFocus()) {
                        len = editPhoneNumber.getSelectionEnd();
                    }
                    stringBuilder.replace(0, len, "");
                    editPhoneNumber.setText(stringBuilder);
                }
                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            editPhoneNumber.setShowSoftInputOnFocus(false);
        }

        if (Build.VERSION.SDK_INT >= 11) {
            editPhoneNumber.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editPhoneNumber.setTextIsSelectable(true);
        } else {
            editPhoneNumber.setRawInputType(InputType.TYPE_NULL);
            editPhoneNumber.setFocusable(true);
        }

        editPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stringBuilder.length() > 0) {
                    editPhoneNumber.setFocusable(true);
                    editPhoneNumber.setFocusableInTouchMode(true);
                    editPhoneNumber.setCursorVisible(true);
                    editPhoneNumber.requestFocus();
                }
            }
        });

        viewCall =  mRootView.findViewById(R.id.fab_call);
        viewCall.setOnClickListener(new View.OnClickListener() {
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

        profileImage = (ImageView) mRootView.findViewById(R.id.profile_image);
        addContact = (RelativeLayout) mRootView.findViewById(R.id.add_contact);

        profileImage.setVisibility(View.INVISIBLE);
        addContact.setVisibility(View.INVISIBLE);


        linearContact = (LinearLayout) mRootView.findViewById(R.id.list_contact);
        linearContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.title_dialog));
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.custom_dialog, null);

                ListView listView = (ListView) v.findViewById(R.id.list_view_contact);

                listView.setAdapter(new FindContactAdapter(getActivity(), R.layout.row_listview_dialog, listContact));
                listView.setAdapter(new FindContactAdapter(getActivity(), R.layout.row_listview_dialog, listContact));


                builder.setView(v);

                builder.setPositiveButton(getResources().getString(R.string.btn_exit_dialog), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                final AlertDialog alert = builder.create();
                alert.show();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Contact contact = (Contact) adapterView.getAdapter().getItem(i);
                        txtName.setText(contact.getName());
                        txtPhone.setText(contact.getPhoneNumber());
                        stringBuilder.replace(0, stringBuilder.length(), contact.getPhoneNumber());
                        editPhoneNumber.setText(stringBuilder);
                        if (editPhoneNumber.hasFocus()) {
                            editPhoneNumber.setFocusable(false);
                        }
                        alert.dismiss();
                    }
                });
            }
        });

        nameAndContact = (LinearLayout) mRootView.findViewById(R.id.name_and_phone);
        layoutContactInfo = (LinearLayout) mRootView.findViewById(R.id.layout_contact_info);

        layoutContactInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                stringBuilder = stringBuilder.replace(0, stringBuilder.length(), txtPhone.getText().toString());
                editPhoneNumber.setText(stringBuilder);
                editPhoneNumber.setFocusable(false);
            }
        });
        nameAndContact.setVisibility(View.INVISIBLE);
        linearContact.setVisibility(View.INVISIBLE);

    }

    public void initVoice() {
        viewVoice = mRootView.findViewById(R.id.img_voice_call);
        viewVoice.setOnClickListener(new View.OnClickListener() {
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
            listContact = contactMatching.getContactMatching();
            int size = listContact.size();
            if (size > 0) {
                Contact contact = listContact.get(0);
                addContact.setVisibility(View.INVISIBLE);
                profileImage.setVisibility(View.VISIBLE);
                nameAndContact.setVisibility(View.VISIBLE);
                txtName.setText(contact.getName());
                linearContact.setVisibility(View.VISIBLE);
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
                txtNumberResult.setText(size + "");
            } else {
                profileImage.setVisibility(View.INVISIBLE);
                addContact.setVisibility(View.VISIBLE);
                nameAndContact.setVisibility(View.INVISIBLE);
                linearContact.setVisibility(View.INVISIBLE);
            }
        } else {
            profileImage.setVisibility(View.INVISIBLE);
            addContact.setVisibility(View.INVISIBLE);
            nameAndContact.setVisibility(View.INVISIBLE);
            linearContact.setVisibility(View.INVISIBLE);
        }

    }
}

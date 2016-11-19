package com.bk.hica17.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.adapter.CallLogAdapter;
import com.bk.hica17.model.CallLog;
import com.bk.hica17.model.Contact;

import java.util.ArrayList;

/**
 * Created by trung on 17/11/2016.
 */
public class CallLogFragment extends Fragment {

    View mRootView;
    LinearLayout llCatalogCall;
    TextView txtCatalogCallLog;
    LinearLayout llCatalogCallLogList;
    RecyclerView mRVCallLog;
    ArrayList<CallLog> mCallLogList;
    boolean isVisibleCatalogCallLog = false;
    CallLogAdapter callLogAdapter;

    LinearLayout llCatalogAll;
    LinearLayout llCatalogMiss;
    LinearLayout llCatalogGo;
    LinearLayout llCatalogBack;
    LinearLayout llCatalogUnknown;

    public static int CATALOG_MISS = 1;
    public static int CATALOG_GO = 2;
    public static int CATALOG_BACK = 3;
    public static int CATALOG_UNKNOW = 4;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.calllog_fragment, container, false);
        llCatalogCall = (LinearLayout) mRootView.findViewById(R.id.llCatalogCallLog);
        txtCatalogCallLog = (TextView) mRootView.findViewById(R.id.txtCatalogCallLog);
        llCatalogCallLogList = (LinearLayout) mRootView.findViewById(R.id.llCatalogCallLogList);
        mRVCallLog = (RecyclerView) mRootView.findViewById(R.id.rvCallLog);

        llCatalogAll = (LinearLayout) mRootView.findViewById(R.id.llCatalogAll);
        llCatalogMiss = (LinearLayout) mRootView.findViewById(R.id.llCatalogMiss);
        llCatalogGo = (LinearLayout) mRootView.findViewById(R.id.llCatalogGo);
        llCatalogBack = (LinearLayout) mRootView.findViewById(R.id.llCatalogBack);
        llCatalogUnknown = (LinearLayout) mRootView.findViewById(R.id.llCatalogUnknown);

        initData();
        initRV();

        setOnClickListenner();

        return mRootView;
    }

    public void initData(){

        mCallLogList = new ArrayList<CallLog>();
        mCallLogList.add(new CallLog("28 phút trước", new Contact("Trung", "096384332", 1), CATALOG_MISS, "Không kết nối"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("Tu", "096384332", 1), CATALOG_MISS, "Không kết nối"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("Tung 413", "096384332", 1), CATALOG_GO, "Cuộc gọi đi"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("Khanh A2", "096384332", 1), CATALOG_BACK, "Không kết nối"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("trung", "096384332", 1), CATALOG_UNKNOW, "Không kết nối"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("trung", "096384332", 1), CATALOG_MISS, "Không kết nối"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("Nguyen Tung", "096384332", 1), CATALOG_UNKNOW, "Không kết nối"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("trung", "096384332", 1), CATALOG_GO, "Cuộc gọi đi"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("trung", "096384332", 1), CATALOG_GO, "Không kết nối"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("Khanh A2", "096384332", 1), CATALOG_UNKNOW, "Không kết nối"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("Khanh A2", "096384332", 1), CATALOG_BACK, "Không kết nối"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("trung", "096384332", 1), CATALOG_GO, "Cuộc gọi đi"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("trung", "096384332", 1), CATALOG_MISS, "Không kết nối"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("trung", "096384332", 1), CATALOG_BACK, "Không kết nối"));
        mCallLogList.add(new CallLog("28 phút trước", new Contact("trung", "096384332", 1), CATALOG_BACK, "Không kết nối"));
    }

    public void initRV(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRVCallLog.setLayoutManager(layoutManager);

        callLogAdapter = new CallLogAdapter(getActivity(), mCallLogList);
        mRVCallLog.setAdapter(callLogAdapter);
    }

    public void visibleCatalogCallLog(){

        llCatalogCallLogList.getBackground().setAlpha(0);
        llCatalogCallLogList.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_trans_down_call_log);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llCatalogCallLogList.getBackground().setAlpha(100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        llCatalogCallLogList.startAnimation(animation);
    }

    public void invisibleCatalogCallLog(){

        llCatalogCallLogList.getBackground().setAlpha(0);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_trans_up_call_log);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llCatalogCallLogList.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        llCatalogCallLogList.startAnimation(animation);
    }

    public void setOnClickListenner(){

        llCatalogCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isVisibleCatalogCallLog){

                    visibleCatalogCallLog();
                    isVisibleCatalogCallLog = true;
                }
                else {

                    invisibleCatalogCallLog();
                    isVisibleCatalogCallLog = false;
                }


            }
        });

        llCatalogAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invisibleCatalogCallLog();
                txtCatalogCallLog.setText("Tất cả các cuộc gọi");
                isVisibleCatalogCallLog = false;
                setRvDataChange(mCallLogList);
            }
        });

        llCatalogMiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invisibleCatalogCallLog();
                txtCatalogCallLog.setText("Cuộc gọi nhỡ");
                isVisibleCatalogCallLog = false;
                ArrayList<CallLog> callLogListFilter = filterCallLog(CATALOG_MISS);
                setRvDataChange(callLogListFilter);
            }
        });

        llCatalogGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invisibleCatalogCallLog();
                txtCatalogCallLog.setText("Cuộc gọi đi");
                isVisibleCatalogCallLog = false;
                ArrayList<CallLog> callLogListFilter = filterCallLog(CATALOG_GO);
                setRvDataChange(callLogListFilter);
            }
        });

        llCatalogBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invisibleCatalogCallLog();
                txtCatalogCallLog.setText("Cuộc gọi đã nhận");
                isVisibleCatalogCallLog = false;
                ArrayList<CallLog> callLogListFilter = filterCallLog(CATALOG_BACK);
                setRvDataChange(callLogListFilter);
            }
        });

        llCatalogUnknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invisibleCatalogCallLog();
                txtCatalogCallLog.setText("Cuộc gọi từ số không xác định");
                isVisibleCatalogCallLog = false;
                ArrayList<CallLog> callLogListFilter = filterCallLog(CATALOG_UNKNOW);
                setRvDataChange(callLogListFilter);
            }
        });

    }

    public ArrayList<CallLog> filterCallLog(int status){

        ArrayList<CallLog> callLogListFilter = new ArrayList<CallLog>();
        int size = mCallLogList.size();

        for (int i=0; i<size; i++){

            CallLog callLog = mCallLogList.get(i);
            if (status == callLog.getCatalog()){
                callLogListFilter.add(callLog);
            }

        }

        return callLogListFilter;
    }

    public void setRvDataChange( ArrayList<CallLog> callLogListFilter){

        callLogAdapter = new CallLogAdapter(getActivity(), callLogListFilter);
        mRVCallLog.setAdapter(callLogAdapter);
    }


}

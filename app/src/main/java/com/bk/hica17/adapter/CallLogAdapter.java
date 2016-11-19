package com.bk.hica17.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bk.hica17.R;
import com.bk.hica17.adapter.viewholder.CallLogViewHolder;
import com.bk.hica17.model.CallLog;

import java.util.ArrayList;

/**
 * Created by trung on 18/11/2016.
 */
public class CallLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mActivity;
    ArrayList<CallLog> mCallLogList;

    public CallLogAdapter(Activity activity, ArrayList<CallLog> callLogList){

        mActivity = activity;
        mCallLogList = callLogList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = layoutInflater.inflate(R.layout.item_call_log_view, parent, false);

        CallLogViewHolder holder = new CallLogViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CallLog callLog = mCallLogList.get(position);

        ((CallLogViewHolder) holder).populate(callLog);
    }

    @Override
    public int getItemCount() {
        return mCallLogList.size();
    }
}

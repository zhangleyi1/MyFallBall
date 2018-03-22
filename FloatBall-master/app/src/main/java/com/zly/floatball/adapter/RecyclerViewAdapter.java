package com.zly.floatball.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zly.floatball.R;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2018/3/21.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {

    private final ArrayList<AppData> mList;
    private final Context mContext;
    private LayoutInflater mInflater;
    private String TAG = "RecyclerViewAdapter";
    CallBack mCallBack;
    private int mMenuSize = 0;

    public RecyclerViewAdapter(Context context, ArrayList<AppData> list, CallBack callBack, int menuSize) {
        mContext = context;
        mCallBack = callBack;
        mList = list;
        mInflater = LayoutInflater.from(context);
        mMenuSize = menuSize;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "zly --> onCreateViewHolder.");
        View view = mInflater.inflate(R.layout.app_item, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        Log.d(TAG, "zly --> onBindViewHolder position:" + position);
        holder.mImageView.setImageBitmap(mList.get(position).mIcon);
        holder.mTextView.setText(mList.get(position).mName);
        holder.mCheckBox.setChecked(mList.get(position).mState);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "zly --> onCheckedChanged isChecked:" + isChecked);
                if (isChecked) {
                    mMenuSize++;
                } else {
                    mMenuSize--;
                }

                if (mMenuSize > 5) {
                    mMenuSize--;
                    Toast.makeText(mContext, "You choose too many apps.", Toast.LENGTH_SHORT).show();
                    buttonView.setChecked(false);
                    return;
                }
                mList.get(position).setState(isChecked);
                mCallBack.finishLoader();
            }
        });
        if (position == (mList.size() - 1)) {
            mCallBack.finishLoader();
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        CheckBox mCheckBox;
        private String TAG = "CustomViewHolder";

        public CustomViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv);
            mTextView = itemView.findViewById(R.id.tv);
            mCheckBox = itemView.findViewById(R.id.cb);
        }
    }

    public interface CallBack {
        abstract void finishLoader();
    }
}

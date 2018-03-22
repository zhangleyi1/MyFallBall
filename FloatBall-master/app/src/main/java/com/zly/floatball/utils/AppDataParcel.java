package com.zly.floatball.utils;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.zly.floatball.adapter.AppData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/21.
 */

public class AppDataParcel implements Parcelable {
    public ArrayList<AppData> mAppDataList = new ArrayList<AppData>();
    private AppData mAppData;

    public AppDataParcel(Parcel in) {
        mAppData = new AppData();
        mAppData.setIcon((Bitmap) in.readParcelable(Bitmap.class.getClassLoader()));
        mAppData.setName(in.readString());
        mAppData.setState(in.readByte() != 0 ? true:false);
        mAppDataList = in.readArrayList(AppData.class.getClassLoader());
    }

    public static final Creator<AppDataParcel> CREATOR = new Creator<AppDataParcel>() {
        @Override
        public AppDataParcel createFromParcel(Parcel in) {
            return new AppDataParcel(in);
        }

        @Override
        public AppDataParcel[] newArray(int size) {
            return new AppDataParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable((Parcelable) mAppData.getIcon(), flags);
        dest.writeString(mAppData.getName());
        dest.writeByte((byte) (mAppData.getState() ? 1:0));
        dest.writeList(mAppDataList);
    }
}

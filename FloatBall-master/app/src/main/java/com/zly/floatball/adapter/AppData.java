package com.zly.floatball.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/3/21.
 */

public class AppData implements Parcelable{
    Bitmap mIcon;
    String mName;
    boolean mState;

    public AppData(Parcel in) {
        mName = in.readString();
        mState = in.readByte() != 0;
    }

    public static final Creator<AppData> CREATOR = new Creator<AppData>() {
        @Override
        public AppData createFromParcel(Parcel in) {
            return new AppData(in);
        }

        @Override
        public AppData[] newArray(int size) {
            return new AppData[size];
        }
    };

    public AppData() {

    }

    public Bitmap getIcon() {
        return mIcon;
    }

    public void setIcon(Bitmap mIcon) {
        this.mIcon = mIcon;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public boolean getState() {
        return mState;
    }

    public void setState(boolean mState) {
        this.mState = mState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeByte((byte) (mState ? 1 : 0));
    }
}

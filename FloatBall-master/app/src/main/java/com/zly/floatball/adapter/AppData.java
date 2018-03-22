package com.zly.floatball.adapter;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/3/21.
 */

public class AppData implements Parcelable{
    Bitmap mIcon;
    String mName;
    boolean mState;
    String mPackagesName;
    String mClassName;

    public AppData(Parcel in) {
        mIcon = (Bitmap) in.readParcelable(Bitmap.class.getClassLoader());
        mName = in.readString();
        mState = in.readByte() != 0;
        mPackagesName = in.readString();
        mClassName = in.readString();
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

    public String getPackagesName() {
        return mPackagesName;
    }

    public void setPackagesName(String mPackagesName) {
        this.mPackagesName = mPackagesName;
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(String mClassName) {
        this.mClassName = mClassName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mIcon, flags);
        dest.writeString(mName);
        dest.writeByte((byte) (mState ? 1 : 0));
        dest.writeString(mPackagesName);
        dest.writeString(mClassName);
    }
}

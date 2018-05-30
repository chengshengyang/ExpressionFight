package com.csy.fight;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chengshengyang on 2018/4/3.
 *
 * @author chengshengyang
 */

public class MyProcess implements Parcelable {

    int pid;
    String name;
    boolean underLine;

    public boolean isUnderLine() {
        return underLine;
    }

    public void setUnderLine(boolean underLine) {
        this.underLine = underLine;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyProcess(int pid, String name) {
        this.pid = pid;
        this.name = name;
    }

    public MyProcess(Parcel in) {
        pid = in.readInt();
        name = in.readString();
    }

    public static final Creator<MyProcess> CREATOR = new Creator<MyProcess>() {
        @Override
        public MyProcess createFromParcel(Parcel in) {
            return new MyProcess(in);
        }

        @Override
        public MyProcess[] newArray(int size) {
            return new MyProcess[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pid);
        dest.writeString(name);
    }
}

package com.ll.app.demoforbinder;

import android.os.Parcel;
import android.os.Parcelable;

public class ProcessInfo implements Parcelable {

    public String processName;

    public int pid;

    public int uid;

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // 写和读的顺序，建议统一按照属性声明顺序，避免出错
        parcel.writeString(processName);
        parcel.writeInt(pid);
        parcel.writeInt(uid);
    }

    // 数据流带有out时，必须声明和实现此方法，否则编译器会报错
    public void readFromParcel(Parcel parcel){
        // 读取顺序按照属性声明的顺序
        this.processName = parcel.readString();
        this.pid = parcel.readInt();
        this.uid = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // 必须实现Parcelable类型的静态属性
    // 此处引用类型必须是具体范型类型，否则AIDL生成对应方法会报错
    public static final Parcelable.Creator<ProcessInfo> CREATOR = new Parcelable.Creator<ProcessInfo>(){

        @Override
        public ProcessInfo createFromParcel(Parcel parcel) {
            // 写和读的顺序，建议统一按照属性声明顺序，避免出错
            ProcessInfo info = new ProcessInfo();
            info.processName = parcel.readString();
            info.pid = parcel.readInt();
            info.uid = parcel.readInt();
            return info;
        }

        @Override
        public ProcessInfo[] newArray(int size) {
            return new ProcessInfo[size];
        }
    };
}

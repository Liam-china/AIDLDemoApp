// IRemoteObject.aidl
package com.ll.app.demoforbinder;

import com.ll.app.demoforbinder.ProcessInfo;
import android.graphics.Bitmap;

// Declare any non-default types here with import statements
interface IRemoteObject {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    /**
     * Parcel自带支持的复杂类型,String,CharSequence,List,Map
     * 老版本编译器不必声明数据流向，
     **/
    void complexTypes(in String str,in CharSequence ch,in List list,in Map map);

    // 若没有声明oneway，默认是同步调用，会阻塞当前调用线程
    void syncCall();

    // oneway 关键字还可以声明AIDL接口，表示此接口所有方法均是异步调用方式
    oneway void asyncCall();

    // 数据流向tag:定义跨进程通信中数据流向的tag，如in out inout，基本数据不必声明
    // in  数据只能从客户端流向服务端
    // out 数据只能从服务端流向客户端
    // inout 可双向流动
    void getRemoteProcess(in ProcessInfo client);

    // 内核
    // 应用进程端
    void sendBitmap(in Bitmap bmp);

    // throw AndroidRuntimeException的处理
    void throwException();
}

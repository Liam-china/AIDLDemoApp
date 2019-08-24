package com.ll.app.demoforbinder;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.Map;

public class AIDLService extends Service {

    private static final String TAG = AIDLService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    private AIDLBinderServer mBinder = new AIDLBinderServer();

    private static class AIDLBinderServer extends com.ll.app.demoforbinder.IRemoteObject.Stub{

//        @Override
//        public void getRemoteProcess(ProcessInfo client, ProcessInfo server) throws RemoteException {
//
//        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            Log.d(TAG, "basicType()");
        }

        @Override
        public void complexTypes(String str, CharSequence ch, List list, Map map) throws RemoteException {
            Log.d(TAG, "complexTypes()");
            Log.d(TAG, "String=" + str);
            Log.d(TAG, "CharSequence=" + ch);
            Log.d(TAG, "List="+ list==null?null:list.toString());
            Log.d(TAG, "Map=" + map.toString());
            Log.d(TAG, "complexTypes()--end");
        }

        @Override
        public void syncCall() throws RemoteException {
            // 让当前 Binder线程 休眠10s
            Log.d(TAG, "syncCall() thread = " + Thread.currentThread().getName());
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            Log.d(TAG, "syncCall()--end");
        }

        @Override
        public void asyncCall() throws RemoteException {
            Log.d(TAG, "asyncCall() thread name =  " + Thread.currentThread().getName());
            // 让当前 Binder线程 休眠10s
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            Log.d(TAG, "asyncCall()--end");
        }

        @Override
        public void sendBitmap(Bitmap bmp) throws RemoteException {
            Log.d(TAG, "sendBitmap() bmp" + bmp);
        }

        @Override
        public void throwException() throws RemoteException {
            Log.d(TAG, "throwException()");
            // 引申出 Binder机制支持异常类型
            throw new NullPointerException("Binder支持异常类型传递");
        }

        @Override
        public void getRemoteProcess(ProcessInfo client) throws RemoteException {
            Log.d(TAG, "getRemoteProcess()");
            Log.d(TAG, "pid=" + client.pid);
            Log.d(TAG, "uid=" + client.uid);
            Log.d(TAG, "getRemoteProcess()--end");
        }
    }
}

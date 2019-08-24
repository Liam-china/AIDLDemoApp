package com.ll.app.demoforbinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientActivity extends Activity {

    private static final String TAG = ClientActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        initUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initUI(){
        OnFunButtonClick l = new OnFunButtonClick();
        View view = findViewById(R.id.bind_service_btn);
        view.setOnClickListener(l);

        view = findViewById(R.id.unbind_service_btn);
        view.setOnClickListener(l);

        view = findViewById(R.id.remote_async_call_btn);
        view.setOnClickListener(l);

        view = findViewById(R.id.remote_sync_call_btn);
        view.setOnClickListener(l);

        view = findViewById(R.id.send_parcelable_btn);
        view.setOnClickListener(l);

        view = findViewById(R.id.send_big_data_btn);
        view.setOnClickListener(l);

        view = findViewById(R.id.send_basic_types_btn);
        view.setOnClickListener(l);

        view = findViewById(R.id.send_complex_types_btn);
        view.setOnClickListener(l);

        view = findViewById(R.id.throw_exception_btn);
        view.setOnClickListener(l);
    }

    private android.content.ServiceConnection mServiceConn = new InnerServiceConnection();

    // 绑定远程Service，并获取Binder对象
    private void bindService(){
        Log.d(TAG, "bindService()");
        Intent intent = new Intent(this, AIDLService.class);
        bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bindService()--end");
    }

    // 解绑远程Service
    private void unBindService(){
        Log.d(TAG,"unBindService()");
        unbindService(mServiceConn);
        Log.d(TAG, "unBindService()--end");
    }

    // 同步远程调用
    private void syncCall(){
        if (mRemoteInterface == null) return;
        long start = System.currentTimeMillis();
        try{
            mRemoteInterface.syncCall();
        }catch (RemoteException e){
            e.printStackTrace();
        }
        Log.d(TAG, "syncCall() duration=" + (System.currentTimeMillis() - start));
    }

    // 异步远程调用
    private void asyncCall(){
        if (mRemoteInterface == null) return;
        long start = System.currentTimeMillis();
        try{
            mRemoteInterface.asyncCall();
        }catch (RemoteException e){
            e.printStackTrace();
        }
        Log.d(TAG, "syncCall() duration=" + (System.currentTimeMillis() - start));
    }

    // 发送自定义数据
    private void sendParcelable(){
        if (mRemoteInterface == null) return;
    }

    private Bitmap mBigBmp;

    // 发送大对象数据
    private void sendBigData(){
        if (mRemoteInterface == null) return;
        if (mBigBmp == null){
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // only decode options
            BitmapFactory.decodeResource(getResources(), R.drawable.almond, opts);
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = 1;
            mBigBmp = BitmapFactory.decodeResource(getResources(), R.drawable.almond, opts);
        }
        try{
            mRemoteInterface.sendBitmap(mBigBmp);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    private void sendBasicTypes(){
        if (mRemoteInterface == null) return;
        try{
            mRemoteInterface.basicTypes(1,2l,true,3.0f, 4.00, "500x");
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    private void sendComplexTypes(){
        if (mRemoteInterface == null) return;
        try{
            List<String> list = new ArrayList<>();
            list.add("list0");
            list.add("list1");
            list.add("list2");
            Map<String, String> map = new HashMap<>();
            map.put("key0", "value0");
            map.put("key1", "value1");
            map.put("key2", "value2");
            mRemoteInterface.complexTypes("String", "Chars", list, map);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    private void throwException(){
        if (mRemoteInterface == null) return;
        try{
            mRemoteInterface.throwException();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class OnFunButtonClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.bind_service_btn:
                    bindService();
                    break;
                case R.id.unbind_service_btn:
                    unBindService();
                    break;
                case R.id.remote_sync_call_btn:
                    syncCall();
                    break;
                case R.id.remote_async_call_btn:
                    asyncCall();
                    break;
                case R.id.send_parcelable_btn:
                    sendParcelable();
                    break;
                case R.id.send_big_data_btn:
                    sendBigData();
                    break;
                case R.id.send_basic_types_btn:
                    sendBasicTypes();
                    break;
                case R.id.send_complex_types_btn:
                    sendComplexTypes();
                    break;
                case R.id.throw_exception_btn:
                    throwException();
                    break;
            }
        }
    }

    private com.ll.app.demoforbinder.IRemoteObject mRemoteInterface;
    private InnerDeathRecipient mDeathRecipient;

    private class InnerServiceConnection implements android.content.ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected() thread=" + Thread.currentThread().getName());
            mRemoteInterface = com.ll.app.demoforbinder.IRemoteObject.Stub.asInterface(iBinder);
            if (mDeathRecipient == null){
                mDeathRecipient = new InnerDeathRecipient();
            }
            try{
                iBinder.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected() thread=" + Thread.currentThread().getName());
            mRemoteInterface = null;
        }
    }

    private class InnerDeathRecipient implements IBinder.DeathRecipient{

        @Override
        public void binderDied() {
            Log.d(TAG, "binderDied() thread=" + Thread.currentThread().getName());
        }
    }
}

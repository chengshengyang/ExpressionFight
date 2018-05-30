package com.csy.fight.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;

import com.csy.fight.IMyAidlInterface;
import com.csy.fight.MyProcess;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        //暴露给客户端
        return binder;
    }

    /**
     * 创建并实现AIDL接口
     */
    private final IMyAidlInterface.Stub binder = new IMyAidlInterface.Stub() {

        @Override
        public int getPid() throws RemoteException {
            return Process.myPid();
        }

        @Override
        public MyProcess getProcess(MyProcess clientProcess) throws RemoteException {
            MyProcess process = new MyProcess(Process.myPid(), getCurProcessName(MyService.this));
            return process;
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                               double aDouble, String aString) throws RemoteException {

        }
    };

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}

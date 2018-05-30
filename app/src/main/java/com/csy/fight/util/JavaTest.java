package com.csy.fight.util;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import com.csy.fight.IMyAidlInterface;
import com.csy.fight.app.ExpressionFightApplication;
import com.csy.fight.data.model.StringResult;
import com.csy.fight.factory.AbstractOperation;
import com.csy.fight.factory.AddFactory;
import com.csy.fight.factory.DivFactory;
import com.csy.fight.factory.IFactory;
import com.csy.fight.factory.MulFactory;
import com.csy.fight.factory.SubFactory;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.csy.fight.http.LoggerInterceptor.TAG;

/**
 * Created by chengshengyang on 2018/3/7.
 *
 * @author chengshengyang
 */

public class JavaTest {

    private IMyAidlInterface myService;

    public JavaTest() {
        IFactory addFactory = new AddFactory();
        IFactory subFactory = new SubFactory();
        IFactory mulFactory = new MulFactory();
        IFactory divFactory = new DivFactory();
        testFactoryMethod(addFactory);
        testFactoryMethod(subFactory);
        testFactoryMethod(mulFactory);
        testFactoryMethod(divFactory);

        testUserInfo();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = IMyAidlInterface.Stub.asInterface(service);
            try {
                Log.i(TAG, "Client pid= " + Process.myPid());
                Log.i(TAG, "RemoteService pid= " + myService.getPid());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "Service has unexpectedly disconnected");
            myService = null;
        }
    };

    private void initService() {
        Intent intent = new Intent();
        //intent.setClass(this, MyService.class);
        //bindService(intent, connection, Service.BIND_AUTO_CREATE);
    }

    class ListNode {
        int val;
        ListNode next;
        public ListNode(int val) {
            this.val = val;
        }
    }

    private void testUserInfo() {
        HashMap<String, Object> body = new HashMap<>(1);
        body.put("userName", "expression");
        Call<StringResult> call = ExpressionFightApplication.getInstance().getHttpService().getUser(body);
        call.enqueue(new Callback<StringResult>() {
            @Override
            public void onResponse(Call<StringResult> call, Response<StringResult> response) {
                StringResult result = response.body();
                Log.e(TAG, "user name = " + result.getStringValue());
            }

            @Override
            public void onFailure(Call<StringResult> call, Throwable t) {
                Log.e(TAG, "error msg: " + t.getMessage());
            }
        });
    }

    private void testFactoryMethod(IFactory iFactory) {
        AbstractOperation abstractOperation = iFactory.createOperation();
        abstractOperation.setNumberA(10);
        abstractOperation.setNumberB(7);
        double result = abstractOperation.getResult();
        Log.e(TAG, "cccc----result = " + result);
    }

    class Solution {
        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            if (l1 == null && l2 == null) {
                return null;
            }

            ListNode head = new ListNode(0);
            ListNode currentNode = head;
            //进位
            int fix = 0;
            while ((l1 != null) || (l2 != null) || (fix != 0)) {
                int val1 = 0;
                int val2 = 0;
                if (l1 != null) {
                    val1 = l1.val;
                    l1 = l1.next;
                }

                if (l2 != null) {
                    val2 = l2.val;
                    l2 = l2.next;
                }

                int sum = val1 + val2 + fix;
                fix = 0;
                if (sum > 9) {
                    fix = 1;
                    sum -= 10;
                }

                currentNode.next = new ListNode(0);
                currentNode.next.val = sum;
                currentNode = currentNode.next;
            }

            if (head.next == null) {
                head.next = new ListNode(0);
            }

            return head.next;
        }
    }

    static class MyParcelable implements Parcelable {
        private int mData;
        private int mData2;
        private String mString;

        public MyParcelable(int mData, int mData2, String mString) {
            this.mData = mData;
            this.mData2 = mData2;
            this.mString = mString;
        }

        public MyParcelable(Parcel in) {
            mData = in.readInt();
            mData2 = in.readInt();
            mString = in.readString();
        }

        public static final Creator<MyParcelable> CREATOR = new Creator<MyParcelable>() {
            @Override
            public MyParcelable createFromParcel(Parcel in) {
                return new MyParcelable(in);
            }

            @Override
            public MyParcelable[] newArray(int size) {
                return new MyParcelable[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mData);
            dest.writeInt(mData2);
            dest.writeString(mString);
        }
    }
}

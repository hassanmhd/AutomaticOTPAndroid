package com.warrag.otpreader;

import android.app.Activity;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class OTPManager {

    private Activity mActivity;
    private OTPBroadcastReceiver.OTPReceiverListener mOtpReceiverListener;
    private OTPBroadcastReceiver mOTPBroadcastReceiver = new OTPBroadcastReceiver();
    public OTPManager(OTPManagerBuilder builder){
        this.mActivity = builder.mActivity;
        this.mOtpReceiverListener = builder.otpReceiverListener;
    }

    public void start(){
        initSMSReceiver();
        mOTPBroadcastReceiver.setOtpReceiverListener(mOtpReceiverListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        unregisterReceiver();
        mActivity.registerReceiver(mOTPBroadcastReceiver, intentFilter);
    }

    private void unregisterReceiver(){
        try{
            mActivity.unregisterReceiver(mOTPBroadcastReceiver);
        }catch (Exception e){
            //DO NOTHING
        }
    }
    private void initSMSReceiver(){
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(mActivity);

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
                e.printStackTrace();
            }
        });
    }


    public static class OTPManagerBuilder{
        private Activity mActivity;
        private OTPBroadcastReceiver.OTPReceiverListener otpReceiverListener;

        public OTPManagerBuilder(Activity activity){
            this.mActivity = activity;
        }

        public OTPManagerBuilder setListener(OTPBroadcastReceiver.OTPReceiverListener otpReceiverListener){
            this.otpReceiverListener = otpReceiverListener;
            return this;
        }

        public OTPManager build(){
            return new OTPManager(this);
        }
    }
}

package com.warrag.automaticotpreceiver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.warrag.otpreader.OTPBroadcastReceiver;
import com.warrag.otpreader.OTPManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAppSignatures();
        new OTPManager.OTPManagerBuilder(this).setListener(new OTPBroadcastReceiver.OTPReceiverListener() {
            @Override
            public void onOTPReceived(String message) {
                //PArse the message here and decide what to do
            }

            @Override
            public void onOTPTimeout() {
                //Handle timeout here, it means 5 minutes passed without receiving the sms
            }
        }).build().start();
    }


    private void getAppSignatures(){
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        for (String signature:
                appSignatureHelper.getAppSignatures()) {
            Log.e("APPSignature",signature + "\n");
        };
    }
}

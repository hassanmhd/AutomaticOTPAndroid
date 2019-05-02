package com.warrag.otpreader;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
public class OTPBroadcastReceiver extends BroadcastReceiver {

    private OTPReceiverListener otpReceiverListener = null;

    public void setOtpReceiverListener(OTPReceiverListener otpReceiverListener) {
        this.otpReceiverListener = otpReceiverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.
                    if (otpReceiverListener!=null){
                        otpReceiverListener.onOTPReceived(message);
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    if (otpReceiverListener!=null){
                        otpReceiverListener.onOTPTimeout();
                    }
                    break;
            }
        }
    }


    public interface OTPReceiverListener{
        void onOTPReceived(String message);
        void onOTPTimeout();
    }
}

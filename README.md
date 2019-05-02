# AutomaticOTPAndroid
An easy to use Automatic OTP reader library for android. This is based on GoogleApiClient and SmsRettriever and doesn't require any permission.


Gradle
------
Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```


Step 2. Add the dependency

```
dependencies {
    ...
    implementation 'com.github.hassanmhd:AutomaticOTPAndroid:1.0.0'
}
```

Usage
-----
**Java**

```java
new OTPManager.OTPManagerBuilder(this).setListener(new OTPBroadcastReceiver.OTPReceiverListener() {
            @Override
            public void onOTPReceived(String message) {
                //Parse the message here and decide what to do
            }

            @Override
            public void onOTPTimeout() {
                //Handle timeout here, it means 5 minutes passed without receiving the sms
            }
        }).build().start();
```

**Kotlin**
```kotlin
OTPManager.OTPManagerBuilder(this).setListener(object : OTPBroadcastReceiver.OTPReceiverListener {
   
            override fun onOTPReceived(message: String) {
            
            }

            override fun onOTPTimeout() {
            
            }
        }).build().start()
```

And Thats it!


FAQ
---

**What is the right message format?**

By default, Any message format will be received if it has the following:
1. A message should include `<#>` or `[#]` in the beginning of the message to let the android system understand that this is an OTP message.
2. A message should include an 11 characters `[YOUR_APP_SIGNATURE]` at the end of the message to make google play services understand that the message received is intented to your application. 

***example***

```
<#> Hellow user, Your activation code is: XXXX
[APP_SIGNATURE]
```

**How can I generate my app signature?**

**For Debuge version:**
If you're still developing your app, you can check the demo application and use the class `AppSignauterHelper.java` used in the demo to generate your app's signature that you'll be using to compose the messages in the backend for your tests. But **MAKE SURE THAT YOU DELETED THIS CLASS BEFORE RELEASING YOUR APP**


**For releasing to Google play:**
If you're going to release the application to google play you there is some work to be done. Because google play signs the apps you publish, this changes your application's signature after it's uploaded to google play. So you'll need to get the new application signature to be set in your backend.
But don't worry, the process is really easy and all you have to do is:
1.Download the app signing certificate `deployment_cert.der` file from google play console.
2.Convert the `deployment_cert.der` file to a `.jks` file using this command: 

```
keytool -importcert -alias YOUR_ALIAS -file deployment_cert.der -keystore certificate.jks -storepass YOUR_PASSWORD
```

3. use the command below to generate the hash after generating the `certificate.jks` file

```
keytool -exportcert -alias YOUR_ALIAS -keystore certificate.jks | xxd -p | tr -d "[:space:]" | echo -n YOUR_PACKAGE `cat` | sha256sum | tr -d "[:space:]-" | xxd -r -p | base64 | cut -c1-11

```

And Done! now you should see the 11 characters hash code for your google play application!.


For more details on how to generate the app signature key you can check this answer in [stackoverflow](https://stackoverflow.com/a/55829661/5082132)


Changelog
---------

* **1.0.0**
    * Initial release


License
-------

    Copyright 2019 Hassan Warrag

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

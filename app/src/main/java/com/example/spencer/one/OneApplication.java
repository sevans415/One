package com.example.spencer.one;

import android.app.Application;

import com.backendless.Backendless;

/**
 * Created by spencerevans on 7/1/16.
 */
public class OneApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.initApp(this,"57E5BB1D-6B46-E8C8-FF56-242ED817A500",
                "5CBC656E-B7A9-9782-FF3F-FA19AA6D9A00",
                "v1");
    }
}

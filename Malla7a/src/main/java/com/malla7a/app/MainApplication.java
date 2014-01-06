package com.malla7a.app;

import android.app.Application;
import android.content.res.Configuration;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

/**
 * Created by Karim on 1/5/14.
 */
public class MainApplication extends Application {
    private static MainApplication singleton;

    public MainApplication getInstance(){
        return singleton;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        Parse.initialize(this, "a5fs8EC2F45zdmXqohcUzGsSLpTAMXFcGOiSZ26K", "D2mfcHytUYSdwJId0DyWdevLvvN9DssexRfb4dcU");
        PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

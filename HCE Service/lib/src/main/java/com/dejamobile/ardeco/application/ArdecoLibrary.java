package com.dejamobile.ardeco.application;

import android.app.Application;

/**
 * Created by 6l20 on 21/04/2015.
 */
public class ArdecoLibrary extends Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        updateInstance(this);
    }

    public static void updateInstance(Application instance)
    {
        ArdecoLibrary.instance=instance;
    }

    public static Application getInstance()
    {
        return instance;
    }


}

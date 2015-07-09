package com.dejamobile.ardeco.application;

import android.app.Application;

/**
 * Created by 6l20 on 21/04/2015.
 */
public class ArdecoLibrary extends Application {

    private static ArdecoLibrary instance;

    @Override
    public void onCreate() {
        super.onCreate();
        updateInstance(this);
    }

    private static void updateInstance(ArdecoLibrary instance)
    {
        ArdecoLibrary.instance=instance;
    }

    public static ArdecoLibrary getInstance()
    {
        return instance;
    }


}

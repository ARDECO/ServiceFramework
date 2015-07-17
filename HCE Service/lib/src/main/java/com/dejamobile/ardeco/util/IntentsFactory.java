package com.dejamobile.ardeco.util;

import android.content.ComponentName;
import android.content.Intent;

import com.dejamobile.ardeco.lib.BuildConfig;

/**
 * Created by Sylvain on 03/07/2015.
 */
public class IntentsFactory {

    private IntentsFactory() {
    }

    public static Intent buildPushOkIntent(){
        return buildIntentWithMessage("MobileReadOk");
    }

    public static Intent buildPushKoIntent(){
        return buildIntentWithMessage("MobileReadNok");
    }

    private static Intent buildIntentWithMessage(String msg) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(ComponentName.unflattenFromString(BuildConfig.DAW_TAGLET_TARGET));
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("ardeco_app",msg);
        return intent;
    }
}

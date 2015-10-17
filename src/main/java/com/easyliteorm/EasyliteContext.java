package com.easyliteorm;


import android.app.Activity;
import android.content.Context;

/**
 * EasyliteContext class provides instance
 * of android.content.Context utilized by Easylite.
 * [Note] This class must not be part of library API.
 * @author Mario
 */
public final class EasyliteContext extends Activity {
    private final static EasyliteContext INSTANCE = new EasyliteContext();

    private EasyliteContext (){}

    protected static Context getEasyliteContext (){
        return INSTANCE;
    }
}

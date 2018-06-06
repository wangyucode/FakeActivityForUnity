package cn.wycode.fakeactivityforunity.utils;

import android.util.Log;

import cn.wycode.fakeactivityforunity.BuildConfig;


/**
 * Created by wayne on 2018/1/4.
 */

public class LogUtil {

    private static final String KEY_WORD = "WYLog";

    public static void d(String s) {
        if (BuildConfig.DEBUG)
            Log.d(KEY_WORD, s);
    }

    public static void d(Class clazz, String s) {
        if (BuildConfig.DEBUG)
            Log.d(KEY_WORD + " form " + clazz.getSimpleName(), s);
    }


    public static void i(String s) {
        Log.i(KEY_WORD, s);
    }

    public static void i(Class clazz, String s) {
        Log.i(KEY_WORD + " form " + clazz.getSimpleName(), s);
    }


    public static void e(String s) {
        Log.e(KEY_WORD, s);
    }

    public static void e(Object form, String s) {
        Log.e(KEY_WORD + " form " + form.getClass().getSimpleName(), s);
    }

    public static void e(String s, Throwable t) {
        Log.e(KEY_WORD, s, t);
    }


    public static void e(Object form, String s, Throwable t) {
        Log.e(KEY_WORD + " form " + form.getClass().getSimpleName(), s, t);
    }


}

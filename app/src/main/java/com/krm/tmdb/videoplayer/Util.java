package com.krm.tmdb.videoplayer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.krm.tmdb.videoplayer.media.IMediaCallback;

import java.lang.reflect.Constructor;

public class Util {
    public static String stringForTime(int timeMs) {
        if (timeMs <= 0) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static void KEEP_SCREEN_ON(Context context) {
        scanForActivity(context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static void KEEP_SCREEN_OFF(Context context) {
        scanForActivity(context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static boolean SET_FULL(Context context) {
        Window w = scanForActivity(context).getWindow();
        boolean b = (w.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return b;
    }

    public static void CLEAR_FULL(Context context) {
        Window w = scanForActivity(context).getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void SET_LANDSCAPE(Context context) {
        scanForActivity(context).setRequestedOrientation
                (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public static void SET_PORTRAIT(Context context) {
        scanForActivity(context).setRequestedOrientation
                (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static void SET_SENSOR(Context context) {
        scanForActivity(context).setRequestedOrientation
                (ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    public static boolean isScreenOriatationPortrait(Context context) {
        int i = context.getResources().getConfiguration().orientation;
        return i != Configuration.ORIENTATION_LANDSCAPE;
    }

    public static void hideBottomUIMenu(Context context) {
        showNavigationBar(context, false);
    }

    public static void showBottomUIMenu(Context context) {
        showNavigationBar(context, true);
    }

    public static void showNavigationBar(Context context, boolean show) {
        try {
            Activity a = scanForActivity(context);
            if (show) {
                if (Build.VERSION.SDK_INT < 19) { // lower api
                    View v = a.getWindow().getDecorView();
                    v.setSystemUiVisibility(View.GONE);
                } else {
                    a.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    );
                }

            } else {
                if (Build.VERSION.SDK_INT < 19) { // lower api
                    View v = a.getWindow().getDecorView();
                    v.setSystemUiVisibility(View.VISIBLE);
                } else {
                    a.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int dp2px(Context context, float value) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }


    public static Activity scanForActivity(Context context) {
        if (context instanceof Activity) {
            Activity a = (Activity) context;
            if (a.getParent() != null)
                return a.getParent();
            else
                return a;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        throw new IllegalStateException("context activity");
    }

    public static int PaserUrl(String url) {
        int mode = 0;
        if (url.startsWith("file") || url.startsWith(ContentResolver.SCHEME_CONTENT) || url.startsWith(ContentResolver.SCHEME_ANDROID_RESOURCE))
            mode = -1;
        if (url.endsWith("m3u8"))
            mode = 1;
        return mode;
    }

    public static <T extends Object> T newInstance(String className, IMediaCallback iMediaCallback) {
        Class<?>[] paramsTypes = new Class[]{IMediaCallback.class};
        try {
            Class<?> cls = Class.forName(className);
            Constructor<?> con = cls.getConstructor(paramsTypes);
            return (T) con.newInstance(new Object[]{iMediaCallback});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

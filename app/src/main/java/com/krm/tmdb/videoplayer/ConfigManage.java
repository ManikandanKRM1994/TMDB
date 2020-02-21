package com.krm.tmdb.videoplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.krm.tmdb.videoplayer.media.AndroidMedia;
import com.krm.tmdb.videoplayer.media.BaseMedia;
import com.krm.tmdb.videoplayer.media.IMediaCallback;
import com.krm.tmdb.videoplayer.media.IMediaControl;
import com.krm.tmdb.videoplayer.rederview.IRenderView;
import com.krm.tmdb.videoplayer.rederview.SufaceRenderView;
import com.krm.tmdb.videoplayer.rederview.TextureRenderView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigManage {

    private static ConfigManage instance;

    private static List<WeakReference<QSVideoView>> videos = new ArrayList<>();

    public static ConfigManage getInstance(Context context) {
        if (instance == null)
            instance = new ConfigManage(context);
        return instance;
    }

    private SharedPreferences preferences;
    private String decodeClassName = "";

    private ConfigManage(Context context) {
        preferences = context.getSharedPreferences("cfg_qsvideo",
                Context.MODE_PRIVATE);
        decodeClassName = preferences.getString("decodeClassName", AndroidMedia.class.getName());
    }

    IRenderView getIRenderView(Context context) {
        if (Build.VERSION.SDK_INT >= 14)
            return new TextureRenderView(context);
        else
            return new SufaceRenderView(context);
    }

    IMediaControl getMediaControl(IMediaCallback iMediaCallback, Class<? extends BaseMedia> claxx) {
        if (iMediaCallback instanceof QSVideoView)
            addVideoView((QSVideoView) iMediaCallback);
        return newInstance(claxx.getName(), iMediaCallback);
    }

    private IMediaControl newInstance(String className, IMediaCallback iMediaCallback) {
        IMediaControl i = Util.newInstance(className, iMediaCallback);
        if (i == null) {
            Log.e(QSVideoView.TAG, "newInstance error: " + iMediaCallback);
            i = new AndroidMedia(iMediaCallback);
        }
        return i;
    }

    private void addVideoView(QSVideoView q) {
        WeakReference<QSVideoView> w = new WeakReference<>(q);
        videos.add(w);
        Iterator<WeakReference<QSVideoView>> iterList = videos.iterator();
        while (iterList.hasNext()) {
            WeakReference<QSVideoView> ww = iterList.next();
            if (ww.get() == null)
                iterList.remove();
        }
    }

    public static void releaseAll() {
        for (WeakReference<QSVideoView> w : videos) {
            QSVideoView q = w.get();
            if (q != null)
                q.release();
        }
        videos.clear();
    }

    public static void releaseOther(QSVideoView qs) {
        for (WeakReference<QSVideoView> w : videos) {
            QSVideoView q = w.get();
            if (q != null & q != qs)
                q.release();
        }
    }

    public void setDecodeMediaClass(String decodeClassName) {
        this.decodeClassName = decodeClassName;
        preferences.edit().putString("decodeClassName", decodeClassName).apply();
    }

    public String getDecodeMediaClass() {
        return decodeClassName;
    }
}

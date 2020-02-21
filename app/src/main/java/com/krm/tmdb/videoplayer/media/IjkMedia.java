package com.krm.tmdb.videoplayer.media;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import java.util.List;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IjkMedia extends IjkBaseMedia {

    public IjkMedia(IMediaCallback iMediaCallback) {
        super(iMediaCallback);
    }

    @Override
    IMediaPlayer getMedia(Context context, String url, Map<String, String> headers, Object... objects) throws Exception {
        IjkMediaPlayer mediaPlayer = new IjkMediaPlayer();

        if (url.startsWith(ContentResolver.SCHEME_CONTENT) || url.startsWith(ContentResolver.SCHEME_ANDROID_RESOURCE))
            mediaPlayer.setDataSource(context, Uri.parse(url), headers);
        else
            mediaPlayer.setDataSource(url, headers);


        if (objects != null && objects.length > 0 && objects[0] instanceof List) {
            List<Option> list = (List<Option>) objects[0];
            for (Option o : list) {
                if (o.strValue != null)
                    mediaPlayer.setOption(o.category, o.name, o.strValue);
                else
                    mediaPlayer.setOption(o.category, o.name, o.longValue);
            }
        }
        return mediaPlayer;
    }


    @Override
    public boolean setSpeed(float rate) {
        if (isPrepar & mediaPlayer != null) {
            ((IjkMediaPlayer) mediaPlayer).setSpeed(rate);
        }
        return true;
    }

    public static class Option {
        int category;
        String name;
        String strValue;
        long longValue;

        public Option(int category, String name, String value) {
            this.category = category;
            this.name = name;
            this.strValue = value;
        }

        public Option(int category, String name, long value) {
            this.category = category;
            this.name = name;
            this.longValue = value;
        }
    }
}

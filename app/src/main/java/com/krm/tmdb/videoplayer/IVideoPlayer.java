package com.krm.tmdb.videoplayer;

import android.graphics.Bitmap;

import com.krm.tmdb.videoplayer.floatwindow.FloatParams;
import com.krm.tmdb.videoplayer.media.BaseMedia;

import java.util.Map;

public interface IVideoPlayer {

    int STATE_NORMAL = 0;
    int STATE_PREPARING = 1;
    int STATE_PLAYING = 2;
    int STATE_PAUSE = 4;
    int STATE_AUTO_COMPLETE = 5;
    int STATE_ERROR = 6;

    int MODE_WINDOW_NORMAL = 100;
    int MODE_WINDOW_FULLSCREEN = 101;
    int MODE_WINDOW_FLOAT_SYS = 102;
    int MODE_WINDOW_FLOAT_ACT = 103;

    int EVENT_PREPARE_START = 10;
    int EVENT_PREPARE_END = 11;
    int EVENT_PLAY = 12;
    int EVENT_PAUSE = 13;
    int EVENT_BUFFERING_START = 14;
    int EVENT_BUFFERING_END = 15;
    int EVENT_ERROR = 16;
    int EVENT_VIDEOSIZECHANGE = 17;
    int EVENT_COMPLETION = 18;
    int EVENT_BUFFERING_UPDATE = 19;
    int EVENT_SEEK_COMPLETION = 20;
    int EVENT_SEEK_TO = 21;

    int EVENT_RELEASE = 88;

    void setUp(String url, Map<String, String> headers, Object option);

    void play();

    void prePlay();

    void pause();

    void seekTo(int duration);

    void setPlayListener(PlayListener playListener);

    void addPlayListener(PlayListener playListener);

    void removePlayListener(PlayListener playListener);

    void setAspectRatio(int aspectRatio);

    void setDecodeMedia(Class<? extends BaseMedia> claxx);

    void openCache();

    boolean onBackPressed();

    boolean isPlaying();

    void enterWindowFullscreen();

    void quitWindowFullscreen();

    boolean enterWindowFloat(FloatParams floatParams);

    void quitWindowFloat();

    boolean setMute(boolean isMute);

    boolean setSpeed(float rate);

    void release();

    Bitmap getCurrentFrame();

    int getPosition();

    int getDuration();

    int getVideoWidth();

    int getVideoHeight();

    int getCurrentMode();

    int getCurrentState();

}

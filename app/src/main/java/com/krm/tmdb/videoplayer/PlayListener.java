package com.krm.tmdb.videoplayer;

public interface PlayListener {

    void onStatus(int status);

    void onMode(int mode);

    void onEvent(int what, Integer... extra);
}

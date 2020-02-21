package com.krm.tmdb.videoplayer;

import java.util.HashSet;
import java.util.Set;

class HandlePlayListener implements PlayListener {

    private PlayListener setPlayListener;
    private Set<PlayListener> playListenerSet;

    @Override
    public void onStatus(int status) {
        if (playListenerSet != null)
            for (PlayListener p : playListenerSet)
                p.onStatus(status);
    }

    @Override
    public void onMode(int mode) {
        if (playListenerSet != null)
            for (PlayListener p : playListenerSet)
                p.onMode(mode);
    }

    @Override
    public void onEvent(int what, Integer... extra) {
        if (playListenerSet != null)
            for (PlayListener p : playListenerSet)
                p.onEvent(what, extra);
    }


    void setListener(PlayListener playListener) {
        removeListener(setPlayListener);
        if (playListener != null) {
            setPlayListener = playListener;
            addListener(playListener);
        }
    }

    void addListener(PlayListener playListener) {
        if (playListener == null)
            return;
        if (playListenerSet == null)
            playListenerSet = new HashSet<>();
        playListenerSet.add(playListener);
    }

    void removeListener(PlayListener playListener) {
        if (playListenerSet == null || playListener == null)
            return;
        playListenerSet.remove(playListener);
    }
}

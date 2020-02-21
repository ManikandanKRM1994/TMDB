package com.krm.tmdb.videoplayer.media;

public interface IMediaCallback {

    void onPrepared(IMediaControl iMediaControl);

    void onCompletion(IMediaControl iMediaControl);

    void onSeekComplete(IMediaControl iMediaControl);

    void onInfo(IMediaControl iMediaControl, int what, int extra);

    void onVideoSizeChanged(IMediaControl iMediaControl, int width, int height);

    void onError(IMediaControl iMediaControl, int what, int extra);

    void onBufferingUpdate(IMediaControl iMediaControl, final float percent);
}

package com.krm.tmdb.videoplayer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.krm.tmdb.R;

import java.util.ArrayList;
import java.util.List;

public class DemoQSVideoView extends QSVideoViewHelp {

    protected ImageView coverImageView;
    protected ViewGroup bottomContainer;
    protected ViewGroup topContainer;
    protected ViewGroup loadingContainer;
    protected ViewGroup errorContainer = findViewById(R.id.error_container);
    protected ViewGroup bufferingContainer;

    protected List<View> changeViews;

    public DemoQSVideoView(Context context) {
        this(context, null);
    }

    public DemoQSVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public DemoQSVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        setUIWithStateAndMode(STATE_NORMAL, currentMode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.video_view;
    }

    protected void initView() {
        topContainer = findViewById(R.id.layout_top);
        bottomContainer = findViewById(R.id.layout_bottom);

        bufferingContainer = findViewById(R.id.buffering_container);
        loadingContainer = findViewById(R.id.loading_container);

        coverImageView = findViewById(R.id.cover);

        changeViews = new ArrayList<>();

        changeViews.add(topContainer);
        changeViews.add(bottomContainer);
        changeViews.add(loadingContainer);
        changeViews.add(errorContainer);
        changeViews.add(coverImageView);
        changeViews.add(startButton);
        changeViews.add(progressBar);
    }

    @Override
    protected void changeUiWithStateAndMode(int status, int mode) {
        switch (status) {
            case STATE_NORMAL:
                showChangeViews(coverImageView, startButton);
                break;
            case STATE_PREPARING:
                showChangeViews(loadingContainer);
                break;
            case STATE_PLAYING:
            case STATE_PAUSE:
            case STATE_AUTO_COMPLETE:
                showChangeViews(startButton,
                        mode >= MODE_WINDOW_FLOAT_SYS ? null : bottomContainer,
                        mode == MODE_WINDOW_FULLSCREEN ? topContainer : null);
                break;
            case STATE_ERROR:
                showChangeViews(errorContainer);
                break;
        }
        updateViewImage(status, mode);
        floatCloseView.setVisibility(mode >= MODE_WINDOW_FLOAT_SYS ? View.VISIBLE : View.INVISIBLE);
        floatBackView.setVisibility(mode >= MODE_WINDOW_FLOAT_SYS ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void dismissControlView(int status, int mode) {
        bottomContainer.setVisibility(View.INVISIBLE);
        topContainer.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        if (status != STATE_AUTO_COMPLETE)
            startButton.setVisibility(View.INVISIBLE);
        if (mode >= MODE_WINDOW_FLOAT_SYS)
            floatCloseView.setVisibility(View.INVISIBLE);
        if (mode >= MODE_WINDOW_FLOAT_SYS)
            floatBackView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onBuffering(boolean isBuffering) {
        bufferingContainer.setVisibility(isBuffering ? VISIBLE : INVISIBLE);
    }

    protected void showChangeViews(View... views) {
        for (View v : changeViews)
            if (v != null)
                v.setVisibility(INVISIBLE);
        for (View v : views)
            if (v != null)
                v.setVisibility(VISIBLE);
    }

    protected void updateViewImage(int status, int mode) {
        startButton.setImageResource(status == STATE_PLAYING ?
                R.drawable.jc_click_pause_selector : R.drawable.jc_click_play_selector);
        fullscreenButton.setImageResource(mode == MODE_WINDOW_FULLSCREEN ?
                R.drawable.jc_shrink : R.drawable.jc_enlarge);
    }

    public ImageView getCoverImageView() {
        return coverImageView;
    }

    public boolean isShowWifiDialog = true;

    @Override
    protected boolean showWifiDialog() {
        if (!isShowWifiDialog)
            return false;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                prepareMediaPlayer();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        return true;
    }

    @Override
    protected void doubleClick() {
        clickFull();
    }

    @Override
    protected void popDefinition(View view, List<QSVideo> qsVideos, int index) {
        VideoPopWindow videoPopWindow = new VideoPopWindow(getContext(), qsVideos, index);
        videoPopWindow.setOnItemListener(new VideoPopWindow.OnItemListener() {
            @Override
            public void OnClick(int position) {
                switchVideo(position);
            }
        });
        videoPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                startDismissControlViewTimer(1314);
            }
        });
        videoPopWindow.showTop(view);
        cancelDismissControlViewTimer();
    }

    protected PopupWindow mProgressDialog;
    protected ProgressBar mDialogProgressBar;
    protected TextView tv_current;
    protected TextView tv_duration;
    protected TextView tv_delta;
    protected ImageView mDialogIcon;

    @SuppressLint("SetTextI18n")
    @Override
    protected boolean showProgressDialog(int delta, int position, int duration) {
        if (mProgressDialog == null) {
            @SuppressLint("InflateParams")
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.jc_dialog_progress, null);
            mDialogProgressBar = localView.findViewById(R.id.duration_progressbar);
            tv_current = localView.findViewById(R.id.tv_current);
            tv_duration = localView.findViewById(R.id.tv_duration);
            tv_delta = localView.findViewById(R.id.tv_delta);
            mDialogIcon = localView.findViewById(R.id.duration_image_tip);
            mProgressDialog = getPopupWindow(localView);

        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.showAtLocation(this, Gravity.CENTER, 0, 0);
        }

        tv_delta.setText(String.format("%s%ss",
                (delta > 0 ? "+" : ""),
                delta / 1000));
        tv_current.setText(Util.stringForTime(position + delta) + "/");
        tv_duration.setText(Util.stringForTime(duration));
        mDialogProgressBar.setProgress((position + delta) * 100 / duration);
        if (delta > 0) {
            mDialogIcon.setBackgroundResource(R.drawable.jc_forward_icon);
        } else {
            mDialogIcon.setBackgroundResource(R.drawable.jc_backward_icon);
        }
        return true;
    }

    @Override
    protected boolean dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        return true;
    }


    protected PopupWindow mVolumeDialog;
    protected ProgressBar mDialogVolumeProgressBar;
    protected TextView mDialogVolumeTextView;
    protected ImageView mDialogVolumeImageView;

    @Override
    protected boolean showVolumeDialog(int nowVolume, int maxVolume) {

        if (mVolumeDialog == null) {
            @SuppressLint("InflateParams")
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.jc_dialog_volume, null);
            mDialogVolumeImageView = localView.findViewById(R.id.volume_image_tip);
            mDialogVolumeTextView = localView.findViewById(R.id.tv_volume);
            mDialogVolumeProgressBar = localView.findViewById(R.id.volume_progressbar);
            mDialogVolumeProgressBar.setMax(maxVolume);
            mVolumeDialog = getPopupWindow(localView);
        }
        if (!mVolumeDialog.isShowing())
            mVolumeDialog.showAtLocation(this, Gravity.TOP, 0, Util.dp2px(getContext(), currentMode == MODE_WINDOW_NORMAL ? 25 : 50));

        mDialogVolumeTextView.setText(String.valueOf(nowVolume));
        mDialogVolumeProgressBar.setProgress(nowVolume);
        return true;
    }

    @Override
    protected boolean dismissVolumeDialog() {
        if (mVolumeDialog != null) {
            mVolumeDialog.dismiss();
        }
        return true;
    }

    protected PopupWindow mBrightnessDialog;
    protected ProgressBar mDialogBrightnessProgressBar;
    protected TextView mDialogBrightnessTextView;

    @Override
    protected boolean showBrightnessDialog(int brightnessPercent, int max) {
        if (mBrightnessDialog == null) {
            @SuppressLint("InflateParams")
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.jc_dialog_brightness, null);
            mDialogBrightnessTextView = localView.findViewById(R.id.tv_brightness);
            mDialogBrightnessProgressBar = localView.findViewById(R.id.brightness_progressbar);
            mDialogBrightnessProgressBar.setMax(max);
            mBrightnessDialog = getPopupWindow(localView);
        }
        if (!mBrightnessDialog.isShowing())
            mBrightnessDialog.showAtLocation(this, Gravity.TOP, 0, Util.dp2px(getContext(), currentMode == MODE_WINDOW_NORMAL ? 25 : 50));

        mDialogBrightnessTextView.setText(String.valueOf(brightnessPercent));
        mDialogBrightnessProgressBar.setProgress(brightnessPercent);
        return true;
    }

    @Override
    protected boolean dismissBrightnessDialog() {
        if (mBrightnessDialog != null) {
            mBrightnessDialog.dismiss();
        }
        return true;
    }

    private PopupWindow getPopupWindow(View popupView) {
        PopupWindow mPopupWindow = new PopupWindow(popupView, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        mPopupWindow.setAnimationStyle(R.style.jc_popup_toast_anim);
        return mPopupWindow;
    }

}

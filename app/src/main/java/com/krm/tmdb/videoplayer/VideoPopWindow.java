package com.krm.tmdb.videoplayer;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.krm.tmdb.R;

import java.util.List;

public class VideoPopWindow extends PopupWindow implements View.OnClickListener {

    int h;

    public VideoPopWindow(Context context, List<QSVideo> qsVideos, int index) {
        ViewGroup popview = (ViewGroup) LayoutInflater.from(context).inflate(
                R.layout.pop_definition, new FrameLayout(context), false);
        float density = context.getResources().getDisplayMetrics().density;
        int padding = (int) (density * 12);
        for (int i = 0; i < qsVideos.size(); i++) {
            QSVideo qsVideo = qsVideos.get(i);
            TextView textView = new TextView(context);
            textView.setId(i);
            textView.setPadding(padding, padding / 2, padding, padding / 2);
            textView.setText(qsVideo.resolution());
            textView.setTextSize(14);
            textView.setOnClickListener(this);
            textView.setTextColor(index == i ? context.getResources().getColor(R.color.colorMain) : 0xffffffff);
            popview.addView(textView);
        }
        int mode = View.MeasureSpec.AT_MOST;
        popview.measure(View.MeasureSpec.makeMeasureSpec(1080, mode),
                View.MeasureSpec.makeMeasureSpec(1920, mode));
        h = popview.getMeasuredHeight();
        setContentView(popview);
        setWidth(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        update();
        ColorDrawable dw = new ColorDrawable(0);
        setBackgroundDrawable(dw);
    }

    public void showTop(View view) {
        showAsDropDown(view, 0, -h - view.getMeasuredHeight());
    }

    @Override
    public void onClick(View v) {
        itemListener.OnClick(v.getId());
        dismiss();
    }

    private OnItemListener itemListener;

    public void setOnItemListener(OnItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public interface OnItemListener {
        void OnClick(int position);
    }


}
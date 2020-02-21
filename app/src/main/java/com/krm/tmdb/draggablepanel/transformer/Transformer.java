package com.krm.tmdb.draggablepanel.transformer;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;

public abstract class Transformer {

    private final View view;
    private final View parent;

    private int marginRight;
    private int marginBottom;

    private float xScaleFactor;
    private float yScaleFactor;

    private int originalHeight;
    private int originalWidth;

    public Transformer(View view, View parent) {
        this.view = view;
        this.parent = parent;
    }

    public float getXScaleFactor() {
        return xScaleFactor;
    }

    public void setXScaleFactor(float xScaleFactor) {
        this.xScaleFactor = xScaleFactor;
    }

    public float getYScaleFactor() {
        return yScaleFactor;
    }

    public void setYScaleFactor(float yScaleFactor) {
        this.yScaleFactor = yScaleFactor;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = Math.round(marginRight);
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = Math.round(marginBottom);
    }

    public void setViewHeight(int newHeight) {
        if (newHeight > 0) {
            originalHeight = newHeight;
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) view.getLayoutParams();
            layoutParams.height = newHeight;
            view.setLayoutParams(layoutParams);
        }
    }

    protected View getView() {
        return view;
    }

    protected View getParentView() {
        return parent;
    }

    public abstract void updatePosition(float verticalDragOffset);

    public abstract void updateScale(float verticalDragOffset);

    public int getOriginalHeight() {
        if (originalHeight == 0) {
            originalHeight = view.getMeasuredHeight();
        }
        return originalHeight;
    }

    public int getOriginalWidth() {
        if (originalWidth == 0) {
            originalWidth = view.getMeasuredWidth();
        }
        return originalWidth;
    }

    public boolean isViewAtTop() {
        return view.getTop() == 0;
    }

    public boolean isAboveTheMiddle() {
        int parentHeight = parent.getHeight();
        float viewYPosition = ViewCompat.getY(view) + (view.getHeight() * 0.5f);
        return viewYPosition < (parentHeight * 0.5);
    }

    public abstract boolean isViewAtRight();

    public abstract boolean isViewAtBottom();

    public abstract boolean isNextToRightBound();

    public abstract boolean isNextToLeftBound();

    public abstract int getMinHeightPlusMargin();

    public abstract int getMinWidthPlusMarginRight();
}

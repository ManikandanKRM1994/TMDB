package com.krm.tmdb.draggablepanel.transformer;

import android.view.View;
import android.widget.RelativeLayout;

class ResizeTransformer extends Transformer {

    private final RelativeLayout.LayoutParams layoutParams;

    ResizeTransformer(View view, View parent) {
        super(view, parent);
        layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
    }

    @Override
    public void updateScale(float verticalDragOffset) {
        layoutParams.width = (int) (getOriginalWidth() * (1 - verticalDragOffset / getXScaleFactor()));
        layoutParams.height = (int) (getOriginalHeight() * (1 - verticalDragOffset / getYScaleFactor()));

        getView().setLayoutParams(layoutParams);
    }

    @Override
    public void updatePosition(float verticalDragOffset) {
        int right = getViewRightPosition(verticalDragOffset);
        int left = right - layoutParams.width;
        int top = getView().getTop();
        int bottom = top + layoutParams.height;

        getView().layout(left, top, right, bottom);
    }

    @Override
    public boolean isViewAtRight() {
        return getView().getRight() + getMarginRight() == getParentView().getWidth();
    }

    @Override
    public boolean isViewAtBottom() {
        return getView().getBottom() + getMarginBottom() == getParentView().getHeight();
    }

    @Override
    public boolean isNextToRightBound() {
        return (getView().getLeft() - getMarginRight()) > getParentView().getWidth() * 0.75;
    }

    @Override
    public boolean isNextToLeftBound() {
        return (getView().getLeft() - getMarginRight()) < getParentView().getWidth() * 0.05;
    }

    @Override
    public int getMinHeightPlusMargin() {
        return (int) (getOriginalHeight() * (1 - 1 / getYScaleFactor()) + getMarginBottom());
    }

    @Override
    public int getMinWidthPlusMarginRight() {
        return (int) (getOriginalWidth() * (1 - 1 / getXScaleFactor()) + getMarginRight());
    }

    private int getViewRightPosition(float verticalDragOffset) {
        return (int) ((getOriginalWidth()) - getMarginRight() * verticalDragOffset);
    }

}

package com.krm.tmdb.draggablepanel.transformer;

import android.view.View;

import androidx.core.view.ViewCompat;

class ScaleTransformer extends Transformer {

    ScaleTransformer(View view, View parent) {
        super(view, parent);
    }

    @Override
    public void updateScale(float verticalDragOffset) {
        ViewCompat.setScaleX(getView(), 1 - verticalDragOffset / getXScaleFactor());
        ViewCompat.setScaleY(getView(), 1 - verticalDragOffset / getYScaleFactor());
    }

    @Override
    public void updatePosition(float verticalDragOffset) {
        ViewCompat.setPivotX(getView(), getView().getWidth() - getMarginRight());
        ViewCompat.setPivotY(getView(), getView().getHeight() - getMarginBottom());
    }

    @Override
    public boolean isViewAtRight() {
        return getView().getRight() == getParentView().getWidth();
    }

    @Override
    public boolean isViewAtBottom() {
        return getView().getBottom() == getParentView().getHeight();
    }

    @Override
    public boolean isNextToLeftBound() {
        return (getView().getRight() - getMarginRight()) < getParentView().getWidth() * 0.6;
    }

    @Override
    public boolean isNextToRightBound() {
        return (getView().getRight() - getMarginRight()) > getParentView().getWidth() * 1.25;
    }

    @Override
    public int getMinHeightPlusMargin() {
        return getView().getHeight();
    }

    @Override
    public int getMinWidthPlusMarginRight() {
        return getOriginalWidth();
    }

}

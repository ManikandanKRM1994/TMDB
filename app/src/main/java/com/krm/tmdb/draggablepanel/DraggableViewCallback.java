package com.krm.tmdb.draggablepanel;

import android.view.View;

import androidx.customview.widget.ViewDragHelper;

class DraggableViewCallback extends ViewDragHelper.Callback {

  private static final int MINIMUM_DX_FOR_HORIZONTAL_DRAG = 5;
  private static final int MINIMUM_DY_FOR_VERTICAL_DRAG = 15;
  private static final float X_MIN_VELOCITY = 1500;
  private static final float Y_MIN_VELOCITY = 1000;

  private DraggableView draggableView;
  private View draggedView;

  public DraggableViewCallback(DraggableView draggableView, View draggedView) {
    this.draggableView = draggableView;
    this.draggedView = draggedView;
  }

  @Override
  public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
    if (draggableView.isDragViewAtBottom()) {
      draggableView.changeDragViewViewAlpha();
    } else {
      draggableView.restoreAlpha();
      draggableView.changeDragViewScale();
      draggableView.changeDragViewPosition();
      draggableView.changeSecondViewAlpha();
      draggableView.changeSecondViewPosition();
      draggableView.changeBackgroundAlpha();
    }
  }

  @Override
  public void onViewReleased(View releasedChild, float xVel, float yVel) {
    super.onViewReleased(releasedChild, xVel, yVel);

    if (draggableView.isDragViewAtBottom() && !draggableView.isDragViewAtRight()) {
      triggerOnReleaseActionsWhileHorizontalDrag(xVel);
    } else {
      triggerOnReleaseActionsWhileVerticalDrag(yVel);
    }
  }

  @Override
  public boolean tryCaptureView(View view, int pointerId) {
    return view.equals(draggedView);
  }

  @Override
  public int clampViewPositionHorizontal(View child, int left, int dx) {
    int newLeft = draggedView.getLeft();
    if ((draggableView.isMinimized() && Math.abs(dx) > MINIMUM_DX_FOR_HORIZONTAL_DRAG) || (
        draggableView.isDragViewAtBottom()
            && !draggableView.isDragViewAtRight())) {
      newLeft = left;
    }
    return newLeft;
  }

  @Override
  public int clampViewPositionVertical(View child, int top, int dy) {
    int newTop = draggableView.getHeight() - draggableView.getDraggedViewHeightPlusMarginTop();
    if (draggableView.isMinimized() && Math.abs(dy) >= MINIMUM_DY_FOR_VERTICAL_DRAG
        || (!draggableView.isMinimized() && !draggableView.isDragViewAtBottom())) {

      final int topBound = draggableView.getPaddingTop();
      final int bottomBound = draggableView.getHeight()
          - draggableView.getDraggedViewHeightPlusMarginTop()
          - draggedView.getPaddingBottom();

      newTop = Math.min(Math.max(top, topBound), bottomBound);
    }
    return newTop;
  }

  private void triggerOnReleaseActionsWhileVerticalDrag(float yVel) {
    if (yVel < 0 && yVel <= -Y_MIN_VELOCITY) {
      draggableView.maximize();
    } else if (yVel > 0 && yVel >= Y_MIN_VELOCITY) {
      draggableView.minimize();
    } else {
      if (draggableView.isDragViewAboveTheMiddle()) {
        draggableView.maximize();
      } else {
        draggableView.minimize();
      }
    }
  }

  private void triggerOnReleaseActionsWhileHorizontalDrag(float xVel) {
    if (xVel < 0 && xVel <= -X_MIN_VELOCITY) {
      draggableView.closeToLeft();
    } else if (xVel > 0 && xVel >= X_MIN_VELOCITY) {
      draggableView.closeToRight();
    } else {
      if (draggableView.isNextToLeftBound()) {
        draggableView.closeToLeft();
      } else if (draggableView.isNextToRightBound()) {
        draggableView.closeToRight();
      } else {
        draggableView.minimize();
      }
    }
  }
}

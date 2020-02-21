package com.krm.tmdb.draggablepanel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.krm.tmdb.R;
import com.krm.tmdb.draggablepanel.transformer.Transformer;
import com.krm.tmdb.draggablepanel.transformer.TransformerFactory;

public class DraggableView extends RelativeLayout {

  private static final int DEFAULT_SCALE_FACTOR = 2;
  private static final int DEFAULT_TOP_VIEW_MARGIN = 30;
  private static final int DEFAULT_TOP_VIEW_HEIGHT = -1;
  private static final float SLIDE_TOP = 0f;
  private static final float SLIDE_BOTTOM = 1f;
  private static final float MIN_SLIDE_OFFSET = 0.1f;
  private static final boolean DEFAULT_ENABLE_HORIZONTAL_ALPHA_EFFECT = true;
  private static final boolean DEFAULT_ENABLE_CLICK_TO_MAXIMIZE = false;
  private static final boolean DEFAULT_ENABLE_CLICK_TO_MINIMIZE = false;
  private static final boolean DEFAULT_ENABLE_TOUCH_LISTENER = true;
  private static final int MIN_SLIDING_DISTANCE_ON_CLICK = 10;
  private static final int ONE_HUNDRED = 100;
  private static final float SENSITIVITY = 1f;
  private static final boolean DEFAULT_TOP_VIEW_RESIZE = false;
  private static final int INVALID_POINTER = -1;

  private int activePointerId = INVALID_POINTER;
  private float lastTouchActionDownXPosition;

  private View dragView;
  private View secondView;

  private FragmentManager fragmentManager;
  private ViewDragHelper viewDragHelper;
  private Transformer transformer;

  private boolean enableHorizontalAlphaEffect;
  private boolean topViewResize;
  private boolean enableClickToMaximize;
  private boolean enableClickToMinimize;
  private boolean touchEnabled;

  private DraggableListener listener;
  private int topViewHeight;
  private float scaleFactorX, scaleFactorY;
  private int marginBottom, marginRight;
  private int dragViewId, secondViewId;

  public DraggableView(Context context) {
    super(context);
  }

  public DraggableView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeAttributes(attrs);
  }

  public DraggableView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initializeAttributes(attrs);
  }

  public boolean isClickToMaximizeEnabled() {
    return enableClickToMaximize;
  }

  public void setClickToMaximizeEnabled(boolean enableClickToMaximize) {
    this.enableClickToMaximize = enableClickToMaximize;
  }

  public boolean isClickToMinimizeEnabled() {
    return enableClickToMinimize;
  }

  public void setClickToMinimizeEnabled(boolean enableClickToMinimize) {
    this.enableClickToMinimize = enableClickToMinimize;
  }

  private boolean isTouchEnabled() {
    return this.touchEnabled;
  }

  public void setTouchEnabled(boolean touchEnabled) {
    this.touchEnabled = touchEnabled;
  }

  public void slideHorizontally(float slideOffset, float drawerPosition, int width) {
    if (slideOffset > MIN_SLIDE_OFFSET && !isClosed() && isMaximized()) {
      minimize();
    }
    setTouchEnabled(slideOffset <= MIN_SLIDE_OFFSET);
    ViewCompat.setX(this, width - Math.abs(drawerPosition));
  }

  public void setXTopViewScaleFactor(float xScaleFactor) {
    transformer.setXScaleFactor(xScaleFactor);
  }

  public void setYTopViewScaleFactor(float yScaleFactor) {
    transformer.setYScaleFactor(yScaleFactor);
  }

  public void setTopViewMarginRight(int topFragmentMarginRight) {
    transformer.setMarginRight(topFragmentMarginRight);
  }

  public void setTopViewMarginBottom(int topFragmentMarginBottom) {
    transformer.setMarginBottom(topFragmentMarginBottom);
  }

  public void setTopViewHeight(int topFragmentHeight) {
    transformer.setViewHeight(topFragmentHeight);
  }

  public void setHorizontalAlphaEffectEnabled(boolean enableHorizontalAlphaEffect) {
    this.enableHorizontalAlphaEffect = enableHorizontalAlphaEffect;
  }

  public void setDraggableListener(DraggableListener listener) {
    this.listener = listener;
  }

  public void setTopViewResize(boolean topViewResize) {
    this.topViewResize = topViewResize;
    initializeTransformer();
  }

  @Override
  public void computeScroll() {
    if (!isInEditMode() && viewDragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this);
    }
  }

  public void maximize() {
    smoothSlideTo(SLIDE_TOP);
    notifyMaximizeToListener();
  }

  public void minimize() {
    smoothSlideTo(SLIDE_BOTTOM);
    notifyMinimizeToListener();
  }

  public void closeToRight() {
    if (viewDragHelper.smoothSlideViewTo(dragView, transformer.getOriginalWidth(),
        getHeight() - transformer.getMinHeightPlusMargin())) {
      ViewCompat.postInvalidateOnAnimation(this);
      notifyCloseToRightListener();
    }
  }

  public void closeToLeft() {
    if (viewDragHelper.smoothSlideViewTo(dragView, -transformer.getOriginalWidth(),
        getHeight() - transformer.getMinHeightPlusMargin())) {
      ViewCompat.postInvalidateOnAnimation(this);
      notifyCloseToLeftListener();
    }
  }

  public boolean isMinimized() {
    return isDragViewAtBottom() && isDragViewAtRight();
  }

  public boolean isMaximized() {
    return isDragViewAtTop();
  }

  public boolean isClosedAtRight() {
    return dragView.getLeft() >= getWidth();
  }

  public boolean isClosedAtLeft() {
    return dragView.getRight() <= 0;
  }

  public boolean isClosed() {
    return isClosedAtLeft() || isClosedAtRight();
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (!isEnabled()) {
      return false;
    }
    switch (MotionEventCompat.getActionMasked(ev) & MotionEventCompat.ACTION_MASK) {
      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP:
        viewDragHelper.cancel();
        return false;
      case MotionEvent.ACTION_DOWN:
        int index = MotionEventCompat.getActionIndex(ev);
        activePointerId = MotionEventCompat.getPointerId(ev, index);
        if (activePointerId == INVALID_POINTER) {
          return false;
        }
        break;
      default:
        break;
    }
    boolean interceptTap = viewDragHelper.isViewUnder(dragView, (int) ev.getX(), (int) ev.getY());
    return viewDragHelper.shouldInterceptTouchEvent(ev) || interceptTap;
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    int actionMasked = MotionEventCompat.getActionMasked(ev);
    if ((actionMasked & MotionEventCompat.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
      activePointerId = MotionEventCompat.getPointerId(ev, actionMasked);
    }
    if (activePointerId == INVALID_POINTER) {
      return false;
    }
    viewDragHelper.processTouchEvent(ev);
    if (isClosed()) {
      return false;
    }
    boolean isDragViewHit = isViewHit(dragView, (int) ev.getX(), (int) ev.getY());
    boolean isSecondViewHit = isViewHit(secondView, (int) ev.getX(), (int) ev.getY());
    analyzeTouchToMaximizeIfNeeded(ev, isDragViewHit);
    if (isMaximized()) {
      dragView.dispatchTouchEvent(ev);
    } else {
      dragView.dispatchTouchEvent(cloneMotionEventWithAction(ev, MotionEvent.ACTION_CANCEL));
    }
    return isDragViewHit || isSecondViewHit;
  }

  private void analyzeTouchToMaximizeIfNeeded(MotionEvent ev, boolean isDragViewHit) {
    switch(ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        lastTouchActionDownXPosition = ev.getX();
        break;
      case MotionEvent.ACTION_UP:
        float clickOffset = ev.getX() - lastTouchActionDownXPosition;
        if (shouldMaximizeOnClick(ev, clickOffset, isDragViewHit)) {
          if (isMinimized() && isClickToMaximizeEnabled()) {
            maximize();
          } else if (isMaximized() && isClickToMinimizeEnabled()) {
            minimize();
          }
        }
        break;
      default:
        break;
    }
  }

  public boolean shouldMaximizeOnClick(MotionEvent ev, float deltaX, boolean isDragViewHit) {
    return (Math.abs(deltaX) < MIN_SLIDING_DISTANCE_ON_CLICK)
        && ev.getAction() != MotionEvent.ACTION_MOVE
        && isDragViewHit;
  }

  private MotionEvent cloneMotionEventWithAction(MotionEvent event, int action) {
    return MotionEvent.obtain(event.getDownTime(), event.getEventTime(), action, event.getX(),
        event.getY(), event.getMetaState());
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    if (isInEditMode())
      super.onLayout(changed, left, top, right, bottom);
    else if (isDragViewAtTop()) {
      dragView.layout(left, top, right, transformer.getOriginalHeight());
      secondView.layout(left, transformer.getOriginalHeight(), right, bottom);
      ViewCompat.setY(dragView, top);
      ViewCompat.setY(secondView, transformer.getOriginalHeight());
    } else {
      secondView.layout(left, transformer.getOriginalHeight(), right, bottom);
    }
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    if (!isInEditMode()) {
      mapGUI();
      initializeTransformer();
      initializeViewDragHelper();
    }
  }

  private void mapGUI() {
    dragView = findViewById(dragViewId);
    secondView = findViewById(secondViewId);
  }

  void setFragmentManager(FragmentManager fragmentManager) {
    this.fragmentManager = fragmentManager;
  }

  void attachTopFragment(Fragment topFragment) {
    addFragmentToView(R.id.drag_view, topFragment);
  }

  void attachBottomFragment(Fragment bottomFragment) {
    addFragmentToView(R.id.second_view, bottomFragment);
  }

  void changeDragViewPosition() {
    transformer.updatePosition(getVerticalDragOffset());
  }

  void changeSecondViewPosition() {
    ViewCompat.setY(secondView, dragView.getBottom());
  }

  void changeDragViewScale() {
    transformer.updateScale(getVerticalDragOffset());
  }

  void changeBackgroundAlpha() {
    Drawable background = getBackground();
    if (background != null) {
      int newAlpha = (int) (ONE_HUNDRED * (1 - getVerticalDragOffset()));
      background.setAlpha(newAlpha);
    }
  }

  void changeSecondViewAlpha() {
    ViewCompat.setAlpha(secondView, 1 - getVerticalDragOffset());
  }

  void changeDragViewViewAlpha() {
    if (enableHorizontalAlphaEffect) {
      float alpha = 1 - getHorizontalDragOffset();
      if (alpha == 0) {
        alpha = 1;
      }
      ViewCompat.setAlpha(dragView, alpha);
    }
  }

  void restoreAlpha() {
    if (enableHorizontalAlphaEffect && ViewCompat.getAlpha(dragView) < 1) {
      ViewCompat.setAlpha(dragView, 1);
    }
  }

  boolean isDragViewAboveTheMiddle() {
    return transformer.isAboveTheMiddle();
  }

  boolean isNextToLeftBound() {
    return transformer.isNextToLeftBound();
  }

  boolean isNextToRightBound() {
    return transformer.isNextToRightBound();
  }

  boolean isDragViewAtTop() {
    return transformer.isViewAtTop();
  }

  boolean isDragViewAtRight() {
    return transformer.isViewAtRight();
  }

  boolean isDragViewAtBottom() {
    return transformer.isViewAtBottom();
  }

  private boolean isViewHit(View view, int x, int y) {
    int[] viewLocation = new int[2];
    view.getLocationOnScreen(viewLocation);
    int[] parentLocation = new int[2];
    this.getLocationOnScreen(parentLocation);
    int screenX = parentLocation[0] + x;
    int screenY = parentLocation[1] + y;
    return screenX >= viewLocation[0]
        && screenX < viewLocation[0] + view.getWidth()
        && screenY >= viewLocation[1]
        && screenY < viewLocation[1] + view.getHeight();
  }

  private void addFragmentToView(final int viewId, final Fragment fragment) {
    fragmentManager.beginTransaction().replace(viewId, fragment).commit();
  }

  private void initializeViewDragHelper() {
    viewDragHelper = ViewDragHelper.create(this, SENSITIVITY, new DraggableViewCallback(this, dragView));
  }

  private void initializeTransformer() {
    TransformerFactory transformerFactory = new TransformerFactory();
    transformer = transformerFactory.getTransformer(topViewResize, dragView, this);
    transformer.setViewHeight(topViewHeight);
    transformer.setXScaleFactor(scaleFactorX);
    transformer.setYScaleFactor(scaleFactorY);
    transformer.setMarginRight(marginRight);
    transformer.setMarginBottom(marginBottom);
  }

  private void initializeAttributes(AttributeSet attrs) {
    TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.draggable_view);
    this.enableHorizontalAlphaEffect =
        attributes.getBoolean(R.styleable.draggable_view_enable_minimized_horizontal_alpha_effect,
            DEFAULT_ENABLE_HORIZONTAL_ALPHA_EFFECT);
    this.enableClickToMaximize =
        attributes.getBoolean(R.styleable.draggable_view_enable_click_to_maximize_view,
            DEFAULT_ENABLE_CLICK_TO_MAXIMIZE);
    this.enableClickToMinimize =
        attributes.getBoolean(R.styleable.draggable_view_enable_click_to_minimize_view,
            DEFAULT_ENABLE_CLICK_TO_MINIMIZE);
    this.topViewResize =
            attributes.getBoolean(R.styleable.draggable_view_top_view_resize, DEFAULT_TOP_VIEW_RESIZE);
    this.topViewHeight = attributes.getDimensionPixelSize(R.styleable.draggable_view_top_view_height,
            DEFAULT_TOP_VIEW_HEIGHT);
    this.scaleFactorX = attributes.getFloat(R.styleable.draggable_view_top_view_x_scale_factor,
            DEFAULT_SCALE_FACTOR);
    this.scaleFactorY = attributes.getFloat(R.styleable.draggable_view_top_view_y_scale_factor,
            DEFAULT_SCALE_FACTOR);
    this.marginBottom = attributes.getDimensionPixelSize(R.styleable.draggable_view_top_view_margin_bottom,
            DEFAULT_TOP_VIEW_MARGIN);
    this.marginRight = attributes.getDimensionPixelSize(R.styleable.draggable_view_top_view_margin_right,
            DEFAULT_TOP_VIEW_MARGIN);
    this.dragViewId =
            attributes.getResourceId(R.styleable.draggable_view_top_view_id, R.id.drag_view);
    this.secondViewId =
            attributes.getResourceId(R.styleable.draggable_view_bottom_view_id, R.id.second_view);
    attributes.recycle();
  }

  private boolean smoothSlideTo(float slideOffset) {
    final int topBound = getPaddingTop();
    int x = (int) (slideOffset * (getWidth() - transformer.getMinWidthPlusMarginRight()));
    int y = (int) (topBound + slideOffset * getVerticalDragRange());
    if (viewDragHelper.smoothSlideViewTo(dragView, x, y)) {
      ViewCompat.postInvalidateOnAnimation(this);
      return true;
    }
    return false;
  }

  private int getDragViewMarginRight() {
    return transformer.getMarginRight();
  }

  private int getDragViewMarginBottom() {
    return transformer.getMarginBottom();
  }

  private float getHorizontalDragOffset() {
    return (float) Math.abs(dragView.getLeft()) / (float) getWidth();
  }

  private float getVerticalDragOffset() {
    return dragView.getTop() / getVerticalDragRange();
  }

  private float getVerticalDragRange() {
    return getHeight() - transformer.getMinHeightPlusMargin();
  }

  private void notifyMaximizeToListener() {
    if (listener != null) {
      listener.onMaximized();
    }
  }

  private void notifyMinimizeToListener() {
    if (listener != null) {
      listener.onMinimized();
    }
  }

  private void notifyCloseToRightListener() {
    if (listener != null) {
      listener.onClosedToRight();
    }
  }

  private void notifyCloseToLeftListener() {
    if (listener != null) {
      listener.onClosedToLeft();
    }
  }

  public int getDraggedViewHeightPlusMarginTop() {
    return transformer.getMinHeightPlusMargin();
  }
}

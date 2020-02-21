package com.krm.tmdb.draggablepanel;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.krm.tmdb.R;

public class DraggablePanel extends FrameLayout {

  private static final int DEFAULT_TOP_FRAGMENT_HEIGHT = 200;
  private static final int DEFAULT_TOP_FRAGMENT_MARGIN = 0;
  private static final float DEFAULT_SCALE_FACTOR = 2;
  private static final boolean DEFAULT_ENABLE_HORIZONTAL_ALPHA_EFFECT = true;
  private static final boolean DEFAULT_ENABLE_CLICK_TO_MAXIMIZE = false;
  private static final boolean DEFAULT_ENABLE_CLICK_TO_MINIMIZE = false;
  private static final boolean DEFAULT_ENABLE_TOUCH_LISTENER = true;
  private static final boolean DEFAULT_TOP_FRAGMENT_RESIZE = false;

  private DraggableView draggableView;
  private DraggableListener draggableListener;

  private FragmentManager fragmentManager;
  private Fragment topFragment;
  private Fragment bottomFragment;
  private int topFragmentHeight;
  private int topFragmentMarginRight;
  private int topFragmentMarginBottom;
  private float xScaleFactor;
  private float yScaleFactor;
  private boolean enableHorizontalAlphaEffect;
  private boolean enableClickToMaximize;
  private boolean enableClickToMinimize;
  private boolean enableTouchListener;

  public DraggablePanel(Context context) {
    super(context);
  }

  public DraggablePanel(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeAttrs(attrs);
  }

  public DraggablePanel(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initializeAttrs(attrs);
  }

  public void setFragmentManager(FragmentManager fragmentManager) {
    this.fragmentManager = fragmentManager;
  }

  public void setTopFragment(Fragment topFragment) {
    this.topFragment = topFragment;
  }

  public void setBottomFragment(Fragment bottomFragment) {
    this.bottomFragment = bottomFragment;
  }

  public void setTopViewHeight(int topFragmentHeight) {
    this.topFragmentHeight = topFragmentHeight;
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

  public void slideHorizontally(float slideOffset, float drawerPosition, int width) {
    draggableView.slideHorizontally(slideOffset, drawerPosition, width);
  }

  public void setXScaleFactor(float xScaleFactor) {
    this.xScaleFactor = xScaleFactor;
  }

  public void setYScaleFactor(float yScaleFactor) {
    this.yScaleFactor = yScaleFactor;
  }

  public void setTopFragmentMarginRight(int topFragmentMarginRight) {
    this.topFragmentMarginRight = topFragmentMarginRight;
  }

  public void setTopFragmentMarginBottom(int topFragmentMarginBottom) {
    this.topFragmentMarginBottom = topFragmentMarginBottom;
  }

  public void setDraggableListener(DraggableListener draggableListener) {
    this.draggableListener = draggableListener;
  }

  public void setEnableHorizontalAlphaEffect(boolean enableHorizontalAlphaEffect) {
    this.enableHorizontalAlphaEffect = enableHorizontalAlphaEffect;
  }

  public void setTopFragmentResize(boolean topViewResize) {
    draggableView.setTopViewResize(topViewResize);
  }

  public void closeToLeft() {
    draggableView.closeToLeft();
  }

  public void closeToRight() {
    draggableView.closeToRight();
  }

  public void maximize() {
    draggableView.maximize();
  }

  public void minimize() {
    draggableView.minimize();
  }

  public void initializeView() {
    checkFragmentConsistency();
    checkSupportFragmentManagerConsistency();

    inflate(getContext(), R.layout.draggable_panel, this);
    draggableView = findViewById(R.id.draggable_view);
    draggableView.setTopViewHeight(topFragmentHeight);
    draggableView.setFragmentManager(fragmentManager);
    draggableView.attachTopFragment(topFragment);
    draggableView.setXTopViewScaleFactor(xScaleFactor);
    draggableView.setYTopViewScaleFactor(yScaleFactor);
    draggableView.setTopViewMarginRight(topFragmentMarginRight);
    draggableView.setTopViewMarginBottom(topFragmentMarginBottom);
    draggableView.attachBottomFragment(bottomFragment);
    draggableView.setDraggableListener(draggableListener);
    draggableView.setHorizontalAlphaEffectEnabled(enableHorizontalAlphaEffect);
    draggableView.setClickToMaximizeEnabled(enableClickToMaximize);
    draggableView.setClickToMinimizeEnabled(enableClickToMinimize);
    draggableView.setTouchEnabled(enableTouchListener);
  }

  public boolean isMaximized() {
    return draggableView.isMaximized();
  }

  public boolean isMinimized() {
    return draggableView.isMinimized();
  }

  public boolean isClosedAtRight() {
    return draggableView.isClosedAtRight();
  }

  public boolean isClosedAtLeft() {
    return draggableView.isClosedAtLeft();
  }

  private void initializeAttrs(AttributeSet attrs) {
    TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.draggable_panel);
    this.topFragmentHeight =
        attributes.getDimensionPixelSize(R.styleable.draggable_panel_top_fragment_height,
            DEFAULT_TOP_FRAGMENT_HEIGHT);
    this.xScaleFactor =
        attributes.getFloat(R.styleable.draggable_panel_x_scale_factor, DEFAULT_SCALE_FACTOR);
    this.yScaleFactor =
        attributes.getFloat(R.styleable.draggable_panel_y_scale_factor, DEFAULT_SCALE_FACTOR);
    this.topFragmentMarginRight =
        attributes.getDimensionPixelSize(R.styleable.draggable_panel_top_fragment_margin_right,
            DEFAULT_TOP_FRAGMENT_MARGIN);
    this.topFragmentMarginBottom =
        attributes.getDimensionPixelSize(R.styleable.draggable_panel_top_fragment_margin_bottom,
            DEFAULT_TOP_FRAGMENT_MARGIN);
    this.enableHorizontalAlphaEffect =
        attributes.getBoolean(R.styleable.draggable_panel_enable_horizontal_alpha_effect,
            DEFAULT_ENABLE_HORIZONTAL_ALPHA_EFFECT);
    this.enableClickToMaximize =
        attributes.getBoolean(R.styleable.draggable_panel_enable_click_to_maximize_panel,
            DEFAULT_ENABLE_CLICK_TO_MAXIMIZE);
    this.enableClickToMinimize =
        attributes.getBoolean(R.styleable.draggable_panel_enable_click_to_minimize_panel,
            DEFAULT_ENABLE_CLICK_TO_MINIMIZE);
    this.enableTouchListener =
        attributes.getBoolean(R.styleable.draggable_panel_enable_touch_listener_panel,
            DEFAULT_ENABLE_TOUCH_LISTENER);
    attributes.recycle();
  }

  private void checkSupportFragmentManagerConsistency() {
    if (fragmentManager == null) {
      throw new IllegalStateException(
          "You have to set the support FragmentManager before initialize DraggablePanel");
    }
  }

  private void checkFragmentConsistency() {
    if (topFragment == null || bottomFragment == null) {
      throw new IllegalStateException(
          "You have to set top and bottom fragment before initialize DraggablePanel");
    }
  }
}

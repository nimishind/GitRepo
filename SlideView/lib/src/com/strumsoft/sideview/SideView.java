package com.strumsoft.sideview;

import com.strumsoft.sideview.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.Log;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class SideView extends LinearLayout implements OnTouchListener {

    private int mControllerId;
    private View mControllerView;

    private int mMainId;
    private View mMainView;

    private int mDataId;
    private View mDataView;

    private int mLastMainSize;

    private boolean mDragging;
    private long mDraggingStarted;
    private float mDragStartX;
    private float mDragStartY;

    private float mPointerOffset;

    GestureDetector mGestureDetector = null;

    private int ScreenWidth = 100;
    private int DefaultPrimaryWidth = 100;
    private int mDataWeight;
    private int mMainWeight;
    private int mDefaultMainSize;

    final static private int MAX_THREASHOLD_DIP = 30;
    final static private int TAP_DRIFT_TOLERANCE = 3;
    final static private int SINGLE_TAP_TIME_LIMIT = 175;

    public SideView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray viewAttrs = context.obtainStyledAttributes(attrs, R.styleable.SlideView);

        RuntimeException e = null;
        mControllerId = viewAttrs.getResourceId(R.styleable.SlideView_controller, 0);
        if (mControllerId == 0) {
            e = new IllegalArgumentException(viewAttrs.getPositionDescription()
                    + ": The required attribute handle must refer to a valid child view.");
        }

        mMainId = viewAttrs.getResourceId(R.styleable.SlideView_mainView, 0);
        if (mMainId == 0) {
            e = new IllegalArgumentException(viewAttrs.getPositionDescription()
                    + ": The required attribute primaryContent must refer to a valid child view.");
        }

        mDataId = viewAttrs.getResourceId(R.styleable.SlideView_dataView, 0);
        if (mDataId == 0) {
            e = new IllegalArgumentException(viewAttrs.getPositionDescription()
                    + ": The required attribute secondaryContent must refer to a valid child view.");
        }

        mMainWeight = getResources().getInteger(
                viewAttrs.getResourceId(R.styleable.SlideView_mainViewWeight, 0));
        mDataWeight = getResources().getInteger(
                viewAttrs.getResourceId(R.styleable.SlideView_dataViewWeight, 0));

        viewAttrs.recycle();

        if (e != null) {
            throw e;
        }

    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        mControllerView = findViewById(mControllerId);
        if (mControllerView == null) {
            String name = getResources().getResourceEntryName(mControllerId);
            throw new RuntimeException("Your Panel must have a child View whose id attribute is 'R.id."
                    + name + "'");

        }
        mMainView = findViewById(mMainId);
        if (mMainView == null) {
            String name = getResources().getResourceEntryName(mMainId);
            throw new RuntimeException("Your Panel must have a child View whose id attribute is 'R.id."
                    + name + "'");

        }

        mLastMainSize = getPrimaryContentSize();

        mDataView = findViewById(mDataId);
        if (mDataView == null) {
            String name = getResources().getResourceEntryName(mDataId);
            throw new RuntimeException("Your Panel must have a child View whose id attribute is 'R.id."
                    + name + "'");

        }
        mGestureDetector = new GestureDetector(new XButtonFlipDetector());

        mControllerView.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent me) {
        ViewGroup.LayoutParams thisParams = getLayoutParams();
        // Only capture drag events if we start
        if (view != mControllerView) {
            return false;
        }
        if (mGestureDetector.onTouchEvent(me)) {
            return true;
        }
        // Log.v("foo", "at "+SystemClock.elapsedRealtime()+" got touch event " + me);
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            mDragging = true;
            mDraggingStarted = SystemClock.elapsedRealtime();
            mDragStartX = me.getX();
            mDragStartY = me.getY();
            if (getOrientation() == VERTICAL) {
                mPointerOffset = me.getRawY() - mMainView.getMeasuredHeight();
            } else {
                mPointerOffset = me.getRawX() - mMainView.getMeasuredWidth();
            }
            return true;
        } else if (me.getAction() == MotionEvent.ACTION_UP) {
            mDragging = false;
            if (mDragStartX < (me.getX() + TAP_DRIFT_TOLERANCE)
                    && mDragStartX > (me.getX() - TAP_DRIFT_TOLERANCE)
                    && mDragStartY < (me.getY() + TAP_DRIFT_TOLERANCE)
                    && mDragStartY > (me.getY() - TAP_DRIFT_TOLERANCE)
                    && ((SystemClock.elapsedRealtime() - mDraggingStarted) < SINGLE_TAP_TIME_LIMIT)) {
                if (isPrimaryContentMaximized() || isSecondaryContentMaximized()) {
                    setPrimaryContentSize(mLastMainSize);
                } else {
                    maximizeSecondaryContent();
                }
            }
            return true;
        } else if (me.getAction() == MotionEvent.ACTION_MOVE) {
            if (getOrientation() == VERTICAL) {
                setPrimaryContentHeight((int) (me.getRawY() - mPointerOffset));
            } else {
                setPrimaryContentWidth((int) (me.getRawX() - mPointerOffset));
            }
        }
        return true;
    }

    public View getHandle() {
        return mControllerView;
    }

    public int getPrimaryContentSize() {
        if (getOrientation() == VERTICAL) {
            return mMainView.getMeasuredHeight();
        } else {
            return mMainView.getMeasuredWidth();
        }

    }

    public boolean setPrimaryContentSize(int newSize) {
        if (getOrientation() == VERTICAL) {
            return setPrimaryContentHeight(newSize);
        } else {
            return setPrimaryContentWidth(newSize);
        }
    }

    private boolean setPrimaryContentHeight(int newHeight) {
        ViewGroup.LayoutParams params = mMainView.getLayoutParams();
        if (mDataView.getMeasuredHeight() < 1 && newHeight > params.height) {
            return false;
        }
        if (newHeight >= 0) {
            params.height = newHeight;
        }
        unMinimizeSecondaryContent();
        mMainView.setLayoutParams(params);
        return true;

    }

    private boolean setPrimaryContentWidth(int newWidth) {
        ViewGroup.LayoutParams params = mMainView.getLayoutParams();

        if (mDataView.getMeasuredWidth() < 1 && newWidth > params.width) {
            return false;
        }
        if (newWidth >= 0) {
            params.width = newWidth;
        }
        unMinimizeSecondaryContent();
        mMainView.setLayoutParams(params);
        return true;
    }

    public boolean isPrimaryContentMaximized() {
        if ((getOrientation() == VERTICAL && (mDataView.getMeasuredHeight() < MAX_THREASHOLD_DIP))
                || (getOrientation() == HORIZONTAL && (mDataView.getMeasuredWidth() < MAX_THREASHOLD_DIP))) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isSecondaryContentMaximized() {
        if ((getOrientation() == VERTICAL && (mMainView.getMeasuredHeight() < MAX_THREASHOLD_DIP))
                || (getOrientation() == HORIZONTAL && (mMainView.getMeasuredWidth() < MAX_THREASHOLD_DIP))) {
            return true;
        } else {
            return false;
        }
    }

    public void maximizePrimaryContent() {
        maximizeContentPane(mMainView, mDataView);
    }

    public void maximizeSecondaryContent() {
        maximizeContentPane(mDataView, mMainView);
    }

    private void maximizeContentPane(View toMaximize, View toUnMaximize) {
        mLastMainSize = getPrimaryContentSize();

        ViewGroup.LayoutParams params = toUnMaximize.getLayoutParams();
        ViewGroup.LayoutParams secondaryParams = toMaximize.getLayoutParams();
        if (getOrientation() == VERTICAL) {
            params.height = 1;
            secondaryParams.height = LayoutParams.FILL_PARENT; // getLayoutParams().height -
                                                               // mHandle.getLayoutParams().height;
        } else {
            params.width = 1;
            secondaryParams.width = LayoutParams.FILL_PARENT; // getLayoutParams().width -
                                                              // mHandle.getLayoutParams().width;
        }
        toUnMaximize.setLayoutParams(params);
        toMaximize.setLayoutParams(secondaryParams);

    }

    private void unMinimizeSecondaryContent() {
        ViewGroup.LayoutParams secondaryParams = mDataView.getLayoutParams();
        if (getOrientation() == VERTICAL) {
            secondaryParams.height = LayoutParams.FILL_PARENT;
        } else {
            secondaryParams.width = LayoutParams.FILL_PARENT;

        }
        mDataView.setLayoutParams(secondaryParams);

    }

    class XButtonFlipDetector extends SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 20;
        private static final int SWIPE_MIN_DISTANCE_PRIM = 15;
        private static final int SWIPE_MAX_OFF_PATH = 100;
        private static final int SWIPE_THRESHOLD_VELOCITY = 50;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                Log.i("Nimish", Math.abs(e1.getRawY() - e2.getRawY()) + " velosity " + velocityX
                        + " We got a fling " + (e2.getRawX() - e1.getRawX()));

                if (Math.abs(e1.getRawY() - e2.getRawY()) <= SWIPE_MAX_OFF_PATH) {
                    if (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        if (e1.getRawX() - e2.getRawX() > SWIPE_MIN_DISTANCE) {
                            // setPrimaryContentSize(400);
                            maximizeSecondaryContent();
                            Log.i("Nimish", " We got a fling max sec content");
                            return true;
                        } else {

                            if (e2.getRawX() - e1.getRawX() > SWIPE_MIN_DISTANCE_PRIM) {
                                setPrimaryContentSize(mDefaultMainSize);
                                // maximizePrimaryContent();
                                Log.i("Nimish", " We got a fling max Prim content");
                                return true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public void setScreenWidth(int width) {
        ScreenWidth = width;
        mDefaultMainSize = (int) (width * 1.0 * mMainWeight / (1.0 * (mDataWeight + mMainWeight)));
        setPrimaryContentWidth(mDefaultMainSize);
    }

};

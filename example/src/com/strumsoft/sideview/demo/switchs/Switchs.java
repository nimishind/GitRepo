package com.strumsoft.sideview.demo.switchs;

import com.strumsoft.sideview.demo.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class Switchs extends RelativeLayout {

    int id;
    private View mSwitchTextLayout;
    private TextView mBtnON;
    private TextView mBtnOFF;
    private ImageView mBtnSelectBg;
    private boolean SwitchStatus;
    private View parent;
    private OnSwitchesChangedListener listener;
    private Context mContext = null;
    private TextView mSwitchText;

    public interface OnSwitchesChangedListener {
        public void switchModeChanged(int id, boolean switchOn);
    }

    public Switchs(Context context) {
        super(context);
        init(context);
    }

    public Switchs(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Switchs(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context) {
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {

        // Initialize context
        mContext = context;
        // add content to layout
        if (parent == null)
            parent = LayoutInflater.from(context).inflate(R.layout.switchs, this);
        else
            return;

        mBtnSelectBg = (ImageView) parent.findViewById(R.id.switch_button_bg);
        mBtnON = (TextView) parent.findViewById(R.id.switch_btn_on);
        mBtnON.setOnClickListener(clickListener);
        mBtnOFF = (TextView) parent.findViewById(R.id.switch_btn_off);
        mBtnOFF.setOnClickListener(clickListener);

        mSwitchTextLayout = parent.findViewById(R.id.switch_text_layout);
        mSwitchText = (TextView) parent.findViewById(R.id.switch_text);

        final GestureDetector gestureDetector = new GestureDetector(new XButtonFlipDetector());
        final OnTouchListener gestureListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        final OnTouchListener pass = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        };

        parent.findViewById(R.id.switch_btn_layout).setOnTouchListener(gestureListener);
        parent.findViewById(R.id.switch_btn_layout).setOnClickListener(clickListener);
        mBtnOFF.setOnTouchListener(gestureListener);
        mBtnON.setOnTouchListener(gestureListener);
        mBtnSelectBg.setOnTouchListener(gestureListener);
        setGroupMode(false);
        try {
            // initialise attributes
            if (attrs != null) {
                int[] attrsArray = new int[] { android.R.attr.id, // 0
                        android.R.attr.background, // 1
                        android.R.attr.layout_width, // 2
                        android.R.attr.layout_height, // 3
                        android.R.attr.textOff,// 4
                        android.R.attr.textOn,// 5
                        android.R.attr.text, // 6
                        android.R.attr.checked };
                TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
                id = ta.getResourceId(0 /* index of attribute in attrsArray */, View.NO_ID);

                // int layout_width = ta.getDimensionPixelSize(2, ViewGroup.LayoutParams.MATCH_PARENT);
                // int layout_height = ta.getDimensionPixelSize(3, ViewGroup.LayoutParams.MATCH_PARENT);
                mBtnOFF.setText(ta.getString(4) == null ? "Off" : ta.getString(4));
                mBtnON.setText(ta.getString(5) == null ? "On" : ta.getString(5));

                String str = ta.getString(6);
                if (null == str) {
                    mSwitchTextLayout.setVisibility(GONE);
                } else
                    mSwitchText.setText(str);

                if (ta.getBoolean(7, false)) {
                    setGroupMode(true);
                } else
                    setGroupMode(false);
                ta.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class XButtonFlipDetector extends SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 40;
        private static final int SWIPE_MAX_OFF_PATH = 150;
        private static final int SWIPE_THRESHOLD_VELOCITY = 25;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) <= SWIPE_MAX_OFF_PATH) {
                    if (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {

                            setSwitchMode(true, false, true);
                            return true;
                        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
                            setSwitchMode(false, false, true);
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private void setSwitchMode(boolean switchOn, boolean force, boolean callListener) {

        if (force || this.SwitchStatus != switchOn) {
            this.SwitchStatus = switchOn;
            if (switchOn) {
                mBtnOFF.setTextColor(Color.GRAY);
                mBtnON.setTextColor(Color.WHITE);
            } else {
                mBtnOFF.setTextColor(Color.WHITE);
                mBtnON.setTextColor(Color.GRAY);

            }
            mBtnSelectBg.setImageResource(switchOn ? R.drawable.group_buttons_sender
                    : R.drawable.group_buttons_group);

            if (callListener && listener != null) {
                listener.switchModeChanged(id, switchOn);
            }
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            // dialog buttons act like radio buttons
            // one of the header radio buttons
            final boolean groupMms = v == mBtnON;
            setSwitchMode(groupMms, false, true);
        }
    };

    public void setGroupMode(boolean groupMms) {
        setSwitchMode(groupMms, true, false);
    }

    public void setListener(OnSwitchesChangedListener listener) {
        this.listener = listener;
    }

    /*
     * Function called to reflect change in the resources
     */
    public void refresh() {
        // groupBtn.setText(R.string.group);
        // senderBtn.setText(R.string.just_me);
        // recipientsReply.setText(R.string.recipients_reply);
        // dialogGroupBtn.setText(R.string.group_prompt_group_desc);
        // dialogSenderBtn.setText(R.string.group_prompt_sender_desc);
        // TextView disclaimer = (TextView)findViewById(R.id.disclaimer);
        // disclaimer.setText(R.string.group_prompt_disclaimer);
        // closeDialog.setText(R.string.close);
    }

    // This is added to accommodate Just Me string which is an large string in French language.
    public void setMassTextPadding() {
        mBtnON.setPadding(1, 0, 0, 0);
        mBtnON.setGravity(Gravity.CENTER_VERTICAL);
    }

    public void changeMassTextPadding() {
        mBtnON.setPadding(7, 0, 0, 0);
        mBtnON.setGravity(Gravity.CENTER_VERTICAL);
        mSwitchText.setGravity(Gravity.CENTER_VERTICAL);
    }

    public boolean isChecked() {
        return SwitchStatus;
    }

    public void setChecked(boolean switchPreviousState) {
        SwitchStatus = switchPreviousState;
        setSwitchMode(switchPreviousState, false, true);
    }

    public void setOnText(String text) {
        mBtnON.setText(text);
        adjustWidth();
    }

    public void setOffText(String text) {
        mBtnOFF.setText(text);
        adjustWidth();
    }

    private void adjustWidth() {
        int widthON = (int) mBtnON.getPaint().measureText(mBtnON.getText().toString());
        int widthOFF = (int) mBtnOFF.getPaint().measureText(mBtnOFF.getText().toString());

        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

        widthON = Math.max(widthON, widthOFF) * 2
                + Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 16.66, metrics));
        LayoutParams par = new LayoutParams(widthON, LayoutParams.WRAP_CONTENT);

        parent.findViewById(R.id.switch_btn_layout).setLayoutParams(par);
    }

}

package com.alexfu.countdownview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alexfu.countdownview.R;

import java.text.DecimalFormat;
import java.util.Calendar;

public class CountDownView extends RelativeLayout {
    private CountDownTimer mTimer;
    private TextView mHours, mMinutes, mSeconds, mMilliseconds;
    private long mBeginTime, mCurrentMillis;
    private boolean mIsTimerRunning = false;
    private static final Calendar mTime = Calendar.getInstance();
    private static final DecimalFormat mFormatter = new DecimalFormat("00");

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.countdownview_main, this, true);

        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        int numColorId = values.getResourceId(R.styleable.CountDownView_numberColor, android.R.attr.textColor);
        int unitColorId = values.getResourceId(R.styleable.CountDownView_unitColor, android.R.attr.textColor);

        if(values.getBoolean(R.styleable.CountDownView_showHour, false)) {
            View v = ((ViewStub) findViewById(R.id.hours_stub)).inflate();
            mHours = (TextView) v.findViewById(R.id.hours);
            ((TextView) v.findViewById(R.id.hours_unit)).setTextColor(getResources().getColorStateList(unitColorId));
            mHours.setTextColor(getResources().getColorStateList(numColorId));
        }

        if(values.getBoolean(R.styleable.CountDownView_showMin, false)) {
            View v = ((ViewStub) findViewById(R.id.minutes_stub)).inflate();
            mMinutes = (TextView) v.findViewById(R.id.minutes);
            ((TextView) v.findViewById(R.id.minutes_unit)).setTextColor(getResources().getColorStateList(unitColorId));
            mMinutes.setTextColor(getResources().getColorStateList(numColorId));
        }

        if(values.getBoolean(R.styleable.CountDownView_showSec, false)) {
            View v = ((ViewStub) findViewById(R.id.seconds_stub)).inflate();
            mSeconds = (TextView) v.findViewById(R.id.seconds);
            ((TextView) v.findViewById(R.id.seconds_unit)).setTextColor(getResources().getColorStateList(unitColorId));
            mSeconds.setTextColor(getResources().getColorStateList(numColorId));
        }

        if(values.getBoolean(R.styleable.CountDownView_showMilli, false)) {
            View v = ((ViewStub) findViewById(R.id.milliseconds_stub)).inflate();
            mMilliseconds = (TextView) v.findViewById(R.id.milliseconds);
            ((TextView) v.findViewById(R.id.milliseconds_unit)).setTextColor(getResources().getColorStateList(unitColorId));
            mMilliseconds.setTextColor(getResources().getColorStateList(numColorId));
        }
    }

    public void setBeginTime(long millisInFuture) {
        mBeginTime = millisInFuture;
    }

    public void setTimer(long millisInFuture) {
        mCurrentMillis = millisInFuture;
        setTime(millisInFuture);
    }

    private void setTime(long millisInFuture) {
        mTime.setTimeInMillis(millisInFuture);
        if(mMinutes != null)
            mMinutes.setText(mFormatter.format(mTime.get(Calendar.MINUTE)));

        if(mSeconds != null)
            mSeconds.setText(mFormatter.format(mTime.get(Calendar.SECOND)));

        if(mMilliseconds != null)
            mMilliseconds.setText(mFormatter.format(mTime.get(Calendar.MILLISECOND)));
    }

    public void start() {
        start(mCurrentMillis);
    }

    public void start(long millisInFuture) {
        mIsTimerRunning = true;
        mTimer = new CountDownTimer(millisInFuture, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrentMillis = millisUntilFinished;
                setTime(mCurrentMillis);
            }

            @Override
            public void onFinish() {
                setTime(0);
            }
        }.start();
    }

    public void stop() {
        if(mTimer != null) {
            mTimer.cancel();
            mCurrentMillis = mTime.getTimeInMillis();
            mIsTimerRunning = false;
        }
    }

    public void reset() {
        stop();
        mCurrentMillis = mBeginTime;
        setTime(mCurrentMillis);
    }

    public boolean isTimerRunning() { return mIsTimerRunning; }

    public long getCurrentMillis() {
        return mCurrentMillis;
    }
}

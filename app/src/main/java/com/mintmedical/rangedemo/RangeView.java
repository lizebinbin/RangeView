package com.mintmedical.rangedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by MooreLi on 2016/11/16.
 */

public class RangeView extends RelativeLayout {
    private String TAG = getClass().getSimpleName();
    private Context mContext;

    private RectF mRectFOut;
    private RectF mRectFIn;
    private Paint mPaint;

    /**
     * 数据值
     */
    private float mTotalMin, mTotalMax;
    private float mNormalMin, mNormalMax;

    private float mTotalOffset;
    private float mNormalOffset;

    private float mValue = -1;

    /**
     * 目标值文字大小  正常范围值大小  环形高度
     */
    private int redTextSize = 23;
    private int greenTextSize = 10;
    private int rangeHeight = 19;

    private SectorView mSectorView;
    private ImageView mPointView;

    public RangeView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public RangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public RangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        //值
        mSectorView = new SectorView(mContext);
        mSectorView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //指针
        mPointView = new ImageView(mContext);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.point);
        mPointView.setImageBitmap(bitmap);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        mPointView.setLayoutParams(params);
        mPointView.setTranslationY(-bitmap.getHeight() / 2);

        RotateAnimation animation = new RotateAnimation(0, -120, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setDuration(2000);
        animation.setFillAfter(true);
        mPointView.startAnimation(animation);

        this.addView(mSectorView);
        this.addView(mPointView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 设置范围（总范围和正常范围）
     *
     * @param totalMin
     * @param totalMax
     * @param normalMin
     * @param normalMax
     */
    public void setRange(float totalMin, float totalMax, float normalMin, float normalMax) {
        this.mTotalMin = totalMin;
        this.mTotalMax = totalMax;
        this.mNormalMin = normalMin;
        this.mNormalMax = normalMax;

        mTotalOffset = mTotalMax - mTotalMin;
        mNormalOffset = mNormalMax - mNormalMin;
        if (mTotalOffset != 0) {
            mSectorView.postInvalidate();
        } else {
            Log.e(TAG, "总范围为0！值设置错误！");
        }
    }

    public void setValue(float value) {
        this.mValue = value;
        mSectorView.postInvalidate();
    }

    class SectorView extends View {
        private int width;
        private int height;

        private int mCircleWidth;

        private int abnormalAngel = 40;

        public SectorView(Context context) {
            super(context);
            initSector();
        }

        public SectorView(Context context, AttributeSet attrs) {
            super(context, attrs);
            initSector();
        }

        public SectorView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initSector();
        }

        private void initSector() {
            mCircleWidth = dip2px(rangeHeight);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.e(TAG, "onDraw  width:" + width + "   height:" + height);
            mRectFOut = new RectF(0, 0, width, width);
            mRectFIn = new RectF(mCircleWidth, mCircleWidth, width - mCircleWidth, width - mCircleWidth);
            /**
             * 计算角度  ------现在固定角度，不需要根据值计算。。。。。
             */
//            float abnormalBeforePercent = (mNormalMin - mTotalMin) / mTotalOffset;
//            float normalPercent = mNormalOffset / mTotalOffset;
//            float abnormalAfterPercent = 1 - abnormalBeforePercent - normalPercent;
//
//            int angleAbnormalFirst = (int) (abnormalBeforePercent * 240f);
//            int angleNormal = (int) (normalPercent * 240f);
//            int angleAbnormalSecond = (int) (abnormalAfterPercent * 240f);

//            canvas.drawArc(mRectFOut, 150, angleAbnormalFirst, true, mPaintRed);
//            canvas.drawArc(mRectFOut, 150 + angleAbnormalFirst, angleNormal, true, mPaintGreen);
//            canvas.drawArc(mRectFOut, 150 + angleAbnormalFirst + angleNormal, angleAbnormalSecond, true, mPaintRed);

            mPaint.setColor(getResources().getColor(R.color.red));
            canvas.drawArc(mRectFOut, 150, abnormalAngel, true, mPaint);
            mPaint.setColor(getResources().getColor(R.color.mainColor));
            canvas.drawArc(mRectFOut, 150 + abnormalAngel, (240 - abnormalAngel * 2), true, mPaint);
            mPaint.setColor(getResources().getColor(R.color.red));
            canvas.drawArc(mRectFOut, 150 + abnormalAngel + (240 - abnormalAngel * 2), abnormalAngel, true, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawArc(mRectFIn, 150 - 1, 240 + 2, true, mPaint);
            /**
             * 绘制监测值
             */
            Rect textRect = new Rect();
            String testValue = getStringValue(mValue);
            mPaint.setTextSize(sp2px(redTextSize));
            mPaint.getTextBounds(testValue, 0, testValue.length(), textRect);
            int strX = width / 2 - (textRect.width() / 2);
            int strY = width - mCircleWidth;
            if (mValue > mNormalMin && mValue < mNormalMax)
                mPaint.setColor(getResources().getColor(R.color.mainColor));
            else
                mPaint.setColor(getResources().getColor(R.color.red));
            canvas.drawText(testValue, strX, strY, mPaint);
            /**
             * 旋转指针 三种情况
             */
            if (mValue != -1) {
                int angel = 0;
                float percent = 0;
                if (mValue < mNormalMin) {
                    percent = (mValue - mTotalMin) / (mNormalMin - mTotalMin);
                    angel = (int) (abnormalAngel * percent);
                } else if (mValue >= mNormalMin && mValue <= mNormalMax) {
                    percent = (mValue - mNormalMin) / mNormalOffset;
                    angel = (int) (abnormalAngel + (240 - abnormalAngel * 2) * percent);
                } else {
                    percent = (mValue - mNormalMax) / (mTotalMax - mNormalMax);
                    angel = (int) (abnormalAngel + (240 - abnormalAngel * 2) + abnormalAngel * percent);
                }
                Log.e(TAG, "angel:" + angel);
                angel -= 120;
                RotateAnimation animation = new RotateAnimation(-120, angel, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(1000);
                animation.setFillAfter(true);
                mPointView.startAnimation(animation);
            }

            /**
             * 确定数值位置
             */
            String normalMin = getStringValue(mNormalMin);
            String normalMax = getStringValue(mNormalMax);
            mPaint.setTextSize(sp2px(greenTextSize));
            mPaint.getTextBounds(normalMin, 0, normalMin.length(), textRect);
            int valueY = width / 2 - (textRect.height() / 2);
            mPaint.setColor(getResources().getColor(R.color.mainColor));
            canvas.drawText(normalMin, mCircleWidth + dip2px(3), valueY, mPaint);
            mPaint.getTextBounds(normalMax, 0, normalMax.length(), textRect);
            canvas.drawText(normalMax, width - mCircleWidth - textRect.width() - dip2px(3), valueY, mPaint);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int widthModel = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightModel = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthModel == MeasureSpec.EXACTLY) {
                width = widthSize;
            } else {
                Log.e(TAG, "width  ==？？？");
                width = widthSize;
            }
            if (heightModel == MeasureSpec.EXACTLY) {
                height = heightSize;
            } else {
                Log.e(TAG, "height  ==？？？");
                height = heightSize;
            }
            setMeasuredDimension(width, width);
        }
    }

    private int dip2px(int dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int sp2px(int sp) {
        float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    private String getStringValue(float value) {
        //向下取整
        int temp = (int) Math.floor(value);
        if (value - (float) temp == 0) {
            return temp + "";
        } else {
            return value + "";
        }
    }
}

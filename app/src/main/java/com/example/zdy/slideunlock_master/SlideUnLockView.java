package com.example.zdy.slideunlock_master;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZDY on 2017/5/10.
 */

public class SlideUnLockView extends RelativeLayout {
    private Paint iconPaint;
    private Bitmap bitmap;
    private int mIcon;
    private static final int IDEL = 1;
    private static final int OPEN = 3;
    private static final int CLOSE = 6;
    private static final int SLIDE = 4;
    private static final int CANCEL = 5;
    private int drawState = IDEL;
    private Paint whitePaint;
    private Paint bluePaint;
    private Paint bgPaint;
    private Paint gouPaint;

    private Bitmap gouBitmap;
    private int mGou = 0;

    private int wholeWidth;
    private int mIconWidth;
    private Bitmap mIconBitmap;
    private int mIconHeight;

    private boolean isFirstDraw = true;
    private String text = "The One Who Wants to Wear a Crown Must Bear the Weight.";
    private int baseline;
    private int textStart = -1;
    private int bitmapLeft = 0;
    private List<TextAndLocation> tAl;
    private Bitmap mGouBitmap;
    private int mGouWidth;
    private int mGouHeight;
    private int xGouStart = 0;
    private int yGouStart = 0;
    private int scalafterWidth = 0;


    public SlideUnLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideUnLockView);
        mIcon = a.getResourceId(R.styleable.SlideUnLockView_sl_icon, 0);
        mGou = a.getResourceId(R.styleable.SlideUnLockView_sl_complete_icon, 0);
        a.recycle();
        if (mIcon != 0) {
            bitmap = ((BitmapDrawable) ContextCompat.getDrawable(context, mIcon)).getBitmap();
        }
        if (mGou != 0) {
            gouBitmap = ((BitmapDrawable) ContextCompat.getDrawable(context, mGou)).getBitmap();
        }
    }

    private void initPaint() {
        iconPaint = new Paint();
        iconPaint.setAntiAlias(true);
        iconPaint.setStyle(Paint.Style.FILL);


        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.BLACK);

        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
//        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(Color.WHITE);

        bluePaint = new Paint();
        bluePaint.setAntiAlias(true);
        bluePaint.setColor(getResources().getColor(R.color.firmare_progress_color));

        gouPaint = new Paint();
        gouPaint.setAntiAlias(true);
        gouPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnTouchListener(touchListener);

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirstDraw) {
            wholeWidth = getWidth();
            //bitmap的高度就是MatchParent
            mIconHeight = getHeight();
            Log.w("lzqhigh", "height" + mIconHeight);
            mIconWidth = bitmap.getWidth();

            mGouHeight = (int) (getHeight() * 0.8);
            mGouWidth = mGouHeight;
            xGouStart = (wholeWidth - mGouWidth) / 2;
            yGouStart = (int) (mIconHeight * 0.1f);
            whitePaint.setTextSize(mIconHeight * 0.32f);
            bluePaint.setTextSize(mIconHeight * 0.32f);

            isFirstDraw = false;
        }

        switch (drawState) {
            case IDEL:
                if (mIconBitmap == null) {
                    mIconBitmap = Bitmap.createScaledBitmap(bitmap, mIconWidth, mIconHeight, true);
                }
                if (scalafterWidth == 0) {
                    scalafterWidth = mIconBitmap.getWidth();
                }
                Rect rect = new Rect();

                whitePaint.getTextBounds(text, 0, text.length(), rect);
                int textWidth = rect.width();
                if (textStart == -1) {
                    textStart = (wholeWidth - textWidth) / 2;
                }
                RectF rectF = new RectF(0, 0, wholeWidth, mIconHeight);
                Paint.FontMetricsInt fontMetrics = whitePaint.getFontMetricsInt();
                baseline = (int) ((rectF.bottom + rectF.top - fontMetrics.bottom - fontMetrics.top) / 2);
                if (tAl == null) {
                    tAl = new ArrayList<>();
                    for (int i = 0; i < text.length(); i++) {
                        tAl.add(new TextAndLocation(text.substring(0, i), textStart + whitePaint.measureText(text.substring(0, i))));
                    }
                }
                canvas.drawText(text, textStart, baseline, whitePaint);
                canvas.drawBitmap(mIconBitmap, 0, 0, iconPaint);
                break;

            case SLIDE:
                if (mIconBitmap != null) {
                    for (int i = 0; i < tAl.size(); i++) {
                        int count = 0;
                        if (bitmapLeft < tAl.get(i).location) {
                            //白色的 不是从0开始
                            Rect whiteRect = new Rect();
                            String cut = text.substring(i, tAl.size());
                            whitePaint.getTextBounds(cut, 0, cut.length(), whiteRect);
                            RectF whiteRectF = new RectF(0, 0, wholeWidth, mIconHeight);
                            Paint.FontMetricsInt BfontMetrics = whitePaint.getFontMetricsInt();
                            int whiteline = (int) ((whiteRectF.bottom + whiteRectF.top - BfontMetrics.bottom - BfontMetrics.top) / 2);
                            canvas.drawText(text.substring(i, tAl.size()), tAl.get(i).location, whiteline, whitePaint);


                            break;
                        } else {
                            // 蓝色的 从0开始
                            Rect blueRect = new Rect();
                            String left = text.substring(0, i + 1);
                            bluePaint.getTextBounds(left, 0, left.length(), blueRect);
                            RectF blueRectF = new RectF(0, 0, wholeWidth, mIconHeight);
                            Paint.FontMetricsInt BfontMetrics = whitePaint.getFontMetricsInt();
                            int bluebaseLine = (int) ((blueRectF.bottom + blueRectF.top - BfontMetrics.bottom - BfontMetrics.top) / 2);
                            canvas.drawText(left, textStart, bluebaseLine, bluePaint);

                        }

                    }
                    canvas.drawBitmap(mIconBitmap, bitmapLeft, 0, iconPaint);


                }
                break;
            case OPEN:
                mGouBitmap = Bitmap.createScaledBitmap(gouBitmap, mGouWidth, mGouHeight, true);
                canvas.drawBitmap(mGouBitmap, xGouStart, yGouStart, gouPaint);
                canvas.drawBitmap(mIconBitmap, bitmapLeft, 0, iconPaint);

                break;
            case CLOSE:
                if (listener != null) {
                    listener.onCancel();
                }
                break;
            case CANCEL:
                canvas.drawText(text, textStart, baseline, whitePaint);
                canvas.drawBitmap(mIconBitmap, 0, 0, iconPaint);
        }
    }

    private boolean areaTag;
    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    float x = event.getX();
                    float y = event.getY();
                    //判断点击焦点在bitmap内
                    if (x <= mIconWidth && 0 < x && 0 < y && y <= mIconWidth) {
                        areaTag = true;
                    } else {
                        areaTag = false;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (areaTag) {
                        drawState = SLIDE;
                        bitmapLeft = (int) (event.getX() - scalafterWidth / 2);
                        if (bitmapLeft >= (wholeWidth - scalafterWidth)) {
                            bitmapLeft = wholeWidth - scalafterWidth;
                        }
                        if (bitmapLeft >= 0 && bitmapLeft <= wholeWidth * 0.68) {
                            drawState = SLIDE;
                            invalidate();
                        } else if (bitmapLeft > wholeWidth * 0.68) {
                            drawState = OPEN;
                            invalidate();
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if (drawState == OPEN) {
                        drawState = CLOSE;
                    } else {
                        drawState = CANCEL;
                    }
                    invalidate();

                    break;
            }
            return true;
        }
    };

    public void setCurrentState() {
        if (drawState != IDEL) {
            drawState = IDEL;
        }
    }


    private onSideStateListener listener;

    public void setOnSileStatListener(onSideStateListener listener) {
        this.listener = listener;
    }

    public interface onSideStateListener {

        void onCancel();
    }


}

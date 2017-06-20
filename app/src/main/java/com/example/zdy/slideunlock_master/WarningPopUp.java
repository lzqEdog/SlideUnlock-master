package com.example.zdy.slideunlock_master;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ZDY on 2017/5/10.
 */

public class WarningPopUp extends PopupWindow {
    private ImageView modeImageView;
    private TextView warningTv;
    private final int P = 1;
    private final int E = 2;
    private final int N = 3;
    private final int NS = 4;
    private Context mContext;
    private SlideUnLockView slideUnLockView;
    public boolean isShowing = false;
    public WarningPopUp(Activity context) {
        mContext = context;
        View rootview = context.getLayoutInflater().inflate(R.layout.warningpopup, null);
        setContentView(rootview);
        slideUnLockView = (SlideUnLockView) rootview.findViewById(R.id.unlockview);
        modeImageView = (ImageView) rootview.findViewById(R.id.warnim);
        warningTv = (TextView) rootview.findViewById(R.id.warntv);
        warningTv.setTextColor(Color.WHITE);
        setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        slideUnLockView.setOnSileStatListener(onSideStateListener);
    }

    private SlideUnLockView.onSideStateListener onSideStateListener = new SlideUnLockView.onSideStateListener() {
        @Override
        public void onCancel() {
            isShowing = false;
            Log.w("lzqpop","disss");
            dismiss();
        }
    };

    @Override
    public void dismiss() {
        super.dismiss();

    }

    public void setWarningRecouse(int id, int num) {
        switch (id) {
            case P:
//                modeImageView.setImageResource(R.mipmap.warning_mode);
                String re = mContext.getResources().getString(R.string.handle_fly);
                warningTv.setMaxLines(4);
                warningTv.setText(re);
                break;
            case E:
//                modeImageView.setImageResource(R.mipmap.warning_mode_e);
//                warningTv.setText();
                break;
            case N:
                warningTv.setMaxLines(8);
                warningTv.setText(mContext.getResources().getString(R.string.warnin_n));
//                modeImageView.setImageResource(R.mipmap.warning_mode_n);
                break;
            case NS:


        }
        slideUnLockView.setCurrentState();

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        isShowing = true;
    }
}

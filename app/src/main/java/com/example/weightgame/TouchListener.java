package com.example.weightgame;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class TouchListener implements View.OnTouchListener {

    private static final String TAG = "TouchListener";
    private static final boolean DEBUG = false;

    // 움직이는 위치대로 드래그하게 할 수 있는 좌표변수
    private float oldXvalue;
    private float oldYvalue;
    private float newXvalue;
    private float newYvalue;
    // 어떤 상자에도 넣지 않았을 때를 위해 필요한 좌표변수
    private float viewX;
    private float viewY;

    private float[] mLeftRectanglePositions = new float[4];
    private float[] mRightRectanglePositions = new float[4];

    private GameActivity gameActivity;

    // 생성자를 정의해서 인자로 activity를 넣어줘야 지정한 activity에서 이미지뷰의 좌표를 받아올 수 있음
    public TouchListener(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.v(TAG, "onTouch(...)");

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.v(TAG, "onTouch(...) Action Down");

                viewX = view.getX();
                viewY = view.getY();
                oldXvalue = view.getX() - motionEvent.getRawX();
                oldYvalue = view.getY() - motionEvent.getRawY();

                Log.d(TAG, "Before X position : "+ oldXvalue + ", Before Y position : " + oldYvalue);
                break;

            case MotionEvent.ACTION_MOVE:
                if(DEBUG) Log.v(TAG, "onTouch(...) Action Move");
                newXvalue = motionEvent.getRawX() + oldXvalue;
                newYvalue = motionEvent.getRawY() + oldYvalue;

                view.animate().x(newXvalue).y(newYvalue).setDuration(0).start();
                if(DEBUG) Log.d(TAG, "After X position : "+ newXvalue + ", After Y position : " + newYvalue);
                break;

            case MotionEvent.ACTION_UP:
                Log.v(TAG, "onTouch(...) Action Up");

                setInRectangle(view);
                break;
        }

        return true;
    }

    // 상자에 넣을 수 있는 함수
    private void setInRectangle(View targetView) {
        mLeftRectanglePositions = gameActivity.getLeftRectanglePositions();
        mRightRectanglePositions = gameActivity.getRightRectanglePositions();

        targetView.animate().x(mLeftRectanglePositions[0]).y(mLeftRectanglePositions[1]).setDuration(0).start();
        if (newXvalue >= mLeftRectanglePositions[0] && newXvalue <= mLeftRectanglePositions[2]
                && newYvalue >= mLeftRectanglePositions[1] && newYvalue <= mLeftRectanglePositions[3]) {
            Log.d(TAG, "setInRectangle(...) Target View in LeftLectangle!");

            targetView.animate().x(mLeftRectanglePositions[0]+10).y(mLeftRectanglePositions[1]+10).setDuration(0).start();
        } else if (newXvalue >= mRightRectanglePositions[0] && newXvalue <= mRightRectanglePositions[2]
                && newYvalue >= mRightRectanglePositions[1] && newYvalue <= mRightRectanglePositions[3]) {
            Log.d(TAG, "setInRectangle(...) Target View in RightLectangle!");

            targetView.animate().x(mRightRectanglePositions[0]+10).y(mRightRectanglePositions[1]+10).setDuration(0).start();
        } else {
            targetView.animate() .x(viewX) .y(viewY) .setDuration(0) .start();

            Log.d(TAG, "setInRectangle(...) Failed into Any Lectangle");
        }
    }
}

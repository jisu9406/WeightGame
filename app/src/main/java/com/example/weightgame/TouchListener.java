package com.example.weightgame;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class TouchListener implements View.OnTouchListener {

    private static final String TAG = "TouchListener";
    private static final boolean DEBUG = false;

    private float oldXvalue;
    private float oldYvalue;
    private float newXvalue;
    private float newYvalue;

    //어떤 상자에도 넣지 않았을 시를 위해 필요한 변수.
    private float viewX;
    private float viewY;


    private float[] mLeftRectanglePositions = new float[4];
    private float[] mRightRectanglePositions = new float[4];

    private GameActivity gameActivity;

    public TouchListener(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d(TAG, "onTouch(...)");

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouch(...) Action Down");

                //어떤 상자에도 넣지 않았을 시를 위해 필요한 변수.
                viewX = view.getX();
                viewY = view.getY();

                oldXvalue = view.getX() - motionEvent.getRawX();
                oldYvalue = view.getY() - motionEvent.getRawY();

                Log.d(TAG, "Event.getX() : "+ motionEvent.getX() + ", Event.getY() : " + motionEvent.getY());
                Log.d(TAG, "View.getX() : "+ view.getX() + ", View.getY() : " + view.getY());

                break;

            case MotionEvent.ACTION_MOVE:
                if(DEBUG) Log.d(TAG, "onTouch(...) Action Move");
                newXvalue = motionEvent.getRawX() + oldXvalue;
                newYvalue = motionEvent.getRawY() + oldYvalue;

                view.animate().x(newXvalue).y(newYvalue).setDuration(0).start();
                if(DEBUG) Log.d(TAG, "After X position : "+ newXvalue + ", After Y position : " + newYvalue);
                break;

            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouch(...) Action Up");

                setInRectangle(view);
                Log.d(TAG, "View.getX() : "+ view.getX() + ", View.getY() : " + view.getY());
                break;
        }

        return true;
    }

    private void setInRectangle(View targetView) {
        Log.v(TAG, "setInRectangle(...)");
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

package com.example.weightgame;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class TouchListener implements View.OnTouchListener {

    private static final String TAG = "TouchListener";

    private float oldXvalue;
    private float oldYvalue;
    private float newXvalue;
    private float newYvalue;
    private float viewXvalue;
    private float viewYvalue;

    private GameActivity gameActivity = new GameActivity();
    private float[] LeftVerseViewPositions = new float[4];

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.v(TAG, "onTouch(...)");

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.v(TAG, "onTouch(...) Action Down");

                oldXvalue = view.getX() - motionEvent.getRawX();
                oldYvalue = view.getY() - motionEvent.getRawY();

                viewXvalue = view.getX();
                viewYvalue = view.getY();
                Log.d(TAG, "Event.getX() : "+ motionEvent.getX() + ", Event.getY() : " + motionEvent.getY());
                Log.d(TAG, "View.getX() : "+ view.getX() + ", View.getY() : " + view.getY());
                Log.d(TAG, "viewXvalue : " + viewXvalue + " viewYvalue : " + viewYvalue);
                break;

            case MotionEvent.ACTION_MOVE:
                Log.v(TAG, "onTouch(...) Action Move");
                view.animate() .x(motionEvent.getRawX() + oldXvalue) .y(motionEvent.getRawY() + oldYvalue) .setDuration(0) .start();

                newXvalue = view.getX();
                newYvalue = view.getY();
                Log.d(TAG, "After X posion : "+ newXvalue + ", After Y posion : " + newYvalue);
                break;

            case MotionEvent.ACTION_UP:
                Log.v(TAG, "onTouch(...) Action Up");

                LeftVerseViewPositions = gameActivity.getLeftVerseViewPositions();
                setInRectangle(view);
                Log.d(TAG, "View.getX() : "+ view.getX() + ", View.getY() : " + view.getY());
                break;
        }

        return true;
    }

    private void setInRectangle(View targetView) {

        Log.v(TAG, "setInRectangle(...)");

        Log.d(TAG, "setInRectangle(...) x: " + LeftVerseViewPositions[0]);
        targetView.animate() .x(LeftVerseViewPositions[0]) .y(LeftVerseViewPositions[1]) .setDuration(0) .start();
//
//        Float[] mRightVerseViewLocations = GameActivity.getInstance().getRightVerseViewLocation();
//
//        if (view.getX() >= mLeftVerseViewLocations[0] && view.getX() <= mLeftVerseViewLocations[2]
//                && view.getY() >= mLeftVerseViewLocations[1] && view.getY() <= mLeftVerseViewLocations[3]) {
//            view.setX(mLeftVerseViewLocations[0] + 10);
//            view.setY(mLeftVerseViewLocations[1] + 10);
//
//            Log.d(TAG, "setInRectangle(...) Target View in LeftLectangle!");
//        } else if (view.getX() >= mRightVerseViewLocations[0] && view.getX() <= mRightVerseViewLocations[2]
//                && view.getY() >= mRightVerseViewLocations[1] && view.getY() <= mRightVerseViewLocations[3]) {
//            view.setX(mRightVerseViewLocations[0] + 10);
//            view.setY(mRightVerseViewLocations[1] + 10);
//
//            Log.d(TAG, "setInRectangle(...) Target View in RightLectangle!");
//        } else {
//            view.animate() .x(viewXvalue) .y(viewYvalue) .setDuration(0) .start();
//
//            Log.d(TAG, "setInRectangle(...) Failed into Any Lectangle");
//        }
    }
}

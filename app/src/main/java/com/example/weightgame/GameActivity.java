package com.example.weightgame;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends Activity {

    private static final String TAG = "GameActivity";

    private static GameActivity instance;

    private GameHandler mGameHandler;
    private List<ImageView> mImageList = new ArrayList<>();
    private ImageView[] mImageViews;
    private int[] mImageWeight = new int[7];
    private Random random = new Random();
    ViewTreeObserver mLeftViewTreeObserver;
    ViewTreeObserver mRightViewTreeObserver;

    private ImageView mFoodImageView1;
    private ImageView mFoodImageView2;
    private ImageView mFoodImageView3;
    private ImageView mFoodImageView4;
    private ImageView mFoodImageView5;
    private ImageView mFoodImageView6;
    private ImageView mFoodImageView7;
    public ImageView mLeftVerseView;
    private ImageView mRightVerseView;

    private TextView mTimerView;
    private TextView mVerseView;

    private float[] mLeftVerseViewPositions = new float[4];
    private float[] mRightVerseViewPositions = new float[4];
    private long startTime = 120000;
    public static final int MESSAGE_TIMER_START = 0;
    public static final int MESSAGE_TIMER_PLAY = 1;
    public static final int MESSAGE_TIMER_END = 2;
    public static final int MESSAGE_SET_POSITION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.v(TAG, "onCreate(...)");

        mFoodImageView1 = findViewById(R.id.food_one);
        mFoodImageView2 = findViewById(R.id.food_two);
        mFoodImageView3 = findViewById(R.id.food_three);
        mFoodImageView4 = findViewById(R.id.food_four);
        mFoodImageView5 = findViewById(R.id.food_five);
        mFoodImageView6 = findViewById(R.id.food_six);
        mFoodImageView7 = findViewById(R.id.food_seven);

        mTimerView = findViewById(R.id.timer_view);

        mLeftVerseView = findViewById(R.id.verse_of_leftview);
        mLeftViewTreeObserver = mLeftVerseView.getViewTreeObserver();
        mRightVerseView = findViewById(R.id.verse_of_rightview);
        mRightViewTreeObserver = mRightVerseView.getViewTreeObserver();
        mVerseView = findViewById(R.id.verse_view);

        mGameHandler = new GameHandler();

        addImageToList();
        setTouchAndWeight();

        mGameHandler.sendEmptyMessageDelayed(MESSAGE_TIMER_START, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume(...)");

        mGameHandler.sendEmptyMessage(MESSAGE_SET_POSITION);
    }

    public static synchronized GameActivity getInstance() {
        if(instance == null){
            instance = new GameActivity();
        }
        return instance;
    }

    private void addImageToList() {
        Log.v(TAG, "addImageToList(...)");

        mImageList.add(mFoodImageView1);
        mImageList.add(mFoodImageView2);
        mImageList.add(mFoodImageView3);
        mImageList.add(mFoodImageView4);
        mImageList.add(mFoodImageView5);
        mImageList.add(mFoodImageView6);
        mImageList.add(mFoodImageView7);

        mImageViews = mImageList.toArray(new ImageView[mImageList.size()]);
        Log.d(TAG, "addImageToList(...) mImageViews.length : " + mImageViews.length);
    }

    private void setTouchAndWeight() {
        Log.v(TAG, "setTouchAndWeight(...)");

        for(int i = 0; i<mImageList.size(); i++) {
            mImageViews[i].setOnTouchListener(new TouchListener());
            mImageWeight[i] = random.nextInt(7);

            for(int j = 0; j<i; j++) {
                if(mImageWeight[i] == mImageWeight[j]){
                    i--;
                }
            }
            Log.d(TAG, "setTouchAndWeight(...) weight of image" + i +": " + mImageWeight[i]);
        }
    }

    private void setTimerText() {
        Log.v(TAG, "setTimerText(...)");

        if(startTime == 0) {
            mTimerView.setText("GameOver");
            mGameHandler.sendEmptyMessage(MESSAGE_TIMER_END);
        } else {

            mTimerView.setText(setTimeFormat(startTime));
        }
    }

    private String setTimeFormat(long targetTime) {
        Log.v(TAG, "setTimeFormat(...)");

        String mOutputText = "00:00";
        long seconds = targetTime / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        minutes %= 60;

        String sec = String.valueOf(seconds);
        String min = String.valueOf(minutes);

        if (seconds < 10)
            sec = "0" + seconds;
        if (minutes < 10)
            min= "0" + minutes;

        mOutputText = min + ":" + sec;
        return mOutputText;
    }

    private void setLeftVerseViewPositions(float x, float y, float xpluswidth, float yplusheight) {

        mLeftVerseViewPositions[0] =  x;
        mLeftVerseViewPositions[1] = y;
        mLeftVerseViewPositions[2]= xpluswidth;
        mLeftVerseViewPositions[3] = yplusheight;

        Log.d(TAG, "setLeftVerseViewPositions(...) x : " + mLeftVerseViewPositions[0] + ", y : " + mLeftVerseViewPositions[1]
                + " , width+x : " + mLeftVerseViewPositions[2] + " , height+y : " + mLeftVerseViewPositions[3]);
    }

    public void setRightVerseViewPositions(float x, float y, float xpluswidth, float yplusheight) {

        mRightVerseViewPositions[0] = x;
        mRightVerseViewPositions[1] = y;
        mRightVerseViewPositions[2] = xpluswidth;
        mRightVerseViewPositions[3] = yplusheight;

        Log.d(TAG, "setRightVerseViewPositions(...) x : " + x + ", y : " + y
                + " , width+x : " + xpluswidth + " , height+y : " + yplusheight);
    }

    public float[] getLeftVerseViewPositions() {
        Log.d(TAG, "getLeftVerseViewPositions(...) x : " + mLeftVerseViewPositions[0] + ", y : " + mLeftVerseViewPositions[1]
                + " , width+x : " + mLeftVerseViewPositions[2] + " , height+y : " + mLeftVerseViewPositions[3]);
        return mLeftVerseViewPositions;
    }
    public float[] getRightVerseViewPositions() {
        Log.d(TAG, "getRightVerseViewPositions(...) x : " + mRightVerseViewPositions[0] + ", y : " + mRightVerseViewPositions[1]
                + " , width+x : " + mRightVerseViewPositions[2] + " , height+y : " + mRightVerseViewPositions[3]);

        return mRightVerseViewPositions;
    }

    public class GameHandler extends Handler {

        private static final String TAG = "GameHandler";

        @Override
        public void handleMessage(Message message) {
            Log.v(TAG, "handleMessage(...)");

            switch (message.what) {
                case MESSAGE_TIMER_START:
                    Log.d(TAG, "handleMessage(...) Timer Started");

                    this.removeMessages(MESSAGE_TIMER_PLAY);
                    this.sendEmptyMessage(MESSAGE_TIMER_PLAY);
                    break;
                case MESSAGE_TIMER_PLAY:
                    //Log.d(TAG, "handleMessage(...) Timer Playing : " + startTime);

                    setTimerText();
                    startTime-=1000;
                    this.sendEmptyMessageDelayed(MESSAGE_TIMER_PLAY, 1000);
                    break;
                case MESSAGE_TIMER_END:
                    Log.d(TAG, "handleMessage(...) Timer End");

                    this.removeMessages(MESSAGE_TIMER_PLAY);
                    break;
                case MESSAGE_SET_POSITION:
                    Log.d(TAG, "handleMessage(...) Get ImageView Positions");

                    setLeftVerseViewPositions(mLeftVerseView.getX(), mLeftVerseView.getY(),
                            mLeftVerseView.getX()+mLeftVerseView.getWidth(), mLeftVerseView.getY()+mLeftVerseView.getHeight());
                    setRightVerseViewPositions(mRightVerseView.getX(), mRightVerseView.getY(),
                            mRightVerseView.getX()+mRightVerseView.getWidth(), mRightVerseView.getY()+mRightVerseView.getHeight());
            }
        }
    }
}

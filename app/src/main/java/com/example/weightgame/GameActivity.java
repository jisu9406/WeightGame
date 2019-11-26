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
    private static final boolean DEBUG = false;
    private static GameActivity instance;

    private GameHandler mGameHandler;
    private List<ImageView> mImageList = new ArrayList<>();
    private ImageView[] mImageViews;
    private int[] mImageWeight = new int[7];
    private Random random = new Random();

    private ImageView mFoodImageView1;
    private ImageView mFoodImageView2;
    private ImageView mFoodImageView3;
    private ImageView mFoodImageView4;
    private ImageView mFoodImageView5;
    private ImageView mFoodImageView6;
    private ImageView mFoodImageView7;
    private ImageView mLeftRectangleView;
    private ImageView mRightRectangleView;

    private TextView mTimerView;
    private TextView mVerseView;

    public float[] mLeftRectanglePositions = new float[4];
    public float[] mRightRectanglePositions = new float[4];
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

        mLeftRectangleView = findViewById(R.id.verse_of_leftview);
        mRightRectangleView = findViewById(R.id.verse_of_rightview);
        mVerseView = findViewById(R.id.verse_view);

        mGameHandler = new GameHandler();

        addImageToList();
        setTouchAndWeight();

        mGameHandler.sendEmptyMessageDelayed(MESSAGE_TIMER_START, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume(...)");
        mGameHandler.sendEmptyMessage(MESSAGE_SET_POSITION);
    }

    //싱글톤
    public static synchronized GameActivity getInstance() {
        if(instance == null){
            instance = new GameActivity();
        }
        return instance;
    }

    // 이미지뷰를 쉽게 쓰기 위해 리스트에 추가하는 함수
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

    // 이미지뷰 각각의 무게 랜덤으로 설정
    private void setTouchAndWeight() {
        Log.v(TAG, "setTouchAndWeight(...)");

        for(int i = 0; i<mImageList.size(); i++) {
            mImageViews[i].setOnTouchListener(new TouchListener(this));
            mImageWeight[i] = random.nextInt(7);

            for(int j = 0; j<i; j++) {
                if(mImageWeight[i] == mImageWeight[j]){
                    i--;
                }
            }
            Log.d(TAG, "setTouchAndWeight(...) weight of image" + i +": " + mImageWeight[i]);
        }
    }

    // 타이머 갱신 함수
    private void setTimerText() {
        if(DEBUG) Log.v(TAG, "setTimerText(...)");

        startTime-=1000;

        if(startTime == 0) {
            mTimerView.setText("GameOver");
            mGameHandler.sendEmptyMessage(MESSAGE_TIMER_END);
        } else {
            mTimerView.setText(setTimerFormat(startTime));
        }
    }

    // 타이머 mm:ss 형식으로 나타낼 수 있는 함수
    private String setTimerFormat(long targetTime) {
        if(DEBUG) Log.v(TAG, "setTimeFormat(...)");

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

    // 왼쪽 상자 좌표 얻어오는 함수
    private void setLeftRectanglePositions(float x, float y, float xpluswidth, float yplusheight) {
        mLeftRectanglePositions[0] =  x;
        mLeftRectanglePositions[1] = y;
        mLeftRectanglePositions[2]= xpluswidth;
        mLeftRectanglePositions[3] = yplusheight;

        Log.d(TAG, "setLeftVerseViewPositions(...) x : " + mLeftRectanglePositions[0] + ", y : " + mLeftRectanglePositions[1]
                + " , width+x : " + mLeftRectanglePositions[2] + " , height+y : " + mLeftRectanglePositions[3]);
    }

    // 오른쪽 상자 좌표 얻어오는 함수
    private void setRightRectanglePositions(float x, float y, float xpluswidth, float yplusheight) {
        mRightRectanglePositions[0] = x;
        mRightRectanglePositions[1] = y;
        mRightRectanglePositions[2] = xpluswidth;
        mRightRectanglePositions[3] = yplusheight;

        Log.d(TAG, "setRightVerseViewPositions(...) x : " + x + ", y : " + y
                + " , width+x : " + xpluswidth + " , height+y : " + yplusheight);
    }

    // 다른 클래스에서 왼쪽 상자 좌표를 사용할 수 있게 보내는 함수
    public float[] getLeftRectanglePositions() {
        Log.d(TAG, "getLeftVerseViewPositions(...) x : " + mLeftRectanglePositions[0] + ", y : " + mLeftRectanglePositions[1]
                + " , width+x : " + mLeftRectanglePositions[2] + " , height+y : " + mLeftRectanglePositions[3]);
        return mLeftRectanglePositions;
    }

    // 다른 클래스에서 오른쪽 상자 좌표를 사용할 수 있게 보내는 함수
    public float[] getRightRectanglePositions() {
        Log.d(TAG, "getRightVerseViewPositions(...) x : " + mRightRectanglePositions[0] + ", y : " + mRightRectanglePositions[1]
                + " , width+x : " + mRightRectanglePositions[2] + " , height+y : " + mRightRectanglePositions[3]);
        return mRightRectanglePositions;
    }

    //게임 기능 다루는 핸들러
    private class GameHandler extends Handler {

        private static final String TAG = "GameHandler";

        @Override
        public void handleMessage(Message message) {

            switch (message.what) {
                case MESSAGE_TIMER_START:
                    Log.d(TAG, "handleMessage(...) Timer Started");

                    this.removeMessages(MESSAGE_TIMER_PLAY);
                    this.sendEmptyMessage(MESSAGE_TIMER_PLAY);
                    break;
                case MESSAGE_TIMER_PLAY:
                    if(DEBUG) Log.d(TAG, "handleMessage(...) Timer Playing : " + startTime);

                    setTimerText();
                    this.sendEmptyMessageDelayed(MESSAGE_TIMER_PLAY, 1000);
                    break;
                case MESSAGE_TIMER_END:
                    Log.d(TAG, "handleMessage(...) Timer End");

                    this.removeMessages(MESSAGE_TIMER_PLAY);
                    break;
                case MESSAGE_SET_POSITION:
                    Log.d(TAG, "handleMessage(...) Get ImageView Positions");

                    setLeftRectanglePositions(mLeftRectangleView.getX(), mLeftRectangleView.getY(),
                            mLeftRectangleView.getX()+mLeftRectangleView.getWidth(), mLeftRectangleView.getY()+mLeftRectangleView.getHeight());
                    setRightRectanglePositions(mRightRectangleView.getX(), mRightRectangleView.getY(),
                            mRightRectangleView.getX()+mRightRectangleView.getWidth(), mRightRectangleView.getY()+mRightRectangleView.getHeight());
                    break;
            }
        }
    }
}

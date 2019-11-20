package com.example.weightgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private Button mStartButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartButton = findViewById(R.id.gamestart_button);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Game Start Button Clicked");
                LoadGameActivity();
            }
        });
    }

    private void LoadGameActivity() {
        Log.v(TAG, "LoadGameActivity(...)");

        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}

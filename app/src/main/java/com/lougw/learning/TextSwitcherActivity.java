package com.lougw.learning;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class TextSwitcherActivity extends AppCompatActivity {
    private TextSwitcher mSwitcher;
    private int mCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_switcher);
        mSwitcher = (TextSwitcher) findViewById(R.id.switcher);

        // Set the factory used to create TextViews to switch between.
        mSwitcher.setFactory(mFactory);

        /*
         * Set the in and out animations. Using the fade_in/out animations
         * provided by the framework.
         */
        Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);


        /*
         * Setup the 'next' button. The counter is incremented when clicked and
         * the new value is displayed in the TextSwitcher. The change of text is
         * automatically animated using the in/out animations set above.
         */
        Button nextButton = (Button) findViewById(R.id.button);
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCounter++;

                mSwitcher.setText(String.valueOf(mCounter));

            }
        });

        // Set the initial text without an animation
        mSwitcher.setCurrentText(String.valueOf(mCounter));

    }
    private ViewSwitcher.ViewFactory mFactory = new ViewSwitcher.ViewFactory() {

        @Override
        public View makeView() {

            // Create a new TextView
            TextView t = new TextView(TextSwitcherActivity.this);
            t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            t.setTextAppearance(TextSwitcherActivity.this, android.R.style.TextAppearance_Large);
            return t;
        }
    };

}

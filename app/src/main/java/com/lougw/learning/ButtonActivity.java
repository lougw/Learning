package com.lougw.learning;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

public class ButtonActivity extends AppCompatActivity {
    View tv;
    View tv2;
    TransitionSet transitionSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        ViewStub  viewStub=findViewById(R.id.view_stub);
        viewStub.inflate();

        final FrameLayout root_c = findViewById(R.id.root_c);
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv_2);
        Slide slide = new Slide(Gravity.LEFT);
        slide.addTarget(tv);
//                slide.excludeTarget(tv, true);
        slide.setDuration(400);
        Fade fade = new Fade();
        fade.addTarget(tv);
//                fade.excludeTarget(tv, true);
        fade.setDuration(400);
        transitionSet= new TransitionSet()
                .addTransition(slide)
                .addTransition(fade);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LgwTag","tvtvtvtvtvtv");
            }
        });
        TransitionManager.beginDelayedTransition(root_c, transitionSet);
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv2.getVisibility() == View.VISIBLE) {
                    tv2.setVisibility(View.GONE);
                } else {
                    tv2.setVisibility(View.VISIBLE);
                }
            }
        });
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Slide slide = new Slide(Gravity.LEFT);
//                slide.addTarget(tv);
////                slide.excludeTarget(tv, true);
//                slide.setDuration(400);
//                Fade fade = new Fade();
//                fade.addTarget(tv);
////                fade.excludeTarget(tv, true);
//                fade.setDuration(400);
//                TransitionSet transitionSet = new TransitionSet()
//                        .addTransition(slide)
//                        .addTransition(fade);
//                TransitionManager.beginDelayedTransition(root_c, transitionSet);

                if (tv.getVisibility() == View.VISIBLE) {
                    tv.setVisibility(View.GONE);
                    tv2.setVisibility(View.GONE);
                } else {
                    tv.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}

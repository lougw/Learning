package com.lougw.learning;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnimalActivity extends AppCompatActivity {
    @BindView(R.id.indicator)
    View indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
        ButterKnife.bind(this);

    }
    int initvalue=0;
    int initvalue2=0;
    @OnClick({R.id.btn_animal, R.id.btn_animal_clear})
    public void Click(View v) {

        ValueAnimator stickAnimation = ValueAnimator.ofInt(0, 200,0);
        stickAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animFraction = ((int) animation.getAnimatedValue());
                int value=animFraction - initvalue;
                Log.d("LgwTag", "value : " + (value)+" "+animation.isRunning());
                initvalue=animFraction;
                if(value!=200){

                }


            }
        });
        stickAnimation.addListener(new Animator.AnimatorListener(){

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("LgwTag", "value : " +" "+animation.isRunning());

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d("LgwTag", "value : "+" "+animation.isRunning());

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        stickAnimation.setDuration(500);
//        ValueAnimator stickAnimation2 = ValueAnimator.ofInt(200, 0);
//        stickAnimation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int animFraction = ((int) animation.getAnimatedValue());
//                Log.d("LgwTag", "value 222 : " + (animFraction - initvalue2));
//                initvalue2=animFraction;
//
//            }
//        });
//        stickAnimation2.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.play(stickAnimation);
        set.start();




//
//        if (v.getId() == R.id.btn_animal) {
//            indicator.setPivotX(100f);
//            AnimatorSet set = new AnimatorSet();
//            ObjectAnimator animator = ObjectAnimator.ofFloat(indicator, "scaleX", 1.0f, 2.0f, 1.0f);
//            ObjectAnimator animator2 = ObjectAnimator.ofFloat(indicator, "translationX", 200);
//            set.playTogether(animator, animator2);
//            set.setDuration(400);
//            set.start();
//        } else if (v.getId() == R.id.btn_animal_clear) {
//            indicator.setPivotX(0f);
//            AnimatorSet set = new AnimatorSet();
//            ObjectAnimator animator = ObjectAnimator.ofFloat(indicator, "scaleX", 1.0f, 2.0f, 1.0f);
//            ObjectAnimator animator2 = ObjectAnimator.ofFloat(indicator, "translationX", 0);
//            set.playTogether(animator, animator2);
//            set.setDuration(400);
//            set.start();
//        }
    }

}

package com.lougw.learning;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @OnClick({R.id.btn_animal, R.id.btn_animal_clear})
    public void Click(View v) {
        if (v.getId() == R.id.btn_animal) {
            indicator.setPivotX(100f);
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator animator = ObjectAnimator.ofFloat(indicator, "scaleX", 1.0f, 2.0f, 1.0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(indicator, "translationX", 200);
            set.playTogether(animator, animator2);
            set.setDuration(400);
            set.start();
        } else if (v.getId() == R.id.btn_animal_clear) {
            indicator.setPivotX(0f);
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator animator = ObjectAnimator.ofFloat(indicator, "scaleX", 1.0f, 2.0f, 1.0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(indicator, "translationX", 0);
            set.playTogether(animator, animator2);
            set.setDuration(400);
            set.start();
        }
    }

}

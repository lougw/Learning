package com.lougw.learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Random;


public class CalculateActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioGroup questions_group;
    private RadioButton tag_add;
    private RadioButton tag_del;
    private Button questions;
    private AppCompatTextView big;
    private AppCompatTextView small;
    private AppCompatTextView arithmetic;
    private AppCompatTextView result_tv;
    private AppCompatTextView result_tag;

    private Button btn0;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button confirm;
    private Button cancel;
    private int firstNum;
    private int endNum;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        questions_group = findViewById(R.id.questions_group);
        tag_add = findViewById(R.id.tag_add);
        tag_del = findViewById(R.id.tag_del);
        questions = findViewById(R.id.questions);
        arithmetic = findViewById(R.id.arithmetic);
        result_tv = findViewById(R.id.result_tv);
        result_tag = findViewById(R.id.result_tag);
        big = findViewById(R.id.big);
        small = findViewById(R.id.small);
        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        questions.setOnClickListener(this);
        questions.setOnClickListener(this);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);


        questions_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tag_add:
                        tag_del.setChecked(false);
                        arithmetic.setText("+");
                        break;
                    case R.id.tag_del:
                        tag_add.setChecked(false);
                        arithmetic.setText("-");
                        break;
                }
                firstNum = 0;
                endNum = 0;
                big.setText("");
                small.setText("");
                result_tv.setText("");
                result_tag.setText("");

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.questions:
                onQuestions();
                break;
            case R.id.btn0:
                onCalculate(0);
                break;
            case R.id.btn1:
                onCalculate(1);
                break;
            case R.id.btn2:
                onCalculate(2);
                break;
            case R.id.btn3:
                onCalculate(3);
                break;
            case R.id.btn4:
                onCalculate(4);
                break;
            case R.id.btn5:
                onCalculate(5);
                break;
            case R.id.btn6:
                onCalculate(6);
                break;
            case R.id.btn7:
                onCalculate(7);
                break;
            case R.id.btn8:
                onCalculate(8);
                break;
            case R.id.btn9:
                onCalculate(9);
                break;
            case R.id.confirm:
                if (tag_add.isChecked()) {
                    if (firstNum + endNum == result) {
                        Toast.makeText(getApplicationContext(), "恭喜你答对了", Toast.LENGTH_SHORT).show();
                        result_tag.setText("✔️");

                    } else {
                        result_tag.setText("❌");
                        Toast.makeText(getApplicationContext(), "你个笨蛋答错了", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (firstNum - endNum == result) {
                        result_tag.setText("✔️");
                        Toast.makeText(getApplicationContext(), "恭喜你答对了", Toast.LENGTH_SHORT).show();
                    } else {
                        result_tag.setText("❌");

                        Toast.makeText(getApplicationContext(), "你个笨蛋答错了", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
            case R.id.cancel:
                result = 0;
                result_tv.setText("");
                result_tag.setText("");
                break;
        }
    }


    private void onQuestions() {
        result_tv.setText("");
        result_tag.setText("");
        result = 0;
        int first = new Random().nextInt(20);
        int second = new Random().nextInt(20);
        if (tag_add.isChecked()) {
            firstNum = first;
            endNum = second;
        } else {
            if (first >= second) {
                firstNum = first;
                endNum = second;
            } else {
                firstNum = second;
                endNum = first;
            }
        }
        big.setText("" + firstNum);
        small.setText("" + endNum);
    }


    private void onCalculate(int num) {
        if (firstNum == 0 || endNum == 0) {
            Toast.makeText(getApplicationContext(), "请先出题!", Toast.LENGTH_SHORT).show();
            return;
        }
        int tmp = 0;
        if (result > 0) {
            tmp = result * 10 + num;
        } else {
            tmp = num;
        }
        if (tmp > 100) {
            Toast.makeText(getApplicationContext(), "答题超出范围!", Toast.LENGTH_SHORT).show();
            return;
        }
        result = tmp;
        result_tv.setText("" + result);
    }
}
package com.lougw.learning;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lougw.learning.utils.UIUtils;
import com.lougw.learning.utils.adapter.BaseRecyclerAdapter;
import com.lougw.learning.utils.adapter.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Random;


public class CalculateActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "CalculateActivity";

    private RadioGroup algorithm_rg;
    private RadioButton plus_rb;
    private RadioButton minus_rb;
    private RadioButton multiply_rb;
    private RadioButton divide_rb;
    private Button exam;
    private Button confirm;
    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter mBaseRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private SeekBar range_sb;
    private TextView range;
    private SeekBar num_sb;
    private TextView num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        algorithm_rg = findViewById(R.id.algorithm_rg);
        algorithm_rg.setOnCheckedChangeListener(this);
        exam = findViewById(R.id.exam);
        range_sb = findViewById(R.id.range_sb);
        plus_rb = findViewById(R.id.plus_rb);
        minus_rb = findViewById(R.id.minus_rb);
        multiply_rb = findViewById(R.id.multiply_rb);
        divide_rb = findViewById(R.id.divide_rb);
        range = findViewById(R.id.range);
        num = findViewById(R.id.num);
        num_sb = findViewById(R.id.num_sb);
        range_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                range.setText("数字范围(" + setRangeSeekBar(seekBar) + ")");

            }
        });
        num_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                num.setText("出题个数(" + setRangeSeekBar(seekBar) + ")");

            }
        });
        confirm = findViewById(R.id.confirm);
        exam.setOnClickListener(this);
        confirm.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mBaseRecyclerAdapter = new BaseRecyclerAdapter(getApplicationContext()) {
            @Override
            public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new CalculateViewHolder(mLayoutInflater.inflate(R.layout.item_calculate_view, parent, false), getApplicationContext());
            }
        };
        mRecyclerView.setAdapter(mBaseRecyclerAdapter);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.exam) {
            doExam();
        } else if (v.getId() == R.id.confirm) {
            confirm();
        }

    }

    private void doExam() {
        ArrayList<CalculateBean> data = new ArrayList();
        int progress = range_sb.getProgress();
        int num = num_sb.getProgress();
        for (int i = 0; i < num; i++) {
            CalculateBean bean = new CalculateBean();
            if (plus_rb.isChecked()) {
                bean.setAlgorithm(Algorithm.PLUS);
                bean.setFirstNum(new Random().nextInt(progress));
                bean.setSecondNum(new Random().nextInt(progress));

            } else if (minus_rb.isChecked()) {
                bean.setAlgorithm(Algorithm.MINUS);
                int first = new Random().nextInt(progress);
                int second = new Random().nextInt(progress);
                if (first < second) {
                    int temp = first;
                    first = second;
                    second = temp;
                }
                bean.setFirstNum(first);
                bean.setSecondNum(second);

            } else if (multiply_rb.isChecked()) {
                bean.setAlgorithm(Algorithm.MULTIPLY);
                bean.setFirstNum(new Random().nextInt(progress));
                bean.setSecondNum(new Random().nextInt(progress));
            } else if (divide_rb.isChecked()) {
                bean.setAlgorithm(Algorithm.DIVIDE);
                int first = new Random().nextInt(progress);
                int second = new Random().nextInt(progress);
                if (first < second) {
                    int temp = first;
                    first = second;
                    second = temp;
                }
                bean.setFirstNum(first);
                bean.setSecondNum(second);
            }
            data.add(bean);
        }
        mBaseRecyclerAdapter.replaceAll(data);
    }

    static class CalculateViewHolder extends BaseRecyclerViewHolder<CalculateBean> {
        AppCompatTextView first_num;
        AppCompatTextView arithmetic;
        AppCompatTextView second_num;
        AppCompatEditText result_tv;
        AppCompatTextView result_state;

        public CalculateViewHolder(View view, Context mContext) {
            super(view, mContext);
            result_state = view.findViewById(R.id.result_state);
            first_num = view.findViewById(R.id.first_num);
            arithmetic = view.findViewById(R.id.arithmetic);
            second_num = view.findViewById(R.id.second_num);
            result_tv = view.findViewById(R.id.result_tv);
            result_tv.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = s.toString();
                    if (TextUtils.isEmpty(str)) {
                        CalculateBean bean = getData();
                        if (bean != null) {
                            bean.setResult(-999);
                        }
                    } else {
                        CalculateBean bean = getData();
                        if (bean != null) {
                            if (str.contains(".")) {
                                bean.setResult(Integer.parseInt(str));
                            } else {
                                bean.setResult(Integer.parseInt(str));
                            }
                            Log.d(TAG, "refreshView: " + bean.getResult());
                        }
                    }

                }
            });
        }

        @Override
        public void refreshView(int position) {
            itemView.setBackgroundColor(getAdapterPosition() / 2 == 0
                    ? UIUtils.getColor(R.color.transparent_per_5_white)
                    : UIUtils.getColor(R.color.transparent_per_10_white));
            CalculateBean bean = getData();
            if (bean.getAlgorithm() == Algorithm.PLUS) {
                arithmetic.setText("+");
            } else if (bean.getAlgorithm() == Algorithm.MINUS) {
                arithmetic.setText("-");
            } else if (bean.getAlgorithm() == Algorithm.MULTIPLY) {
                arithmetic.setText("*");
            } else if (bean.getAlgorithm() == Algorithm.DIVIDE) {
                arithmetic.setText("/");
            }
            first_num.setText(String.valueOf(bean.getFirstNum()));
            second_num.setText(String.valueOf(bean.getSecondNum()));
            result_tv.setText((bean.getResult() == -999 ? "" : bean.getResult()) + "");
            if (bean.getState() == AnswerState.ERROR_ANSWERED) {
                result_state.setVisibility(View.VISIBLE);
            } else {
                result_state.setVisibility(View.INVISIBLE);
            }
            Log.d(TAG, "refreshView: " + bean.getResult());
        }

        @Override
        public void recycle() {

        }
    }

    private int setRangeSeekBar(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        int tempProgress = progress;
        if (progress < 10) {
            tempProgress = 10;
        } else if (progress >= 10 && progress < 15) {
            tempProgress = 10;
        } else if (progress >= 15 && progress < 25) {
            tempProgress = 20;
        } else if (progress >= 25 && progress < 35) {
            tempProgress = 30;
        } else if (progress >= 35 && progress < 45) {
            tempProgress = 40;
        } else if (progress >= 45 && progress < 55) {
            tempProgress = 50;
        } else if (progress >= 55 && progress < 65) {
            tempProgress = 60;
        } else if (progress >= 65 && progress < 75) {
            tempProgress = 70;
        } else if (progress >= 75 && progress < 85) {
            tempProgress = 80;
        } else if (progress >= 85 && progress < 95) {
            tempProgress = 90;
        } else if (progress >= 95 && progress <= 100) {
            tempProgress = 100;
        }
        seekBar.setProgress(tempProgress);
        return tempProgress;
    }


    private void confirm() {
        int firstErrorPosition = 0;
        boolean hasError = false;
        ArrayList<CalculateBean> data = (ArrayList<CalculateBean>) mBaseRecyclerAdapter.getData();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            CalculateBean bean = data.get(i);
            if (plus_rb.isChecked()) {
                if (bean.getFirstNum() + bean.getSecondNum() == bean.getResult()) {
                    bean.setState(AnswerState.SUCCESS_ANSWERED);
                } else {
                    bean.setState(AnswerState.ERROR_ANSWERED);
                    if (!hasError) {
                        firstErrorPosition = i;
                    }
                    hasError = true;
                }
            } else if (minus_rb.isChecked()) {
                if (bean.getFirstNum() - bean.getSecondNum() == bean.getResult()) {
                    bean.setState(AnswerState.SUCCESS_ANSWERED);
                } else {
                    bean.setState(AnswerState.ERROR_ANSWERED);
                    if (!hasError) {
                        firstErrorPosition = i;
                    }
                    hasError = true;
                }

            } else if (multiply_rb.isChecked()) {
                if (bean.getFirstNum() * bean.getSecondNum() == bean.getResult()) {
                    bean.setState(AnswerState.SUCCESS_ANSWERED);
                } else {
                    bean.setState(AnswerState.ERROR_ANSWERED);
                    if (!hasError) {
                        firstErrorPosition = i;
                    }
                    hasError = true;
                }
            } else if (divide_rb.isChecked()) {
                if (bean.getFirstNum() / bean.getSecondNum() == bean.getResult()) {
                    bean.setState(AnswerState.SUCCESS_ANSWERED);
                } else {
                    bean.setState(AnswerState.ERROR_ANSWERED);
                    if (!hasError) {
                        firstErrorPosition = i;
                    }
                    hasError = true;
                }
            }

        }
        if (hasError) {
            mRecyclerView.scrollToPosition(firstErrorPosition);
            Toast.makeText(getApplicationContext(),"答题有错误，请修改后再提交",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"恭喜你答了100分",Toast.LENGTH_LONG).show();
        }
        mBaseRecyclerAdapter.notifyItemRangeChanged(0, size);
    }

}
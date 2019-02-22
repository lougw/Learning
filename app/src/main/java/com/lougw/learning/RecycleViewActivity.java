package com.lougw.learning;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecycleViewActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.head)
    ImageView head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        ButterKnife.bind(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, null));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((CViewHolder) holder).textViewCompat.setText("" + position);
            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });

//        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int state) {
//                super.onScrollStateChanged(recyclerView, state);
//                switch (state) {
//                    case RecyclerView.SCROLL_STATE_IDLE:
//                        //滑动停止
////                        recycler_view.offsetChildrenVertical(-20);
//                        break;
//                    case RecyclerView.SCROLL_STATE_DRAGGING:
//                    case RecyclerView.SCROLL_STATE_SETTLING:
//                        //正在滚动
//
//                        break;
//
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                float TranslationY = head.getTranslationY();
//                float ScrollY = TranslationY - dy;
//                if (ScrollY >= 0) {
//                    ScrollY = 0;
//                }
//                if (ScrollY <= -200) {
//                    ScrollY = -200;
//                }
//                Log.d("LgwTag", "  ScrollY : " + ScrollY);
//
//                head.setTranslationY(ScrollY);
//
//            }
//        });

        linearLayoutManager.scrollToPositionWithOffset(0, 0);

    }

    private float mScrolledX = 0;

    public class CViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textViewCompat;

        public CViewHolder(View itemView) {
            super(itemView);
            textViewCompat = (AppCompatTextView) itemView.findViewById(R.id.text);
        }
    }


}

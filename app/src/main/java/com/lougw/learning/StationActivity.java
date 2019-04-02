package com.lougw.learning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    ArrayList<Integer> datas = new ArrayList<>();
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        ButterKnife.bind(this);
        for (int i = 0; i < 100; i++) {
            datas.add(i);
        }
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new RecyclerView.Adapter() {
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
                return datas.size();
            }
        };
        recycler_view.setAdapter(mAdapter);

    }


    public static class CViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textViewCompat;

        public CViewHolder(View itemView) {
            super(itemView);
            textViewCompat = (AppCompatTextView) itemView.findViewById(R.id.text);
        }
    }

}

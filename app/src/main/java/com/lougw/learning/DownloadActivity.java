package com.lougw.learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lougw.downloader.DownloadInfo;
import com.lougw.downloader.DownloadObserver;
import com.lougw.downloader.DownloadRequest;
import com.lougw.downloader.Downloader;
import com.lougw.downloader.utils.DownloadUtils;
import com.lougw.learning.utils.adapter.BaseRecyclerAdapter;
import com.lougw.learning.utils.adapter.BaseRecyclerViewHolder;

import java.util.ArrayList;

public class DownloadActivity extends AppCompatActivity implements BaseRecyclerAdapter.OnItemClickListener<DownloadInfo>, DownloadObserver {
    RecyclerView mRecyclerView;
    BaseRecyclerAdapter mBaseAdapter;
    ArrayList<DownloadInfo> urls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mBaseAdapter = new BaseRecyclerAdapter(getApplicationContext(), this) {

            @Override
            public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                return new DownloadHolder(mLayoutInflater.inflate(R.layout.item_download_view, parent, false), mContext);
            }
        };
        mBaseAdapter.setNormalItem(false);
        mRecyclerView.setAdapter(mBaseAdapter);
        DownloadInfo info = new DownloadInfo.Builder("https://github.com/Bilibili/ijkplayer/archive/k0.8.8.zip", "k0.8.8.zip").build();
        urls.add(info);
        mBaseAdapter.replaceAll(urls);
        Downloader.getInstance().registerObserver(this);
    }

    @Override
    public void onItemClick(View view, DownloadInfo o, int position) {

    }

    @Override
    public void onViewClick(RecyclerView.ViewHolder viewHolder, View view, DownloadInfo o, int position) {
        if (view.getId() == R.id.download) {
            Downloader.getInstance().downLoad(o);
        } else {
            Downloader.getInstance().pause(o);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Downloader.getInstance().unRegisterObserver(this);
    }

    @Override
    public void onDownloadStateChanged(DownloadRequest request) {
        if (request != null) {
            Log.d("DownloadActivity", "speed : " + request.getSpeed() + " progress : " + request.getProgress());
        }


    }
}

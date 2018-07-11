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
        DownloadInfo info = new DownloadInfo.Builder("http://dl.cm.ksmobile.com/static/res/38/95/libmodel.zip", "libmodel.zip").build();
        DownloadInfo info2 = new DownloadInfo.Builder("http://daohang-test.s3.amazonaws.com/live/emoji/2017-08-16_21:11:15/cat.zip", "cat.zip").build();
        DownloadInfo info3 = new DownloadInfo.Builder("http://s.live.ksmobile.net/live/emoji/2016-12-20_18:10:02/noodle1.zip", "noodle1.zip").build();
        DownloadInfo info4 = new DownloadInfo.Builder("http://s.live.ksmobile.net/live/emoji/2016-11-23_11:00:16/1479436319013_flowerEncodeResource.zip", "1479436319013_flowerEncodeResource.zip").build();
        DownloadInfo info5 = new DownloadInfo.Builder("http://s.live.ksmobile.net/live/emoji/2016-11-23_11:02:28/1479436469442_sportyCarEncodeResource.zip", "1479436469442_sportyCarEncodeResource.zip").build();
        urls.add(info);
        urls.add(info2);
        urls.add(info3);
        urls.add(info4);
        urls.add(info5);
        mBaseAdapter.replaceAll(urls);
        Downloader.getInstance().registerObserver(this);
    }

    @Override
    public void onItemClick(View view, DownloadInfo o, int position) {

    }

    @Override
    public void onViewClick(RecyclerView.ViewHolder viewHolder, View view, DownloadInfo o, int position) {
        if (view.getId() == R.id.download) {
            Downloader.getInstance().download(o);
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

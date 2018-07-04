package com.lougw.learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lougw.downloader.DownloadInfo;
import com.lougw.downloader.Downloader;
import com.lougw.learning.utils.adapter.BaseRecyclerAdapter;
import com.lougw.learning.utils.adapter.BaseRecyclerViewHolder;

import java.util.ArrayList;

public class DownloadActivity extends AppCompatActivity implements BaseRecyclerAdapter.OnItemClickListener<DownloadInfo> {
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
        DownloadInfo info = new DownloadInfo.Builder().Url("https://download.virtualbox.org/virtualbox/5.2.14/virtualbox-5.2_5.2.14-123301~Ubuntu~bionic_amd64.deb").FileName("aaa").build();
        urls.add(info);
        mBaseAdapter.replaceAll(urls);
    }

    @Override
    public void onItemClick(View view, DownloadInfo o, int position) {

    }

    @Override
    public void onViewClick(RecyclerView.ViewHolder viewHolder, View view, DownloadInfo o, int position) {
        if(view.getId()==R.id.download){
            Downloader.getInstance().getDownloadManager().downLoad(o);
        }else{
            Downloader.getInstance().getDownloadManager().pause(o);
        }


    }
}

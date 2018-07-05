package com.lougw.learning;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.lougw.downloader.DownloadInfo;
import com.lougw.learning.utils.adapter.BaseRecyclerViewHolder;

public class DownloadHolder extends BaseRecyclerViewHolder<DownloadInfo> {
    Button download;
    Button pause;
    ProgressBar progressBar;

    public DownloadHolder(View view, Context mContext) {
        super(view, mContext);
        download = (Button) view.findViewById(R.id.download);
        pause = (Button) view.findViewById(R.id.pause);
        progressBar = (ProgressBar) view.findViewById(R.id.download_progress);

    }

    @Override
    public void refreshView(int position) {
        DownloadInfo info = getData();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListenerReference != null && mListenerReference.get() != null) {
                    mListenerReference.get().onViewClick(DownloadHolder.this, v, getData(), getAdapterPosition());
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListenerReference != null && mListenerReference.get() != null) {
                    mListenerReference.get().onViewClick(DownloadHolder.this, v, getData(), getAdapterPosition());
                }
            }
        });
    }

    @Override
    public void recycle() {

    }
}

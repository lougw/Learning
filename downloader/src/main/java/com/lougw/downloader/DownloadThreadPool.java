/*******************************************************************************
 * Copyright 2011-2013
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.lougw.downloader;

import android.content.Context;
import android.text.TextUtils;

import com.lougw.downloader.db.DownloadDataBase;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载线程管理
 */
class DownloadThreadPool {
    private final static String TAG = DownloadThreadPool.class.getSimpleName();
    private int mPoolSize = 2;
    private ExecutorService mThreadPool;
    private Context mContext;
    private DownloadDataBase downLoadDataBase;
    private CopyOnWriteArrayList<DownloadRequest> mDownloadRequests;

    DownloadThreadPool(Context context) {
        this(context, 2);
    }

    DownloadThreadPool(Context context, int poolSize) {
        mPoolSize = poolSize;
        mThreadPool = Executors.newFixedThreadPool(mPoolSize);
        mContext = context;
        mDownloadRequests = new CopyOnWriteArrayList<DownloadRequest>();
    }


    synchronized boolean enqueue(DownloadRequest request) {
        if (request == null || TextUtils.isEmpty(request.getGuid())) {
            return false;
        }

        if (!isInQueue(request)) {
            request.setDownloadStatus(DownloadStatus.STATUS_IDLE);
            mThreadPool.execute(new DownloadMonitor(request, downLoadDataBase));
            return true;
        } else {
            return false;
        }
    }

    public void onDequeue(DownloadRequest request) {
        mDownloadRequests.remove(request);

    }

    private boolean isInQueue(DownloadRequest request) {
        String newGuid = request.getGuid();
        for (DownloadRequest tmpRequest : mDownloadRequests) {
            if (null == tmpRequest) {
                continue;
            }
            if (newGuid.equalsIgnoreCase(tmpRequest.getGuid())) {
                return true;
            }
        }
        return false;
    }

    public void setDownLoadDatabase(DownloadDataBase downLoadDataBase) {
        this.downLoadDataBase = downLoadDataBase;

    }

    /**
     * 获取线程池的所有任务 不包括已经完成的任务
     *
     * @return List<DownloadRequest>
     */
    List<DownloadRequest> getDownloadRequestInPool() {
        return mDownloadRequests;
    }

    /**
     * 根据id获取正在执行的任务中与之对应的任务
     *
     * @param id
     * @return
     */
    synchronized DownloadRequest getDownloadRequest(long id) {
        for (DownloadRequest r : mDownloadRequests) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

}

package com.lougw.downloader;

public class DownloadCallBack implements DownloadListener {

    @Override
    public void onEnqueue(DownloadRequest request) {
        Downloader.getInstance().notifyDownloadStateChanged(request);
//        Intent intent = new Intent();
//        intent.setAction(Config.ACTION);
//        intent.putExtra(Config.ACTION_MEAN,
//                Config.ON_ENQUEUE);
//        intent.putExtra(Config.DOWNLOAD_REQUEST, request);
//        Downloader.getInstance().getContext().sendBroadcast(intent);
    }

    @Override
    public void onStart(DownloadRequest request) {
//        Intent intent = new Intent();
//        intent.setAction(Config.ACTION);
//        intent.putExtra(Config.ACTION_MEAN,
//                Config.ON_START);
//        intent.putExtra(Config.DOWNLOAD_REQUEST, request);
//        Downloader.getInstance().getContext().sendBroadcast(intent);
    }

    @Override
    public void onDownloading(DownloadRequest request) {
    }

    @Override
    public void onError(DownloadRequest request) {
        Downloader.getInstance().deleteRequest(request);
        Downloader.getInstance().notifyDownloadStateChanged(request);
//        Intent intent = new Intent();
//        intent.setAction(Config.ACTION);
//        intent.putExtra(Config.ACTION_MEAN,
//                Config.ON_ERROR);
//        intent.putExtra(Config.DOWNLOAD_REQUEST, request);
//        Downloader.getInstance().getContext().sendBroadcast(intent);
    }

    @Override
    public void onComplete(DownloadRequest request) {
        Downloader.getInstance().deleteRequest(request);
        Downloader.getInstance().notifyDownloadStateChanged(request);
//        Intent intent = new Intent();
//        intent.setAction(Config.ACTION);
//        intent.putExtra(Config.ACTION_MEAN,
//                Config.ON_COMPLETE);
//        intent.putExtra(Config.DOWNLOAD_REQUEST, request);
//        Downloader.getInstance().getContext().sendBroadcast(intent);
    }

    @Override
    public void onPause(DownloadRequest request) {
        Downloader.getInstance().deleteRequest(request);
        Downloader.getInstance().notifyDownloadStateChanged(request);
//        Intent intent = new Intent();
//        intent.setAction(Config.ACTION);
//        intent.putExtra(Config.ACTION_MEAN,
//                Config.ON_PAUSED);
//        intent.putExtra(Config.DOWNLOAD_REQUEST, request);
//        Downloader.getInstance().getContext().sendBroadcast(intent);
    }

    @Override
    public void onDequeue(DownloadRequest request) {
//        Intent intent = new Intent();
//        intent.setAction(Config.ACTION);
//        intent.putExtra(Config.ACTION_MEAN,
//                Config.ON_DEQUEUE);
//        intent.putExtra(Config.DOWNLOAD_REQUEST, request);
//        Downloader.getInstance().getContext().sendBroadcast(intent);
    }

    @Override
    public void onProgress(DownloadRequest request) {
        Downloader.getInstance().notifyDownloadStateChanged(request);
//        Intent intent = new Intent();
//        intent.setAction(Config.ACTION);
//        intent.putExtra(Config.ACTION_MEAN,
//                Config.ON_PROGRESS);
//        intent.putExtra(Config.DOWNLOAD_REQUEST, request);
//        Downloader.getInstance().getContext().sendBroadcast(intent);

    }
}

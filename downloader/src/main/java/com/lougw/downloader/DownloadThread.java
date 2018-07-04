package com.lougw.downloader;

import android.text.TextUtils;

import com.lougw.downloader.utils.DLogUtil;
import com.lougw.downloader.utils.DToastUtil;
import com.lougw.downloader.utils.DownloadUtils;
import com.lougw.downloader.utils.NetWorkUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * <b>下载线程
 */
@SuppressWarnings("deprecation")
public class DownloadThread extends Thread {
    /* 下载地址 */
    private String downUrl;
    private String destPath;

    /* 下载开始位置 */
    private long startPos;
    /* 线程ID */
    private int threadId = -1;

    /* 已经下载的长度 */
    private volatile long downLength;

    /* 下载线程是否完成了应该下载的文件 长度 */
    private boolean finish = false;

    /* 下载线程状态标记 */
    private Status state = Status.RUNNING;

    /**
     * @see
     */
    private DownloadMonitor downloader;
    private DownloadRequest request;

    /**
     * 下载线程是否出错
     *
     * @note 下载线程完成了下载但是在关闭流的时候出错，不属于下载出错
     */
    private boolean isError = false;
    private String location;
    private HttpClient httpClient;
    /**
     * 下载失败错误码
     */
    private int mErrorCode = ErrorCode.NORMAL;

    /**
     * @param downloader <a>HttpDownloader</a>
     * @param tid        线程ID
     */
    public DownloadThread(DownloadMonitor downloader, DownloadRequest request,
                          int tid) {
        this.request = request;
        this.downUrl = request.getSrcUri();
        this.location = downUrl;
        this.downloader = downloader;
        this.threadId = tid;
        this.downLength = request.getDownloadSize();
        this.destPath = request.getDestUri();
    }

    @Override
    public void run() {
        doDownload();
    }

    private void showToast() {
        DToastUtil.showMessage(R.string.download_no_space);
    }

    private void doDownload() {
        boolean lowMemToastDisplayed = false;

        // 如果当前线程没有结束并且下载任务也没有被取消则下载
        if (!downloader.isCancel()) {
            RandomAccessFile threadFile = null;
            InputStream inStream = null;
            try {
                startPos += downLength;
                DLogUtil.e("THREAD", "" + startPos);

                DownloadUtils.getDownLoadFileHome();
                if (DownloadUtils.isMemoryLow()) {
                    if (!lowMemToastDisplayed) {
                        showToast();
                        lowMemToastDisplayed = true;
                    }
                    downloader.setStatus(DownloadStatus.STATUS_PAUSE);
                    stopDownload();
                    return;
                }

                HttpResponse response = httpGetRedirect(downUrl);
                if (response == null || TextUtils.isEmpty(location)) {
                    downloader.setStatus(DownloadStatus.STATUS_ERROR);
                    isError = true;
                    return;
                }

                File file = new File(destPath);
                // 设置线程下载的开始位置，应该为初始下载位置startPos + 已下载长度downLength
                HttpEntity entry = response.getEntity();
                long contentLength = entry.getContentLength();
                if (request.getTotalSize() == 0) {
                    request.setTotalSize(contentLength);
                }
                inStream = entry.getContent();
                byte[] buffer = new byte[8192];
                threadFile = new RandomAccessFile(file, "rwd");
                threadFile.seek(startPos);
                int length;
                while ((length = inStream.read(buffer)) != -1) {

                    if (!NetWorkUtil.hasNetwork(Downloader.getInstance().getContext())) {
                        DToastUtil.showMessage(R.string.download_no_network);
                        downloader.setStatus(DownloadStatus.STATUS_PAUSE);
                        downloader.setReDownload();
                        stopDownload();
                        break;
                    }

                    DownloadStatus status = downloader.getStatus();
                    if (DownloadStatus.STATUS_PAUSE == status
                            || DownloadStatus.STATUS_ERROR == status
                            || DownloadStatus.STATUS_COMPLETE == status
                            || DownloadStatus.STATUS_DELETE == status
                            || state == Status.FINISH) {
                        stopDownload();
                        break;
                    }
                    threadFile.write(buffer, 0, length);
                    downLength += length;
                    downloader.append(length);
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
                downloader.setStatus(DownloadStatus.STATUS_ERROR);
                isError = true;
            } catch (FileNotFoundException e) {
                downloader.setStatus(DownloadStatus.STATUS_ERROR);
                isError = true;
            } catch (IOException e) {
                if (e instanceof SocketException || e instanceof UnknownHostException
                        || e instanceof SocketTimeoutException
                        || e instanceof SSLPeerUnverifiedException
                        || e instanceof org.apache.http.conn.ConnectTimeoutException) {
                    downloader.setStatus(DownloadStatus.STATUS_PAUSE);
                    isError = false;
                    downloader.setReDownload();
                } else {
                    if (DownloadUtils.isMemoryLow()) {
                        if (!lowMemToastDisplayed) {
                            showToast();
                            lowMemToastDisplayed = true;
                        }
                        downloader.setStatus(DownloadStatus.STATUS_PAUSE);
                    } else {
                        downloader.setStatus(DownloadStatus.STATUS_ERROR);
                        isError = true;
                    }
                }

            } catch (Exception e) {
                if (DownloadUtils.isMemoryLow()) {
                    if (!lowMemToastDisplayed) {
                        showToast();
                        lowMemToastDisplayed = true;
                    }
                }
                downloader.setStatus(DownloadStatus.STATUS_ERROR);
                isError = true;
                DLogUtil.log("doDownload:  " + " Exception" + e.toString());
            } finally {
                try {
                    if (threadFile != null) {
                        threadFile.close();
                    }
                    if (inStream != null) {
                        inStream.close();
                    }
                } catch (IOException e) {
                    if (e instanceof SocketException || e instanceof UnknownHostException
                            || e instanceof SocketTimeoutException
                            || e instanceof SSLPeerUnverifiedException
                            || e instanceof org.apache.http.conn.ConnectTimeoutException) {
                        if (downloader.getStatus() != DownloadStatus.STATUS_DELETE) {
                            downloader.setStatus(DownloadStatus.STATUS_PAUSE);
                        }
                    } else {
                        if (DownloadUtils.isMemoryLow()) {
                            if (!lowMemToastDisplayed) {
                                showToast();
                                lowMemToastDisplayed = true;
                            }
                            downloader.setStatus(DownloadStatus.STATUS_ERROR);
                        }
                        e.printStackTrace();
                    }
                }

                if (downLength >= downloader.getDownloadRequest().getTotalSize()) {
                    finish = true;
                } else {
                    if (DownloadUtils.isMemoryLow()) {
                        if (!lowMemToastDisplayed) {
                            showToast();
                            lowMemToastDisplayed = true;
                        }
                        downloader.setStatus(DownloadStatus.STATUS_PAUSE);
                    } else {
                        if (DownloadStatus.STATUS_START == downloader.getStatus()) {
                            downloader.setStatus(DownloadStatus.STATUS_ERROR);
                            isError = true;
                        }
                    }
                    DLogUtil.log("doDownload:  " + " finish but size is < total size");
                }
                state = Status.FINISH;
            }
        } else {
            DLogUtil.log("doDownload  downloader cancel");
        }
    }

    private HttpResponse httpGetRedirect(String url) throws ClientProtocolException, IOException {
        httpClient = getHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Range", "bytes=" + startPos + "-");
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
        httpGet.setParams(httpParams);
        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
                || response.getStatusLine().getStatusCode() == HttpStatus.SC_PARTIAL_CONTENT) {
            if (response.getEntity() != null && response.getEntity().getContentType() != null &&
                    response.getEntity().getContentType().getValue() != null) {
                String value = response.getEntity().getContentType().getValue();
                if (value.contains("text/xml") || value.contains("text/html")) {
                    return null;
                }
            }
            return response;
        } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY
                || response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY) {
            // 从头中取出转向的地址
            Header locationHeader = response.getLastHeader("location");
            location = locationHeader.getValue();
            return httpGetRedirect(location);
        }
        return null;
    }

    /**
     * @Description:
     * @Author
     */
    private HttpClient getHttpClient() {
        if (null == httpClient) {
            httpClient = new DefaultHttpClient();
        }
        return httpClient;
    }

    /**
     * @Description:
     */
    private void stopDownload() {
        getHttpClient().getConnectionManager().shutdown();
    }

    /**
     * 下载是否完成
     *
     * @return
     */
    public boolean isFinish() {
        return finish;
    }

    /**
     * 下载线程是否发生错误
     *
     * @return
     */
    public boolean isError() {
        return isError;
    }

    public Status getStatus() {
        return state;
    }

    public enum Status {
        RUNNING, FINISH
    }

}

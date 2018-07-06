package com.lougw.downloader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.util.Log;

import com.lougw.downloader.DownloadListener;
import com.lougw.downloader.DownloadRequest;


/**
 * <b>单列模式的数据库操作类
 */
public class DownloadDataBaseIml implements DownloadDataBase {
    private static final String TAG = "DownloadDataBaseIml";

    private DownloadDBHelper helper;
    private SQLiteDatabase db;
    private SQLiteDatabase newDB;
    private DownloadListener mDownloadListener;
    private Object mObjLock = new Object();

    private volatile int dbReopening = 0;
    private Object mReopenLock = new Object();
    public static final int DB_REOPENING = 1;
    public static final int DB_REOPENING_OVER = 0;
    private volatile int mTryCount = 0;
    private static final int MAX_TRY_COUNT = 10;
    private static final int TIME_TO_WAIT_NEXT_OPERATE = 300;

    private int getReopenFlag() {
        return dbReopening;
    }

    private void setReopenFlag(int reopen) {
        dbReopening = reopen;
    }

    private boolean checkTryCountValid() {
        if (mTryCount > MAX_TRY_COUNT)
            return false;
        try {
            Thread.sleep(TIME_TO_WAIT_NEXT_OPERATE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void incrementTryCount() {
        mTryCount++;
    }

    private void LogDB() {
        StackTraceElement st[] = Thread.currentThread().getStackTrace();
        for (int i = 0; i < st.length; i++) {
            Log.e(TAG, "STACK = " + st[i]);
        }
    }

    private SQLiteDatabase getDb() {
        SQLiteDatabase lDb;
        synchronized (mReopenLock) {
            if (DB_REOPENING == getReopenFlag()) {
                try {
                    mReopenLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lDb = db;
        }
        return lDb;
    }

    private void reOpenDb() {
        LogDB();
        setReopenFlag(DB_REOPENING);
        synchronized (mReopenLock) {
            if (null != db && db.isOpen())
                db.close();
            db = helper.getWritableDatabase();
            setReopenFlag(DB_REOPENING_OVER);
            mReopenLock.notifyAll();
        }
    }

    public DownloadDataBaseIml(Context context) {
        helper = DownloadDBHelper.getInstance(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public long insert(DownloadRequest request) {
        long insertResult = 0;
        ContentValues values = request.toContentValues();
        mDownloadListener.onEnqueue(request);

        synchronized (mObjLock) {
            newDB = getDb();
            try {
                insertResult = newDB.insert(DownloadDBHelper.TABLE_NAME, null, values);
            } catch (SQLiteDiskIOException e) {
                reOpenDb();
                newDB = getDb();
                incrementTryCount();
                if (checkTryCountValid())
                    insertResult = insert(request);
            }
        }

        return insertResult;

    }

    @Override
    public synchronized int update(DownloadRequest request) {
        int rowCountResult = 0;
        rowCountResult = updateEx(request);
        switch (request.getDownloadStatus()) {
            case STATUS_IDLE:
                mDownloadListener.onEnqueue(request);
                break;
            case STATUS_START:
                mDownloadListener.onStart(request);
                break;
            case STATUS_DOWNLOADING:
                mDownloadListener.onDownloading(request);
                break;
            case STATUS_PAUSE:
                mDownloadListener.onPause(request);
                break;
            case STATUS_ERROR:
                mDownloadListener.onError(request);
                break;
            case STATUS_COMPLETE:
                mDownloadListener.onComplete(request);
                break;
            default:
                break;
        }

        return rowCountResult;
    }

    @Override
    public void progress(DownloadRequest request) {
        mDownloadListener.onProgress(request);
    }

    public int updateEx(DownloadRequest request) {
        int rowCountResult = 0;
        ContentValues values = request.toContentValues();
        String[] whereArgs = {
                "" + request.getId()
        };
        String where = DownloadColumns._ID + "=?";

        synchronized (mObjLock) {
            newDB = getDb();
            try {
                rowCountResult = newDB.update(DownloadDBHelper.TABLE_NAME, values, where,
                        whereArgs);
            } catch (SQLiteDiskIOException e) {
                reOpenDb();
                newDB = getDb();
                incrementTryCount();
                if (checkTryCountValid())
                    rowCountResult = updateEx(request);
            }
        }

        return rowCountResult;
    }

    @Override
    public void delete(DownloadRequest request) {
        int delResult = 0;
        delResult = deleteEx(request);
        if (delResult > 0)
            mDownloadListener.onDequeue(request);
        return;
    }

    public int deleteEx(DownloadRequest request) {
        int delResult = 0;
        String[] whereArgs = {
                "" + request.getId()
        };
        String where = DownloadColumns._ID + "=?";

        synchronized (mObjLock) {
            newDB = getDb();
            try {
                delResult = newDB.delete(DownloadDBHelper.TABLE_NAME, where, whereArgs);
            } catch (SQLiteDiskIOException e) {
                reOpenDb();
                newDB = getDb();
                incrementTryCount();
                if (checkTryCountValid())
                    deleteEx(request);
            }
        }
        return delResult;
    }

    public Cursor query(String[] projection, String selection,
                        String[] selectionArgs, String orderBy) {
        Cursor cursorResultCursor = null;
        synchronized (mObjLock) {
            newDB = getDb();
            try {
                cursorResultCursor = newDB.query(DownloadDBHelper.TABLE_NAME, projection,
                        selection,
                        selectionArgs, null, null, orderBy);
            } catch (SQLiteDiskIOException e) {
                if (null != cursorResultCursor) {
                    cursorResultCursor.close();
                    cursorResultCursor = null;
                }
                reOpenDb();
                newDB = getDb();
                incrementTryCount();
                if (checkTryCountValid())
                    query(projection, selection, selectionArgs, orderBy);
            }
        }
        return cursorResultCursor;
    }

    public void setDownloadListener(DownloadListener listener) {
        mDownloadListener = listener;
    }
}

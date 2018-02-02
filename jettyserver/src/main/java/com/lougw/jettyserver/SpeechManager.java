package com.lougw.jettyserver;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2018/02/02
 *     desc   :
 * </pre>
 */

public class SpeechManager {
    private final android.os.Handler mHandler = new android.os.Handler();
    private SpeechObserver mSpeechObserver;

    private SpeechManager() {
    }

    private static class SpeechHolder {
        public final static SpeechManager instance = new SpeechManager();
    }

    public static SpeechManager getInstance() {
        return SpeechHolder.instance;
    }

    /**
     * 注册观察者
     */
    public void registerObserver(SpeechObserver observer) {
        mSpeechObserver = observer;
    }

    /**
     * 反注册观察者
     */
    public void unRegisterObserver(SpeechObserver observer) {
        mSpeechObserver = null;
    }

    public void notifySpeechMessage(final int page, final int direction) {
        if (mSpeechObserver != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mSpeechObserver.onSpeech(page, direction);
                }
            });
        }
    }

    public interface SpeechObserver {
        void onSpeech(int page, int direction);
    }
}

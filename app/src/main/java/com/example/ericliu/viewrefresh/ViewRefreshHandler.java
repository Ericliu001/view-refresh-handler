package com.example.ericliu.viewrefresh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.lang.ref.WeakReference;


/**
 * Created by ericliu on 26/11/16.
 */

public final class ViewRefreshHandler {
    private static final long MINI_SECS_ONE_MINUTE = 1000 * 60;
    private final Handler mHandler;
    private Runnable mRunnableDecorator;

    public ViewRefreshHandler() {
        mHandler = new Handler(Looper.getMainLooper());
    }


    public void executePerMinute(ViewRunnable runnable) {
        executePeriodically(runnable, MINI_SECS_ONE_MINUTE);
    }

    public void executePeriodically(final ViewRunnable runnable, final long interval) {
        cancelPendingTask();
        mRunnableDecorator = new Runnable() {
            @Override
            public void run() {
                runnable.run();
                if (runnable.viewRef.get() != null) {
                    scheduleNext(interval);
                } else {
                    // stop refreshing when the reference of the View is gone.
                    cancelPendingTask();
                }
            }
        };
        scheduleNext(interval);
    }

    private void cancelPendingTask() {
        mRunnableDecorator = null;
        mHandler.removeCallbacksAndMessages(null);
    }


    private void scheduleNext(long interval) {
        if (mRunnableDecorator != null) {
            mHandler.postDelayed(mRunnableDecorator, interval);
        }
    }


    public static abstract class ViewRunnable implements Runnable {
        private final WeakReference<View> viewRef;
        private final Bundle mArgs;

        public ViewRunnable(View view, Bundle args) {
            viewRef = new WeakReference<View>(view);
            if (args == null) {
                mArgs = new Bundle();
            } else {
                mArgs = args;
            }
        }

        @Override
        public void run() {
            View view = viewRef.get();
            if (view != null) {
                run(view, mArgs);
            }
        }

        protected abstract void run(View view, Bundle args);
    }
}


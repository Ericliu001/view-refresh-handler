package com.example.ericliu.viewrefresh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;

import java.lang.ref.WeakReference;


/**
 * Created by ericliu on 26/11/16.
 * <p>
 * A composite of a {@link Handler} to schedule operations periodically.
 */

public final class ViewRefreshHandler {
    private static final long MINI_SECS_ONE_MINUTE = 1000 * 60;
    private static final long MINI_SECS_ONE_SECOND = 1000;

    private final Handler mHandler;
    private Runnable mRunnableDecorator;

    public ViewRefreshHandler() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void executePerSecond(ViewRunnable runnable) {
        executePeriodically(runnable, MINI_SECS_ONE_SECOND);
    }

    public void executePerMinute(ViewRunnable runnable) {
        executePeriodically(runnable, MINI_SECS_ONE_MINUTE);
    }

    public void executePeriodically(final ViewRunnable runnable, final long interval) {
        cancelPendingTask();

        /**
         * creates a decorator class of the runnable being passed in.
         * after executing the run method in the runnable, call {@link #scheduleNext(long)}
         * to schedule the next call.
         */
        mRunnableDecorator = new Runnable() {
            @Override
            public void run() {
                runnable.run();
                if (runnable.viewRef.get() != null && !runnable.terminate) {
                    scheduleNext(interval);
                } else {
                    // stop refreshing when the View is gone.
                    cancelPendingTask();
                }
            }
        };
        mHandler.post(mRunnableDecorator);
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


    /**
     * A subclass class of {@link Runnable} which only holds a WeakReference of a View object,
     * <p>
     * as a result, it avoids the problem of memory leak caused by not releasing references of View objects.
     *
     * @param <T> - the View instance to be operated on.
     */
    public static abstract class ViewRunnable<T extends View> implements Runnable {
        private final WeakReference<T> viewRef;
        private final Bundle mArgs;
        private boolean terminate = false; // set true to stop executing repeating tasks

        public ViewRunnable(T view, @Nullable Bundle args) {
            viewRef = new WeakReference<>(view);
            if (args == null) {
                mArgs = new Bundle();
            } else {
                mArgs = args;
            }
        }

        @Override
        public void run() {
            T view = viewRef.get();
            if (view != null) {
                run(view, mArgs);
            }
        }

        protected void terminate() {
            terminate = true;
        }

        protected abstract void run(T view, Bundle args);
    }
}


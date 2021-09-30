package com.itsaky.androidide.tasks;

import android.os.Handler;
import android.os.Looper;
import com.blankj.utilcode.util.ThrowableUtils;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.itsaky.androidide.utils.Logger;

public class TaskExecutor {
    
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback<R> {
        void complete(R result);
    }
    
    public interface CallbackWithError<R> {
        void complete(R result, Throwable error);
    }

    public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {
        executor.execute(() -> {
            try {
				final R result = callable.call();
				handler.post(() -> {
					callback.complete(result);
				});
			} catch (Throwable th) {}
        });
    }
    
    public <R> void executeAsyncProvideError(Callable<R> callable, CallbackWithError<R> callback) {
        executor.execute(() -> {
            Throwable error = null;
            R result = null;
            
            try {
                result = callable.call();
            } catch (Throwable th) {
                error = th;
            }
            final R resultCopied = result;
            final Throwable errorCopied = error;
            
            handler.post(() -> {
                callback.complete(resultCopied, errorCopied);
            });
        });
    }
}

package com.itsaky.androidide.tasks;

import android.os.Handler;
import android.os.Looper;
import com.blankj.utilcode.util.ThrowableUtils;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.itsaky.androidide.utils.Logger;

public class TaskExecutor {
    private final Executor executor = Executors.newSingleThreadExecutor(); // change according to your requirements
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback<R> {
        void onComplete(R result);
    }

    public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {
        executor.execute(() -> {
            try {
				final R result = callable.call();
				handler.post(() -> {
					callback.onComplete(result);
				});
			} catch (Throwable th) {}
        });
    }
}

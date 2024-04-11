package com.triadss.doctrack2.notification;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.triadss.doctrack2.config.constants.NotificationConstants;


public class NotificationBackgroundWorker extends Worker {
    Context context;
    public NotificationBackgroundWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @Override
    public Result doWork() {
        String receiverUserUid = getInputData().getString(NotificationConstants.RECEIVER_ID);

        // Do the work here--in this case, upload the images.
        Log.e("TEST", "Running Work for " + receiverUserUid);
        Toast.makeText(context, "Running Work for " + receiverUserUid, Toast.LENGTH_SHORT).show();

        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }
}

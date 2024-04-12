package com.triadss.doctrack2.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.NotificationConstants;
import com.triadss.doctrack2.config.constants.SessionConstants;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.NotificationDTO;
import com.triadss.doctrack2.repoositories.NotificationRepository;

import java.util.ArrayList;
import java.util.List;


public class NotificationBackgroundWorker extends Worker {
    NotificationRepository getnotify = new NotificationRepository();
    NotificationDTO notifyDti = new NotificationDTO();
    List<NotificationDTO> notificationList = new ArrayList<NotificationDTO>();
    private SharedPreferences sharedPref;
    Context context;
    public NotificationBackgroundWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }


/*Do Work: TODO
    1. Fetch lastRequestDate
    2. Fetch new Notifications for user
    3. Notify each notifications received
*/
    private void LastRequestDate()
    {

    }
    private void UserNotifications()
    {

    }
    private void NotificationsReceived()
    {

    }
    @Override
    public Result doWork() {
        String receiverUserUid = getInputData().getString(NotificationConstants.RECEIVER_ID);
        DateTimeDto datetimedto = new DateTimeDto();

        SharedPreferences sharedPref = context.getSharedPreferences(SessionConstants.SessionPreferenceKey, Context.MODE_PRIVATE);
        String lastRequestDate = sharedPref.getString(SessionConstants.LastRequestDate, null);
        if (lastRequestDate == null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(SessionConstants.LastRequestDate, datetimedto.GetCurrentTimeStamp().toString());
            editor.apply();
        }

        Log.e("TEST", "Running Work for " + receiverUserUid + " since " + lastRequestDate);
        // scheduleNotification(getNotification("1 second delay"), 1000);

        getnotify.fetchUserNotification(receiverUserUid, lastRequestDate, new NotificationRepository.FetchNotificationAddCallback() {
            @Override
            public void onSuccess(List<NotificationDTO> notificationList) { // fetch notification data
                for (NotificationDTO notifyDti : notificationList) {
                    scheduleNotification(notifyDti, 1000);
                    Log.e("TEST", "Found notifcation" + notifyDti.getTitle() + " at " + 
                        DateTimeDto.ToDateTimeDto(notifyDti.getDateSent()).ToString());
                }

                //Update Notification
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(SessionConstants.LastRequestDate, datetimedto.GetCurrentTimeStamp().toString());
                editor.apply();
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error if needed
                System.out.println();
            }
        });

        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }


    // Modify the scheduleNotification method to accept notification content
    public void scheduleNotification(NotificationDTO dto, int delay) {
        Intent notificationIntent = new Intent(context, NotificationService.class);
        notificationIntent.putExtra(NotificationService.NOTIFICATION_ID, 1);

        notificationIntent.putExtra(NotificationService.NOTIFICATION, getNotification(dto));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    // Modify the getNotification method to accept content parameter
    public Notification getNotification(NotificationDTO dto) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationConstants.DEFAULT_NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(dto.getTitle());
        // Use the custom content for the notification
        builder.setContentText(dto.getContent());
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NotificationConstants.NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }

}

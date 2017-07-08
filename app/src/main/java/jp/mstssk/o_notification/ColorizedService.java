package jp.mstssk.o_notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


public class ColorizedService extends Service {

    private static final String TAG = "ColorizedService";

    private static final String ACTION_START = "jp.mstssk.o_notification.action.START";
    private static final String ACTION_END = "jp.mstssk.o_notification.action.END";

    private static final String EXTRA_COLOR_INT = "jp.mstssk.o_notification.extra.COLOR";
    private static final String EXTRA_COLORIZED_BOOL = "jp.mstssk.o_notification.extra.COLORIZED";

    public ColorizedService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_START)) {
            @ColorInt int color = intent.getIntExtra(EXTRA_COLOR_INT, ColorizedFragment.Color.RED.color);
            boolean colorized = intent.getBooleanExtra(EXTRA_COLORIZED_BOOL, true);
            startForeground(21, createNotification(color, colorized));
            Log.i(TAG, "startForeground");
        } else if (intent.getAction().equals(ACTION_END)) {
            stopSelf();
            Log.i(TAG, "stopSelf");
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(Context context, @ColorInt int color, boolean colorized) {
        Intent intent = new Intent(context, ColorizedService.class);
        intent.setAction(ACTION_START);
        intent.putExtra(EXTRA_COLOR_INT, color);
        intent.putExtra(EXTRA_COLORIZED_BOOL, colorized);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    private Notification createNotification(@ColorInt int color, boolean colorized) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NotifyUtils.CHANNEL_ID_COLOR,
                    "Colorized Channel", NotificationManagerCompat.IMPORTANCE_MAX);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, ColorizedService.class);
        intent.setAction(ACTION_END);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getForegroundService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotifyUtils.CHANNEL_ID_COLOR)
                .setContentTitle("Colored notification")
                .setContentText("Content text")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColorized(colorized)
                .setColor(color)
                // .setOngoing(true) // startForegroundした通知は勝手にongoingになる
                .addAction(new NotificationCompat.Action(R.mipmap.ic_launcher, "Close", pendingIntent));
        return builder.build();
    }
}

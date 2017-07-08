package jp.mstssk.o_notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;

public class MessagingFragment extends Fragment {

    static final String TAG = "MessagingFragment";

    /**
     * for System uses
     */
    @Deprecated
    public MessagingFragment() {
    }

    public static MessagingFragment newInstance() {
        return new MessagingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messaging, container, false);

        view.findViewById(R.id.notify_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessaging();
            }
        });
        view.findViewById(R.id.notify_historic_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    showHistoricMessaging();
                } else {
                    Toast.makeText(getActivity(), "Only for Android O.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void showMessaging() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ensureChannel();
        }

        NotificationCompat.Action action = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            Intent replyIntent = new Intent(getActivity(), MainActivity.class);
            PendingIntent replyPendingIntent =
                    PendingIntent.getActivity(getActivity(), 1,
                            replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            android.support.v4.app.RemoteInput remoteInput = new android.support.v4.app.RemoteInput.Builder(NotifyUtils.REMOTE_INPUT_KEY)
                    .setLabel("comment")
                    .build();
            action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher,
                    "Reply", replyPendingIntent)
                    .addRemoteInput(remoteInput)
                    .build();
        }

        NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle("Mike");
        style.setConversationTitle("How are you?");
        style.addMessage(new NotificationCompat.MessagingStyle.Message(NotifyUtils.LOREM_IPSUM, new Date().getTime(), "Mike"));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), NotifyUtils.CHANNEL_ID_MESSAGING)
                .setContentTitle("Messaging title")
                .setContentText("This notification is with messages.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(style);
        if (action != null) {
            builder.addAction(action);
        }
        NotificationManagerCompat.from(getActivity()).notify(31, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showHistoricMessaging() {

        Notification.Action action = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            Intent replyIntent = new Intent(getActivity(), MainActivity.class);
            PendingIntent replyPendingIntent =
                    PendingIntent.getActivity(getActivity(), 1,
                            replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            android.app.RemoteInput remoteInput = new android.app.RemoteInput.Builder(NotifyUtils.REMOTE_INPUT_KEY)
                    .setLabel("comment")
                    .build();
            action = new Notification.Action.Builder(R.mipmap.ic_launcher,
                    "Reply", replyPendingIntent)
                    .addRemoteInput(remoteInput)
                    .build();
        }

        Notification.MessagingStyle style = new Notification.MessagingStyle("Mike");
        style.setConversationTitle("How are you?");
        style.addMessage(NotifyUtils.LOREM_IPSUM, new Date().getTime(), "Mike");
        style.addHistoricMessage(new Notification.MessagingStyle.Message(NotifyUtils.DUIS_AUTE, new Date().getTime(), "Mike"));

        Notification.Builder builder = new Notification.Builder(getActivity(), NotifyUtils.CHANNEL_ID_MESSAGING)
                .setContentTitle("Historic Messaging Style")
                .setContentText("This notification is with historic messages.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(style);
        if (action != null) {
            builder.addAction(action);
        }

        getActivity().getSystemService(NotificationManager.class).notify(32, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void ensureChannel() {
        NotificationChannel channel = new NotificationChannel(NotifyUtils.CHANNEL_ID_MESSAGING,
                "Messaging Channel", NotificationManagerCompat.IMPORTANCE_DEFAULT);
        getActivity().getSystemService(NotificationManager.class).createNotificationChannel(channel);
    }

}

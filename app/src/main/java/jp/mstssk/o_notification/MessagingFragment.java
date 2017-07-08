package jp.mstssk.o_notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessagingFragment extends Fragment {

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

        view.findViewById(R.id.notify_historic_messages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessagingNotification();
            }
        });

        return view;
    }

    private void showMessagingNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NotifyUtils.CHANNEL_ID_MESSAGING,
                    "1st Channel", NotificationManagerCompat.IMPORTANCE_DEFAULT);
            getActivity().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), NotifyUtils.CHANNEL_ID_MESSAGING)
                .setContentTitle("Messaging title")
                .setContentText("This notification is with historic messages.")
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setStyle(new NotificationCompat.MessagingStyle()) TODO addHistricMessages
                ;
        NotificationManagerCompat.from(getActivity()).notify(2, builder.build());
    }

}

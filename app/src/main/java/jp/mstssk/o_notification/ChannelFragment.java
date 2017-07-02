package jp.mstssk.o_notification;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChannelFragment extends Fragment {

    public static final String CHANNEL_ID_1ST = "CHANNEL_ID_1ST";
    public static final String CHANNEL_ID_2ND = "CHANNEL_ID_2ND";
    public static final String CHANNEL_ID_CONFIG = "CHANNEL_ID_CONFIG";
    public static final String CHANNEL_GROUP_ID_FOO = "CHANNEL_GROUP_ID_FOO";
    public static final String CHANNEL_ID_3RD_WITH_GROUP_FOO = "CHANNEL_ID_3RD_WITH_GROUP_FOO";
    public static final String CHANNEL_ID_4th_WITH_GROUP_FOO = "CHANNEL_ID_4th_WITH_GROUP_FOO";

    /**
     * for System uses
     */
    @Deprecated
    public ChannelFragment() {
    }

    public static ChannelFragment newInstance() {
        //noinspection deprecation
        return new ChannelFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel, container, false);

        view.findViewById(R.id.notify_with_no_channel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotificatinWithNoChannel();
            }
        });
        view.findViewById(R.id.notify_with_1st_channel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotificatinWithChannel_1();
            }
        });
        view.findViewById(R.id.notify_with_2nd_channel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotificatinWithChannel_2();
            }
        });
        view.findViewById(R.id.notify_with_configured_channel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotificatinWithChannel_SpecifiedConfigures();
            }
        });
        view.findViewById(R.id.notify_with_3rd_channel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotificatinWithChannelGroup_1();
            }
        });
        view.findViewById(R.id.notify_with_4th_channel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotificatinWithChannelGroup_2();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Delete old channel, if you changed id.
            getActivity().getSystemService(NotificationManager.class).deleteNotificationChannel("channel1");
        }
    }

    private void showNotificatinWithNoChannel() {
        //noinspection deprecation
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
                .setContentTitle("Content Title")
                .setContentText("Content text")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManagerCompat manager = NotificationManagerCompat.from(getActivity());
        manager.notify(1, builder.build());
    }

    private void showNotificatinWithChannel_1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_1ST,
                    "1st Channel", NotificationManagerCompat.IMPORTANCE_DEFAULT);
            getActivity().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID_1ST)
                .setContentTitle("1st Content Title")
                .setContentText("Content text")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManagerCompat.from(getActivity()).notify(2, builder.build());
    }

    private void showNotificatinWithChannel_2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_2ND,
                    "2nd Channel", NotificationManagerCompat.IMPORTANCE_DEFAULT);
            getActivity().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID_2ND)
                .setContentTitle("2nd Content Title")
                .setContentText("Content text")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManagerCompat.from(getActivity()).notify(3, builder.build());
    }

    private void showNotificatinWithChannel_SpecifiedConfigures() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_CONFIG,
                    "Configured Channel", NotificationManagerCompat.IMPORTANCE_DEFAULT);
            channel.setDescription("This is Sample Configured Channel.");
            channel.setLightColor(Color.argb(0, 1, 0, 0));
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            getActivity().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID_CONFIG)
                .setContentTitle("Configured Content Title")
                .setContentText("Content text")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManagerCompat.from(getActivity()).notify(4, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private NotificationChannelGroup buildChannelGroupFoo() {
        return new NotificationChannelGroup(CHANNEL_GROUP_ID_FOO, "Group Foo");
    }

    private void showNotificatinWithChannelGroup_1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannelGroup group = buildChannelGroupFoo();
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_3RD_WITH_GROUP_FOO,
                    "3rd Channel", NotificationManagerCompat.IMPORTANCE_DEFAULT);
            channel.setGroup(group.getId());
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannelGroup(group);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID_3RD_WITH_GROUP_FOO)
                .setContentTitle("3rd Content Title")
                .setContentText("Content text")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManagerCompat.from(getActivity()).notify(5, builder.build());
    }

    private void showNotificatinWithChannelGroup_2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannelGroup group = buildChannelGroupFoo();
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_4th_WITH_GROUP_FOO,
                    "4th Channel", NotificationManagerCompat.IMPORTANCE_DEFAULT);
            channel.setGroup(group.getId());
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannelGroup(group);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID_4th_WITH_GROUP_FOO)
                .setContentTitle("4th Content Title")
                .setContentText("Content text")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManagerCompat.from(getActivity()).notify(6, builder.build());
    }
}

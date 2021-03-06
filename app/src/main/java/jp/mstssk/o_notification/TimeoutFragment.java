package jp.mstssk.o_notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class TimeoutFragment extends Fragment {

    static final String TAG = "TimeoutFragment";

    /**
     * for System uses
     */
    @Deprecated
    public TimeoutFragment() {
    }

    public static TimeoutFragment newInstance() {
        return new TimeoutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeout, container, false);

        view.findViewById(R.id.show_channel_settings)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                            intent.putExtra(Settings.EXTRA_CHANNEL_ID, NotifyUtils.CHANNEL_ID_TIMEOUT);
                            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Only for Android O.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        view.findViewById(R.id.notify_with_timeout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWithTimeout();
            }
        });
        return view;
    }

    private void showWithTimeout() {
        long durationSec;
        try {
            String input = ((EditText) getView().findViewById(R.id.timeout_seconds)).getText().toString();
            durationSec = Long.valueOf(input);
            if (durationSec <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Duration is invalid.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NotifyUtils.CHANNEL_ID_TIMEOUT,
                    "Timeout Channel", NotificationManagerCompat.IMPORTANCE_DEFAULT);
            getActivity().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), NotifyUtils.CHANNEL_ID_TIMEOUT)
                .setContentTitle("Timeout " + durationSec + " sec")
                .setContentText("This notification be canceled after " + durationSec + " seconds.")
                .setSmallIcon(R.drawable.ic_timer)
                .setTimeoutAfter(durationSec * 1000);
        NotificationManagerCompat.from(getActivity()).notify(TAG, 11, builder.build());
    }
}

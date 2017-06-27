package jp.mstssk.notification_channel_sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChannelFragment extends Fragment {

    /** for System uses */
    @Deprecated
    public ChannelFragment() {}

    public static ChannelFragment newInstance() {
        return new ChannelFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_channel, container, false);
    }




    private void showNotificatinWithNoChannel(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
                .setContentTitle("Content Title")
                .setContentText("Content text")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManagerCompat manager = NotificationManagerCompat.from(getActivity());
        manager.notify(1, builder.build());
    }

    private void showNotificatinWithChannel_1(){
    }

    private void showNotificatinWithChannel_2(){
    }

    private void showNotificatinWithChannel_SpecifiedChannelName(){
    }

    private void showNotificatinWithChannelGroup_1(){
    }

    private void showNotificatinWithChannelGroup_2(){
    }
}

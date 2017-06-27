package jp.mstssk.o_notification;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TimeoutFragment extends Fragment {

    /** for System uses */
    @Deprecated
    public TimeoutFragment() {
    }

    public static TimeoutFragment newInstance() {
        return new TimeoutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeout, container, false);
    }



}

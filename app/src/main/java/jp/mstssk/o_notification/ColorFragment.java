package jp.mstssk.o_notification;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ColorFragment extends Fragment {

    /** for System uses */
    @Deprecated
    public ColorFragment() {
    }

    public static ColorFragment newInstance() {
        return new ColorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_color, container, false);
    }

}

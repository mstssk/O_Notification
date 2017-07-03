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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ColorizedFragment extends Fragment {

    CheckBox colorizedCheckbox;
    Spinner colorSpinner;


    /**
     * for System uses
     */
    @Deprecated
    public ColorizedFragment() {
    }

    public static ColorizedFragment newInstance() {
        return new ColorizedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colorized, container, false);

        colorizedCheckbox = view.findViewById(R.id.checkbox_colorized);
        colorSpinner = view.findViewById(R.id.spinner_color);
        ArrayAdapter<Color> colorArrayAdapter = new ArrayAdapter<Color>(getActivity(), android.R.layout.simple_spinner_item, Color.values());
        colorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorArrayAdapter);

        view.findViewById(R.id.notify_with_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWithColor();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.colorizedCheckbox = null;
        this.colorSpinner = null;
    }

    private void showWithColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NotifyUtils.CHANNEL_ID_COLOR,
                    "Colorized Channel", NotificationManagerCompat.IMPORTANCE_MAX);
            getActivity().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        Color color = ((Color) colorSpinner.getSelectedItem());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), NotifyUtils.CHANNEL_ID_COLOR)
                .setContentTitle(color.text + " notification")
                .setContentText("Content text")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColorized(colorizedCheckbox.isChecked())
                .setColor(color.color)
                .setOngoing(true)
//                .setStyle(new android.support.v7.app.NotificationCompat.MediaStyle())
                .setTimeoutAfter(10 * 1000);
        NotificationManagerCompat.from(getActivity()).notify(21, builder.build());
    }

    private enum Color {
        // DEFAULT("Default", null),
        RED("Red", 0xf44336),
        PINK("Pink", 0xe91e63),
        PURPLE("Purple", 0x9c27b0),
        DEEP_PURPLE("Deep Purple", 0x673ab7),
        INDIGO("Indigo", 0x3f51b5),
        BLUE("Blue", 0x2196f3),
        LIGHT("Light Blue", 0x03a9f4),
        CYAN("Cyan", 0x00bcd4),
        TEAL("Teal", 0x009688),
        GREEN("Green", 0x4caf50),
        LIGHT_GREEN("Light Green", 0x8bc34a),
        LIME("Lime", 0xcddc39),
        YELLOW("Yellow", 0xffeb3b),
        AMBER("Amber", 0xffc107),
        ORANGE("Orange", 0xff9800),
        DEEP_ORANGE("Deep Orange", 0xff5722),
        BROWN("Brown", 0x795548),
        GREY("Grey", 0x9e9e9e),
        BLUE_GREY("Blue Grey", 0x607d8),
        WHITE("White", 0xffffff),
        BLACK("Black", 1);

        String text;
        Integer color;

        Color(String text, Integer color) {
            this.text = text;
            this.color = color;
        }
    }
}

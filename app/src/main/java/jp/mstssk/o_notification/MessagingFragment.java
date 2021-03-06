package jp.mstssk.o_notification;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MessagingFragment extends Fragment {

    static final String TAG = "MessagingFragment";
    private static final int REQUEST_CODE_PICK_CONTACT = 1;

    ImageView contactImg;
    TextView contactName;
    private Uri mContactUri = null;

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

        contactName = view.findViewById(R.id.contact_name);
        contactImg = view.findViewById(R.id.image_preview);
        view.findViewById(R.id.button_choose_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessagingFragmentPermissionsDispatcher.chooseContactWithCheck(MessagingFragment.this);
            }
        });

        view.findViewById(R.id.show_channel_settings)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                            intent.putExtra(Settings.EXTRA_CHANNEL_ID, NotifyUtils.CHANNEL_ID_MESSAGING);
                            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Only for Android O.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MessagingFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void showMessaging() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ensureChannel();
        }

        NotificationCompat.Action action = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            Intent replyIntent = new Intent(getActivity(), MainActivity.class);
            PendingIntent replyPendingIntent =
                    PendingIntent.getActivity(getActivity(), 1,
                            replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteInput remoteInput = new RemoteInput.Builder(NotifyUtils.REMOTE_INPUT_KEY)
                    .setLabel("comment")
                    .build();
            action = new NotificationCompat.Action.Builder(R.drawable.ic_message,
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
                .setSmallIcon(R.drawable.ic_message)
                .setStyle(style);
        if (action != null) {
            builder.addAction(action);
        }
        if (mContactUri != null) {
            builder.setLargeIcon(ContactUtils.getContactBitmap(getActivity(), mContactUri));
            builder.addPerson(mContactUri.toString());
        }
        int messageCount = getInputMessageCount();
        if (messageCount > 0) {
            builder.setNumber(messageCount);
        }
        NotificationManagerCompat.from(getActivity()).notify(TAG, 31, builder.build());
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
            action = new Notification.Action.Builder(R.drawable.ic_message,
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
                .setSmallIcon(R.drawable.ic_message)
                .setStyle(style);
        if (action != null) {
            builder.addAction(action);
        }
        if (mContactUri != null) {
            builder.setLargeIcon(ContactUtils.getContactBitmap(getActivity(), mContactUri));
            builder.addPerson(mContactUri.toString());
        }
        int messageCount = getInputMessageCount();
        if (messageCount > 0) {
            builder.setNumber(messageCount);
        }
        getActivity().getSystemService(NotificationManager.class).notify(TAG, 32, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void ensureChannel() {
        NotificationChannel channel = new NotificationChannel(NotifyUtils.CHANNEL_ID_MESSAGING,
                "Messaging Channel", NotificationManagerCompat.IMPORTANCE_DEFAULT);
        getActivity().getSystemService(NotificationManager.class).createNotificationChannel(channel);
    }

    private int getInputMessageCount() {
        try {
            String input = ((EditText) getView().findViewById(R.id.message_count)).getText().toString();
            if (input.length() < 1) {
                return 0;
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "count is invalid.", Toast.LENGTH_SHORT).show();
        }
        return 0;
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    void chooseContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactUri = data.getData();
                    mContactUri = contactUri;
                    contactName.setText(ContactUtils.getContactName(getActivity(), contactUri));
                    contactImg.setImageURI(ContactUtils.getContactImageUri(getActivity(), contactUri));
                } else {
                    mContactUri = null;
                    contactName.setText(null);
                    contactImg.setImageResource(android.R.color.transparent);
                }
                break;
        }
    }

}

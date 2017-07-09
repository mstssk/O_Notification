package jp.mstssk.o_notification;

import android.app.Notification;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MainActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if (intent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Set<String> categories = intent.getCategories();
                if (categories != null && categories.contains(Notification.INTENT_CATEGORY_NOTIFICATION_PREFERENCES)) {
                    String channelId = intent.getStringExtra(Notification.EXTRA_CHANNEL_ID);
                    int position = getFragmentPositionByChannelId(channelId);
                    mViewPager.setCurrentItem(position);
                    setIntent(null); // consume a intent.
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
                if (remoteInput != null) {
                    setIntent(null); // consume a intent.
                    String str = remoteInput.getString(NotifyUtils.REMOTE_INPUT_KEY);
                    Toast.makeText(this, "getResultsFromIntent: " + str, Toast.LENGTH_LONG).show();
                }
                // See MessagingFragment
                NotificationManagerCompat.from(this).cancel(31);
                NotificationManagerCompat.from(this).cancel(32);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getFragmentPositionByChannelId(String channelId) {
        if (channelId == null) {
            return 0; // fallback
        } else switch (channelId) {
            case NotifyUtils.CHANNEL_ID_1ST:
            case NotifyUtils.CHANNEL_ID_2ND:
            case NotifyUtils.CHANNEL_ID_CONFIG:
            case NotifyUtils.CHANNEL_GROUP_ID_FOO:
            case NotifyUtils.CHANNEL_ID_3RD_WITH_GROUP_FOO:
            case NotifyUtils.CHANNEL_ID_4th_WITH_GROUP_FOO:
                return 0;
            case NotifyUtils.CHANNEL_ID_TIMEOUT:
                return 1;
            case NotifyUtils.CHANNEL_ID_COLOR:
                return 2;
            case NotifyUtils.CHANNEL_ID_MESSAGING:
                return 4;
        }
        return 0; // fallback
    }

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ChannelFragment.newInstance();
                case 1:
                    return TimeoutFragment.newInstance();
                case 2:
                    return ColorizedFragment.newInstance();
                case 3:
                    return MessagingFragment.newInstance();
            }
            throw new IllegalArgumentException("position:" + position + " is not implemented.");
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}

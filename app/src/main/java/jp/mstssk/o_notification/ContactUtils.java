package jp.mstssk.o_notification;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ContactUtils {

    private static final String TAG = "ContactUtils";

    static String getContactName(Context c, Uri contactUri) {
        if (contactUri == null) {
            return null;
        }
        Cursor cursor = c.getContentResolver().query(contactUri, null, null, null,
                null);
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        int idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        return cursor.getString(idx);
    }

    static Uri getContactImageUri(Context c, Uri contactUri) {
        if (contactUri == null) {
            return null;
        }
        Cursor cursor = c.getContentResolver().query(contactUri, null, null, null,
                null);
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        int idx = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID);
        String hasPhoto = cursor.getString(idx);
        return Uri.withAppendedPath(contactUri,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }

    static Bitmap getContactBitmap(Context context, Uri contactUri) {
        InputStream is = null;
        try {
            is = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), contactUri);
            if (is == null) {
                return null;
            }
            return BitmapFactory.decodeStream(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        }
    }
}

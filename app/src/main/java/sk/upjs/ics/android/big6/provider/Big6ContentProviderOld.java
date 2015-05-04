package sk.upjs.ics.android.big6.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import java.util.List;

import sk.upjs.ics.android.util.Defaults;

import static sk.upjs.ics.android.util.Defaults.ALL_COLUMNS;
import static sk.upjs.ics.android.util.Defaults.AUTOGENERATED_ID;
import static sk.upjs.ics.android.util.Defaults.NO_CONTENT_OBSERVER;
import static sk.upjs.ics.android.util.Defaults.NO_GROUP_BY;
import static sk.upjs.ics.android.util.Defaults.NO_HAVING;
import static sk.upjs.ics.android.util.Defaults.NO_SELECTION;
import static sk.upjs.ics.android.util.Defaults.NO_SELECTION_ARGS;
import static sk.upjs.ics.android.util.Defaults.NO_SORT_ORDER;

public class Big6ContentProviderOld extends ContentProvider {
    public static final String AUTHORITY = "sk.upjs.ics.android.big6.Big6ContentProvider";

    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(Provider.Big6.TABLE_NAME)
            .build();

    private static final String MIME_TYPE_TRAINING_HISTORY = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + Provider.Big6.TABLE_NAME;

    public static final int URI_MATCH_TRAININGS = 0;


    private UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String[] COLUMN_NAMES = new String[]{
            Provider.Big6._ID,
            Provider.Big6.YEAR,
            Provider.Big6.MONTH,
            Provider.Big6.DAY,
            Provider.Big6.TRAINING,
            Provider.Big6.TYPE
    };
    private DatabaseOpenHelper databaseOpenHelper;

    @Override
    public boolean onCreate() {
        uriMatcher.addURI(AUTHORITY, Provider.Big6.TABLE_NAME, URI_MATCH_TRAININGS);

        databaseOpenHelper = new DatabaseOpenHelper(this.getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch(uriMatcher.match(uri)) {
            case URI_MATCH_TRAININGS:
                Cursor cursor = getTrainingsCursor();
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                return null;
        }
    }

    private Cursor getTrainingsCursor(){
        SQLiteDatabase db = databaseOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(Provider.Big6.TABLE_NAME,
                ALL_COLUMNS,
                NO_SELECTION,
                NO_SELECTION_ARGS,
                NO_GROUP_BY,
                NO_HAVING,
                NO_SORT_ORDER);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        return MIME_TYPE_TRAINING_HISTORY;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch(uriMatcher.match(uri)){
            case URI_MATCH_TRAININGS:
                List<String> segments = uri.getPathSegments();
                int year = Integer.parseInt(segments.get(1));
                int month = Integer.parseInt(segments.get(2));
                int day = Integer.parseInt(segments.get(3));
                String trainingvalues = segments.get(4);
                int type = Integer.parseInt(segments.get(5));

                ContentValues newContentValues = new ContentValues();
                newContentValues.put(Provider.Big6._ID, AUTOGENERATED_ID);
                newContentValues.put(Provider.Big6.YEAR, year);
                newContentValues.put(Provider.Big6.MONTH, month);
                newContentValues.put(Provider.Big6.DAY, day);
                newContentValues.put(Provider.Big6.TRAINING, trainingvalues);
                newContentValues.put(Provider.Big6.TYPE, type);

                databaseOpenHelper.getWritableDatabase()
                        .insert(Provider.Big6.TABLE_NAME, Defaults.NO_NULL_COLUMN_HACK, newContentValues);
                Uri monthUri = CONTENT_URI
                        .buildUpon()
                        .appendPath(String.valueOf(year))
                        .appendPath(String.valueOf(month))
                        .build();
                getContext()
                        .getContentResolver()
                        .notifyChange(monthUri, NO_CONTENT_OBSERVER);

                return uri;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

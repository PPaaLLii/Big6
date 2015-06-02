package sk.upjs.ics.android.big6.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;
import sk.upjs.ics.android.util.Defaults;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory;
import static sk.upjs.ics.android.big6.provider.Big6Provider.PhotoUri;
import static sk.upjs.ics.android.util.Defaults.ALL_COLUMNS;
import static sk.upjs.ics.android.util.Defaults.NO_CONTENT_OBSERVER;
import static sk.upjs.ics.android.util.Defaults.NO_GROUP_BY;
import static sk.upjs.ics.android.util.Defaults.NO_HAVING;
import static sk.upjs.ics.android.util.Defaults.NO_SELECTION;
import static sk.upjs.ics.android.util.Defaults.NO_SELECTION_ARGS;
import static sk.upjs.ics.android.util.Defaults.NO_SORT_ORDER;
import static sk.upjs.ics.android.util.Defaults.NO_NULL_COLUMN_HACK;


public class Big6ContentProvider extends ContentProvider {

    public static final String ALL_ROWS = null;

    public static final String AUTHORITY = "sk.upjs.ics.android.big6.provider.Big6ContentProvider";

    public static final Uri TRAINING_HISTORY_CONTENT_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(Big6Provider.TrainingHistory.TABLE_NAME)
            .build();

    public static final Uri PHOTO_URI_CONTENT_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(Big6Provider.PhotoUri.TABLE_NAME)
            .build();

    private static final String MIME_TYPE_TRAINING_HISTORY = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + Big6Provider.TrainingHistory.TABLE_NAME;

    private static final String MIME_TYPE_PHOTO_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + Big6Provider.PhotoUri.TABLE_NAME;

    public static final int URI_MATCH_TRAININGS = 0;

    public static final int URI_MATCH_PHOTO = 1;

    private UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public Big6ContentProvider() {
    }


    private DatabaseOpenHelper databaseHelper;

    @Override
    public boolean onCreate() {

        uriMatcher.addURI(AUTHORITY, Big6Provider.TrainingHistory.TABLE_NAME, URI_MATCH_TRAININGS);
        uriMatcher.addURI(AUTHORITY, Big6Provider.PhotoUri.TABLE_NAME, URI_MATCH_PHOTO);

        databaseHelper = new DatabaseOpenHelper(this.getContext());

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
            case URI_MATCH_PHOTO:
                Cursor cursor1 = getPhotoCursor();
                cursor1.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor1;
            default:
                return null;
        }
    }

    private Cursor getPhotoCursor() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(Big6Provider.PhotoUri.TABLE_NAME,
                ALL_COLUMNS,
                NO_SELECTION,
                NO_SELECTION_ARGS,
                NO_GROUP_BY,
                NO_HAVING,
                NO_SORT_ORDER);
        return cursor;
    }

    private Cursor getTrainingsCursor(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(Big6Provider.TrainingHistory.TABLE_NAME,
                ALL_COLUMNS,
                NO_SELECTION,
                NO_SELECTION_ARGS,
                NO_GROUP_BY,
                NO_HAVING,
                NO_SORT_ORDER);
        return cursor;
    }

    @Override // only for Training History
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long id = ContentUris.parseId(uri);
        if (id != -1l) {
            int affectedRows = databaseHelper.getWritableDatabase()
                .delete(TrainingHistory.TABLE_NAME, TrainingHistory._ID + " = " + id, Defaults.NO_SELECTION_ARGS);
            getContext().getContentResolver().notifyChange(TRAINING_HISTORY_CONTENT_URI, NO_CONTENT_OBSERVER);
            return affectedRows;
        }
        //else delete all items in table
        // http://stackoverflow.com/questions/19183294/what-is-the-best-way-in-android-to-delete-all-rows-from-a-table
        // https://groups.google.com/forum/#!topic/android-developers/wK5gZ-VxcSg
        int affectedRows = databaseHelper.getWritableDatabase()
                .delete(TrainingHistory.TABLE_NAME, ALL_ROWS, Defaults.NO_SELECTION_ARGS);
        getContext().getContentResolver().notifyChange(TRAINING_HISTORY_CONTENT_URI, NO_CONTENT_OBSERVER);
        return affectedRows;
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)){
            case URI_MATCH_TRAININGS:
                return MIME_TYPE_TRAINING_HISTORY;
            case URI_MATCH_PHOTO:
                return MIME_TYPE_PHOTO_URI;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.w(getClass().getName(), "INSERT at: " + uri);
        switch (uriMatcher.match(uri)) {
            case URI_MATCH_TRAININGS:
                Calendar calendar = Calendar.getInstance();
                ContentValues contentValues = new ContentValues();
                contentValues.put(TrainingHistory._ID, Defaults.AUTOGENERATED_ID);
                contentValues.put(TrainingHistory.YEAR, calendar.get(Calendar.YEAR));
                contentValues.put(TrainingHistory.MONTH, calendar.get(Calendar.MONTH)+1);
                contentValues.put(TrainingHistory.DAY, calendar.get(Calendar.DAY_OF_MONTH));
                contentValues.put(TrainingHistory.TRAINING, values.getAsString(TrainingHistory.TRAINING));
                contentValues.put(TrainingHistory.TYPE, values.getAsString(TrainingHistory.TYPE));

                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                long newId = db.insert(TrainingHistory.TABLE_NAME, NO_NULL_COLUMN_HACK, contentValues);
                getContext().getContentResolver().notifyChange(TRAINING_HISTORY_CONTENT_URI, NO_CONTENT_OBSERVER);
                return ContentUris.withAppendedId(TRAINING_HISTORY_CONTENT_URI, newId);

            case URI_MATCH_PHOTO:
                System.out.println("UriMatchPhoto!");
                Log.e(getClass().getName(), "UriMatchPhoto!");
                Calendar calendar1 = Calendar.getInstance();
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put(PhotoUri._ID, Defaults.AUTOGENERATED_ID);
                contentValues1.put(PhotoUri.URI, values.getAsString(PhotoUri.URI));
                contentValues1.put(PhotoUri.YEAR, calendar1.get(Calendar.YEAR));
                contentValues1.put(PhotoUri.MONTH, calendar1.get(Calendar.MONTH)+1);
                contentValues1.put(PhotoUri.DAY, calendar1.get(Calendar.DAY_OF_MONTH));
                contentValues1.put(PhotoUri.DESCRIPTION, values.getAsString(PhotoUri.DESCRIPTION));

                SQLiteDatabase db1 = databaseHelper.getWritableDatabase();
                long newId1 = db1.insert(PhotoUri.TABLE_NAME, NO_NULL_COLUMN_HACK, contentValues1);
                getContext().getContentResolver().notifyChange(PHOTO_URI_CONTENT_URI, NO_CONTENT_OBSERVER);
                Log.w(getClass().getName(), "vkladam uri: " + values.getAsString(PhotoUri.URI));
                return ContentUris.withAppendedId(PHOTO_URI_CONTENT_URI, newId1);
        }
        return null;
    }

    /*@Override
    public Uri insert(Uri uri, ContentValues values) {
        Calendar calendar = Calendar.getInstance();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TrainingHistory._ID, Defaults.AUTOGENERATED_ID);
        contentValues.put(TrainingHistory.YEAR, calendar.get(Calendar.YEAR));
        contentValues.put(TrainingHistory.MONTH, calendar.get(Calendar.MONTH));
        contentValues.put(TrainingHistory.DAY, calendar.get(Calendar.DAY_OF_MONTH));
        contentValues.put(TrainingHistory.TRAINING, values.getAsString(TrainingHistory.TRAINING));
        contentValues.put(TrainingHistory.TYPE, values.getAsString(TrainingHistory.TYPE));

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long newId = db.insert(TrainingHistory.TABLE_NAME, NO_NULL_COLUMN_HACK, contentValues);
        getContext().getContentResolver().notifyChange(CONTENT_URI, NO_CONTENT_OBSERVER);
        return ContentUris.withAppendedId(CONTENT_URI, newId);
    }*/

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

package sk.upjs.ics.android.big6;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Calendar;

import de.ecotastic.android.camerautil.lib.CameraIntentHelperActivity;
import de.ecotastic.android.camerautil.util.BitmapHelper;
import sk.upjs.ics.android.big6.provider.Big6ContentProvider;
import sk.upjs.ics.android.util.Defaults;

import static sk.upjs.ics.android.big6.provider.Big6Provider.PhotoUri;

//http://stackoverflow.com/questions/459729/how-to-display-a-list-of-images-in-a-listview-in-android
//http://stackoverflow.com/questions/14785806/android-how-to-make-an-activity-return-results-to-the-activity-which-calls-it
//http://developer.android.com/training/basics/intents/result.html

public class PhotoActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID_PHOTO_URI = 1;
    public static final Bundle NO_BUNDLE = null;
    public static final int INSERT_PHOTO_TOKEN = 1;
    public static final int REQUEST_CODE = 0;
    private static final int PICK_CONTACT_REQUEST = 0;
    private GridView gridView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        gridView = (GridView) findViewById(R.id.ImagesGridView);
        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w(getClass().getName(), "On photo click!");
                Intent intent = new Intent(PhotoActivity.this, PhotoDetailActivity.class);
                intent.putExtra("photo", imageAdapter.getItem(position).getUri());
                intent.putExtra("description", imageAdapter.getItem(position).getDescription());
                intent.putExtra("year", imageAdapter.getItem(position).getYear());
                intent.putExtra("month", imageAdapter.getItem(position).getMonth());
                intent.putExtra("day", imageAdapter.getItem(position).getDay());
                startActivityForResult(intent, REQUEST_CODE);
            }
        });*/

        imageAdapter = new ImageAdapter(this, this);
        gridView.setAdapter(imageAdapter);

        getLoaderManager().initLoader(LOADER_ID_PHOTO_URI, NO_BUNDLE, this);
    }


    public void onShootButtonClick(View v) {
        startCameraIntent();
    }

    private void startCameraIntent() {
        Intent takePhoto = new Intent(PhotoActivity.this, TakePhotoActivity.class);
        startActivityForResult(takePhoto, PICK_CONTACT_REQUEST);
    }

    private void insertIntoContentProvider(String uri) {
        Uri contentUri = Big6ContentProvider.PHOTO_URI_CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(PhotoUri.URI, uri);
        //Log.w(getClass().getName(),"uri to store: "+ uri.toString());
        values.put(PhotoUri.DESCRIPTION, "");

        AsyncQueryHandler insertHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                getLoaderManager().restartLoader(LOADER_ID_PHOTO_URI, NO_BUNDLE, PhotoActivity.this);
                Toast.makeText(PhotoActivity.this, "Photo saved!", Toast.LENGTH_SHORT).show();
            }
        };

        insertHandler.startInsert(INSERT_PHOTO_TOKEN, Defaults.NO_COOKIE, contentUri, values);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    String uri = res.getString("param_result");
                    //Log.d(PhotoActivity.class.getName(), "result:"+uri);
                    insertIntoContentProvider(uri);
                    getLoaderManager().restartLoader(LOADER_ID_PHOTO_URI, NO_BUNDLE, this);
                    //TODO: insert description
                }else{
                    //Log.d(PhotoActivity.class.getName(), "ERROR!!!");
                    //Toast.makeText(this, "Sorry, there was a problem. Please try again!", Toast.LENGTH_SHORT).show();
                    //getLoaderManager().restartLoader(LOADER_ID_PHOTO_URI, NO_BUNDLE, this);
                }
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(Big6ContentProvider.PHOTO_URI_CONTENT_URI);
        //Log.w(getClass().getName(), "Loader uri: " +loader.getUri().toString());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null){
            //Log.e(getClass().getName(), "Cursor je null!!!");
        }else {
            imageAdapter.clear();
            while (cursor.moveToNext()) {
                Photo photo = new Photo();
                photo.setUri(cursor.getString(cursor.getColumnIndex(PhotoUri.URI)));
                photo.setDescription(cursor.getString(cursor.getColumnIndex(PhotoUri.DESCRIPTION)));
                photo.setYear(cursor.getString(cursor.getColumnIndex(PhotoUri.YEAR)));
                photo.setMonth(cursor.getString(cursor.getColumnIndex(PhotoUri.MONTH)));
                photo.setDay(cursor.getString(cursor.getColumnIndex(PhotoUri.DAY)));
                photo.setId(cursor.getString(cursor.getColumnIndex(PhotoUri._ID)));
                imageAdapter.insert(photo, 0);
            }
            cursor.close();
            imageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Log.w(getClass().getName(), "onLoadReset!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LOADER_ID_PHOTO_URI, NO_BUNDLE, this);
    }
}

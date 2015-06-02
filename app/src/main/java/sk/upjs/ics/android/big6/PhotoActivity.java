package sk.upjs.ics.android.big6;

import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Calendar;

import de.ecotastic.android.camerautil.lib.CameraIntentHelperActivity;
import de.ecotastic.android.camerautil.util.BitmapHelper;
import sk.upjs.ics.android.big6.provider.Big6ContentProvider;
import sk.upjs.ics.android.util.Defaults;

import static sk.upjs.ics.android.big6.provider.Big6Provider.PhotoUri;

//http://stackoverflow.com/questions/459729/how-to-display-a-list-of-images-in-a-listview-in-android
public class PhotoActivity extends CameraIntentHelperActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID_PHOTO_URI = 1;
    public static final Bundle NO_BUNDLE = null;
    public static final int INSERT_PHOTO_TOKEN = 1;
    public static final int REQUEST_CODE = 0;
    private ListView imageView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageView = (ListView) findViewById(R.id.ImagesListView);
        imageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PhotoActivity.this, PhotoDetailActivity.class);
                intent.putExtra("photo", imageAdapter.getItem(position).getUri());
                intent.putExtra("description", imageAdapter.getItem(position).getDescription());
                intent.putExtra("year", imageAdapter.getItem(position).getYear());
                intent.putExtra("month", imageAdapter.getItem(position).getMonth());
                intent.putExtra("day", imageAdapter.getItem(position).getDay());
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        imageAdapter = new ImageAdapter(this);
        imageView.setAdapter(imageAdapter);

        getLoaderManager().initLoader(LOADER_ID_PHOTO_URI, NO_BUNDLE, this);
    }


    public void onShootButtonClick(View v) {
        startCameraIntent();
    }

    @Override
    protected void onPhotoUriFound() {
        super.onPhotoUriFound();
        Photo photo = new Photo();
        Calendar cal = Calendar.getInstance();
        photo.setYear(String.valueOf(cal.get(Calendar.YEAR)));
        photo.setMonth(String.valueOf(cal.get(Calendar.MONTH) + 1));
        photo.setDay(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        photo.setUri(photoUri.toString());
        photo.setDescription("");

        //photo = BitmapHelper.shrinkBitmap(photo, 300, this.rotateXDegrees);
        imageAdapter.insert(photo, 0);
        imageAdapter.notifyDataSetChanged();
        insertIntoContentProvider(photoUri);
        Log.w(getClass().getName(), "photo uri: " + photoUri.toString());
        //TODO: insert description

        //Delete photo in second location (if applicable)
        if (this.preDefinedCameraUri != null && !this.preDefinedCameraUri.equals(this.photoUri)) {
            BitmapHelper.deleteImageWithUriIfExists(this.preDefinedCameraUri, this);
        }
        //Delete photo in third location (if applicable)
        if (this.photoUriIn3rdLocation != null) {
            BitmapHelper.deleteImageWithUriIfExists(this.photoUriIn3rdLocation, this);
        }
    }

    private void insertIntoContentProvider(Uri uri) {
        Uri contentUri = Big6ContentProvider.PHOTO_URI_CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(PhotoUri.URI, uri.toString());
        Log.w(getClass().getName(),"uri to store: "+ uri.toString());
        values.put(PhotoUri.DESCRIPTION, "");

        AsyncQueryHandler insertHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(PhotoActivity.this, "Photo saved!", Toast.LENGTH_SHORT).show();
            }
        };

        insertHandler.startInsert(INSERT_PHOTO_TOKEN, Defaults.NO_COOKIE, contentUri, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(Big6ContentProvider.PHOTO_URI_CONTENT_URI);
        Log.w(getClass().getName(), "Loader uri: " +loader.getUri().toString());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null){
            Log.e(getClass().getName(), "Cursor je null!!!");
        }else {
            while (cursor.moveToNext()) {
                Photo photo = new Photo();
                photo.setUri(cursor.getString(cursor.getColumnIndex(PhotoUri.URI)));
                photo.setDescription(cursor.getString(cursor.getColumnIndex(PhotoUri.DESCRIPTION)));
                photo.setYear(cursor.getString(cursor.getColumnIndex(PhotoUri.YEAR)));
                photo.setMonth(cursor.getString(cursor.getColumnIndex(PhotoUri.MONTH)));
                photo.setDay(cursor.getString(cursor.getColumnIndex(PhotoUri.DAY)));
                imageAdapter.insert(photo, 0);
            }
            cursor.close();
            imageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.w(getClass().getName(), "onLoadReset!");
    }
}

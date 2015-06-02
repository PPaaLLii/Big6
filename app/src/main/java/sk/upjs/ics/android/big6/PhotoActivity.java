package sk.upjs.ics.android.big6;

import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
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
    private ListView imageView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageView = (ListView) findViewById(R.id.ImagesListView);

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
        Bitmap photo = BitmapHelper.readBitmap(this, this.photoUri);
        if (photo != null) {
            photo = BitmapHelper.shrinkBitmap(photo, 300, this.rotateXDegrees);
            imageAdapter.insert(photo, 0);
            imageAdapter.notifyDataSetChanged();
            insertIntoContentProvider(photoUri);
            Log.w(getClass().getName(), "photo uri: " + photoUri.toString());
        }else{
            Log.w(getClass().getName(), "photo is null");
        }

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
        ArrayList<Uri> photos = new ArrayList<>();
        if(cursor == null){
            Log.e(getClass().getName(), "Cursor je null!!!");
        }else {
            while (cursor.moveToNext()) {
                Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(PhotoUri.URI)));
                photos.add(uri);
                Log.w(getClass().getName(), "Uri in cursor: " + uri.toString());
            }
            cursor.close();
            //imageAdapter = new ImageAdapter(this);

            for(Uri uri: photos){
                Bitmap photo = BitmapHelper.readBitmap(this, uri);
                if (photo != null) {
                    photo = BitmapHelper.shrinkBitmap(photo, 300, this.rotateXDegrees);
                    imageAdapter.insert(photo, 0);
                }
            }
            imageAdapter.notifyDataSetChanged();

            //imageView.invalidate();
            //imageView.setAdapter(imageAdapter);
            //Log.w(getClass().getName(), String.valueOf(imageAdapter.getCount()));

            //Log.w(getClass().getName(), "onLoadFinished!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.w(getClass().getName(), "onLoadReset!");
    }
}

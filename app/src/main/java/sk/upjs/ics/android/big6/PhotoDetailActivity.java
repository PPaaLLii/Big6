package sk.upjs.ics.android.big6;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.ecotastic.android.camerautil.util.BitmapHelper;
import sk.upjs.ics.android.big6.provider.Big6ContentProvider;
import sk.upjs.ics.android.util.Defaults;

public class PhotoDetailActivity extends ActionBarActivity {

    private static final int DELETE_PHOTO_TOKEN = 0;
    private String uri;
    private String description;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        uri =  (String) getIntent().getSerializableExtra("photo");
        description = (String) getIntent().getSerializableExtra("description");
        String year = (String) getIntent().getSerializableExtra("year");
        String month = (String) getIntent().getSerializableExtra("month");
        String day = (String) getIntent().getSerializableExtra("day");
        id = (String) getIntent().getSerializableExtra("id");

        Bitmap photo = BitmapHelper.readBitmap(this, Uri.parse(uri));

        ImageView photoDetailImageView = (ImageView) findViewById(R.id.photoDetailImageView);
        TextView photoDescriptionTextView = (TextView) findViewById(R.id.photoDescriptionTextView);
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);


        if (photo != null){
            photoDetailImageView.setImageBitmap(photo);
        }

        photoDescriptionTextView.setText(description);
        dateTextView.setText(day + "."+month+"."+year);
        //TODO: localization?

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_share:
                share();
                return true;
            case R.id.action_delete:
                delete();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void delete() {
        //TODO leak?
        AsyncQueryHandler deleteHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                notifyPhotoActivityToClearAdapter();
                Toast.makeText(PhotoDetailActivity.this, "Photo was deleted!", Toast.LENGTH_SHORT)
                        .show();
            }
        };
        Uri photo = ContentUris.withAppendedId(Big6ContentProvider.PHOTO_URI_CONTENT_URI, Long.parseLong(id));
        //Log.w(getClass().getName(), "deleting uri: " + photo.toString());
        deleteHandler.startDelete(DELETE_PHOTO_TOKEN, Defaults.NO_COOKIE, photo,
                Defaults.NO_SELECTION, Defaults.NO_SELECTION_ARGS);
        finish();
    }

    private void notifyPhotoActivityToClearAdapter() {
        //TODO: implement
    }

    private void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setDataAndType(Uri.parse(uri), "image/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, description);
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }
}

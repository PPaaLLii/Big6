package sk.upjs.ics.android.big6;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import de.ecotastic.android.camerautil.lib.CameraIntentHelperActivity;
import de.ecotastic.android.camerautil.util.BitmapHelper;

//http://stackoverflow.com/questions/459729/how-to-display-a-list-of-images-in-a-listview-in-android
public class PhotoActivity extends CameraIntentHelperActivity {

    private ListView imagesView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imagesView = (ListView) findViewById(R.id.ImagesListView);

        imageAdapter = new ImageAdapter(this);
        imagesView.setAdapter(imageAdapter);
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
}

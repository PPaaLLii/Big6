package sk.upjs.ics.android.big6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.ecotastic.android.camerautil.util.BitmapHelper;

/**
 * Created by Pavol on 30. 5. 2015.
 */
//http://stackoverflow.com/questions/6814575/image-adapter-adding-imageviews-dynamically-to-gridview-without-cursor
//http://www.learn2crack.com/2014/01/android-custom-gridview.html

public class ImageAdapter extends BaseAdapter {

    private static final int REQUEST_CODE = 0;
    private Context mContext;
    private ArrayList<Photo> photos = new ArrayList<>();
    private Activity activity;

    public ImageAdapter(Context c, Activity activity) {
        mContext = c;
        this.activity = activity;
    }

    public int getCount() {
        return photos.size();
    }

    public void insert(Photo photo, int position){
        photos.add(position, photo);
    }

    public Photo getItem(int position) {
        return photos.get(position);
    }

    public long getItemId(int position) {
        return position;    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = inflater.inflate(R.layout.photo_grid_item, parent, false);
            TextView dateTextView = (TextView) grid.findViewById(R.id.photoGridItemDate);
            ImageView imageView = (ImageView) grid.findViewById(R.id.photoGridItemImageView);

            final Photo photo = photos.get(position);

            //TODO: stringBuilder implementation in photo class
            String date = photo.getDay() + "." +photo.getMonth()+ "." + photo.getYear();
            //Log.w(ImageAdapter.class.getName(), "date: " + date);
            dateTextView.setText(date);

            Bitmap photo1 = BitmapHelper.readBitmap(mContext, Uri.parse(photos.get(position).getUri()));
            if (photo != null){
                imageView.setImageBitmap(photo1);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.w(getClass().getName(), "On photo click!");
                    Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                    intent.putExtra("photo", photo.getUri());
                    intent.putExtra("description", photo.getDescription());
                    intent.putExtra("year", photo.getYear());
                    intent.putExtra("month", photo.getMonth());
                    intent.putExtra("day", photo.getDay());
                    intent.putExtra("id", photo.getId());
                    activity.startActivityForResult(intent, REQUEST_CODE);
                }
            });

        } else {
            grid = (View) convertView;
        }

        return grid;

        /* if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(600, 450));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        Bitmap photo = BitmapHelper.readBitmap(mContext, Uri.parse(photos.get(position).getUri()));
        if (photo != null){
            imageView.setImageBitmap(photo);
        }
        return imageView; */
    }

    public void clear() {
        this.photos.clear();
    }

    /*private void get_images(){
        File directory = new File(Variables.PATH_FOTOS);

        File[] archivos =directory.listFiles();
        photos = new Bitmap[archivos.length];

        for (int cont=0; cont<archivos.length;cont++){

            File imgFile = new  File(archivos[cont].toString());
            photos[cont] = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
    }*/
}
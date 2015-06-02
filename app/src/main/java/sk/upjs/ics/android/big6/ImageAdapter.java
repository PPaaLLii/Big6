package sk.upjs.ics.android.big6;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import de.ecotastic.android.camerautil.util.BitmapHelper;

/**
 * Created by Pavol on 30. 5. 2015.
 */
//http://stackoverflow.com/questions/6814575/image-adapter-adding-imageviews-dynamically-to-gridview-without-cursor

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Photo> photos = new ArrayList<>();

    public ImageAdapter(Context c) {
        mContext = c;    }

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
        ImageView imageView;
        if (convertView == null) {
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
        return imageView;
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
package sk.upjs.ics.android.big6;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Pavol on 2. 6. 2015.
 */
public class Photo {

    private String uri;

    private String description;

    private String year;

    private String month;

    private String day;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
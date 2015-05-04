package sk.upjs.ics.android.big6.provider;

/**
 * Created by Pavol on 23. 4. 2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

import sk.upjs.ics.android.util.Defaults;

import static sk.upjs.ics.android.util.Defaults.DEFAULT_CURSOR_FACTORY;
import static sk.upjs.ics.android.util.Defaults.NO_NULL_COLUMN_HACK;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "bix6";
    public static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context){
        super(context, DATABASE_NAME, DEFAULT_CURSOR_FACTORY, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s TEXT,"
                + "%s INTEGER"
                + ")";
        String sql = String.format(sqlTemplate,
                Provider.Big6.TABLE_NAME,
                Provider.Big6._ID,
                Provider.Big6.YEAR,
                Provider.Big6.MONTH,
                Provider.Big6.DAY,
                Provider.Big6.TRAINING,
                Provider.Big6.TYPE);

        db.execSQL(sql);

        Calendar calendar = Calendar.getInstance();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Provider.Big6._ID, Defaults.AUTOGENERATED_ID);
        contentValues.put(Provider.Big6.YEAR, calendar.get(Calendar.YEAR));
        contentValues.put(Provider.Big6.MONTH, calendar.get(Calendar.MONTH));
        System.out.println(calendar.get(Calendar.MONTH));
        contentValues.put(Provider.Big6.DAY, calendar.get(Calendar.DAY_OF_MONTH));
        contentValues.put(Provider.Big6.TRAINING, "10,2,10,2,-1,15,3,15,3,10,3");
        contentValues.put(Provider.Big6.TYPE, 0);

        db.insert(Provider.Big6.TABLE_NAME, NO_NULL_COLUMN_HACK, contentValues);

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(Provider.Big6._ID, Defaults.AUTOGENERATED_ID);
        contentValues1.put(Provider.Big6.YEAR, calendar.get(Calendar.YEAR));
        contentValues1.put(Provider.Big6.MONTH, calendar.get(Calendar.MONTH));
        //FIXME fix month-1
        contentValues1.put(Provider.Big6.DAY, calendar.get(Calendar.DAY_OF_MONTH));
        contentValues1.put(Provider.Big6.TRAINING, "10,2,10,2,-1,20,3,20,3,20,3");
        contentValues1.put(Provider.Big6.TYPE, 0);

        db.insert(Provider.Big6.TABLE_NAME, NO_NULL_COLUMN_HACK, contentValues1);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
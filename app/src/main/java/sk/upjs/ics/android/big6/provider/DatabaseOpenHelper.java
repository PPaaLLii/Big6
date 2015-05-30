package sk.upjs.ics.android.big6.provider;

/**
 * Created by Pavol on 23. 4. 2015.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static sk.upjs.ics.android.util.Defaults.DEFAULT_CURSOR_FACTORY;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "big6";
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
                Big6Provider.TrainingHistory.TABLE_NAME,
                Big6Provider.TrainingHistory._ID,
                Big6Provider.TrainingHistory.YEAR,
                Big6Provider.TrainingHistory.MONTH,
                Big6Provider.TrainingHistory.DAY,
                Big6Provider.TrainingHistory.TRAINING,
                Big6Provider.TrainingHistory.TYPE);

        db.execSQL(sql);

        String sqlTemplate1 = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s TEXT"
                + ")";
        String sql1 = String.format(sqlTemplate1,
                Big6Provider.PhotoUri.TABLE_NAME,
                Big6Provider.PhotoUri._ID,
                Big6Provider.PhotoUri.URI,
                Big6Provider.PhotoUri.YEAR,
                Big6Provider.PhotoUri.MONTH,
                Big6Provider.PhotoUri.DAY,
                Big6Provider.PhotoUri.DESCRIPTION);

        db.execSQL(sql1);

        /*Calendar calendar = Calendar.getInstance();

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
        contentValues1.put(Provider.Big6.DAY, calendar.get(Calendar.DAY_OF_MONTH));
        contentValues1.put(Provider.Big6.TRAINING, "10,2,10,2,-1,20,3,20,3,20,3");
        contentValues1.put(Provider.Big6.TYPE, 0);

        db.insert(Provider.Big6.TABLE_NAME, NO_NULL_COLUMN_HACK, contentValues1);*/
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}

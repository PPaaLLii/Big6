package sk.upjs.ics.android.big6;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import sk.upjs.ics.android.big6.provider.Big6ContentProvider;
import sk.upjs.ics.android.util.Defaults;


public class SettingsActivity extends PreferenceActivity {

    public static final int DELETE_TRAINING_HISTORY_TOKEN = 0;
    public static final int DELETE_PHOTOS_TOKEN = 1;
    public static final long ALL_IDS = -1l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        addPreferencesFromResource(R.xml.settings);

        //http://stackoverflow.com/questions/5330677/android-preferences-onclick-event
        //http://www.mkyong.com/android/android-alert-dialog-example/
        Preference myPref = (Preference) findPreference("deleteDatabaseButton");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(context)
                        .setTitle("Warning!")
                        .setMessage("This will delete all your Training History! \n Do you want to continue?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteAllHistory();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        })
                        .show();
                return true;
            }
        });

        Preference myPref1 = (Preference) findPreference("deletePhotosButton");
        myPref1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(context)
                        .setTitle("Warning!")
                        .setMessage("This will delete all your Photos in this application! \n Photos won't be deleted from phone! \n Do you want to continue?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteAllPhotos();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    private void deleteAllHistory() {
        //TODO leak?
        AsyncQueryHandler deleteHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                Toast.makeText(SettingsActivity.this, "Training history database was deleted", Toast.LENGTH_SHORT)
                        .show();
            }
        };
        Uri trainingsHistory = Big6ContentProvider.TRAINING_HISTORY_CONTENT_URI;
        Log.w(getClass().getName(), "uri: "+trainingsHistory.toString());
        deleteHandler.startDelete(DELETE_TRAINING_HISTORY_TOKEN, Defaults.NO_COOKIE, trainingsHistory,
                Defaults.NO_SELECTION, Defaults.NO_SELECTION_ARGS);
    }

    private void deleteAllPhotos() {
        //TODO leak?
        AsyncQueryHandler deleteHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                notifyPhotoActivityToClearAdapter();
                Toast.makeText(SettingsActivity.this, "Photos was deleted!", Toast.LENGTH_SHORT)
                        .show();
            }
        };
        Uri photos = Big6ContentProvider.PHOTO_URI_CONTENT_URI;
        Log.w(getClass().getName(), "uri: "+photos.toString());
        deleteHandler.startDelete(DELETE_PHOTOS_TOKEN, Defaults.NO_COOKIE, photos,
                Defaults.NO_SELECTION, Defaults.NO_SELECTION_ARGS);
    }

    private void notifyPhotoActivityToClearAdapter() {
        //TODO: Implement!
    }
}

package sk.upjs.ics.android.big6;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.prefs.Preferences;

import sk.upjs.ics.android.big6.R;
import sk.upjs.ics.android.big6.provider.TrainingsContentProvider;
import sk.upjs.ics.android.util.Defaults;


public class SettingsActivity extends PreferenceActivity {

    public static final int DELETE_NOTE_TOKEN = 0;
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
        Uri selectedNoteUri = ContentUris.withAppendedId(TrainingsContentProvider.CONTENT_URI, ALL_IDS);
        deleteHandler.startDelete(DELETE_NOTE_TOKEN, Defaults.NO_COOKIE, selectedNoteUri,
                Defaults.NO_SELECTION, Defaults.NO_SELECTION_ARGS);
    }
}

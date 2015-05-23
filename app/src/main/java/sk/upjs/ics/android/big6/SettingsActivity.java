package sk.upjs.ics.android.big6;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.prefs.Preferences;

import sk.upjs.ics.android.big6.R;


public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        addPreferencesFromResource(R.xml.settings);

        //http://stackoverflow.com/questions/5330677/android-preferences-onclick-event
        Preference myPref = (Preference) findPreference("deleteDatabaseButton");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getApplicationContext(), "database deleted!", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(context)
                            .setTitle("Warning!")
                            .setMessage("This will delete all your Training History! \n Do you want to continue?")
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    //TODO delete database
                                }})
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }})
                            .show();
                return true;
            }
        });
    }


    // http://stackoverflow.com/questions/5456993/how-to-open-alertdialog-from-preference-screen
    /*@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Toast.makeText(getApplicationContext(), "database deleted!", Toast.LENGTH_SHORT).show();
        if (key.equals("@string/deleteDatabaseButton")) {
            new AlertDialog.Builder(this)
                    .setTitle("Warning!")
                    .setMessage("This will delete all your Training History! \n Do you want to continue?")
                    .setPositiveButton("OK", null)
                    .show();

            Toast.makeText(getApplicationContext(), "database deleted!", Toast.LENGTH_SHORT).show();
        }
    }*/
}

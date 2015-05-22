package sk.upjs.ics.android.big6;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import sk.upjs.ics.android.big6.R;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    // http://stackoverflow.com/questions/5456993/how-to-open-alertdialog-from-preference-screen
    @Override
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
    }
}

package sk.upjs.ics.android.big6;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Pavol on 8. 6. 2015.
 */
public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, new SettingsFragment())
                .commit();
    }

}

package sk.upjs.ics.android.big6;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Object> {

    private ListView big6ListView;

    public static final int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        big6ListView = (ListView) findViewById(R.id.big6ListView);
        String[] big6 = {"PUSHUPS", "SQUAT", "PULLUPS", "LEG RAISE", "BRIDGE", "HANDSTAND PUSHUPS"};
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, big6);
        big6ListView.setAdapter(adapter);

        getLoaderManager().initLoader(LOADER_ID, Bundle.EMPTY, this);


        //service
        RemindTrainingSchedule.schedule(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.trainingHistoryAction) {
            Intent intent = new Intent(this, TrainingHistoryActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}

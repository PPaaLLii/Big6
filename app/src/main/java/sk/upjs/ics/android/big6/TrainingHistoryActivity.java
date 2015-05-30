package sk.upjs.ics.android.big6;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import sk.upjs.ics.android.big6.provider.Big6ContentProvider;

import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.YEAR;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.MONTH;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.DAY;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.TRAINING;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.TYPE;




public class TrainingHistoryActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final Bundle NO_BUNDLE = null;
    private ListView trainingHistoryListView;

    public static final int LOADER_ID_TRAINING_HISTORY = 0;
    private TrainingHistoryAdapter trainingHistoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_history);

        getLoaderManager().initLoader(LOADER_ID_TRAINING_HISTORY, NO_BUNDLE, this);

        trainingHistoryListView = (ListView) findViewById(R.id.trainingHistoryFragmentListView);
        //trainingHistoryListView.setAdapter(initializeAdapter());
    }

    private ListAdapter initializeAdapter() {
        /*
        String[] from = {Provider.Big6.TRAINING};
        //int[] to = {R.id.list_item};
        int[] to = {android.R.id.text1};
        this.adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, Defaults.NO_CURSOR, from, to, Defaults.NO_FLAGS);
        return this.adapter;
        */
        ArrayList<Training> trainings = new ArrayList<>();
        Training training = new Training("2015","10","15","blablablabla",0);
        trainings.add(training);
        trainingHistoryAdapter = new TrainingHistoryAdapter(this, trainings);
        return trainingHistoryAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_training_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(Big6ContentProvider.TRAINING_HISTORY_CONTENT_URI);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        ArrayList<Training> trainings = new ArrayList<>();

        while(cursor.moveToNext()){
            Training training = new Training();
            training.setYear(cursor.getString(cursor.getColumnIndex(YEAR)));
            training.setMonth(cursor.getString(cursor.getColumnIndex(MONTH)));
            training.setDay(cursor.getString(cursor.getColumnIndex(DAY)));
            training.setTraining(cursor.getString(cursor.getColumnIndex(TRAINING)));
            training.setType(Integer.parseInt(cursor.getString(cursor.getColumnIndex(TYPE))));
            trainings.add(training);
        }
        cursor.close();
        trainingHistoryAdapter = new TrainingHistoryAdapter(this, trainings);
        trainingHistoryListView.setAdapter(trainingHistoryAdapter);
        Log.w(getClass().getName(), "onLoadFinished!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // do nothing
    }
}

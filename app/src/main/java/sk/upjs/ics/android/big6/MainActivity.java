package sk.upjs.ics.android.big6;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import sk.upjs.ics.android.big6.provider.Big6Provider;
import sk.upjs.ics.android.big6.provider.Big6ContentProvider;
import sk.upjs.ics.android.util.Defaults;


public class MainActivity extends ActionBarActivity implements Big6Fragment.OnFragmentInteractionListener {


    private int INSERT_NOTE_TOKEN = 0;
    private long type;
    private Spinner warmupFirstStepSpinner;
    private Spinner warmupSecondStepSpinner;
    private Spinner warmupThirdStepSpinner;
    private EditText warmupFirstEditText;
    private EditText warmupSecondEditText;
    private EditText warmupThirdEditText;
    private EditText firstSetEditText;
    private EditText secondSetEditText;
    private EditText thirdSetEditText;
    private Spinner trainingStepSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if(savedInstanceState == null) {
            //Log.d(MainActivity.class.getName(), "savedInstanceState == null");
            if (isSinglePane()) {
                showBig6Pane();
            }
        //}

        warmupFirstStepSpinner = (Spinner) findViewById(R.id.warmupFirstStepSpinner);
        warmupSecondStepSpinner = (Spinner) findViewById(R.id.warmupSecondStepSpinner);
        warmupThirdStepSpinner = (Spinner) findViewById(R.id.warmupThirdStepSpinner);

        warmupFirstEditText = (EditText) findViewById(R.id.warmupFirstEditText);
        warmupSecondEditText = (EditText) findViewById(R.id.warmupSecondEditText);
        warmupThirdEditText = (EditText) findViewById(R.id.warmupThirdEditText);

        firstSetEditText = (EditText) findViewById(R.id.firstSetEditText);
        secondSetEditText = (EditText) findViewById(R.id.secondSetEditText);
        thirdSetEditText = (EditText) findViewById(R.id.thirdSetEditText);

        trainingStepSpinner = (Spinner) findViewById(R.id.trainingStepSpinner);
        //service
        //RemindTrainingSchedule.schedule(this);
    }

    public boolean isSinglePane() {
        if(findViewById(R.id.singleFragmentLayout) != null)
            Log.d(MainActivity.class.getName(), "is single pane!");
        else Log.d(MainActivity.class.getName(), "is not single pane!");
        return findViewById(R.id.singleFragmentLayout) != null;
    }

    private boolean isBig6FragmentShown() {
        return findViewById(R.id.trainingHistoryFragmentListView) == null && findViewById(R.id.trainingTextView) == null;
    }

    private void showBig6Pane() {
        Log.d(MainActivity.class.getName(), "Showing big6 pane!");
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.singleFragmentLayout, new Big6Fragment())
                .commit();
    }

    private void showTrainingHistoryPane() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.singleFragmentLayout, new TrainingHistoryFragment())
                .commit();
    }

    private void showTrainingPane(int id) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.singleFragmentLayout, TrainingFragment.newInstance(id))
                .commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem big6ActionItem = menu.findItem(R.id.big6Action);
        MenuItem trainingHistoryActionItem = menu.findItem(R.id.trainingHistoryAction);
        MenuItem takePhotoItem = menu.findItem(R.id.takePhotoAction);

        if(isSinglePane()) {
            if(isBig6FragmentShown()) {
                big6ActionItem.setVisible(false);
                trainingHistoryActionItem.setVisible(true);
                takePhotoItem.setVisible(true);
            } else {
                trainingHistoryActionItem.setVisible(false);
                big6ActionItem.setVisible(true);
            }
        } else {
            big6ActionItem.setVisible(false);
            trainingHistoryActionItem.setVisible(false);
            takePhotoItem.setVisible(true);
        }
        return true;
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

        switch(id) {
            case R.id.big6Action:
                showBig6Pane();
                invalidateOptionsMenu();
                return true;
            case R.id.trainingHistoryAction:
                showTrainingHistoryPane();
                invalidateOptionsMenu();
                return true;
            case R.id.takePhotoAction:
                startActivityForResult(new Intent(this, PhotoActivity.class), 0);
                invalidateOptionsMenu();
                return true;
            case R.id.action_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), 0);
                invalidateOptionsMenu();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(long id) {
        type = id;
        // start TrainingFragment
        //Log.w(MainActivity.class.getName(), "before if");
        if(isSinglePane()){
            //Log.w(MainActivity.class.getName(), "in if");
            showTrainingPane((int) id);
        }else{
            //Log.w(MainActivity.class.getName(), "else");
            TrainingFragment trainingFragment = (TrainingFragment) getFragmentManager()
                    .findFragmentById(R.id.trainingFragment);
            if(trainingFragment == null) {
                return;
            }
            trainingFragment.initializeView((int) id);

        }

    }

    public void submitButtonOnClick(View v){

        //trainingFragment = (TrainingFragment) getFragmentManager().findFragmentById(R.id.trainingFragment);

        String trainingType = String.valueOf((int)type);

        StringBuilder sb = new StringBuilder(""); // "10,2,10,2,10,2,-1,20,3,20,3,20,3"
        sb.append(warmupFirstEditText.getText())
                .append(",")
                .append(warmupFirstStepSpinner.getSelectedItemId()+1)
                .append(",")
                .append(warmupSecondEditText.getText())
                .append(",")
                .append(warmupSecondStepSpinner.getSelectedItemId()+1)
                .append(",")
                .append(warmupThirdEditText.getText())
                .append(",")
                .append(warmupThirdStepSpinner.getSelectedItemId()+1)
                .append(",")
                .append("-1")
                .append(",")
                .append(firstSetEditText.getText())
                .append(",")
                .append(trainingStepSpinner.getSelectedItemId()+1)
                .append(",")
                .append(secondSetEditText.getText())
                .append(",")
                .append(trainingStepSpinner.getSelectedItemId()+1)
                .append(",")
                .append(thirdSetEditText.getText())
                .append(",")
                .append(trainingStepSpinner.getSelectedItemId()+1);

        insertIntoContentProvider(sb.toString(), Integer.parseInt(trainingType));
    }

    private void insertIntoContentProvider(String training, int type) {
        Uri uri = Big6ContentProvider.TRAINING_HISTORY_CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(Big6Provider.TrainingHistory.TRAINING, training);
        values.put(Big6Provider.TrainingHistory.TYPE, type);

        //TODO: leak?
        AsyncQueryHandler insertHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                if(!isSinglePane()){
                    TrainingHistoryFragment trainingHistoryFragment = (TrainingHistoryFragment) getFragmentManager()
                            .findFragmentById(R.id.trainingHistoryFragment);
                    trainingHistoryFragment.notifyAdapterDataSetChange();
                }
                Toast.makeText(MainActivity.this, "Training was saved!", Toast.LENGTH_SHORT)
                        .show();
            }
        };

        insertHandler.startInsert(INSERT_NOTE_TOKEN, Defaults.NO_COOKIE, uri, values);
    }


}

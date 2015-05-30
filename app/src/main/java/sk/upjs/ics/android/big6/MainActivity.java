package sk.upjs.ics.android.big6;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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


public class MainActivity extends Activity implements Big6Fragment.OnFragmentInteractionListener {


    private int INSERT_NOTE_TOKEN = 0;
    private long type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            if (isSinglePane()) {
                showBig6Pane();
            }
        }

        //service
        //RemindTrainingSchedule.schedule(this);
    }

    public boolean isSinglePane() {
        return findViewById(R.id.singleFragmentLayout) != null;
    }

    private boolean isBig6FragmentShown() {
        return findViewById(R.id.trainingHistoryFragmentListView) == null && findViewById(R.id.trainingTextView) == null;
    }

    private void showBig6Pane() {
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
        // spusti TrainingFragment
        Log.w(MainActivity.class.getName(), "before if");
        if(isSinglePane()){
            Log.w(MainActivity.class.getName(), "in if");
            showTrainingPane((int) id);
        }else{
            Log.w(MainActivity.class.getName(), "else");
            TrainingFragment trainingFragment = (TrainingFragment) getFragmentManager()
                    .findFragmentById(R.id.trainingFragment);
            if(trainingFragment == null) {
                return;
            }
            trainingFragment.initializeView((int) id);

        }

    }

    public void submitButtonOnClick(View v){
        Spinner warmupFirstStepSpinner = (Spinner) findViewById(R.id.warmupFirstStepSpinner);
        Spinner warmupSecondStepSpinner = (Spinner) findViewById(R.id.warmupSecondStepSpinner);
        Spinner warmupThirdStepSpinner = (Spinner) findViewById(R.id.warmupThirdStepSpinner);

        EditText warmupFirstEditText = (EditText) findViewById(R.id.warmupFirstEditText);
        EditText warmupSecondEditText = (EditText) findViewById(R.id.warmupSecondEditText);
        EditText warmupThirdEditText = (EditText) findViewById(R.id.warmupThirdEditText);

        EditText firstSetEditText = (EditText) findViewById(R.id.firstSetEditText);
        EditText secondSetEditText = (EditText) findViewById(R.id.secondSetEditText);
        EditText thirdSetEditText = (EditText) findViewById(R.id.thirdSetEditText);

        TrainingFragment trainingFragment = (TrainingFragment) getFragmentManager().findFragmentById(R.id.trainingFragment);

        String trainingType = String.valueOf((int)type);

        StringBuilder sb = new StringBuilder(); // "10,2,10,2,10,2,-1,20,3,20,3,20,3"
        sb.append(warmupFirstEditText.getText())
                .append(",")
                .append(warmupFirstStepSpinner.getSelectedItemId())
                .append(",")
                .append(warmupSecondEditText.getText())
                .append(",")
                .append(warmupSecondStepSpinner.getSelectedItemId())
                .append(",")
                .append(warmupThirdEditText.getText())
                .append(",")
                .append(warmupThirdStepSpinner.getSelectedItemId())
                .append(",")
                .append(-1)
                .append(",")
                .append(firstSetEditText.getText())
                .append(",")
                .append(trainingType)
                .append(",")
                .append(secondSetEditText.getText())
                .append(",")
                .append(trainingType)
                .append(",")
                .append(thirdSetEditText.getText())
                .append(",")
                .append(trainingType);

        insertIntoContentProvider(sb.toString(), Integer.parseInt(trainingType));
    }

    private void insertIntoContentProvider(String training, int type) {
        Uri uri = Big6ContentProvider.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(Big6Provider.TrainingHistory.TRAINING, training);
        values.put(Big6Provider.TrainingHistory.TYPE, type);

        AsyncQueryHandler insertHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(MainActivity.this, "Training was saved!", Toast.LENGTH_SHORT)
                        .show();
            }
        };

        insertHandler.startInsert(INSERT_NOTE_TOKEN, Defaults.NO_COOKIE, uri, values);
    }

}

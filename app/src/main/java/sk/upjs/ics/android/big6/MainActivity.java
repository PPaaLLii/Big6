package sk.upjs.ics.android.big6;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import sk.upjs.ics.android.big6.provider.Big6Provider;
import sk.upjs.ics.android.big6.provider.Big6ContentProvider;
import sk.upjs.ics.android.util.Defaults;


public class MainActivity extends ActionBarActivity implements Big6Fragment.OnFragmentInteractionListener {
    private static final boolean DO_NOT_READ_AGAIN = false;
//http://www.truiton.com/2014/11/bound-service-example-android/

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
    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;
    private Ticker ticker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.settings, DO_NOT_READ_AGAIN);

        if(savedInstanceState != null) {

            if(isSinglePane()) {
                String tag = (String) savedInstanceState.get("tag");
                if(tag != null) {
                    switch (tag) {
                        case "Big6Fragment":
                            Big6Fragment big6Fragment = (Big6Fragment) getFragmentManager().findFragmentByTag("Big6Fragment");

                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.singleFragmentLayout, big6Fragment, "Big6Fragment")
                                    .commit();
                            break;

                        case "TrainingFragment":
                            TrainingFragment trainingFragment = (TrainingFragment) getFragmentManager().findFragmentByTag("TrainingFragment");

                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.singleFragmentLayout, trainingFragment, "TrainingFragment")
                                    .commit();
                            break;

                        case "TrainingHistoryFragment":
                            TrainingHistoryFragment trainingHistoryFragment = (TrainingHistoryFragment) getFragmentManager().findFragmentByTag("TrainingHistoryFragment");

                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.singleFragmentLayout, trainingHistoryFragment, "TrainingHistoryFragment")
                                    .commit();
                            break;
                    }
                }else{
                    showBig6Pane();
                }
            }
        }else{
            if(isSinglePane()){
                showBig6Pane();
            }
        }

        //loading ticker sound
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        soundID = soundPool.load(this, R.raw.electronic_chime, 1);
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
    private boolean isTrainingHistoryFragmentShown() {
        return findViewById(R.id.trainingHistoryFragmentListView) != null;
    }

    private boolean isTrainingFragmentShown() {
        return findViewById(R.id.trainingTextView) != null;
    }

    private void showBig6Pane() {
        //Log.d(MainActivity.class.getName(), "Showing big6 pane!");
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.singleFragmentLayout, new Big6Fragment(), "Big6Fragment")
                .commit();
    }

    private void showTrainingHistoryPane() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.singleFragmentLayout, new TrainingHistoryFragment(), "TrainingHistoryFragment")
                .commit();
    }

    private void showTrainingPane(int id) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.singleFragmentLayout, TrainingFragment.newInstance(id), "TrainingFragment")
                .commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem big6ActionItem = menu.findItem(R.id.big6Action);
        MenuItem trainingHistoryActionItem = menu.findItem(R.id.trainingHistoryAction);
        MenuItem takePhotoActionItem = menu.findItem(R.id.takePhotoAction);
        MenuItem settingsActionItem = menu.findItem(R.id.settingsAction);
        //Log.w(getClass().getName(), "onPrepareOptionsMenu!");

        if(isSinglePane()) {
            if(isBig6FragmentShown()) {
                big6ActionItem.setVisible(false);
                trainingHistoryActionItem.setVisible(true);
                takePhotoActionItem.setVisible(true);
                settingsActionItem.setVisible(true);

            }
            if(isTrainingFragmentShown()){
                big6ActionItem.setVisible(true);
                trainingHistoryActionItem.setVisible(false);
                takePhotoActionItem.setVisible(false);
                settingsActionItem.setVisible(false);
            }
            if(isTrainingHistoryFragmentShown()){
                big6ActionItem.setVisible(true);
                trainingHistoryActionItem.setVisible(false);
                takePhotoActionItem.setVisible(false);
                settingsActionItem.setVisible(false);
            }
        } else {
            big6ActionItem.setVisible(false);
            trainingHistoryActionItem.setVisible(false);
            takePhotoActionItem.setVisible(true);
            settingsActionItem.setVisible(true);
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
            case R.id.settingsAction:
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
            invalidateOptionsMenu();
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

        StringBuilder sb = new StringBuilder(""); // "10,2,10,2,10,2,-1,20,3,20,3,20,3"
        if(warmupFirstEditText == null){
            //Log.w(MainActivity.class.getName(), "warmupFirstEditText == null");
        }
        sb.append(warmupFirstEditText.getText().toString())
                .append(",")
                .append(warmupFirstStepSpinner.getSelectedItemId()+1)
                .append(",")
                .append(warmupSecondEditText.getText().toString())
                .append(",")
                .append(warmupSecondStepSpinner.getSelectedItemId()+1)
                .append(",")
                .append(warmupThirdEditText.getText().toString())
                .append(",")
                .append(warmupThirdStepSpinner.getSelectedItemId()+1)
                .append(",")
                .append("-1")
                .append(",")
                .append(firstSetEditText.getText().toString())
                .append(",")
                .append(trainingStepSpinner.getSelectedItemId()+1)
                .append(",")
                .append(secondSetEditText.getText().toString())
                .append(",")
                .append(trainingStepSpinner.getSelectedItemId()+1)
                .append(",")
                .append(thirdSetEditText.getText().toString())
                .append(",")
                .append(trainingStepSpinner.getSelectedItemId() + 1);

        insertIntoContentProvider(sb.toString(), Integer.parseInt(trainingType));

        if(isSinglePane()){
            showBig6Pane();
        }
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
                    TrainingFragment trainingFragment = (TrainingFragment) getFragmentManager()
                            .findFragmentById(R.id.trainingFragment);
                    trainingFragment.notifyDataSetChange();
                }
                Toast.makeText(MainActivity.this, "Training was saved!", Toast.LENGTH_SHORT)
                        .show();
            }
        };

        insertHandler.startInsert(INSERT_NOTE_TOKEN, Defaults.NO_COOKIE, uri, values);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.w(getClass().getName(), "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Log.w(getClass().getName(), "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(ticker != null){
            ticker.cancel(true);
        }
        Button playButton = (Button) findViewById(R.id.playButton);
        if(playButton != null){
            playButton.setText("Play");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(isSinglePane()) {
            if (isBig6FragmentShown()) {
                outState.putString("tag", "Big6Fragment");
                return;
            }
            if (isTrainingFragmentShown()) {
                outState.putString("tag", "TrainingFragment");
                return;
            }
            if (isTrainingHistoryFragmentShown()) {
                outState.putString("tag", "TrainingHistoryFragment");
                return;
            }
        }
    }

    public void playButtonOnClick(View view){
        Button playButton = (Button) findViewById(R.id.playButton);
        if (playButton.getText().toString().equals("Play")){
            Log.d(getClass().getName(), "Button Play Pressed");
            playButton.setText("Stop");
            ticker = new Ticker();
            ticker.execute("");
        }else{
            playButton.setText("Play");
            Log.d(getClass().getName(), "Button Stop Pressed");
            ticker.cancel(true);
        }
    }

    private class Ticker extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            float actualVolume = (float) audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = (float) audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = actualVolume / maxVolume;
            // Is the sound loaded already?
            if (loaded) {
                while(!isCancelled()) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    //Log.e("Test", "Played sound");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        //do nothing
                    }
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            String message;
            if(success) {
                message = "Success!";
            } else {
                message = "Error!";
            }
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
package sk.upjs.ics.android.big6;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity implements Big6Fragment.OnFragmentInteractionListener {



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
        return findViewById(R.id.big6ListView) != null;
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

        if(isSinglePane()) {
            if(isBig6FragmentShown()) {
                big6ActionItem.setVisible(false);
            } else {
                trainingHistoryActionItem.setVisible(false);
            }
        } else {
            big6ActionItem.setVisible(false);
            trainingHistoryActionItem.setVisible(false);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(long id) {
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
}

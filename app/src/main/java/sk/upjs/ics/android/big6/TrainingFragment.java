package sk.upjs.ics.android.big6;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Date;

import sk.upjs.ics.android.big6.provider.Big6ContentProvider;
import sk.upjs.ics.android.big6.provider.Big6Provider;
import sk.upjs.ics.android.util.Defaults;

import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.TYPE;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.TRAINING;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.YEAR;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.MONTH;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.DAY;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARG_TRAINING_TYPE = "TRAINING_TYPE";
    private static final int LAST_TRAINING_LOADER = 0;
    private TextView trainingTextView;
    private Spinner warmupFirstNumberSpinner;
    private Spinner warmupSecondNumberSpinner;
    private Spinner warmupThirdNumberSpinner;
    private Spinner trainingStepSpinner;
    private int type = 0;
    private static final Bundle NO_BUNDLE = null;
    private TextView lastTrainingWarmUpTextView;
    private TextView lastTrainingTrainingTextView;

    public TrainingFragment() {
        // Required empty public constructor
    }

    public static TrainingFragment newInstance(int id) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_TRAINING_TYPE, id);

        TrainingFragment fragment = new TrainingFragment();
        fragment.setArguments(arguments);

        return fragment;
    }


    public String getTrainingType() {
        Bundle arguments = getArguments();
        if(arguments != null && arguments.containsKey(ARG_TRAINING_TYPE)) {
            Resources res = getResources();
            String[] big6 = res.getStringArray(R.array.trainingTypes);
            return big6[arguments.getInt(ARG_TRAINING_TYPE)];
        }

        return getString(R.string.pushups);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_training, container, false);

        lastTrainingWarmUpTextView = (TextView) fragmentLayout.findViewById(R.id.lastTrainingWarmUpDataTextView);
        lastTrainingTrainingTextView = (TextView) fragmentLayout.findViewById(R.id.lastTrainingTrainingDataTextView);

        trainingTextView = (TextView) fragmentLayout.findViewById(R.id.trainingTypeTextView);
        trainingTextView.setText(getTrainingType());

        warmupFirstNumberSpinner = (Spinner) fragmentLayout.findViewById(R.id.warmupFirstStepSpinner);
        warmupSecondNumberSpinner = (Spinner) fragmentLayout.findViewById(R.id.warmupSecondStepSpinner);
        warmupThirdNumberSpinner = (Spinner) fragmentLayout.findViewById(R.id.warmupThirdStepSpinner);
        trainingStepSpinner = (Spinner) fragmentLayout.findViewById(R.id.trainingStepSpinner);

        //http://developer.android.com/guide/topics/ui/controls/spinner.html
        //http://www.mkyong.com/android/android-spinner-drop-down-list-example/
        ArrayAdapter<CharSequence> adapter
                = ArrayAdapter.createFromResource(this.getActivity(), R.array.step_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        warmupFirstNumberSpinner.setAdapter(adapter);
        warmupSecondNumberSpinner.setAdapter(adapter);
        warmupThirdNumberSpinner.setAdapter(adapter);
        trainingStepSpinner.setAdapter(adapter);

        warmupFirstNumberSpinner.setSelection(0);
        warmupSecondNumberSpinner.setSelection(0);
        warmupThirdNumberSpinner.setSelection(0);
        trainingStepSpinner.setSelection(0);

        getLoaderManager().initLoader(LAST_TRAINING_LOADER, NO_BUNDLE, this);

        return fragmentLayout;
    }

    public void initializeView(int id) {
        TextView trainingTextView = (TextView) getView().findViewById(R.id.trainingTypeTextView);
        Resources res = getResources();
        String[] big6 = res.getStringArray(R.array.trainingTypes);
        trainingTextView.setText(big6[id]);
        this.type = id;
        getLoaderManager().restartLoader(LAST_TRAINING_LOADER, NO_BUNDLE, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.invalidateOptionsMenu();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //https://developer.android.com/training/load-data-background/setup-loader.html#Extend

        Uri uri = Big6ContentProvider.TRAINING_HISTORY_CONTENT_URI;

        String[] projection = Defaults.NO_PROJECTION;

        String selection = TYPE + " = ?";

        String[] selectionArgs = new String[] {String.valueOf(this.type)};

        String sortOrder = YEAR +" desc, "+ MONTH +" desc, "+ DAY +" desc limit 1";

        CursorLoader loader = new CursorLoader(
                this.getActivity(),
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder);

        Log.e(getClass().getName(), loader.getSelection() + " " + Arrays.toString(loader.getSelectionArgs()) + " " + loader.getSortOrder());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor == null){
            Log.e(getClass().getName(), "Cursor je null!!!");
        }else {
            String training = "";
            while(cursor.moveToNext()) {
                training = cursor.getString(cursor.getColumnIndex(TRAINING));
                Log.w(getClass().getName(), "--------------------" + training + "--------------------");
            }
            cursor.close();

            if(!training.equals("")) {
                String[] lastTraining = training.split("-1");
                lastTrainingWarmUpTextView.setText(lastTraining[0]);
                lastTrainingTrainingTextView.setText(lastTraining[1].substring(1));
            }else{
                lastTrainingWarmUpTextView.setText("No previous training found");
                lastTrainingTrainingTextView.setText("No previous training found");
            }

            Log.w(getClass().getName(), "onLoadFinished!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
            // do nothing TODO: really?
    }

    public void notifiDataSetChange() {
        getLoaderManager().restartLoader(LAST_TRAINING_LOADER, NO_BUNDLE, this);
    }
}

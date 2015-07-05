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

import sk.upjs.ics.android.big6.provider.Big6ContentProvider;
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

    private int getIntTrainingType() {
        Bundle arguments = getArguments();
        if(arguments != null && arguments.containsKey(ARG_TRAINING_TYPE)) {
            return arguments.getInt(ARG_TRAINING_TYPE);
        }
        return 0;
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
        this.type = getIntTrainingType();
        //Log.d("OnCreateView type setup", String.valueOf(getIntTrainingType()));

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
        getLoaderManager().restartLoader(LAST_TRAINING_LOADER, NO_BUNDLE, this);
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
        //Log.d("", "onAttach");
        notifyDataSetChange();
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

        //Log.e(getClass().getName(), loader.getSelection() + " " + Arrays.toString(loader.getSelectionArgs()) + " " + loader.getSortOrder());
        //Log.e(getClass().getName(), "loader created");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor == null){
            //Log.e(getClass().getName(), "Cursor je null!!!");
        }else {
            String training = "";
            while(cursor.moveToNext()) {
                training = cursor.getString(cursor.getColumnIndex(TRAINING));
                //Log.w(getClass().getName(), "--------------------" + training + "--------------------");
            }
            cursor.close();

            if(!training.equals("")) {
                //Log.e(getClass().getName(), training);
                String[] lastTraining = training.split("-1");

                StringBuilder sb = new StringBuilder();

                String[] warmUp;
                if(lastTraining.length==0){
                    warmUp = new String[0];
                }else{
                    warmUp = lastTraining[0].split(",");
                }

                for(int i=0; i<warmUp.length; i++){
                    sb.append(" " + warmUp[i]);
                }

                lastTrainingWarmUpTextView.setText(sb.toString());

                StringBuilder sb1 = new StringBuilder();
                String[] training1;
                if(lastTraining.length==0){
                    training1=new String[0];
                }else{
                    training1 = lastTraining[1].split(",");
                }

                for(int i=0; i<training1.length; i++){
                    sb1.append(" " + training1[i]);
                }

                lastTrainingTrainingTextView.setText(sb1.toString());

            }else{
                //Log.e(getClass().getName(), "no training found");
                String noTrainingsFound = getResources().getString(R.string.noTrainingsFound);
                lastTrainingWarmUpTextView.setText(noTrainingsFound);
                lastTrainingTrainingTextView.setText(noTrainingsFound);
            }

            //Log.w(getClass().getName(), "onLoadFinished!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
            // do nothing TODO: really?
    }

    public void notifyDataSetChange() {
        getLoaderManager().restartLoader(LAST_TRAINING_LOADER, NO_BUNDLE, this);
    }
}

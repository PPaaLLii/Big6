package sk.upjs.ics.android.big6;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import sk.upjs.ics.android.big6.provider.Big6ContentProvider;

import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.DAY;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.MONTH;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.TRAINING;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.TYPE;
import static sk.upjs.ics.android.big6.provider.Big6Provider.TrainingHistory.YEAR;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingHistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final Bundle NO_BUNDLE = null;
    private ListView trainingHistoryFragmentListView;

    public static final int LOADER_ID_TRAINING_HISTORY = 0;
    private TrainingHistoryAdapter trainingHistoryAdapter;

    public TrainingHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_training_history, container, false);
        trainingHistoryFragmentListView = (ListView) fragmentLayout.findViewById(R.id.trainingHistoryFragmentListView);
        getLoaderManager().initLoader(LOADER_ID_TRAINING_HISTORY, NO_BUNDLE, this);

        ArrayList<Training> trainings = new ArrayList<>();
        trainings.add(new Training("2015", "2", "6", "10,2,10,2,-1,20,3,20,3,20,3", 1));

        trainingHistoryAdapter = new TrainingHistoryAdapter(this.getActivity(), trainings);
        trainingHistoryFragmentListView.setAdapter(trainingHistoryAdapter);

        return fragmentLayout;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this.getActivity());
        loader.setUri(Big6ContentProvider.TRAINING_HISTORY_CONTENT_URI);
        //Log.e(getClass().getName(), loader.getUri().toString());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ArrayList<Training> trainings = new ArrayList<>();
        if(cursor == null){
            Log.e(getClass().getName(), "Cursor je null!!!");
        }else {
            while (cursor.moveToNext()) {
                Training training = new Training();
                training.setYear(cursor.getString(cursor.getColumnIndex(YEAR)));
                training.setMonth(cursor.getString(cursor.getColumnIndex(MONTH)));
                training.setDay(cursor.getString(cursor.getColumnIndex(DAY)));
                training.setTraining(cursor.getString(cursor.getColumnIndex(TRAINING)));
                training.setType(Integer.parseInt(cursor.getString(cursor.getColumnIndex(TYPE))));
                trainings.add(0, training);
            }
            cursor.close();
            trainingHistoryAdapter.setAll(trainings);
            trainingHistoryAdapter.notifyDataSetChanged();

            //Log.w(getClass().getName(), "onLoadFinished!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // do nothing TODO: really?
    }

    public void notifyAdapterDataSetChange() {
        getLoaderManager().restartLoader(LOADER_ID_TRAINING_HISTORY, NO_BUNDLE, this);
        this.trainingHistoryAdapter.notifyDataSetChanged();
        Log.w(getClass().getName(), "adapter notifyDataSetChanged");
    }
}

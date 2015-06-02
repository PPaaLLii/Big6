package sk.upjs.ics.android.big6;


import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingFragment extends Fragment {

    public static final String ARG_TRAINING_TYPE = "TRAINING_TYPE";
    private TextView trainingTextView;
    private Spinner warmupFirstNumberSpinner;
    private Spinner warmupSecondNumberSpinner;
    private Spinner warmupThirdNumberSpinner;
    private Spinner trainingStepSpinner;
    private int type = 0;

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


        return fragmentLayout;
    }

    public void initializeView(int id) {
        TextView trainingTextView = (TextView) getView().findViewById(R.id.trainingTypeTextView);
        Resources res = getResources();
        String[] big6 = res.getStringArray(R.array.trainingTypes);
        trainingTextView.setText(big6[id]);
    }
}

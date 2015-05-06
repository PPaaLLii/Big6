package sk.upjs.ics.android.big6;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.upjs.ics.android.util.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingFragment extends Fragment {

    public static final String ARG_TRAINING_TYPE = "TRAINING_TYPE";

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


    private String getTrainingType() {
        Bundle arguments = getArguments();
        if(arguments != null && arguments.containsKey(ARG_TRAINING_TYPE)) {
            return Utils.convertType(arguments.getInt(ARG_TRAINING_TYPE));
        }
    return "PUSHUPS";
}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_training, container, false);

        TextView trainingTextView = (TextView) fragmentLayout.findViewById(R.id.trainingTypeTextView);
        trainingTextView.setText(getTrainingType());

        return fragmentLayout;
    }

    public void initializeView(int id) {
        TextView trainingTextView = (TextView) getView().findViewById(R.id.trainingTypeTextView);
        trainingTextView.setText(Utils.convertType(id));
    }
}

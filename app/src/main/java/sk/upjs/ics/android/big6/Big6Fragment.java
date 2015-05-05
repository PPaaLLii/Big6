package sk.upjs.ics.android.big6;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Big6Fragment extends Fragment {

    private ListView big6ListView;

    public Big6Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_big6, container, false);

        this.big6ListView = (ListView) fragmentLayout.findViewById(R.id.big6ListView);
        String[] big6 = {"PUSHUPS", "SQUAT", "PULLUPS", "LEG RAISE", "BRIDGE", "HANDSTAND PUSHUPS"};
        ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, big6);
        big6ListView.setAdapter(adapter);

        return fragmentLayout;
    }


}

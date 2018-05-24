package fr.unice.polytech.polybfm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Baptiste on 25/05/2018.
 */

public class SuprFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    VisualisationAdapter visualisationAdapter;

    public SuprFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SuprFragment newInstance() {
        return new SuprFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_visu, container, false);
        ((ListView) rootView.findViewById(R.id.listView)).setAdapter(visualisationAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try{
            List<Issue> liste = new DataBaseHelper(getContext()).getDeletedIssues();
            visualisationAdapter = new VisualisationAdapter(getActivity().getApplicationContext(), liste);
            ((ListView) getActivity().findViewById(R.id.listView)).setAdapter(visualisationAdapter);
        }catch (Exception e){}
    }
}


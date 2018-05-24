package fr.unice.polytech.polybfm.fragments;

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

import fr.unice.polytech.polybfm.DetailActivity;
import fr.unice.polytech.polybfm.R;
import fr.unice.polytech.polybfm.utilities.VisualisationAdapter;
import fr.unice.polytech.polybfm.model.Issue;
import fr.unice.polytech.polybfm.utilities.DataBaseHelper;

/**
 * Created by Baptiste on 24/05/2018.
 */

public class VisuFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    VisualisationAdapter visualisationAdapter;

    public VisuFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static VisuFragment newInstance() {
        return new VisuFragment();
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
            List<Issue> liste = new DataBaseHelper(getContext()).getCurrentIssues();
            visualisationAdapter = new VisualisationAdapter(getActivity().getApplicationContext(), liste);
            ((ListView) getActivity().findViewById(R.id.listView)).setAdapter(visualisationAdapter);
            ((ListView) getActivity().findViewById(R.id.listView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Issue issue = (Issue) parent.getItemAtPosition(position);

                    new DataBaseHelper(getContext()).viewEvent(issue.getKey());
                    issue.viewIssue();

                    Intent intent = new Intent(getContext(),DetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    intent.putExtra("key",issue.getKey());
                    intent.putExtra("title",issue.getTitle());
                    intent.putExtra("reporter",issue.getReporter());
                    intent.putExtra("emergency",issue.getEmergency());
                    intent.putExtra("category",issue.getCategory());
                    intent.putExtra("place",issue.getPlace());
                    intent.putExtra("date",issue.getDate());
                    intent.putExtra("pathToPhoto",issue.getPathToPhoto());
                    intent.putExtra("view",issue.isViewed());
                    intent.putExtra("deleted",issue.isDeleted());
                    startActivity(intent);

                }
            });
        }catch (Exception e){}
    }
}

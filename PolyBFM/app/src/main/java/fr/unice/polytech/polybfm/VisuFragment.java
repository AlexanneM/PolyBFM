package fr.unice.polytech.polybfm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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
            List<Issue> liste = getIssuesFromDB();
            visualisationAdapter = new VisualisationAdapter(getActivity().getApplicationContext(), liste);
            ((ListView) getActivity().findViewById(R.id.listView)).setAdapter(visualisationAdapter);
        }catch (Exception e){}
    }



    private List<Issue> getIssuesFromDB(){
        List<Issue> liste = new ArrayList<>();

        DatabaseHandler handler = new DatabaseHandler(this.getContext(), "DBpolyBFM", null, 3);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Issue WHERE deleted = 0",null);
        cursor.moveToFirst();

        while (! cursor.isAfterLast()) {
            int key = cursor.getInt(0);
            String title = cursor.getString(1);
            String reporter = cursor.getString(2);
            String emergency = cursor.getString(3);
            String category = cursor.getString(4);
            String place = cursor.getString(5);
            String date = cursor.getString(6);
            String pathToPhoto = cursor.getString(7);
            boolean viewed = cursor.getInt(8)==1;
            boolean deleted = cursor.getInt(9)==1;

            Issue issue = new Issue(key, title, reporter, emergency, category, place, date, pathToPhoto, viewed, deleted);
            liste.add(issue);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return liste;
    }
}

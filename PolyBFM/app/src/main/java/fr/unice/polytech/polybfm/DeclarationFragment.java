package fr.unice.polytech.polybfm;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class DeclarationFragment extends Fragment {

    private String ISSUE_TITLE;
    private String ISSUE_REPORTER = "placeholder reporter";
    private String ISSUE_EMERGENCY;
    private String ISSUE_CATEGORY;
    private String ISSUE_PLACE;
    private String ISSUE_DATE;
    private String ISSUE_PHOTO = "placeholder Photo";
    private int ISSUE_VIEWED = 0;

    public DeclarationFragment() {}

    public static DeclarationFragment newInstance() {
        return new DeclarationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_declaration, container, false);
        Spinner Categorie = (Spinner) rootView.findViewById(R.id.categorie);
        Spinner Urgence = (Spinner) rootView.findViewById(R.id.urgence);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Categorie.setAdapter(categoryAdapter);
        ArrayAdapter<CharSequence> UrgenceAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.emergency_array, android.R.layout.simple_spinner_item);
        UrgenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Urgence.setAdapter(UrgenceAdapter);

        Button button = rootView.findViewById(R.id.declarer);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner Categorie = (Spinner) rootView.findViewById(R.id.categorie);
                EditText Titre = (EditText) rootView.findViewById(R.id.titre);
                EditText Lieu = (EditText) rootView.findViewById(R.id.lieu);
                Spinner Urgence = (Spinner) rootView.findViewById(R.id.urgence);

                ISSUE_CATEGORY = Categorie.toString();
                ISSUE_TITLE = Titre.getText().toString();
                ISSUE_PLACE = Lieu.getText().toString();
                ISSUE_EMERGENCY = Urgence.toString();
                ISSUE_DATE = Calendar.getInstance().getTime().toString();

                addEvent();
            }
        });

        return rootView;
    }

    private void addEvent(){
        DatabaseHandler handler = new DatabaseHandler(getContext(), "DBpolyBFM", null, 2);
        SQLiteDatabase db = handler.getWritableDatabase();
        db.rawQuery("INSERT INTO Issue (title, reporter, emergency, category, place, date, photo, viewed) VALUES ('"+ISSUE_TITLE+"', '"+ISSUE_REPORTER+"', '"+ISSUE_EMERGENCY+"', '"+ISSUE_CATEGORY+"', '"+ISSUE_PLACE+"', '"+ISSUE_DATE+"', '"+ISSUE_PHOTO+"', '"+ISSUE_VIEWED+"')",null);
    }
}

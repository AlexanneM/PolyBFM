package fr.unice.polytech.polybfm;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DeclarationFragment extends Fragment {

    private String ISSUE_TITLE;
    private String ISSUE_REPORTER;
    private String ISSUE_EMERGENCY;
    private String ISSUE_CATEGORY;
    private String ISSUE_PLACE;
    private String ISSUE_DATE;
    private String ISSUE_PHOTO;
    private int ISSUE_VIEWED = 0;

    public DeclarationFragment() {}

    public static DeclarationFragment newInstance() {
        return new DeclarationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_declaration, container, false);
        Button button = rootView.findViewById(R.id.declarer);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText Categorie = (EditText) rootView.findViewById(R.id.categorie);
                EditText Titre = (EditText) rootView.findViewById(R.id.titre);
                EditText Lieu = (EditText) rootView.findViewById(R.id.lieu);
                EditText Urgence = (EditText) rootView.findViewById(R.id.urgence);

                ISSUE_CATEGORY = Categorie.getText().toString();
                ISSUE_TITLE = Titre.getText().toString();
                ISSUE_PLACE = Lieu.getText().toString();
                ISSUE_EMERGENCY = Urgence.getText().toString();
            }
        });

        return rootView;
    }
}

package fr.unice.polytech.polybfm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class VisualisationAdapter extends ArrayAdapter<Issue> {
    private Bitmap bitmap;

    VisualisationAdapter(Context context, List<Issue> issues) {
        super(context, 0, issues);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_visualisation, null);
        }

        Issue issue = getItem(position);

        //catégorie titre lieu nouveau urgence date photo

        ImageView imageCategorie = convertView.findViewById(R.id.imageCategorieVisu);
        TextView titre = convertView.findViewById(R.id.titreVisu);
        TextView lieu = convertView.findViewById(R.id.lieuVisu);
        ImageView newIncident = convertView.findViewWithTag(R.id.imageNewIncidentVisu);
        TextView urgence = convertView.findViewById(R.id.urgenceVisu);
        TextView date = convertView.findViewById(R.id.dateVisu);
        ImageView photo = convertView.findViewById(R.id.photoVisu);

        assert issue != null;
        String categorie = issue.getCategory();
        switch (categorie) {
            case "Casse":
                imageCategorie.setImageDrawable(imageCategorie.getContext().getResources().getDrawable(R.drawable.casse));
                break;
            case "Eau":
                imageCategorie.setImageDrawable(imageCategorie.getContext().getResources().getDrawable(R.drawable.goutte));
                break;
            case "Electricité":
                imageCategorie.setImageDrawable(imageCategorie.getContext().getResources().getDrawable(R.drawable.eclair));
                break;
            case "Ménage":
                imageCategorie.setImageDrawable(imageCategorie.getContext().getResources().getDrawable(R.drawable.balai));
                break;
            default:
                imageCategorie.setImageDrawable(imageCategorie.getContext().getResources().getDrawable(R.drawable.point_interrogation));
                break;
        }

        titre.setText(issue.getTitle());
        lieu.setText(issue.getPlace());

        newIncident.setImageDrawable(newIncident.getContext().getResources().getDrawable(R.drawable.logo_notif));
        if (issue.isViewed()) {
            newIncident.setVisibility(View.INVISIBLE);
        } else {
            newIncident.setVisibility(View.VISIBLE);
        }

        urgence.setText(issue.getEmergency());
        if (issue.getEmergency().endsWith("faible")) {
            urgence.setTextColor(Color.GREEN);
        } else if (issue.getEmergency().endsWith("moyenne")) {
            urgence.setTextColor(Color.YELLOW);
        } else if (issue.getEmergency().endsWith("forte")) {
            urgence.setTextColor(Color.RED);
        }

        date.setText(issue.getDate());

        String pathToPhoto = issue.getPathToPhoto();
        bitmap = BitmapFactory.decodeFile(pathToPhoto);
        photo.setImageBitmap(bitmap);

        return convertView;
    }
}
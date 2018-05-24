package fr.unice.polytech.polybfm;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import fr.unice.polytech.polybfm.utilities.DataBaseHelper;

/**
 * Created by Baptiste on 24/05/2018.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_full_screen);

        ( (TextView) findViewById(R.id.titreVisu)).setText(getIntent().getStringExtra("title"));
        ( (TextView) findViewById(R.id.lieuVisu)).setText(getIntent().getStringExtra("place"));
        ( (TextView) findViewById(R.id.dateVisu)).setText(getIntent().getStringExtra("date"));

        TextView urgence = (TextView) findViewById(R.id.urgenceVisu);
        urgence.setText(getIntent().getStringExtra("emergency"));
        if (getIntent().getStringExtra("emergency").endsWith("faible")) {
            urgence.setTextColor(Color.GREEN);
        } else if (getIntent().getStringExtra("emergency").endsWith("moyenne")) {
            urgence.setTextColor(Color.YELLOW);
        } else if (getIntent().getStringExtra("emergency").endsWith("forte")) {
            urgence.setTextColor(Color.RED);
        }

        ImageView imageCategorie = ( (ImageView) findViewById(R.id.imageCategorieVisu));
        String categorie = getIntent().getStringExtra("category");
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

        String path = getIntent().getStringExtra("pathToPhoto");
        ImageView photo = (ImageView) findViewById(R.id.photoVisu);
        if(path.isEmpty()){
            photo.setVisibility(View.INVISIBLE);
        }
        else {
            photo.setVisibility(View.VISIBLE);
            try{
                photo.setImageBitmap(BitmapFactory.decodeFile(path));
            }catch (Exception e){}
        }

        ImageButton button = findViewById(R.id.boutonSupprimer);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DataBaseHelper(getApplicationContext()).deleteEvent(getIntent().getIntExtra("key",0));
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

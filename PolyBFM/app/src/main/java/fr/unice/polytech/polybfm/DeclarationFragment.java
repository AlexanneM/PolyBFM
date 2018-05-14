package fr.unice.polytech.polybfm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class DeclarationFragment extends Fragment {

    private String ISSUE_TITLE;
    private String ISSUE_REPORTER = "placeholder reporter";
    private String ISSUE_EMERGENCY;
    private String ISSUE_CATEGORY;
    private String ISSUE_PLACE;
    private String ISSUE_DATE;
    private String ISSUE_PHOTO = "placeholder Photo";
    private int ISSUE_VIEWED = 0;
    private Button imageButton;
    private ImageView photo;

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
        imageButton = rootView.findViewById(R.id.prendrePhoto);
        photo = rootView.findViewById(R.id.photo);


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            imageButton.setEnabled(false);
            requestPermissions(new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //file = Uri.fromFile(getOutputMediaFile());
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                //startActivityForResult(intent, 100);

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }

            }

        });

        return rootView;
    }

    private void addEvent(){
        DatabaseHandler handler = new DatabaseHandler(getContext(), "DBpolyBFM", null, 2);
        SQLiteDatabase db = handler.getWritableDatabase();
        db.rawQuery("INSERT INTO Issue (title, reporter, emergency, category, place, date, photo, viewed) VALUES ('"+ISSUE_TITLE+"', '"+ISSUE_REPORTER+"', '"+ISSUE_EMERGENCY+"', '"+ISSUE_CATEGORY+"', '"+ISSUE_PLACE+"', '"+ISSUE_DATE+"', '"+ISSUE_PHOTO+"', '"+ISSUE_VIEWED+"')",null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                imageButton.setEnabled(true);
            }
        }
    }
}

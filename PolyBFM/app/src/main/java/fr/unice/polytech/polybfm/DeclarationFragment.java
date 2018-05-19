package fr.unice.polytech.polybfm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private Button gallerie;
    private String mCurrentPhotoPath;
    private Uri file;
    private String photoPath;


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
            requestPermissions(new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] { Manifest.permission.CAMERA}, 0);
                }
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                }

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            Toast toast = Toast.makeText(getActivity(), "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        if (photoFile != null) {
                            photoPath = photoFile.getAbsolutePath();
                            Uri photoUri = FileProvider.getUriForFile(getContext(),"com.example.android.fileprovider", photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(takePictureIntent, 10);
                        }
                    }
                }


            }

        });

        gallerie = rootView.findViewById(R.id.gallerie);
        gallerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] { Manifest.permission.CAMERA}, 0);
                }
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                }

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent gallerypickerIntent = new Intent(Intent.ACTION_PICK);
                    gallerypickerIntent.setType("image/*");
                    startActivityForResult(gallerypickerIntent, 20);
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
            if(requestCode==10&&resultCode==RESULT_OK){
                ImageView photo = getView().findViewById(R.id.photo);
                photo.setImageBitmap(BitmapFactory.decodeFile(photoPath));

            }
            if(requestCode==20&&resultCode==RESULT_OK){
                Bitmap bm=null;
                if(intent!=null){
                    try{
                        bm=MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),intent.getData());
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
                photo.setImageBitmap(bm);
            }
            super.onActivityResult(requestCode,resultCode,intent);
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRENCH).format(Calendar.getInstance().getTime());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}

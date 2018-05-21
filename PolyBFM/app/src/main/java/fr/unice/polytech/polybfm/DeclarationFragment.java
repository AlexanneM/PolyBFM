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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private String photoPath;
    private Bitmap bitmap;



    public DeclarationFragment() {}

    public static DeclarationFragment newInstance() {
        return new DeclarationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                ISSUE_PHOTO = photoPath;

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

                        File photoFile = getOutputMediaFile();
                        photoPath = photoFile.getAbsolutePath();
                        Uri photoUri = FileProvider.getUriForFile(getContext(),"com.example.android.fileprovider", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(takePictureIntent, 10);

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

        if (savedInstanceState != null) {
            //if there is a bundle, use the saved image resource (if one is there)
            bitmap = savedInstanceState.getParcelable("BitmapImage");
            if(bitmap != null){
                photo.setImageBitmap(bitmap);
                photoPath = savedInstanceState.getString("path_to_picture");
            }

        }
        return rootView;
    }

    private void addEvent(){
        DatabaseHandler handler = new DatabaseHandler(getContext(), "DBpolyBFM", null, 2);
        SQLiteDatabase db = handler.getWritableDatabase();
        db.rawQuery("INSERT INTO Issue (title, reporter, emergency, category, place, date, photo, viewed) VALUES ('"+ISSUE_TITLE+"', '"+ISSUE_REPORTER+"', '"+ISSUE_EMERGENCY+"', '"+ISSUE_CATEGORY+"', '"+ISSUE_PLACE+"', '"+ISSUE_DATE+"', '"+ISSUE_PHOTO+"', '"+ISSUE_VIEWED+"')",null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){

        if(requestCode==10 && resultCode==RESULT_OK){
            galleryAddPic();
            bitmap = BitmapFactory.decodeFile(photoPath);
            photo.setImageBitmap(BitmapFactory.decodeFile(photoPath));

        }
        if(requestCode==20&&resultCode==RESULT_OK){
            bitmap = null;
            if(intent!=null){
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),intent.getData());
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            photo.setImageBitmap(bitmap);
            }

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PolyBFM");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(bitmap != null){
            outState.putParcelable("BitmapImage", bitmap);
            outState.putString("path_to_picture", photoPath);
        }

    }


}

package fr.unice.polytech.polybfm.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.unice.polytech.polybfm.MainActivity;
import fr.unice.polytech.polybfm.R;
import fr.unice.polytech.polybfm.model.Issue;
import fr.unice.polytech.polybfm.utilities.DataBaseHelper;
import fr.unice.polytech.polybfm.utilities.NotificationHelper;

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
    private int ISSUE_DELETED =0;
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
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

                ISSUE_CATEGORY = String.valueOf(Categorie.getSelectedItem());
                ISSUE_TITLE = Titre.getText().toString();
                ISSUE_PLACE = Lieu.getText().toString();
                ISSUE_EMERGENCY = String.valueOf(Urgence.getSelectedItem());
                String temp = String.valueOf(Calendar.getInstance().getTime());
                ISSUE_DATE = temp.substring(0,16);
                ISSUE_PHOTO = photoPath;

                Issue issue = new Issue(0,ISSUE_TITLE,ISSUE_REPORTER,ISSUE_EMERGENCY,ISSUE_CATEGORY,ISSUE_PLACE,ISSUE_DATE,ISSUE_PHOTO,false,false);
                new DataBaseHelper(getContext()).addNewEvent(issue);
                new NotificationHelper(getContext(),String.valueOf(R.string.notif_titre),String.valueOf(R.string.notif_texte)).sendNotification();
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        imageButton = rootView.findViewById(R.id.prendrePhoto);
        photo = rootView.findViewById(R.id.photo);
        photo.setImageDrawable(getResources().getDrawable(R.drawable.appareil_photo));

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
            photoPath = savedInstanceState.getString("path_to_picture");
            if(photoPath != null){
                photo.setImageBitmap(BitmapFactory.decodeFile(photoPath));
            }

        }
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){

        if(requestCode==10 && resultCode==RESULT_OK){
            galleryAddPic();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inSampleSize = 8;
            final Bitmap image = BitmapFactory.decodeFile(photoPath, opt);
            image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] bitmapdata = baos.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            photo.setImageBitmap(BitmapFactory.decodeFile(photoPath));

        }
        if(requestCode==20&&resultCode==RESULT_OK){
            bitmap = null;
            if(intent!=null){
                try{
                    File photoFile = getOutputMediaFile();
                    photoPath = photoFile.getAbsolutePath();
                    InputStream inputStream = getActivity().getApplicationContext().getContentResolver().openInputStream(intent.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(photoFile);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();


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
        if(photoPath != null){
            outState.putString("path_to_picture", photoPath);
        }

    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

}

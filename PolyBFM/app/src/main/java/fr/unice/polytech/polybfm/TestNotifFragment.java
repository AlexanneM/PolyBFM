package fr.unice.polytech.polybfm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.support.v4.graphics.drawable.IconCompat.createWithAdaptiveBitmap;


/**
 * Created by Baptiste on 12/05/2018.
 */

public class TestNotifFragment extends Fragment {

    Notification myNotification;

    public TestNotifFragment(){}

    public static TestNotifFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TestNotifFragment fragment = new TestNotifFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_testnotif, container, false);
        final TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        Button button = rootView.findViewById(R.id.buttonTest);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(rootView.getContext(), 0, intent, 0);

                int x = numberOfIssues();

                myNotification = new NotificationCompat.Builder(rootView.getContext())
                        .setSmallIcon(R.drawable.logo_notif)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentTitle(getResources().getString(R.string.notif_titre))
                        .setContentText(""+x+getResources().getString(R.string.notif_texte) )
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();


                ( (NotificationManager) rootView.getContext().getSystemService(rootView.getContext().NOTIFICATION_SERVICE)).notify(1,myNotification);
            }
        });

        return rootView;
    }

    private int numberOfIssues(){
        DatabaseHandler handler = new DatabaseHandler(getActivity().getApplicationContext(), "DBpolyBFM", null, 3);
        SQLiteDatabase db = handler.getWritableDatabase();
        //Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Issue WHERE viewed = 0",null);
        Cursor cursor = db.rawQuery("SELECT COUNT(*) AS _id FROM Issue",null);
        cursor.moveToFirst();
        int x = cursor.getInt(0);
        db.close();
        return x;
    }
}
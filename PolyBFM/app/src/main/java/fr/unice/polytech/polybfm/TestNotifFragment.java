package fr.unice.polytech.polybfm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.R.drawable;

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
                textView.setText("WAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

                myNotification = new Notification.Builder(rootView.getContext())
                        .setContentTitle("New issues")
                        .setContentText("New issues : "+numberOfIssues())
                        .setSmallIcon(R.drawable.sample)
                        .build();


                ( (NotificationManager) rootView.getContext().getSystemService(rootView.getContext().NOTIFICATION_SERVICE)).notify(1,myNotification);
            }
        });

        return rootView;
    }

    private int numberOfIssues(){
        DatabaseHandler handler = new DatabaseHandler(getContext(), "DBpolyBFM", null, 1);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Issue WHERE viewed = 0",null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
}
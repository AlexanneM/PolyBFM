package fr.unice.polytech.polybfm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Baptiste on 24/05/2018.
 */

public class NotificationHelper {
    Notification myNotification;
    Context currentContext;
    String notifTitre;
    String notifTexte;
    int newEvents;

    public NotificationHelper(Context context, String notifTitre, String notifTexte){
        this.currentContext = context;
        this.notifTitre = notifTitre;
        this.notifTexte = notifTexte;
    }

    public void sendNotification(){

        Intent intent = new Intent(currentContext,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(currentContext, 0, intent, 0);

        newEvents = numberOfIssues();

        myNotification = new NotificationCompat.Builder(currentContext)
                .setSmallIcon(R.drawable.logo_notif)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(notifTitre)
                .setContentText(""+newEvents+notifTexte)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();


        ( (NotificationManager) currentContext.getSystemService(currentContext.NOTIFICATION_SERVICE)).notify(1,myNotification);
    }

    private int numberOfIssues(){
        DatabaseHandler handler = new DatabaseHandler(currentContext, "DBpolyBFM", null, 3);
        SQLiteDatabase db = handler.getWritableDatabase();
        //Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Issue WHERE viewed = 0",null);
        Cursor cursor = db.rawQuery("SELECT COUNT(*) AS _id FROM Issue",null);
        cursor.moveToFirst();
        int x = cursor.getInt(0);
        db.close();
        return x;
    }
}

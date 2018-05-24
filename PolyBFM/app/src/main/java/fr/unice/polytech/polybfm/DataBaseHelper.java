package fr.unice.polytech.polybfm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 24/05/2018.
 */

public class DataBaseHelper {
    private Context context;
    private SQLiteDatabase db;

    public DataBaseHelper(Context context){
        this.context = context;
    }

    private void openDB(){
        DatabaseHandler handler = new DatabaseHandler(context, "DBpolyBFM", null, 3);
        db = handler.getWritableDatabase();
    }

    public List<Issue> getCurrentIssues(){
        List<Issue> liste = new ArrayList<>();
        openDB();
        Cursor cursor = db.rawQuery("SELECT * FROM Issue WHERE deleted = 0",null);
        cursor.moveToFirst();

        while (! cursor.isAfterLast()) {
            int key = cursor.getInt(0);
            String title = cursor.getString(1);
            String reporter = cursor.getString(2);
            String emergency = cursor.getString(3);
            String category = cursor.getString(4);
            String place = cursor.getString(5);
            String date = cursor.getString(6);
            String pathToPhoto = cursor.getString(7);
            boolean viewed = cursor.getInt(8)==1;
            boolean deleted = cursor.getInt(9)==1;

            Issue issue = new Issue(key, title, reporter, emergency, category, place, date, pathToPhoto, viewed, deleted);
            liste.add(issue);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return liste;
    }

    public int numberOfIssues(){
        openDB();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) AS _id FROM Issue WHERE viewed = 0",null);
        cursor.moveToFirst();
        int x = cursor.getInt(0);
        cursor.close();
        db.close();
        return x;
    }

    public void addNewEvent(Issue issue){
        openDB();
        db.execSQL("INSERT INTO Issue (title, reporter, emergency, category, place, date, photo, viewed, deleted) VALUES ('"+issue.getTitle()+"', '"+issue.getReporter()+"', '"+issue.getEmergency()+"', '"+issue.getCategory()+"', '"+issue.getPlace()+"', '"+issue.getDate()+"', '"+issue.getPathToPhoto()+"', '"+"0"+"', '"+"0"+"')");
        db.close();
    }

    public void deleteEvent(int id){
        openDB();
        db.execSQL("UPDATE Issue SET deleted = 1 WHERE _id = "+id);
        db.close();
    }

    public void viewEvent(int id){
        openDB();
        db.execSQL("UPDATE Issue SET viewed = 1 WHERE _id = "+id);
        db.close();
    }
}

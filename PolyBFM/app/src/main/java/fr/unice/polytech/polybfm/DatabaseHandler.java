package fr.unice.polytech.polybfm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Baptiste on 10/05/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    //following are the different data inside the table
    static final String ISSUE_KEY = "id";
    static final String ISSUE_TITLE = "title";
    static final String ISSUE_REPORTER = "reporter";
    static final String ISSUE_EMERGENCY = "emergency";
    static final String ISSUE_CATEGORY = "category";
    static final String ISSUE_DATE = "date";
    static final String ISSUE_PHOTO = "photo";
    static final String ISSUE_VIEW = "view";

    static final String ISSUE_TABLE_NAME = "Issue";
    static final String ISSUE_TABLE_CREATE =
            "CREATE TABLE " + ISSUE_TABLE_NAME + " (" +
                    ISSUE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ISSUE_TITLE + " TEXT, " +
                    ISSUE_REPORTER + " TEXT, " +
                    ISSUE_EMERGENCY + " TEXT, " +
                    ISSUE_CATEGORY + " TEXT, " +
                    ISSUE_DATE + " TEXT, " +
                    ISSUE_PHOTO + " TEXT, " +
                    ISSUE_VIEW + " INTEGER);";
    static final String ISSUE_TABLE_DROP = "DROP TABLE IF EXISTS " + ISSUE_TABLE_NAME + ";";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ISSUE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ISSUE_TABLE_DROP);
        onCreate(db);
    }
}


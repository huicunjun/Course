package lx.curriculumschedule.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
    private final SQLiteDatabase writableDatabase;
    private SQLiteOpenHelper db;


    public DBAdapter(SQLiteOpenHelper db) {
        this.db = db;
         writableDatabase = db.getWritableDatabase();
    }

    public void queryAll() {
    }

    public void update() {
    }

    public void delete() {

    }

    public void insert() {

    }


}

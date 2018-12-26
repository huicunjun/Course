package lx.curriculumschedule.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {


    static final String name = "mydata.db";
    public DBHelper(Context context) {
        super(context, name, null, 666);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table Course (id integer primary key autoincrement ,name text,teacher text)";
        /**
         * id integer primary key autoincrement, author text,
         *
         *                 price real, pages integer, name text)
         */


        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

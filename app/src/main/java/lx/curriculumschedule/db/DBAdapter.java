package lx.curriculumschedule.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import lx.curriculumschedule.bean.Course;

public class DBAdapter implements SQLImp {
    private final SQLiteDatabase writableDatabase;
    private SQLiteOpenHelper db;
    private final static String tableName = "Course";

    public DBAdapter(SQLiteOpenHelper db) {
        this.db = db;
         writableDatabase = db.getWritableDatabase();
    }

    @Override
    public void insert(Course course) {
        String name = course.getName() ;
        String address = course.getAddress();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("address",address);
        writableDatabase.insert(tableName,null,contentValues);

    }

    @Override
    public void delete(Course course) {

    }

    @Override
    public void update() {

    }

    @Override
    public List<Course> queryAll() {
        Cursor cursor = writableDatabase.query(tableName, null, null, null, null, null, null);
       if (cursor.moveToFirst()){
           do {
               String string = cursor.getString(1);
           }while (cursor.moveToNext());
       }
        return null;
    }


}
interface SQLImp{
    void insert(Course course);
    void delete(Course course);
    void update();
    List<Course> queryAll();
}

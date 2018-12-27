package lx.curriculumschedule.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lx.curriculumschedule.bean.Course;

public class DBAdapter {
    private SQLiteDatabase 主人;
    private DBHelper 别墅;

    public DBAdapter(Context context) {
        this.别墅 = new DBHelper(context);
        主人 = 别墅.getWritableDatabase();
    }

    public void insert(int id, String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("name",name);
        主人.insert("Course",null,contentValues);
    }
    public List<String> queryAll(){
        Cursor course = 主人.query("Course", null, null, null, null, null, null);

        List<String> list = new ArrayList<>();
        if (course.moveToFirst()){
            do {
                String name = course.getString(1);
                list.add(name);
            }while (course.moveToNext());
        }

        return list;

   /*     while (){

        }
        for (int i = 0; i < 5; i++) {

        }

        do {

        }while (true);*/
    }

   /* private final SQLiteDatabase writableDatabase;
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

*/
}
interface SQLImp{
    void insert(Course course);
    void delete(Course course);
    void update();
    List<Course> queryAll();
}

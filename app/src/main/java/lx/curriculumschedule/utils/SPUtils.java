package lx.curriculumschedule.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
    private  SharedPreferences sp;
   // private  Context context = IApplication.getContext();


    public  SharedPreferences getSp() {
        return sp;
    }

    public  SPUtils(Context context, String name) {
        sp = context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }


    public  void putString(String key,String s){
        sp.edit().putString(key,s).apply();
    }
    public  String getString(String key,String s){
        return sp.getString(key,s);
    }
    public  String getString(String key){
        return sp.getString(key,"");
    }


    public  void putBoolean(String key,boolean b){
         sp.edit().putBoolean(key,b).apply();
    }
    public  boolean getBoolean(String key,boolean b) {
       return sp.getBoolean(key, b);
    }

    public  void putLong(String key,long b){
        sp.edit().putLong(key,b).apply();
    }
    public  long getLong(String key,long b) {
        return sp.getLong(key, b);
    }

}

package com.example.cinna.myapplication;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mydata.db"; //数据库名称
    private static final int version = 1; //数据库版本

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists keyword("
                + "_id INTEGER primary key,"
                + "word text)";

        db.execSQL(sql);

        sql = "create table if not exists links("
                +"_id INTEGER primary key,"
                +"title text,"
                +"date text,"
                +"addr text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onOpen(SQLiteDatabase db){
        String sql = "create table if not exists keyword("
                + "_id INTEGER primary key,"
                + "word text)";

        db.execSQL(sql);

        sql = "create table if not exists links("
                +"_id INTEGER primary key,"
                +"title text,"
                +"date text,"
                +"addr text)";
        db.execSQL(sql);

    }



}

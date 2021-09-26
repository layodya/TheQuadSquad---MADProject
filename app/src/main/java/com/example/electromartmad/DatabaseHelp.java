package com.example.electromartmad;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DatabaseHelp extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "payment.db";
    public static final String TABLE_NAME = "credit_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "ADDR";
    public static final String COL_4 = "POSTAL";
    public static final String COL_5 = "PHONE";

    public DatabaseHelp(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +TABLE_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,ADDR TEXT,POSTAL TEXT,PHONE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name , String addr , String postal , String phone){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,addr);
        contentValues.put(COL_4,postal);
        contentValues.put(COL_5,phone);

        long result = db.insert(TABLE_NAME,null,contentValues);

        if (result == -1)
        {
          return false;
        }
        else
        {
            return true;
        }

    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " +TABLE_NAME , null);
        return res;
    }

    public boolean updateData(String id,String name , String addr , String postal , String phone)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,addr);
        contentValues.put(COL_4,postal);
        contentValues.put(COL_5,phone);

        db.update(TABLE_NAME,contentValues,"ID = ?", new String[] {id} );
        return  true;
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME,"ID = ?" , new String[] {id});
    }
}

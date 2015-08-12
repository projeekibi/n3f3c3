package com.test.cb.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CB on 3.2.2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME   = "mcuzdanDB";
    // Contacts table name
    private static final String TABLE_CARDS = "mc_kartlar";
    public Card card;
    public Cursor cursor;
    public List<Card> cards = new ArrayList<Card>();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_CARDS + "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,kart_adi VARCHAR(50),kart_logo BLOB, kart_no VARCHAR(24)" + ")";
        Log.d("DBHelper", "SQL : " + sql);
        db.execSQL(sql);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        onCreate(db);
    }

    public void insertCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("kart_adi", card.getKartAdi());
        values.put("kart_no", card.getKartNo());
        values.put("kart_logo", card.getKartLogo());
        db.insert(TABLE_CARDS, null,values);
        db.close();
    }

    public void deleteCard(long kartID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //kartID =+1;
        Log.d("deneme", "Kart id : " + kartID);
        //Log.d("deneme", "kartID : " + Long.toString(kartID));
        //Log.d("deneme","Kart ID: " + getColumn("kart_id",kartID));
        //Log.d("deneme","Kart Adı: " + getColumn("kart_adi", kartID+2));
        //Log.d("deneme","Kart Adı: " + getColumn("kart_adi",1));
        db.delete(TABLE_CARDS, "_id = ?", new String[]{Long.toString(kartID)});
        db.close();
    }

    public String getColumn(String columnName, long kartID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor2;
        String value;
        cursor2 = db.rawQuery("SELECT " + columnName + " FROM " + TABLE_CARDS + " WHERE _id = " + Long.toString(kartID), null);
        cursor2.moveToFirst();
        value = cursor2.getString(cursor2.getColumnIndex(columnName));
        return value;
    }




    public void changeCardName(long kartID , String yeniAd) {



        Log.d("deneme","ust");
        Cursor cursor3;
        SQLiteDatabase db=this.getReadableDatabase();
        Log.d("deneme","orta");

        ContentValues cv = new ContentValues();
        cv.put("kart_adi", yeniAd); //These Fields should be your String values of actual column names

        db.update(TABLE_CARDS, cv, "_id "+"="+Long.toString(kartID), null);



        Log.d("deneme","alt");


    }




    public Cursor fetchAllCards() {
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_CARDS, new String[]{"_id","kart_adi", "kart_no", "kart_logo"}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}


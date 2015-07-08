package com.test.cb.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_CARDS + "(kart_id INTEGER PRIMARY KEY AUTOINCREMENT,kart_adi VARCHAR(50),kart_logo BLOB, kart_no VARCHAR(24)" + ")";

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

    public void deleteCard(Long kartID) {


    }

    public List<Card> getAllCards() {

        List<Card> cards = new ArrayList<Card>();
        SQLiteDatabase db = this.getWritableDatabase();

        // String sqlQuery = "SELECT  * FROM " + TABLE_COUNTRIES;
        // Cursor cursor = db.rawQuery(sqlQuery, null);
        Cursor cursor = db.query(TABLE_CARDS, new String[]{"kart_id","kart_adi", "kart_no", "kart_logo"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Card card = new Card();
            card.setKartAdi(cursor.getString(1));
            card.setKartNo(cursor.getString(2));
            card.setImage(cursor.getBlob(3));
            cards.add(card);
        }
        return cards;
    }
}


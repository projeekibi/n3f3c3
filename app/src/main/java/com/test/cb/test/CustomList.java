package com.test.cb.test;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;
//import com.test.cb.test.RoundImage;

/**
 * Created by CB on 16.12.2014.
 */
public class CustomList extends CursorAdapter {

    //private final Integer[] imageId;
    ImageView imageView;
    RoundImage roundedImage;

    public CustomList(Context context,
                      Cursor cursor) {
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_single, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView txtTitle = (TextView) view.findViewById(R.id.txt);
        imageView = (ImageView) view.findViewById(R.id.img);

        // Extract properties from cursor
        //int kartID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("kart_adi"));

        // Populate fields with extracted properties
        txtTitle.setText(title);

        ByteArrayInputStream imageStream = new ByteArrayInputStream(cursor.getBlob(cursor.getColumnIndexOrThrow("kart_logo")));
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        roundedImage = new RoundImage(theImage);
        imageView.setImageDrawable(roundedImage);

    }

    public void updateList() {
        this.notifyDataSetChanged();
    }

}
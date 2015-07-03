package com.test.cb.test;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;
//import com.test.cb.test.RoundImage;

/**
 * Created by CB on 16.12.2014.
 */
public class CustomList extends BaseAdapter{

    private final Activity context;
    //private final String[] kartlar;
    private List<Card> cardList;

    //private final Integer[] imageId;
    ImageView imageView;
    RoundImage roundedImage;

    public CustomList(Activity context,
                      List<Card> cards) {
        //super(context, R.layout.list_single, cards);

        this.context = context;
        cardList = cards;
    }



    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public Object getItem(int position) {
        return cardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        //txtTitle.setText(kartlar[position]);
        Card card = cardList.get(position);

        txtTitle.setText(card.getKartAdi());
        imageView = (ImageView) rowView.findViewById(R.id.img);
        ByteArrayInputStream imageStream = new ByteArrayInputStream(card.getKartLogo());
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        roundedImage = new RoundImage(theImage);
        imageView.setImageDrawable(roundedImage);

        return rowView;
    }


}
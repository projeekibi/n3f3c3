
package com.test.cb.test;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by CB on 3.2.2015.
 */
public class Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private int kartID;
    private String kartAdi;
    private String kartNo;
    private String kartOzellikleri;
    byte[] kartLogo;

    public Card() {
        super();
    }

    public Card(String kartAdi, String kartNo){
        super();
        this.kartAdi = kartAdi;
        this.kartNo = kartNo;
    }

    public Card(String kartAdi, String kartNo, byte[] kartLogo){
        super();
        this.kartAdi = kartAdi;
        this.kartNo = kartNo;
        this.kartLogo = kartLogo;
    }

    public Card(String kartOzellikleri){    //Kart özelliklerini parametre alan constructor
        super();
        this.kartOzellikleri = kartOzellikleri;
        Log.d("deneme","heyo3");

    }



    public byte[] getKartLogo() {
        return this.kartLogo;
    }

    public void setImage(byte[] kartLogo) {
        this.kartLogo = kartLogo;
    }


    public String getKartNo() {

        return kartNo;
    }

    public void setKartNo(String kartNo) {
        this.kartNo = kartNo;
    }

    public String getKartAdi() {
        return kartAdi;
    }


    public String getKartOzellikleri() {    //Kart özelliklerini alan fonksiyon
        Log.d("deneme","heyo4");
        return kartOzellikleri;

    }


    public Integer getKartID() {
        return kartID;
    }

    public void setKartAdi(String kartAdi) {
        this.kartAdi = kartAdi;
    }
    public void setKartID(Integer kartID) {
        this.kartID = kartID;
    }


}

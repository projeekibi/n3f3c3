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

    public void setKartAdi(String kartAdi) {
        this.kartAdi = kartAdi;
    }


}

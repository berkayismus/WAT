package com.berkaycayli.wat.objects;

import java.util.ArrayList;

public class Ogunler {

  private ArrayList<String> besin_id;
  private String ogun_id;
  private String ogun_tarihi;
  private String ogun_turu;
  private String user_id;

    public Ogunler() {
        // boş constructor
    }

    public Ogunler(ArrayList<String> besin_id, String ogun_id, String ogun_tarihi, String ogun_turu, String user_id) {
        this.besin_id = besin_id;
        this.ogun_id = ogun_id;
        this.ogun_tarihi = ogun_tarihi;
        this.ogun_turu = ogun_turu;
        this.user_id = user_id;
    }

    public ArrayList<String> getBesin_id() {
        return besin_id;
    }

    public void setBesin_id(ArrayList<String> besin_id) {
        this.besin_id = besin_id;
    }

    public String getOgun_id() {
        return ogun_id;
    }

    public void setOgun_id(String ogun_id) {
        this.ogun_id = ogun_id;
    }

    public String getOgun_tarihi() {
        return ogun_tarihi;
    }

    public void setOgun_tarihi(String ogun_tarihi) {
        this.ogun_tarihi = ogun_tarihi;
    }

    public String getOgun_turu() {
        return ogun_turu;
    }

    public void setOgun_turu(String ogun_turu) {
        this.ogun_turu = ogun_turu;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

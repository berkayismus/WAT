package com.berkaycayli.wat.objects;

public class Besin {

    private String besin_id;
    private String besin_adi;
    private String besin_miktar;
    private int besin_kalori;
    private int besin_protein;
    private int besin_seker;
    private int besin_yag;

    public Besin(){
        // boş constructor
    }

    public Besin(String besin_id, String besin_adi, String besin_miktar, int besin_kalori, int besin_protein, int besin_seker, int besin_yag) {
        this.besin_id = besin_id;
        this.besin_adi = besin_adi;
        this.besin_miktar = besin_miktar;
        this.besin_kalori = besin_kalori;
        this.besin_protein = besin_protein;
        this.besin_seker = besin_seker;
        this.besin_yag = besin_yag;
    }

    public Besin(Besin yeniBesin) {
    }

    public String getBesin_id() {
        return besin_id;
    }

    public void setBesin_id(String besin_id) {
        this.besin_id = besin_id;
    }

    public String getBesin_adi() {
        return besin_adi;
    }

    public void setBesin_adi(String besin_adi) {
        this.besin_adi = besin_adi;
    }

    public String getBesin_miktar() {
        return besin_miktar;
    }

    public void setBesin_miktar(String besin_miktar) {
        this.besin_miktar = besin_miktar;
    }

    public int getBesin_kalori() {
        return besin_kalori;
    }

    public void setBesin_kalori(int besin_kalori) {
        this.besin_kalori = besin_kalori;
    }

    public int getBesin_protein() {
        return besin_protein;
    }

    public void setBesin_protein(int besin_protein) {
        this.besin_protein = besin_protein;
    }

    public int getBesin_seker() {
        return besin_seker;
    }

    public void setBesin_seker(int besin_seker) {
        this.besin_seker = besin_seker;
    }

    public int getBesin_yag() {
        return besin_yag;
    }

    public void setBesin_yag(int besin_yag) {
        this.besin_yag = besin_yag;
    }
}

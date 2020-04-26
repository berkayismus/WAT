package com.berkaycayli.wat.objects;

public class Ogunler {

    private Users user;

    private String ogun_turu;
    private String ogun_tarihi;
    private Besin besin;

    public Ogunler(){
        // boş constructor
    }

    public Ogunler(Users user, String ogun_turu, String ogun_tarihi, Besin besin) {
        this.user = user;
        this.ogun_turu = ogun_turu;
        this.ogun_tarihi = ogun_tarihi;
        this.besin = besin;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getOgun_turu() {
        return ogun_turu;
    }

    public void setOgun_turu(String ogun_turu) {
        this.ogun_turu = ogun_turu;
    }

    public String getOgun_tarihi() {
        return ogun_tarihi;
    }

    public void setOgun_tarihi(String ogun_tarihi) {
        this.ogun_tarihi = ogun_tarihi;
    }

    public Besin getBesin() {
        return besin;
    }

    public void setBesin(Besin besin) {
        this.besin = besin;
    }
}

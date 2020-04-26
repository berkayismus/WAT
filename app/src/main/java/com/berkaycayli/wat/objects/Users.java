package com.berkaycayli.wat.objects;

public class Users {
    private String user_id;
    private int user_boy;
    private int user_kilo;
    private String user_hedef;
    private double user_hedef_kalori;
    private String user_etkinlik_duzeyi;
    private String user_cinsiyet;
    private int user_dogum_yili;
    private String user_olusturulma_tarihi;

    public Users(){
        // boş constructor
    }

    // parametreli constructor
    public Users(String user_id, int user_boy, int user_kilo, String user_hedef, double user_hedef_kalori, String user_etkinlik_duzeyi, String user_cinsiyet, int user_dogum_yili, String user_olusturulma_tarihi) {
        this.user_id = user_id;
        this.user_boy = user_boy;
        this.user_kilo = user_kilo;
        this.user_hedef = user_hedef;
        this.user_hedef_kalori = user_hedef_kalori;
        this.user_etkinlik_duzeyi = user_etkinlik_duzeyi;
        this.user_cinsiyet = user_cinsiyet;
        this.user_dogum_yili = user_dogum_yili;
        this.user_olusturulma_tarihi = user_olusturulma_tarihi;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getUser_boy() {
        return user_boy;
    }

    public void setUser_boy(int user_boy) {
        this.user_boy = user_boy;
    }

    public int getUser_kilo() {
        return user_kilo;
    }

    public void setUser_kilo(int user_kilo) {
        this.user_kilo = user_kilo;
    }

    public String getUser_hedef() {
        return user_hedef;
    }

    public void setUser_hedef(String user_hedef) {
        this.user_hedef = user_hedef;
    }

    public double getUser_hedef_kalori() {
        return user_hedef_kalori;
    }

    public void setUser_hedef_kalori(double user_hedef_kalori) {
        this.user_hedef_kalori = user_hedef_kalori;
    }

    public String getUser_etkinlik_duzeyi() {
        return user_etkinlik_duzeyi;
    }

    public void setUser_etkinlik_duzeyi(String user_etkinlik_duzeyi) {
        this.user_etkinlik_duzeyi = user_etkinlik_duzeyi;
    }

    public String getUser_cinsiyet() {
        return user_cinsiyet;
    }

    public void setUser_cinsiyet(String user_cinsiyet) {
        this.user_cinsiyet = user_cinsiyet;
    }

    public int getUser_dogum_yili() {
        return user_dogum_yili;
    }

    public void setUser_dogum_yili(int user_dogum_yili) {
        this.user_dogum_yili = user_dogum_yili;
    }

    public String getUser_olusturulma_tarihi() {
        return user_olusturulma_tarihi;
    }

    public void setUser_olusturulma_tarihi(String user_olusturulma_tarihi) {
        this.user_olusturulma_tarihi = user_olusturulma_tarihi;
    }
}

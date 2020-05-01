package com.berkaycayli.wat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.berkaycayli.wat.objects.Besin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class BesinEkleActivity extends AppCompatActivity {


    EditText tvBesinAd,tvBesinMiktar,tvBesinKalori,tvBesinProtein,tvBesinSeker,tvBesinYag;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_besin_ekle);

        // TextInput Edit
        tvBesinAd = findViewById(R.id.besin_ekle_tvBesinAd);
        tvBesinMiktar = findViewById(R.id.besin_ekle_tvBesinAdet);
        tvBesinKalori = findViewById(R.id.besin_ekle_tvBesinKalori);
        tvBesinProtein = findViewById(R.id.besin_ekle_tvBesinProtein);
        tvBesinSeker = findViewById(R.id.besin_ekle_tvBesinSeker);
        tvBesinYag = findViewById(R.id.besin_ekle_tvBesinYag);



    }

    public void besinKaydet(View v){

        String besinAd="",besinMiktar="";
        int besinKalori=0,besinProtein=0,besinSeker=0,besinYag=0;

        if(!tvBesinAd.getText().toString().equals(""))
            besinAd = tvBesinAd.getText().toString();

        if (!tvBesinMiktar.getText().toString().equals(""))
            besinMiktar = tvBesinMiktar.getText().toString();

           if(besinAd.equals("") && besinMiktar.equals("")
           && besinKalori<=0 && besinProtein<=0 && besinSeker<=0 && besinYag<=0){
               // girilen değerler istenildiği gibi değilse;
               Toast.makeText(getApplicationContext(),"Girdiğiniz değerler boş veya hatalı görünüyor",Toast.LENGTH_SHORT).show();
               textInputSıfırla();
           }
           else{
               // girilen değerler uygunsa kaydedelim
               String besinID = UUID.randomUUID().toString();
               Besin yeniBesin = new Besin(besinID,besinAd,besinMiktar,besinKalori,besinProtein,besinSeker,besinYag);

               // Firestoredaki Besinler koleksiyonuna besin ekleme fonksiyonu yazalım
               CollectionReference besinRef = db.collection("Besinler");
               besinRef.document(besinID).set(yeniBesin).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       Toast.makeText(getApplicationContext(),"Besin kaydedildi",Toast.LENGTH_SHORT).show();
                       textInputSıfırla();
                   }
               })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(getApplicationContext(),"Besin kaydederken hata : "+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                           }
                       });
           }






    }

    public void textInputSıfırla(){
        tvBesinAd.setText("");
        tvBesinMiktar.setText("");
        tvBesinKalori.setText("");
        tvBesinProtein.setText("");
        tvBesinSeker.setText("");
        tvBesinYag.setText("");
    }

    public void sayfaKapat(View v){
        finish();
    }
}

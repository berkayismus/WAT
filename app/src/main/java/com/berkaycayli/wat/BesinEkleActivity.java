package com.berkaycayli.wat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.berkaycayli.wat.objects.Besin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

               besinKalori = Integer.valueOf(tvBesinKalori.getText().toString());
               besinProtein = Integer.valueOf(tvBesinProtein.getText().toString());
               besinSeker = Integer.valueOf(tvBesinSeker.getText().toString());
               besinYag = Integer.valueOf(tvBesinYag.getText().toString());

               // girilen değerler uygunsa kaydedelim
               String besinID = UUID.randomUUID().toString();
               Besin yeniBesin = new Besin(besinID,besinAd,besinMiktar,besinKalori,besinProtein,besinSeker,besinYag);
               Map<String,Object> yeniBesinMap = new HashMap<>();
               yeniBesinMap.put("besin_id",besinID);
               yeniBesinMap.put("besin_adi",besinAd);
               yeniBesinMap.put("besin_miktar",besinMiktar);
               yeniBesinMap.put("besin_kalori",besinKalori);
               yeniBesinMap.put("besin_protein",besinProtein);
               yeniBesinMap.put("besin_seker",besinSeker);
               yeniBesinMap.put("besin_yag",besinYag);
               algoliaEkle(yeniBesinMap);


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


    public static void algoliaEkle(Map<String,Object> yeniBesinMap)  {
        Client client = new Client("7TY31JT4VI", "8dc615d927e06f9689b7b21ef4a9d92d");
        Index index = client.getIndex("Besinler");


        try{
            List<JSONObject> array = new ArrayList<JSONObject>();

            array.add(
                    new JSONObject(yeniBesinMap)
            );

            index.addObjectsAsync(new JSONArray(array), null);
        } catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }


    }


} // class sonu

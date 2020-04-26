package com.berkaycayli.wat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.berkaycayli.wat.objects.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GuestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    // SharedPreferenceses işlemleri
    SharedPreferences sp;

    // Authentication İşlemleri
    FirebaseAuth userAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = userAuth.getCurrentUser();

    // FireStore İşlemleri
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersCollectionRef = db.collection("Users");

    // Widgetleri tanımlama
    Spinner spinnerHedef, spinnerDogumYili, spinnerCinsiyet, spinnerEtkinlikDuzeyi;
    EditText etKilo, etBoy;


    // arrayListlerin Tanımlanması
    ArrayList<String> hedefArrayList = new ArrayList<>();
    ArrayList<Integer> dogumYiliArrayList = new ArrayList<>();
    ArrayList<String> cinsiyetArrayList = new ArrayList<>();
    ArrayList<String> etkinlikDuzeyiArrayList = new ArrayList<>();

    // User classına gönderilecek değişkenler
    String hedefSecilen = "",cinsiyetSecilen = "",etkinlikDuzeyiSecilen = "";
    int dogumYiliSecilen=0,kiloSecilen =0,boySecilen=0;

    @Override
    protected void onStart() {
        super.onStart();

        // Daha önceden GuestActivity formunu doldurmuş kullanıcı varsa
        // OgunActivity'e yönlendirelim
        sp = getSharedPreferences("formOK",MODE_PRIVATE);
        if(sp.contains("formOK")){
            startActivity(new Intent(getApplicationContext(),OgunActivity.class));
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        // widgetlar init ediliyor
        spinnerHedef = findViewById(R.id.spinnerHedef);
        spinnerDogumYili = findViewById(R.id.spinnerDogumYili);
        spinnerCinsiyet = findViewById(R.id.spinnerCinsiyet);
        spinnerEtkinlikDuzeyi = findViewById(R.id.spinnerEtkinlikDuzeyi);
        etKilo = findViewById(R.id.etKilo);
        etBoy = findViewById(R.id.etBoy);

        // arrayListler dolduruluyor
        hedefArrayList.add("Kilo Kaybı");
        hedefArrayList.add("Kilomu Koru");
        hedefArrayList.add("Kilo Alımı");
        /*dogumYiliArrayList.add(1994);
        dogumYiliArrayList.add(2004); */
        dogumYiliDoldur(dogumYiliArrayList);
        cinsiyetArrayList.add("Erkek");
        cinsiyetArrayList.add("Kadın");
        etkinlikDuzeyiArrayList.add("Az aktif");
        etkinlikDuzeyiArrayList.add("Aktif");
        etkinlikDuzeyiArrayList.add("Çok Aktif");

        // Spinnerlar için adapterler oluşturuluyor
        ArrayAdapter<String> hedefAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hedefArrayList);
        ArrayAdapter<Integer> dogumYiliAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, dogumYiliArrayList);
        ArrayAdapter<String> cinsiyetAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cinsiyetArrayList);
        ArrayAdapter<String> etkinlikAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, etkinlikDuzeyiArrayList);

        // Spinnerların adapterları set ediliyor
        spinnerHedef.setAdapter(hedefAdapter);
        spinnerCinsiyet.setAdapter(cinsiyetAdapter);
        spinnerDogumYili.setAdapter(dogumYiliAdapter);
        spinnerEtkinlikDuzeyi.setAdapter(etkinlikAdapter);

        // Spinnerların temaları ayarlanıyor
        hedefAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dogumYiliAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cinsiyetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etkinlikAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Seçilen spinnerların listenerları ayarlanıyor
        spinnerHedef.setOnItemSelectedListener(this);
        spinnerDogumYili.setOnItemSelectedListener(this);
        spinnerCinsiyet.setOnItemSelectedListener(this);
        spinnerEtkinlikDuzeyi.setOnItemSelectedListener(this);


    }

    // DoğumYılı Arraylistini dolduran fonksiyon
    public void dogumYiliDoldur(ArrayList<Integer> dogumYiliArrayList){
        for (int i=1940; i<=2002; i++)
        dogumYiliArrayList.add(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if(parent.getId() == R.id.spinnerHedef)
        {
            hedefSecilen = hedefArrayList.get(position);
           // Toast.makeText(getApplicationContext(),targetChoosen,Toast.LENGTH_SHORT).show();
        }

        if(parent.getId() == R.id.spinnerDogumYili){
            dogumYiliSecilen = dogumYiliArrayList.get(position);
            // Toast.makeText(getApplicationContext(),birthYearChoosen,Toast.LENGTH_SHORT).show();
        }

        if(parent.getId() == R.id.spinnerCinsiyet){
            cinsiyetSecilen = cinsiyetArrayList.get(position);
           // Toast.makeText(getApplicationContext(),genderChoosen,Toast.LENGTH_SHORT).show();
        }

        if(parent.getId() == R.id.spinnerEtkinlikDuzeyi){
            etkinlikDuzeyiSecilen = etkinlikDuzeyiArrayList.get(position);
            // Toast.makeText(getApplicationContext(),actLevelChoosen,Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void kaydetButton(View v){

        kiloSecilen = Integer.parseInt(etKilo.getText().toString());
        boySecilen = Integer.parseInt(etBoy.getText().toString());

        // kullanıcıların girdiği saçma sapan değerlere izin vermeyelim
        if(kiloSecilen<40 || boySecilen<155 || kiloSecilen>150 || boySecilen>220){
            Toast.makeText(getApplicationContext(),"Değerleri doğru girdiğinizden emin olunuz",Toast.LENGTH_SHORT).show();
        }

        // formdaki girilen değerler doğruysa(tutarlıysa) else çalışacak
        else{
            double hedefKalori = hedefKaloriHesapla(hedefSecilen,dogumYiliSecilen,cinsiyetSecilen,kiloSecilen,etkinlikDuzeyiSecilen,boySecilen);
            // final Users yeniKullanici = new Users(hedefSecilen,cinsiyetSecilen,etkinlikDuzeyiSecilen,dogumYiliSecilen,kiloSecilen,boySecilen,hedefKalori);

            // Veritabanına yazabilmek için bugünün tarihini alalım
        /*Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime()); */

            FirebaseUser user = userAuth.getCurrentUser();
            String userID = user.getUid();
            Users newUser = new Users(userID,boySecilen,kiloSecilen,hedefSecilen,hedefKalori,etkinlikDuzeyiSecilen,cinsiyetSecilen
                    ,dogumYiliSecilen, getBugun());

            // Alttaki kod satırları döküman ID'yi , userID olarak ayarlıyor
            usersCollectionRef.document(userID).set(newUser)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(GuestActivity.this, "Bilgileriniz kaydedildi!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),OgunActivity.class));
                            finish();
                        }
                    });


       // form doldurulursa SP'ye bildirelim , çünkü form doldurulduysa OgunActivity'e yönlendireceğiz -> onStart
            SharedPreferences.Editor spEditor = sp.edit();
            spEditor.putString("formOK","ok");
            spEditor.commit();
        } // else bloğu sonu


    }

    // doğru çalışıyor - Berkay -> hedef kalori 2690.2
    public double hedefKaloriHesapla(String hedefSecilen, int dogumYiliSecilen, String cinsiyetSecilen, int kiloSecilen, String etkinlikDuzeyiSecilen, int boySecilen){

        double hedefFaktor=0.0;
        double etkinlikDuzeyiFaktor=0.0;
        double hedefKalori=0.0;
        int yas = 2020-dogumYiliSecilen;

        if(cinsiyetSecilen.equals("Kadın")){

            if(hedefSecilen.equals("Kilo Kaybı")){
                hedefFaktor = 500;
                if(etkinlikDuzeyiSecilen.equals("Az Aktif")){
                    etkinlikDuzeyiFaktor = 50;
                }
            }
            else if(hedefSecilen.equals("Kilomu Koru")){
                hedefFaktor = 650;
                if(etkinlikDuzeyiSecilen.equals("Aktif")){
                    etkinlikDuzeyiFaktor = 125;
                }
            }
            else if(hedefSecilen.equals("Kilo Alımı")){
                hedefFaktor = 800;
                if(etkinlikDuzeyiSecilen.equals("Aktif")){
                    etkinlikDuzeyiFaktor = 200;
                }
            }

            hedefKalori = 655 + ( 9.6 * kiloSecilen ) + ( 1.8 * boySecilen ) - ( 4.7 * yas ) + hedefFaktor + etkinlikDuzeyiFaktor;
            //spText.setText(String.valueOf(hedefFaktor));
        }


        if(cinsiyetSecilen.equals("Erkek")){

            if(hedefSecilen.equals("Kilo Kaybı")){
                hedefFaktor = 500;
                if(etkinlikDuzeyiSecilen.equals("Az Aktif")){
                    etkinlikDuzeyiFaktor = 50;
                }
            }
            else if(hedefSecilen.equals("Kilomu Koru")){
                hedefFaktor = 650;
                if(etkinlikDuzeyiSecilen.equals("Aktif")){
                    etkinlikDuzeyiFaktor = 125;
                }
            }
            else if(hedefSecilen.equals("Kilo Alımı")){
                hedefFaktor = 800;
                if(etkinlikDuzeyiSecilen.equals("Aktif")){
                    etkinlikDuzeyiFaktor = 200;
                }
            }

            hedefKalori = 66 + ( 13.7 * kiloSecilen ) + ( 5 * boySecilen ) - ( 6.8 * yas) + hedefFaktor + etkinlikDuzeyiFaktor;

        }

        return hedefKalori;
    }

    public static String getBugun(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date()); // Find todays date

            return currentDate;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }




}

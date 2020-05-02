package com.berkaycayli.wat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.berkaycayli.wat.adapter.OgunAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class OgunActivity extends AppCompatActivity {

    // Bugünün tarihini tutalım
    String bugunTarih;

    // Widgetleri tanımlama
    private TextView tvKalanKalori, tvAlinanKalori, tvKalanKaloriNumber, tvAlinanKaloriNumber;
    private ImageView imageViewSabahAcKapa,imageViewOglenAcKapa,imageViewAksamAcKapa,imageViewSabah;
    private Toolbar toolbar;

    // RecyclerView & Adapterları tanımlama
    private RecyclerView rvSabah,rvOgle,rvAksam;
    private ArrayList<String> ogunlerList;
    private OgunAdapter ogunAdapter;

    // Firebase tanımlama işlemleri
    private FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userColRef = db.collection("Users");



    @Override
    protected void onStart() {
        super.onStart();

        // kullanıcının hedefine istinaden hedef_kalorisini , kalan kalori alanına çekelim
        userColRef.document(userAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String hedefKalori = documentSnapshot.get("user_hedef_kalori").toString();
                            tvKalanKaloriNumber.setText(hedefKalori+" kcal");
                        }
                    }
                });


        // aşağıdaki fonksiyon bugünün tarihini tutuyor, takvimden değiştirince güncelliyor
        bugunTarih = GuestActivity.getBugun();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogun);


        // widgetleri init etme (initial = başlatma) - arayüzdeki elemanları başlatmak için fonksiyon oluşturdum
        // aslında kısaca findViewById kısımlarını tek bir yerde topladım
        widgetInit();

        // toolbarın düzenleme işlemlerini aşağıdaki metodda yapıyorum. Kodları derli toplu hale getirmek için
        toolbarEdit();

        // RecyclerView'ların düzenleme işlemlerini aşağıdaki metodda yapıyorum. Kodları derli toplu hale getirmek için
        recyclerViewEdit();

        // imageView komponentleri tıklandığı anda ne olacağını aşağıdaki metodda belirliyorum
        imageViewClicked();


    }

    // menu/ogun_activity_menu yü ekleme
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ogun_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    // menüdeki seçeneklere tıklandığında aksiyon belirleme
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cikis:
                // giriş yapmış kullanıcı varsa
                if(userAuth!=null) {
                    userAuth.signOut();
                    SharedPreferences sp = getSharedPreferences("formOK",MODE_PRIVATE);
                    SharedPreferences.Editor spEditor = sp.edit();
                    spEditor.remove("formOK");
                    spEditor.commit();
                    Intent intent = new Intent(OgunActivity.this,UserRegister.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            case R.id.action_takvim:
                // TAKVİM yazısına tıklayınca takvimi açar
                // yil , ay ve günün tanımlandığı yerler bugünün değerlerini otomatik getirir
                Calendar calendar = Calendar.getInstance();
                int yil = calendar.get(Calendar.YEAR);
                int ay = calendar.get(Calendar.MONTH);
                int gun = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(OgunActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        bugunTarih = year+"-"+(month+1)+"-"+dayOfMonth;
                      //  editTextTarih.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },yil,ay,gun);


                datePickerDialog.setTitle("Tarih seçiniz");
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Ayarla",datePickerDialog);
                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"İptal",datePickerDialog);
                datePickerDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void widgetInit(){
        toolbar = findViewById(R.id.toolbarOgun);

        tvKalanKalori = findViewById(R.id.tvKalanKalori);
        tvKalanKaloriNumber = findViewById(R.id.tvKalanKaloriNumber);
        tvAlinanKalori = findViewById(R.id.tvAlinanKalori);
        tvAlinanKaloriNumber = findViewById(R.id.tvAlinanKaloriNumber);

        imageViewSabahAcKapa = findViewById(R.id.imageViewSabahAcKapa);
        imageViewOglenAcKapa = findViewById(R.id.imageViewOglenAcKapa);
        imageViewAksamAcKapa = findViewById(R.id.imageViewAksamAcKapa);

        rvSabah = findViewById(R.id.rvSabah);
        rvOgle = findViewById(R.id.rvOgle);
        rvAksam = findViewById(R.id.rvAksam);

        imageViewSabah = findViewById(R.id.imageViewSabah);

    }

    public void recyclerViewEdit(){
        // rvSabah Düzenleme - (RecyclerView)
        rvSabah.setHasFixedSize(true);
        rvSabah.setLayoutManager(new LinearLayoutManager(this));
        // başka bir görünüm
        //rv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        ogunlerList = new ArrayList<>();
        ogunlerList.add("Elma");
        ogunlerList.add("Armut");

      //  ogunAdapter = new OgunAdapter(this,ogunlerList);
     //   rvSabah.setAdapter(ogunAdapter);

        // rvOgle Düzenleme - (RecyclerView)
        rvOgle.setHasFixedSize(true);
        rvOgle.setLayoutManager(new LinearLayoutManager(this));
      //  rvOgle.setAdapter(ogunAdapter);

        // rvAkşam Düzenleme - (RecyclerView)
        rvAksam.setHasFixedSize(true);
        rvAksam.setLayoutManager(new LinearLayoutManager(this));
      //  rvAksam.setAdapter(ogunAdapter);
    }

    public void toolbarEdit(){
        // toolbar Ayarları
        toolbar.setTitle("Öğünler");
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbarTitleColor));
        toolbar.setSubtitle("Toolbar altbaşlığı");
        toolbar.setLogo(R.drawable.icon_toolbar); // toolbar için icon ayarlama
        setSupportActionBar(toolbar); // toolbarı ayarlama
    }

    public void imageViewClicked(){
        // imageViewSabahAcKapa'nın tıklanabilirliğini ayarlama ve yönetme
        imageViewSabahAcKapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // aç kapa imajına basıldığında yüksekliği "0" yapıyo, eğer sıfırsa eski haline çeviriyor
                // aç-kapa anahtarı gibi çalışıyor
                if(rvSabah.getLayoutParams().height!=0){
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
                    rvSabah.setLayoutParams(lp);
                } else{
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rvSabah.setLayoutParams(lp);
                }
            }
        });

        // imageViewOglenAcKapa'nın tıklanabilirliğini ayarlama ve yönetme
        imageViewOglenAcKapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rvOgle.getLayoutParams().height!=0){
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
                    rvOgle.setLayoutParams(lp);
                } else{
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rvOgle.setLayoutParams(lp);
                }
            }
        });

        // imageViewAksamAcKapa'nın tıklanabilirliğini ayarlama ve yönetme
        imageViewAksamAcKapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rvAksam.getLayoutParams().height!=0){
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
                    rvAksam.setLayoutParams(lp);
                } else{
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rvAksam.setLayoutParams(lp);
                }
            }
        });

        // Sabah cardView'ının + ikonuna tıklandığında olacağı belirliyorum
        imageViewSabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SearchActivity.class));
            }
        });
    }

} // class sonu

package com.berkaycayli.wat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.berkaycayli.wat.adapter.BesinAdapter;
import com.berkaycayli.wat.objects.Besin;

import com.berkaycayli.wat.objects.Ogunler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Genel tanımlamalar
    private Toolbar toolbarSearch;

    // Firestoredan tanımlamaları
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference besinColRef = db.collection("Besinler");

    // Recyclerview için tanımlamalar
    private RecyclerView recyclerViewBesin;
    private List<Besin> besinList = new ArrayList<Besin>();
    private BesinAdapter besinAdapter;

    // besin ekleme yapabilmek için oluşturulan değişkenler
    String ogunTuru = ""; // Öğün türü önceki sayfadan intent ile gelecek ( Seçilen öğüne göre )
    private static final String TAG = "Öğün LOG";

    // algolia tanımlamalar
    Client client = new Client("7TY31JT4VI", "8dc615d927e06f9689b7b21ef4a9d92d");
    Index index = client.getIndex("Besinler");

    // besin eklemek için gerekli olanlar
    FirebaseAuth userAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = userAuth.getCurrentUser();
    ArrayList<String> besinIDArray = new ArrayList<String>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // findViewById ile tanıtılan görsel öğeleri aşağıdaki fonksiyonda topluyorum
        widgetInit();

        // toolbar düzenlemelerini aşağıdaki fonksiyonda gerçekleştiriyorum
        toolbarEdit();

        // recyclerview düzenlemelerini aşağıdaki fonksiyonda gerçekleştiriyorum
        besinGetir();

        // Öğün türünü intent ile önceki aktiviteden alalım
        Bundle extras = getIntent().getExtras();
        ogunTuru = extras.getString("ogun_turu");

        //Intent intent = new Intent(getApplicationContext(),BesinEkleActivity.class);
        //intent.put

        // besinlere tıklandığında ne olacağını ayarlayalım
        userBesinEkle();



    }

    // görsel öğeleri burada tanımlıyorum
    private void widgetInit(){
        toolbarSearch = findViewById(R.id.toolbarSearch);
        recyclerViewBesin = findViewById(R.id.recyclerViewBesin);
        recyclerViewBesin.setHasFixedSize(true);
        recyclerViewBesin.setLayoutManager(new LinearLayoutManager(this));
        besinAdapter = new BesinAdapter(getApplicationContext(),besinList);
        recyclerViewBesin.setAdapter(besinAdapter);


    }

    // toolbar düzenlemelerini bu fonksiyonda gerçekleştiriyorum
    private void toolbarEdit(){
        toolbarSearch.setTitle("Besin Ara");
        toolbarSearch.setSubtitle("Eklemek için dokunun");
        // aşağıdaki kod ile toolbarın sol tarafa ikon eklenebilir
        // toolbarSearch.setLogo(R.drawable.ic_close_black_24dp);
        toolbarSearch.setTitleTextColor(getResources().getColor(R.color.tvTextColor));
        setSupportActionBar(toolbarSearch);

    }

    // SearchActivity için res/menu klasörünün altında önceden oluşturduğum menüyü ekliyorum
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_activity_menu,menu);

        // arama işlemleri için gerekli olan kodlamaları yapıyorum
        MenuItem item = menu.findItem(R.id.action_ara);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    // Menüdeki itemlara tıklandığında ne olacağını ayarlıyorum
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_photo:
               // Toast.makeText(this, "Foto ikonuna tıklandı", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),ResimCekActivity.class));
                return true;
            case R.id.action_close:
                finish();
                return true;
            case R.id.action_besin_ekle:
                startActivity(new Intent(getApplicationContext(),BesinEkleActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // arama kısmına herhangi bir şey yazıp enter'a(veya telefondaysa arama ikonuna) basılınca tetiklenecek fonksiyon
    @Override
    public boolean onQueryTextSubmit(String arananKelime) {
        //Log.e("Yapılan arama: ",arananKelime);

        return true;
    }

    // arama kısmına her harf girildiğinde tetiklenecek fonksiyon
    // aslında bu bir listener, harf girilip girilmediğini dinliyor
    @Override
    public boolean onQueryTextChange(String newText) {

        // algolia ile arama yapılıyor
        Query query = new Query(newText)
                .setAttributesToRetrieve("besin_adi")
                .setHitsPerPage(20);




       index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(@Nullable JSONObject content, @Nullable AlgoliaException e) {
                try {
                    JSONArray hits = content.getJSONArray("hits");
                    //List<String> list = new ArrayList<>();
                   // System.out.println();
                    List<String> list = new ArrayList<String>();
                    for (int i=0; i<hits.length(); i++){
                        JSONObject jsonObject = hits.getJSONObject(i);
                        // json olarak algolia besin indisini döndürüyorflutter
                        //System.out.println(jsonObject);

                        String besin_adi = jsonObject.getString("besin_adi");
                        list.add(besin_adi);
                        besinGetir(besin_adi);

                    }
                    // arrayadapter tanımı
                    // recyclerViewBesin.setAdapter(arrayAdapter);
                   System.out.println("Arama sonucu "+list.toString());

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

            }
        });



       // Log.d("Girilen harf: ",newText);
        return true;


    }

    private void besinGetir(){

        besinColRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                // sonradan eklenen listenin tamamını tekrar eklemesin diye besinList.clear
                besinList.clear();
                for (DocumentSnapshot documentSnapshot : documentSnapshots){
                    Besin besin = documentSnapshot.toObject(Besin.class);
                    besinList.add(besin);
                }
                // besinAdapter'ü durumdan haberdar edelim
                besinAdapter.notifyDataSetChanged();
            }
        });


    } // besinGetir Sonu

    private void besinGetir(String besin_adi){
        besinColRef
                .whereEqualTo("besin_adi", besin_adi)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Besin> arananBesinList = new ArrayList<Besin>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Besin arananBesin = document.toObject(Besin.class);
                                arananBesinList.add(arananBesin);
                                //

                            }
                            besinAdapter = new BesinAdapter(getApplicationContext(),arananBesinList);
                            recyclerViewBesin.setAdapter(besinAdapter);
                            userBesinEkle();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void userBesinEkle(){

        // Ogunler -> userID -> bugununTarihi -> ogunTuru
        besinAdapter.setOnItemClickListener(new BesinAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                //Toast.makeText(getApplicationContext(),"SearchActivity "+besinList.get(position).getBesin_adi(),Toast.LENGTH_SHORT).show();
                String userID = currentUser.getUid();
                String ogun_tarihi = GuestActivity.getBugun();
                String ogunID = userID+"/"+GuestActivity.getBugun()+"/"+ogunTuru;
                besinIDArray.add(besinList.get(position).getBesin_id());
                Ogunler ogun = new Ogunler(besinIDArray,ogunID,ogun_tarihi,ogunTuru,userID);
                DocumentReference ogunDocRef = db.collection("Ogunler").document(userID+"/"+GuestActivity.getBugun()+"/"+ogunTuru);
                ogunDocRef.set(ogun)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),besinList.get(position).getBesin_adi()+" "+ogunTuru+" öğününüze eklendi",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Firestore", "Besin eklerken hata",e);
                            }
                        });

            }
        });
    }








} // class sonu


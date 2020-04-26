package com.berkaycayli.wat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.berkaycayli.wat.adapter.BesinAdapter;
import com.berkaycayli.wat.objects.Besin;
import com.berkaycayli.wat.objects.Ogunler;
import com.berkaycayli.wat.objects.Users;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Tanımlamalar yapılıyor
    private Toolbar toolbarSearch;

    // Firestoredan veri çekmek için gerekli olan tanımlamalar yapılıyor
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference besinRef = db.collection("Besinler");

    // Recyclerview için tanımlamalar
    private RecyclerView recyclerViewBesin;
    private BesinAdapter besinAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // findViewById ile tanıtılan görsel öğeleri aşağıdaki fonksiyonda topluyorum
        widgetInit();

        // toolbar düzenlemelerini aşağıdaki fonksiyonda gerçekleştiriyorum
        toolbarEdit();

        // recyclerview düzenlemelerini aşağıdaki fonksiyonda gerçekleştiriyorum
        setupRecyclerView();


    }

    // görsel öğeleri burada tanımlıyorum
    private void widgetInit(){
        toolbarSearch = findViewById(R.id.toolbarSearch);

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
                Toast.makeText(this, "Foto ikonuna tıklandı", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // arama kısmına herhangi bir şey yazıp enter'a(veya telefondaysa arama ikonuna) basılınca tetiklenecek fonksiyon
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("Yapılan arama: ",query);
        return true;
    }

    // arama kısmına her harf girildiğinde tetiklenecek fonksiyon
    // aslında bu bir listener, harf girilip girilmediğini dinliyor
    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("Girilen harf: ",newText);
        return true;
    }

    private void setupRecyclerView(){

        Query query = besinRef.orderBy("besin_adi", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Besin> options = new FirestoreRecyclerOptions.Builder<Besin>()
                .setQuery(query,Besin.class)
                .build();

        besinAdapter = new BesinAdapter(options);
        recyclerViewBesin = findViewById(R.id.recyclerViewBesin);
        recyclerViewBesin.setHasFixedSize(true);
        recyclerViewBesin.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBesin.setAdapter(besinAdapter);

        // besinlerden herhangi birine tıklayınca ne olacağını ayarlayalım
        besinAdapter.setOnItemClickListener(new BesinAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Besin besin = documentSnapshot.toObject(Besin.class);
                Toast.makeText(getApplicationContext(),"Seçilen besin : "+besin.getBesin_adi(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        besinAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        besinAdapter.stopListening();
    }
}

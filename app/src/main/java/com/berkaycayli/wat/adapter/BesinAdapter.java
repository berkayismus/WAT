package com.berkaycayli.wat.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.berkaycayli.wat.GuestActivity;
import com.berkaycayli.wat.R;
import com.berkaycayli.wat.objects.Besin;
import com.berkaycayli.wat.objects.Ogunler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class BesinAdapter extends RecyclerView.Adapter<BesinAdapter.BesinHolder> {
    private Context context;
    private List<Besin> besinList;
    // private List<Besin> besinSorguListDisaridanGelen;

    // Firebase işlemleri
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();


    // clicks
    private OnItemClickListener listener;

    public BesinAdapter(Context context, List<Besin> besinList) {
        this.context = context;
        this.besinList = besinList;

    }

    public class BesinHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvBesinAdi, tvBesinMiktar, tvBesinKalori;
        private CardView cardViewBesin;

        public BesinHolder(@NonNull View itemView) {
            super(itemView);
            tvBesinAdi = itemView.findViewById(R.id.card_besin_tvBesinAdi);
            tvBesinMiktar = itemView.findViewById(R.id.card_besin_tvBesinMiktar);
            tvBesinKalori = itemView.findViewById(R.id.card_besin_tvBesinKalori);
            cardViewBesin = itemView.findViewById(R.id.cardViewBesin);

            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION && listener!=null){
                listener.onItemClick(position);

            }
            //Toast.makeText(v.getContext(),"Tıklanan besin "+besinList.get(getAdapterPosition()).getBesin_adi(),Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public BesinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_besin,parent,false);

        return new BesinHolder(itemView);

    }

    // sürekli çalışır, tüm liste öğelerini belirlenen tasarıma döker
    @Override
    public void onBindViewHolder(@NonNull BesinHolder holder, int position) {
        final Besin besin = besinList.get(position);


        holder.tvBesinAdi.setText(besin.getBesin_adi());
        holder.tvBesinKalori.setText(besin.getBesin_kalori() + " Kalori");
        holder.tvBesinMiktar.setText(besin.getBesin_miktar());
/*        holder.cardViewBesin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Seçtiğiniz besin "+besin.getBesin_adi(),Toast.LENGTH_SHORT).show();

            }
        });*/



    }

    @Override
    public int getItemCount() {
        return besinList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }



} // class sonu

package com.berkaycayli.wat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.berkaycayli.wat.R;
import com.berkaycayli.wat.objects.Besin;

import java.util.List;

public class OgunAdapter extends RecyclerView.Adapter<OgunAdapter.OgunCardItemsHolder> {
    private Context context;
    private List<Besin> besinlerGelenList;
   // private List<Besin> besinSorguListDisaridanGelen;

    public OgunAdapter(Context context, List<Besin> besinlerGelenList) {
        this.context = context;
        this.besinlerGelenList = besinlerGelenList;

    }

    public class OgunCardItemsHolder extends RecyclerView.ViewHolder{
        private TextView tvBesinAdi, tvBesinMiktar, tvBesinKalori;
        private CardView cardViewOgun;

        public OgunCardItemsHolder(@NonNull View itemView) {
            super(itemView);
            cardViewOgun = itemView.findViewById(R.id.cardViewBesin);
            tvBesinAdi = itemView.findViewById(R.id.card_besin_tvBesinAdi);
            tvBesinMiktar = itemView.findViewById(R.id.card_besin_tvBesinMiktar);
            tvBesinKalori = itemView.findViewById(R.id.card_besin_tvBesinKalori);
        }
    }

    @NonNull
    @Override
    public OgunCardItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_besin,parent,false);

        return new OgunCardItemsHolder(itemView);

    }

    // sürekli çalışır, tüm liste öğelerini belirlenen tasarıma döker
    @Override
    public void onBindViewHolder(@NonNull OgunCardItemsHolder holder, int position) {
        final Besin besin = besinlerGelenList.get(position);


        holder.tvBesinAdi.setText(besin.getBesin_adi());
        holder.tvBesinKalori.setText(String.valueOf(besin.getBesin_kalori()) + " Kalori");
        holder.tvBesinMiktar.setText(besin.getBesin_miktar());
        holder.cardViewOgun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Seçtiğiniz öğün: "+besin.getBesin_adi(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return besinlerGelenList.size();
    }



}

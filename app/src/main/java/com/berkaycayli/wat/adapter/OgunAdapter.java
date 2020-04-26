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

import java.util.List;

public class OgunAdapter extends RecyclerView.Adapter<OgunAdapter.OgunCardItemsHolder> {
    private Context context;
    private List<String> ogunlerDisaridanGelenList;

    public OgunAdapter(Context context, List<String> ulkelerDisaridanGelenList) {
        this.context = context;
        this.ogunlerDisaridanGelenList = ulkelerDisaridanGelenList;
    }

    public class OgunCardItemsHolder extends RecyclerView.ViewHolder{
        public TextView tvOgunAdi,tvOgunMiktar,tvOgunKalori;
        public CardView cardViewOgun;

        public OgunCardItemsHolder(@NonNull View itemView) {
            super(itemView);
            cardViewOgun = itemView.findViewById(R.id.cardViewBesin);
            tvOgunAdi = itemView.findViewById(R.id.card_besin_tvBesinAdi);
            tvOgunMiktar = itemView.findViewById(R.id.card_besin_tvBesinMiktar);
            tvOgunKalori = itemView.findViewById(R.id.card_besin_tvBesinKalori);

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
       final String ogun = ogunlerDisaridanGelenList.get(position);

        holder.tvOgunAdi.setText(ogun);
        holder.cardViewOgun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Seçtiğiniz öğün: "+ogun,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ogunlerDisaridanGelenList.size();
    }



}

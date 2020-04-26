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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class BesinAdapter extends FirestoreRecyclerAdapter<Besin,BesinAdapter.BesinHolder> {

    private OnItemClickListener listener;


    public BesinAdapter(@NonNull FirestoreRecyclerOptions<Besin> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BesinHolder holder, int position, @NonNull Besin model) {
        holder.tvBesinAdi.setText(model.getBesin_adi());
        holder.tvBesinKalori.setText(model.getBesin_kalori() + " kalori");
        holder.tvBesinMiktar.setText(model.getBesin_miktar());
    }

    @NonNull
    @Override
    public BesinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_besin, parent, false);
        return new BesinHolder(v);
    }

    class BesinHolder extends RecyclerView.ViewHolder {
        public TextView tvBesinAdi, tvBesinKalori, tvBesinMiktar;

        public BesinHolder(@NonNull View itemView) {
            super(itemView);
            tvBesinAdi = itemView.findViewById(R.id.card_besin_tvBesinAdi);
            tvBesinKalori = itemView.findViewById(R.id.card_besin_tvBesinKalori);
            tvBesinMiktar = itemView.findViewById(R.id.card_besin_tvBesinMiktar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

        }


    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}

package com.example.javamaps.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javamaps.databinding.RecyclerRowBinding;
import com.example.javamaps.view.MapsActivity;
import com.example.javamaps.view.model.Place;

import java.util.Collections;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceHolder> {

    List<Place> placelist;

    public PlaceAdapter(List<Place> placelist) {
        this.placelist = placelist;
    }
    public List<Place> getPlaceList() {
        return placelist;
    }
    public void setPlaceList(List<Place> newList) {
        placelist = newList;
        notifyDataSetChanged(); // veya notifyItemRemoved(position) gibi daha özelleştirilmiş şekilde
    }



    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PlaceHolder(recyclerRowBinding);
    }

    @Override
    public int getItemCount() {
        return placelist.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int position) {
        holder.recyclerRowBinding.textViewItem.setText(placelist.get(position).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), MapsActivity.class);
                intent.putExtra("info","old");
                intent.putExtra("place",placelist.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    public class PlaceHolder extends  RecyclerView.ViewHolder{
        RecyclerRowBinding recyclerRowBinding;
        public PlaceHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;
        }
    }
}
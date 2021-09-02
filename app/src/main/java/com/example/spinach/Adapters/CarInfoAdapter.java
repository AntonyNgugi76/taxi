package com.example.spinach.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spinach.CarInfoGetterSetter;
import com.example.spinach.R;

import java.util.ArrayList;

public class CarInfoAdapter extends RecyclerView.Adapter<CarInfoAdapter.InfoViewHolder> {

    ArrayList<CarInfoGetterSetter> carInfoGetterSetters;


    public CarInfoAdapter(ArrayList<CarInfoGetterSetter> carInfoGetterSetters) {
        this.carInfoGetterSetters = carInfoGetterSetters;
    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_image, parent, false);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarInfoAdapter.InfoViewHolder holder, int position) {
        CarInfoGetterSetter carInfoGetterSetter = carInfoGetterSetters.get(position);
        holder.image.setImageResource(carInfoGetterSetter.getImage());

    }

    @Override
    public int getItemCount() {
        return carInfoGetterSetters.size();
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public InfoViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}


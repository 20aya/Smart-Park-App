package com.andriod.smartparking1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andriod.smartparking1.Delete_plate;
import com.andriod.smartparking1.R;
import com.andriod.smartparking1.Classes.plate_vehicle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class vehicle_list_adapter extends RecyclerView.Adapter<vehicle_list_adapter.MyViewHolder2> {
  Context context2 ;
  ArrayList<plate_vehicle> vehicles_list ;


    public vehicle_list_adapter(Context context2 , ArrayList<plate_vehicle> vehicles_list) {
        this.vehicles_list = vehicles_list;
        this.context2 = context2;
    }// end of constructer


    @NonNull
    @NotNull
    @Override
    public vehicle_list_adapter.MyViewHolder2 onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v2 = LayoutInflater.from(context2).inflate(R.layout.items_of_vehicles , parent , false);

        return new MyViewHolder2(v2);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull vehicle_list_adapter.MyViewHolder2 holder, int position) {

        plate_vehicle p2 = vehicles_list.get(position); // we get the object of vehicle

        holder.imageforplate.setImageResource(R.drawable.pplat2);
        holder.text_vehicle.setText("Vehicle "+(position+1) + ":" );
        holder.vehiclenum.setText(p2.getPlatenum());
        System.out.println("PLEASEEEEE WORKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        holder.deletbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n2 = new Intent(context2, Delete_plate.class);
                n2.putExtra("userid",p2.getUserid_norm() );
                n2.putExtra("plateid" , p2.getPlate_id());
                n2.putExtra("plate_num" , p2.getPlatenum());
                context2.startActivity(n2);
            }
        });//end delete button
    }

    @Override
    public int getItemCount() {
        return this.vehicles_list.size();
    }

    public static class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView text_vehicle , vehiclenum , deletbtn;
        ImageView imageforplate;

        public MyViewHolder2(@NonNull @NotNull View itemView) {
            super(itemView);
            text_vehicle  = itemView.findViewById(R.id.textView45);
            vehiclenum =itemView.findViewById(R.id.textView46);
            imageforplate =itemView.findViewById(R.id.imageView10);
            deletbtn = itemView.findViewById(R.id.textView99);

        }
    }// end of class2
}// end of class


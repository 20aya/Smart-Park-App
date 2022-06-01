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

import com.andriod.smartparking1.Classes.BookedPark;
import com.andriod.smartparking1.ChatforOwner;
import com.andriod.smartparking1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Adapterbookeduser extends RecyclerView.Adapter<Adapterbookeduser.vholder2>{

    Context context3 ;
   ArrayList<BookedPark> booked_list ;

    FirebaseDatabase database44;
    //database44 = FirebaseDatabase.getInstance();

    public Adapterbookeduser(Context context3, ArrayList< BookedPark> booked_list) {
        this.booked_list = booked_list;
        this.context3 = context3;
    }



    @NonNull
    @NotNull
    @Override
    public Adapterbookeduser.vholder2 onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v4 = LayoutInflater.from(context3).inflate(R.layout.item_bookedusers_own , parent , false);

        return new Adapterbookeduser.vholder2(v4);
    }


    public void onBindViewHolder(@NonNull @NotNull Adapterbookeduser.vholder2 holder, int position) {
         //assigning values of park:
        System.out.println(position + " mnbnbmnbmnbmnbmnbmnbmnbmnbmnbmnbmbbmmnmbnmbnmbnmnbn");
        holder.pricetxt.setText( booked_list.get(position).getFull_price());
        holder.totxt.setText(booked_list.get(position).getTime_to());
        holder.fromtxt.setText( booked_list.get(position).getTime_from());
        holder.statustxt.setText( booked_list.get(position).getStatusbooked());
        holder.personimage.setImageResource(R.drawable.booked_user_icon);
        holder.platetxt.setText(booked_list.get(position).getPlatenumber());
        holder.Daytxt.setText(booked_list.get(position).getDay());
      //  booked_list.get(position).get
     //  String userid = (String)position;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(booked_list.get(position).getBooker_id());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
              //  for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (snapshot.child("uid").getValue().toString().equals(booked_list.get(position).getBooker_id())) {
                        holder.nametxt.setText(snapshot.child("namme").getValue().toString());
                        holder.phonetxt.setText(snapshot.child("phonenum").getValue().toString());
                   // }//end if
                }//end for
            }//end on datachange

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
       //holder.nametxt.setText(ref.child("namme").get);

        holder.messageimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context3 , ChatforOwner.class);
                intent.putExtra("name", holder.nametxt.getText().toString());
                intent.putExtra("userid" , booked_list.get(position).getBooker_id());

                context3.startActivity(intent);
            }//end onclick
        });//end handling image message
    }//end onbind view holder

    @Override
    public int getItemCount() {

        return this.booked_list.size();
    }



    public static class vholder2 extends RecyclerView.ViewHolder {

   TextView priceh , pricetxt , statush , statustxt, nameh , nametxt , phoneh, phonetxt,
        timeh , fromtxt , toh , totxt , plateh , platetxt, Dayh , Daytxt ;
   ImageView personimage, messageimage;
        public vholder2(@NonNull @NotNull View itemView) {
            super(itemView);
            Dayh = itemView.findViewById(R.id.textView52);
            Daytxt= itemView.findViewById(R.id.textView54);
            priceh = itemView.findViewById(R.id.textView87);
            pricetxt = itemView.findViewById(R.id.textView88);
            statush = itemView.findViewById(R.id.textView79);
            statustxt =itemView.findViewById(R.id.textView85);
            nameh = itemView.findViewById(R.id.ghj44);
            nametxt = itemView.findViewById(R.id.textView80);
            phoneh = itemView.findViewById(R.id.textView81);
            phonetxt = itemView.findViewById(R.id.textView82);
            timeh = itemView.findViewById(R.id.textView83);
            fromtxt = itemView.findViewById(R.id.textView84);
            toh = itemView.findViewById(R.id.trew44);
            totxt = itemView.findViewById(R.id.textView86);
            personimage = itemView.findViewById(R.id.imageView23);
            messageimage = itemView.findViewById(R.id.imageView24);
            plateh = itemView.findViewById(R.id.textView52pt);
            platetxt = itemView.findViewById(R.id.textView54pt);

        }
    }//end of inner class
}// end class adapter

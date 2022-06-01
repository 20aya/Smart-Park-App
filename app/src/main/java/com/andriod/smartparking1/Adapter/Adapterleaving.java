package com.andriod.smartparking1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andriod.smartparking1.Classes.BookedPark;
import com.andriod.smartparking1.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Adapterleaving extends RecyclerView.Adapter<Adapterleaving.vholder>{

    Context context3 ;
    ArrayList<BookedPark> booked_list ;

    FirebaseDatabase database44;
    //database44 = FirebaseDatabase.getInstance();

    public Adapterleaving(Context context3, ArrayList<BookedPark> booked_list) {
        this.booked_list = booked_list;
        this.context3 = context3;
    }

    @NonNull
    @NotNull
    @Override
    public Adapterleaving.vholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v4 = LayoutInflater.from(context3).inflate(R.layout.item_leaving , parent , false);

        return new Adapterleaving.vholder(v4);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Adapterleaving.vholder holder, int position) {
       /// SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        ///SimpleDateFormat sdfe2= new SimpleDateFormat("HH:mm");
      ///  Date date = new Date();
      ///  sdf.format(date);
      ///  System.out.println(sdfe2.format(date));
     ///   System.out.println("testing the timmmmeerre");
      ///  String timefrom = booked_list.get(position).getTime_from().toString().substring(0,4);

       readingfirst(holder, position);

    }// onBindviewholder

    public void readingpic_locat(String owner, String spot2, DatabaseReference refrence, Adapterleaving.vholder holder) {
        database44 = FirebaseDatabase.getInstance();
        DatabaseReference resf = database44.getReference().child("ParkingSpot").child(owner);
        resf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot22) {
                for (DataSnapshot dataSnapshot: snapshot22.getChildren()){
                    System.out.println(spot2 + " spotyuu");
                    String []  spotids = spot2.split("/");
                    if (dataSnapshot.child("spot_id").getValue().toString().equals(spotids[0])){
                        holder.t3locattext.setText(dataSnapshot.child("longitude").getValue().toString()
                        + " , "+dataSnapshot.child("latitude").getValue().toString());

                        if (dataSnapshot.hasChild("linkpic")){
                            Glide.with(context3).load(dataSnapshot.child("linkpic").getValue().toString()).into(holder.imageperk3); }// end if
                        else{
                            holder.imageperk3.setImageResource(R.drawable.spoticon);}// end of else



                    }//end if
                      spotids= null;

                }//end for
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }// end of readingpic_stat




    @Override
    public int getItemCount() {
        return booked_list.size();
    }

    public static class vholder extends RecyclerView.ViewHolder {

        // refering to alll the elements :
         TextView t3locat , t3locattext , t3day, t3daytext , t3from, t3fromtext ,
        t3to , t3totext, t3sat, t3stattext ;
         ImageView imageperk3;


        public vholder(@NonNull @NotNull View itemView) {
            super(itemView);
            t3locat = itemView.findViewById(R.id.textView30);
            t3locattext= itemView.findViewById(R.id.textView34);
            t3day = itemView.findViewById(R.id.textView67);
            t3daytext = itemView.findViewById(R.id.textView68);
            t3from = itemView.findViewById(R.id.textView69);
            t3fromtext = itemView.findViewById(R.id.textView70);
            t3to = itemView.findViewById(R.id.textView71);
            t3totext = itemView.findViewById(R.id.textView72);
            t3sat = itemView.findViewById(R.id.textView73);
            t3stattext = itemView.findViewById(R.id.textView74);
            imageperk3 = itemView.findViewById(R.id.imageView20);


        }



    }// end of the inner class


    public void readingfirst(Adapterleaving.vholder holder, int position){

        DatabaseReference refrence54;
        database44 = FirebaseDatabase.getInstance();
        refrence54 = database44.getReference("BookedPark").child(booked_list.get(position).getBooker_id().toString());
        refrence54.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot22) {
                for (DataSnapshot dataSnapshot: snapshot22.getChildren()) {
                    System.out.println("aaaaaaaahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                    if (dataSnapshot.child("spotid_").getValue().toString().equals(
                            booked_list.get(position).getSpotid_()) && dataSnapshot.child("statusbooked").getValue().toString().equals("Open")){
                        System.out.println("please pleasea please please please pleasea please please please pleasea please please");
                        ///if (dataSnapshot.child("day").getValue().toString().equals(sdf.format(date))
                        /// ){

                        //changing the status of the park t o booked :)
                        ////*String booked = "booked";
                        DatabaseReference refrence44 = refrence54.child(dataSnapshot.child("spotid_").getValue().toString());
                        // ** dataSnapshot.child("statusbooked").getRef().setValue("Open");
                        ////* Map<String, Object> Map = new HashMap<>();
                        ////* Map.put("statusbooked", "Open");
                        ////* refrence44.updateChildren(Map);

                        //now putting it to the page with maybe counter if its possiable
                        holder.t3fromtext.setText(dataSnapshot.child("time_from").getValue().toString());
                        holder.t3totext.setText( dataSnapshot.child("time_to").getValue().toString());
                        if(dataSnapshot.child("statusbooked").getValue().toString() == null){
                            holder.t3stattext.setText(" Left ");
                        }//end if
                        //else if (dataSnapshot.child("statusbooked").getValue().toString().equals("No Attend") != true){
                        holder.t3stattext.setText( dataSnapshot.child("statusbooked").getValue().toString());//}
                        holder.t3daytext.setText(dataSnapshot.child("day").getValue().toString());
                        //calling mathod to read location &pic
                        readingpic_locat(booked_list.get(position).getOwnerid_(), dataSnapshot.child("spotid_").getValue().toString() , refrence44
                                , holder);

                        System.out.println(booked_list.get(position).getOwnerid_()+ "// plusss //"+dataSnapshot.child("spotid_").getValue().toString() );
                        //, holder);
                        System.out.println("bvbvbvbvbvbvbvbvvbvbvvvvbvbvvvvcvbv");
                        ///}//end if2
                    }//end if


                }//end for
            }//ondatachange



            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });















    }//end reading first
}//end of adapterleaving

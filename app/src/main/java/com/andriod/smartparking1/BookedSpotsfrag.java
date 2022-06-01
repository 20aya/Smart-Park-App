package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.andriod.smartparking1.Adapter.booked_Adapter;
import com.andriod.smartparking1.Classes.BookedPark;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BookedSpotsfrag extends AppCompatActivity {
 RecyclerView recyclerView33 ;
 ArrayList <BookedPark> bookedparkslist ;
 booked_Adapter booked_adapter;
FirebaseDatabase db ;
DatabaseReference refsatabase ;
FirebaseAuth fauthh ;
boolean stre ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_booked_spots);
        ProgressDialog progressDialog = new ProgressDialog(BookedSpotsfrag.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        recyclerView33 = findViewById(R.id.recgh1);
        recyclerView33.setHasFixedSize(true);
        recyclerView33.setLayoutManager(new LinearLayoutManager(this));
        bookedparkslist = new ArrayList<BookedPark>();
        booked_adapter = new booked_Adapter(BookedSpotsfrag.this , bookedparkslist);
        recyclerView33.setAdapter(booked_adapter);

        fauthh = FirebaseAuth.getInstance();
        String userids = fauthh.getUid();
        db= FirebaseDatabase.getInstance();
        ////*.child(userids)
        System.out.println("thththththththththththththththththththththththththththththhth");
        System.out.println(userids);
        refsatabase = db.getReference().child("BookedPark").child(userids);
        refsatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot1: snapshot.getChildren()) {
                   // for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                        // for (DataSnapshot )

                        //    plate_vehicle  p3 = dataSnapshot.getValue(plate_vehicle.class);
                        String parkid = dataSnapshot1.child("spotid_").getValue().toString();
                        String ownerid = dataSnapshot1.child("ownerid_").getValue().toString();
                        String time_from = dataSnapshot1.child("time_from").getValue().toString();
                        String time_to = dataSnapshot1.child("time_to").getValue().toString();
                        String day = dataSnapshot1.child("day").getValue().toString();
                        String full_price = dataSnapshot1.child("full_price").getValue().toString();
                        //  BookedPark hg = new BookedPark(userids,ownerid, parkid);
                        BookedPark hg = new BookedPark(userids, ownerid, parkid, time_from, time_to, day, full_price, "onhold"
                        , dataSnapshot1.child("platenumber").getValue().toString());

                        bookedparkslist.add(hg);

                   // }//inner for
                }//end of for
                booked_adapter.notifyDataSetChanged();
                progressDialog.dismiss();
                // for to not repeting plates :
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }





}
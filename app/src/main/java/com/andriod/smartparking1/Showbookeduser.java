package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.andriod.smartparking1.Adapter.Adapterbookeduser;
import com.andriod.smartparking1.Classes.BookedPark;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Showbookeduser extends AppCompatActivity {
    String ownerid , spotid;
    TextView titletxt;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance() ;
    String bookerid;
    ArrayList <BookedPark> happy = new ArrayList<BookedPark>() ;
    Adapterbookeduser adapterbookeduser ;
    RecyclerView recyclerView33;
    ImageView imagetitle ;
    String userid3;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_showbookeduser);
        ownerid = getIntent().getStringExtra("ownerid");
        spotid = getIntent().getStringExtra("spotid");
       // bookerid =getIntent().getStringExtra("bookerid");
       userid3 = firebaseAuth.getCurrentUser().getUid();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        recyclerView33 = findViewById(R.id.recbooked);
        recyclerView33.setHasFixedSize(true);
        recyclerView33.setLayoutManager(new LinearLayoutManager(this));
        //imagetitle.findViewById(R.id.imageView25);


     //   imagetitle.setImageResource(R.drawable.icon303);
        adapterbookeduser = new Adapterbookeduser(Showbookeduser.this , happy);
        recyclerView33.setAdapter(adapterbookeduser);
imagetitle= findViewById(R.id.imageView25yt);
        //reading frome firebase so we can find the booked spots first:

        DatabaseReference databaseReference = firebaseDatabase.getReference("BookedPark");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    bookerid = dataSnapshot.getKey();
                    System.out.println("bvbbvbvbvbvbvbvbvbvbvbvvbvbvvbvbvbvbvvbbvbvbvbbbbbvbbvbvbvbvbvbvbvbvbvbvb");
                    System.out.println(bookerid);
                   for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                           //  for (DataSnapshot dataSnapshot1 : dataSnapshot3.getChildren()) {
                        //   System.out.println("  bvbbvbvbvbvbvbvbvbvbvbvvbvbvvbvbvbvbvvbbvbvbvbbbbbvbbvbvbvbvbvbvbvbvbvbvb");
                        //   for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                           //    for (DataSnapshot dataSnapshot1 : dataSnapshot4.getChildren()) {
                                   System.out.println(dataSnapshot1.child("day").getValue() + "  bvbbvbvbvbvbvbvbvbvbvbvvbvbvvbvbvbvbvvbbvbvbvbbbbbvbbvbvbvbvbvbvbvbvbvbvb");
                                   System.out.println(dataSnapshot1.child("spotid_").getValue().equals(spotid) + " " + spotid + dataSnapshot1.child("spotid_").getValue());
                                   String[] spotdatabase ;
                                   spotdatabase = dataSnapshot1.child("spotid_").getValue().toString().split("/");
                                   if (spotdatabase[0].equals(spotid)) {
                                       System.out.println("bvbbvbvbvbvbvbvbvbvbvbvvbvbvvbvbvbvbvvbbvbvbvbbbbbvbbvbvbvbvbvbvbvbvbvbvb");
                                       String parkid = dataSnapshot1.child("spotid_").getValue().toString();
                                       String ownerid = dataSnapshot1.child("ownerid_").getValue().toString();
                                       String time_from = dataSnapshot1.child("time_from").getValue().toString();
                                       String time_to = dataSnapshot1.child("time_to").getValue().toString();
                                       String day = dataSnapshot1.child("day").getValue().toString();
                                       String full_price = dataSnapshot1.child("full_price").getValue().toString();
                                       String status = dataSnapshot1.child("statusbooked").getValue().toString();
                                       //  BookedPark hg = new BookedPark(userids,ownerid, parkid);
                                       BookedPark hg = new BookedPark(bookerid, ownerid, parkid, time_from, time_to, day, full_price, status,
                                               dataSnapshot1.child("platenumber").getValue().toString());
                                       happy.add(hg);

                                   }//end if



                   }//end inner for
                }//end for

                adapterbookeduser.notifyDataSetChanged();
                progressDialog.dismiss();
            }//end of onDataChange

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }//end on create
}//end class
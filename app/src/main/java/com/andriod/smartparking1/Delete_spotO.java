package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andriod.smartparking1.Adapter.Listadapter_p;
import com.andriod.smartparking1.Classes.Parkingspot;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Delete_spotO extends AppCompatActivity {
    String price, type , piclink , ownerid , spotid;
    TextView txtprice , txttype , txtquestion ;
    Button bno , byes ;
    ImageView parkimage;
    String userid= "";
    boolean check = true;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_spot_o);
        txtprice = findViewById(R.id.textView76);
        txttype = findViewById(R.id.textView78);
        txtquestion = findViewById(R.id.textView10rrrr3);
        bno = findViewById(R.id.button15);
        byes= findViewById(R.id.button13);
        parkimage = findViewById(R.id.imageView22ovv);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Deleting...");


        price = getIntent().getStringExtra("price");
        type = getIntent().getStringExtra("type");
        piclink = getIntent().getStringExtra("piclink");
        ownerid = getIntent().getStringExtra("ownerid");
        spotid = getIntent().getStringExtra("spotid");

        txtprice.setText("Price : " + price);
        txttype.setText("Type : " + type);

        if (piclink!= null){
            Glide.with(getApplicationContext()).load(piclink).into(parkimage);
        }//end if
        else{

           parkimage.setImageResource(R.drawable.spoticon);
        }// end else


        bno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), ParkownerMain_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });// end button no


        byes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                boolean res = checkavailbalitiy(spotid);
            }
        });
    }// end on create

    public boolean checkavailbalitiy(String  ps1){

        DatabaseReference red = FirebaseDatabase.getInstance().getReference("BookedPark");

        red.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    for (DataSnapshot dataSnapshot1n : dataSnapshot.getChildren()){

                        String [] idssw =dataSnapshot1n.child("spotid_").getValue().toString().split("/");
                        System.out.println("noor" +idssw[0].equals(ps1) + "/" + idssw[0] + " /"+ ps1  );
                        if (idssw[0].equals(ps1)) {
                            System.out.println(  dataSnapshot1n.child("statusbooked").getValue().toString()+"lkjhg22" + (dataSnapshot1n.child("statusbooked").getValue().toString().equals("On Hold")==true||
                                    dataSnapshot1n.child("statusbooked").getValue().toString().equals("Open")==true)   );
                            if(  dataSnapshot1n.child("statusbooked").getValue().toString().equals("On Hold")==true ||
                                    dataSnapshot1n.child("statusbooked").getValue().toString().equals("Open")==true ){
                                System.out.println( "lkjhg3" + dataSnapshot1n.child("statusbooked").getValue().toString()+ "/");

                                check = false;

                                break;

                            }//end if





                        }//outer if



                    }//end inner for
                }//end for outer

                   if(check ==  false ){
                       progressDialog.dismiss();
                       Toast.makeText(getApplicationContext(), "You can not delete your spot, try another Time"
                               , Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(Delete_spotO.this, ParkownerMain_Page.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       getApplicationContext().startActivity(intent);
                       finish();
                                     }//end final if
                else {
                    canceling ();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });//end firebase

        System.out.println(check +" check12313");
        return  check;



    }//END CHECKING AVAILABLITY


    public void canceling(){
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("ParkingSpot").child(ownerid);
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // if(dataSnapshot.hasChild())
                    if (dataSnapshot.child("spot_id").getValue().toString().equals(spotid)) {

                       // boolean res = checkavailbalitiy(spotid);
                      //  if (res ==  true) {
                            ref2.child(spotid).removeValue();
                        deletepastspots();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),  "Spot got deleted Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Delete_spotO.this, ParkownerMain_Page.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                        finish();


                      //  }//end inner if
                        //*else if (res ==true){

                        //*  Toast.makeText(getApplicationContext(), "You cant delete your spot, try another Time"
                        //*  , Toast.LENGTH_SHORT).show();

                        //* }//end second if
                    }// end outer if


                }// end for
            }//ondatachange

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }

    public void deletepastspots(){

        DatabaseReference red223w = FirebaseDatabase.getInstance().getReference("BookedPark");

        red223w.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                     userid = dataSnapshot.getKey().toString();
                    for (DataSnapshot dataSnapshot1n : dataSnapshot.getChildren()){

                        String [] idssw =dataSnapshot1n.child("spotid_").getValue().toString().split("/");

                        if (idssw[0].equals(spotid)) {
                        //  Toast.makeText(Delete_spotO.this, userid+" delete booked", Toast.LENGTH_LONG).show();
                          String id = dataSnapshot1n.getKey();
                          red223w.child(userid).child(id).removeValue();
                          //  progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), " Spot got deleted Successfully!", Toast.LENGTH_SHORT).show();
                          //  Intent intent = new Intent(Delete_spotO.this, ParkownerMain_Page.class);
                         //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         //   getApplicationContext().startActivity(intent);
                          //  finish();




                        }//outer if




                    }//end inner for
                }//end for outer



            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });//end firebase







    }//end delteting past spots
}//end on class
package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andriod.smartparking1.Adapter.Listadapter_p;
import com.andriod.smartparking1.Classes.Parkingspot;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Edit_Spot extends AppCompatActivity {
    String price, type;
    double priced ;
    String linpic2;
    ImageView imageView ;
    String spotid;
    ArrayList arrlistpark ;
    ImageView nng;

    Listadapter_p palist;
    String pricedchange ;
    boolean typechanged;

    String movePrice;
    String movetype;
    String movelinkpic;
    String movespotid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_spot);
         movePrice = getIntent().getStringExtra("price");
        movetype = getIntent().getStringExtra("type");
         movelinkpic = getIntent().getStringExtra("piclink");
        movespotid = getIntent().getStringExtra("spotid");
         nng = findViewById(R.id.imageView22ovv);
        TextView pricetxt = findViewById(R.id.textViewere);
        EditText edprice = findViewById(R.id.editTextNumbern21);
        ImageView mge2 = findViewById(R.id.imageView22op);
        SwitchCompat ss21 = findViewById(R.id.switch21);
        double priced = Double.valueOf(movePrice);
        Button savebtn = findViewById(R.id.button12);
        // imageView.findViewById(R.id.imageView22op);
        //  priced = edprice.getText();

        if (movelinkpic != null) {

            // mge2.setImageResource(R.drawable.spoticon);
            Glide.with(getApplicationContext()).load(movelinkpic).into(mge2);
        }//end if 1

        else {
            mge2.setImageResource(R.drawable.spoticon);
            //Glide.with(getContext()).load(linpic2).into(mge2);
        }//end else
        //  mge2.seti
        edprice.setText(movePrice);
        if (movetype.equals("Indoor")) {
            ss21.setChecked(true);
        }//end if 1
        else {
            ss21.setChecked(false);
        }//end else

        mge2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //saving changes to firebase
        //FirebaseAuth fauthh2;

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edprice.getText().toString().equals("")){
                    Toast.makeText(Edit_Spot.this, "please fill the empty field", Toast.LENGTH_LONG).show();


                }//end if
                else if (edprice.getText().toString().equals("0.00")||edprice.getText().toString().equals("0") ||
                        edprice.getText().toString().equals("0.")||edprice.getText().toString().equals(".")){
                    edprice.setError("price can't be zero");
                    Toast.makeText(Edit_Spot.this, "please specify a price ", Toast.LENGTH_LONG).show();

                }
                else {
                    chang(edprice.getText().toString(), ss21.isChecked());

                    Intent intent = new Intent(Edit_Spot.this, ParkownerMain_Page.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                    finish();
                }//end else not empty
            }// ed of onclivck

        });//save buutton


        Button cancelbut = findViewById(R.id.button14);
        cancelbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Edit_Spot.this, ParkownerMain_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });


        // context.startActivity(intent);
    }//end of oncreate


    public void     chang(String pr2 , boolean bb){
        FirebaseAuth fauthh2;
        fauthh2 = FirebaseAuth.getInstance();
        String ownerrid = fauthh2.getUid();


        DatabaseReference zone1Ref = FirebaseDatabase.getInstance().getReference("ParkingSpot").child(ownerrid);
        zone1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // if(dataSnapshot.hasChild())
                    if (dataSnapshot.child("spot_id").getValue().toString().equals(movespotid)) {
                        dataSnapshot.child("price").getRef().setValue(pr2);
                        // if (dd= true){
                        String  witch_type4;
                        if (bb==true ){
                            witch_type4 = "Indoor"; }//if first
                        else{
                            witch_type4 ="Outdoor"; }//else

                        dataSnapshot.child("spot_type").getRef().setValue(witch_type4);
                        Toast.makeText(getApplicationContext(), "You updated the spot Successfully!!" , Toast.LENGTH_LONG).show();

                        // palist.notifyDataSetChanged();
                        //Intent intt =  new Intent(getContext() , owner_main.class);
                        String prc =  dataSnapshot.child("price").getValue().toString();
                        // chang(prc , witch_type4);

                        //fh.getSu
                        // break;

                    }
                    Parkingspot spotd2 = dataSnapshot.getValue(Parkingspot.class);
                    //  arrlistpark.add(spotd2);

                }
                //   palist.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });



        //return pr2 +","+ bb;
        this.pricedchange = pr2;
        this.typechanged = bb;


    }// end of method


}
package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Delete_bookedspot extends AppCompatActivity {
    String price, location , from , to , day , owned , phone , ownerid , spotid, userid;
    TextView pricetxt , priceh , txtquestion , locationh , locationtxt , fromh , fromtxt , toh , totxt,
    dayh , daytxt , ownedh , ownedtxt, phoneh , phonetxt;
    Button bno , byes ;
    ImageView parkimage;
    boolean statr = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_bookedspot);

        ///intializing viewsss
        priceh = findViewById(R.id.textView50rrr);
        pricetxt = findViewById(R.id.textView51rrr);
        txtquestion = findViewById(R.id.textView10rrrr3);
        locationh = findViewById(R.id.textView45ttrrrr);
        locationtxt = findViewById(R.id.textView46ttrrrr);
        fromh = findViewById(R.id.textView564r);
        fromtxt = findViewById(R.id.textView57ee);
        toh = findViewById(R.id.khgisss);
        totxt = findViewById(R.id.sdgkdbs);
        dayh = findViewById(R.id.textView22o);
        daytxt = findViewById(R.id.textView30kkfgh);
        ownedh = findViewById(R.id.textView52df);
       ownedtxt = findViewById(R.id.textView53kj);
       phoneh = findViewById(R.id.textView54lk);
        phonetxt = findViewById(R.id.oohg);
        bno = findViewById(R.id.button17);
        byes = findViewById(R.id.button16);
        parkimage = findViewById(R.id.imageView22ovv);

        /////getting the values forwarded from the other class

        price = getIntent().getStringExtra("fullprice");
        location = getIntent().getStringExtra("location");
        userid = getIntent().getStringExtra("userid");
        ownerid = getIntent().getStringExtra("ownerid");
        spotid = getIntent().getStringExtra("spotid");
       from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
       day = getIntent().getStringExtra("day");
        phone = getIntent().getStringExtra("phone");
        owned = getIntent().getStringExtra("owned");

        //setting values:
        pricetxt.setText(price);
        locationtxt.setText(location);
        fromtxt.setText(from);
        totxt.setText(to);
        daytxt.setText(day);
        phonetxt.setText(phone);
        ownedtxt.setText(owned);



        ////delting processs:
        bno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), MainPageNormal_user.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });// end button no


        byes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref289 = FirebaseDatabase.getInstance().getReference("BookedPark").child(userid);
                ref289.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            // if(dataSnapshot.hasChild())
                            if (dataSnapshot.child("spotid_").getValue().toString().equals(spotid)) {
                                String idss[] = dataSnapshot.child("spotid_").getValue().toString().split("/");
                                String keyid = idss[1];
                                ref289.child(keyid).removeValue();
                               // idss = null;
                                statr =true ;
                                Toast.makeText(getApplicationContext(), idss[1]+" Your Booking got deleted Successfully!" , Toast.LENGTH_SHORT).show();


                            }// end if
                        }// end for

                        Intent intent = new Intent (getApplicationContext(), MainPageNormal_user.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                        finish();
                    }//ondatachange

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });

if (statr == true ){
    statr = false;
    Intent intent = new Intent (getApplicationContext(), MainPageNormal_user.class);
    getApplicationContext().startActivity(intent);

    finish();
}//end if

    }//end oncreate
}
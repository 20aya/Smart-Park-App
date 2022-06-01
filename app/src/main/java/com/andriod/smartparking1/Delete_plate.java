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

public class Delete_plate extends AppCompatActivity {
TextView txtq , txtplate ;
Button btnyes , btnno;
ImageView image ;
String plateid , userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_plate);
        image = findViewById(R.id.imageView22uy);
        txtq = findViewById(R.id.textView10uy);
        txtplate = findViewById(R.id.textView78uyt);
        txtplate.setText(getIntent().getStringExtra("plate_num").toString());
        btnyes = findViewById(R.id.button13uy);
        btnno = findViewById(R.id.button15uy);
        plateid = getIntent().getStringExtra("plateid");
        userid = getIntent().getStringExtra("userid");
       // txtplate.setText("loveeeee");
        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference refd = FirebaseDatabase.getInstance().getReference("Plates").child(userid);
                refd.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            if (dataSnapshot.child("plate_id").getValue().toString().equals(plateid)){
                                refd.child(plateid).removeValue();
                                Toast.makeText(getApplicationContext(), "Vehicle got deleted Successfully!" , Toast.LENGTH_SHORT).show();


                            }//end if


                        }//end for
                        Intent intent = new Intent (getApplicationContext(),MainPageNormal_user.class);
                        getApplicationContext().startActivity(intent);
                        finish();
                    }//on datachamge

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                }); // addvalue firebsse

            }//end on click
        });//button yes

        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(),MainPageNormal_user.class);
                getApplicationContext().startActivity(intent);
                finish();
            }//end on click
        });//end button no


    }//on create
}
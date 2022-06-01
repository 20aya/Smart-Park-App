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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class editprofile extends AppCompatActivity {
    Button b777save; // logout button
    String userid;
    String type;
    TextView textUsernameh , textUsername ,textUserEmail, textUserEmailh, textUserPhoneh ,  textUserPhone;
    //to fetch data
    FirebaseAuth fAuth44;
    FirebaseUser user44;

    ImageView profileimg, phoneimg, nameimg , emailimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        b777save = (Button) findViewById(R.id.button21);
        textUserEmail = findViewById(R.id.editTextTextEmailAddress3);
        textUserEmailh = findViewById(R.id.textView18);
        textUsername = findViewById(R.id.editTextTextPersonName3);
        textUsernameh = findViewById(R.id.textView12);
        textUserPhone = findViewById(R.id.editTextPhone2);
        textUserPhoneh = findViewById(R.id.textView20);
        profileimg = findViewById(R.id.imageView22);
        phoneimg = findViewById(R.id.imageView8);
        nameimg = findViewById(R.id.imageView6);
        emailimg =findViewById(R.id.imageView7);
        fAuth44 = FirebaseAuth.getInstance();
        userid = fAuth44.getUid();
textUserPhone.setText(getIntent().getStringExtra("phone"));
textUsername.setText(getIntent().getStringExtra("name"));
textUserEmail.setText(getIntent().getStringExtra("email"));

b777save.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if ( textUsername.getText().toString().equals("")|| textUserPhone.getText().toString().equals("")){
            Toast.makeText(editprofile.this, "please fill the empty field!!", Toast.LENGTH_LONG).show();

        }//end if
        else if (textUserPhone.getText().toString().length()<10){
            textUserPhone.setError("not legit phone number!! ");
            Toast.makeText(editprofile.this, "Phone number should be at least 10 digits", Toast.LENGTH_LONG).show();

        }//end if
        else if (textUsername.getText().toString().length()<6){
            textUsername.setError("please enter your full name ");
            Toast.makeText(editprofile.this, "please enter at least ,  your first & last name", Toast.LENGTH_LONG).show();

        }//end if
        else if ((textUsername.getText().toString().equals("")|| textUserPhone.getText().toString().equals(""))==false &&
                textUserPhone.getText().toString().length()>=10
        &&textUsername.getText().toString().length()>=6){


       savinginforamtion();}//end else if














    }


});





    }//oncreate



    private void savinginforamtion() {
        //conditionssssssss here

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                databaseReference.child("namme").getRef().setValue(textUsername.getText().toString());
                databaseReference.child("phonenum").getRef().setValue(textUserPhone.getText().toString());
               // databaseReference.child("emaill").getRef().setValue(textUserEmail.getText().toString());

                type = snapshot.child("type").getValue().toString();
                if (type.equals("Normal Type")){

                    Toast.makeText(getApplicationContext() , "Information updated Successfully!!" ,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(editprofile.this , MainPageNormal_user.class);
                    startActivity(intent);
                    finish();

                }//end if

                else{

                    Toast.makeText(getApplicationContext() , "Information updated Successfully!!" ,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(editprofile.this , ParkownerMain_Page.class);
                    startActivity(intent);
                    finish();

                }//end else

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}///end class
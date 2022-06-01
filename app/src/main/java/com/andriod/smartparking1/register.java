package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andriod.smartparking1.Classes.Users_O;
import com.andriod.smartparking1.Classes.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

public class register extends AppCompatActivity {

    ImageView M2 ;
    TextView t11 ,t22,t33,t44 , t55 , t66 , t77;
    Button b11 ;
    ProgressBar P11;
    FirebaseAuth fAuth ;
    int typeeid;
    //
    RadioGroup rg ;
    RadioButton rbutton ;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fAuth = FirebaseAuth.getInstance(); // getting the instances of the firebase
        // identifying the compoments
        M2 = findViewById(R.id.imageView);
        t11 = findViewById(R.id.textView);
        t22 = findViewById(R.id.textView2);
        t33 = findViewById(R.id.editTextTextPersonName);
        t44 = findViewById(R.id.editTextPhone);
        t55 = findViewById(R.id.editTextTextEmailAddress);
        t66= findViewById(R.id.editTextTextPassword);
        t77 = findViewById(R.id.textView3);
        b11 = findViewById(R.id.button);
        P11= findViewById(R.id.progressBar2);
        rg = (RadioGroup)findViewById(R.id.radiogroup2);
        // finsihing

        // checking if the user is already have an account:
        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainPageNormal_user.class));
            finish();
        }//endif



        //handling clicking the button for registering.
        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = t55.getText().toString().trim();
                String password = t66.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    t55.setError(" email is required!!, please enter it.");// show error message
                     return;  }// end 1 if
                if (TextUtils.isEmpty(password)){
                    t66.setError("Password is required!!, please enter it.");
                    return; }// end 2 if   // checking the length of password for more secuirty.
                if (password.length() < 7){
                    t66.setError("Password should be at least 7 characters!!");
                    return; }// end if 2
                P11.setVisibility(View.VISIBLE); // setting the progress bar
                // now registering the user by firebase // adding on complete listener to handle the registering if success or not
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // if successing then direct the user to the main activity
                            Toast.makeText(register.this, "verifaction link  have been sent to your email account ", Toast.LENGTH_SHORT).show();
                            typeeid = rg.getCheckedRadioButtonId();
                            rbutton = findViewById(typeeid);
                            String typestring = rbutton.getText().toString().trim();
                            // assigning values + creating object for user:) // saving user data to a database :) realtime data base in firebase
                            FirebaseDatabase rootNode = FirebaseDatabase.getInstance();//start pointing at database
                            DatabaseReference refrence = rootNode.getReference("Users");  // now to refrence Users:
                           String idd = task.getResult().getUser().getUid();   //  id for the new user
                            Users_O newwuser = new Users_O(idd, typestring, t33.getText().toString(), t44.getText().toString(), t66.getText().toString(), t55.getText().toString());
                            refrence.child(idd).setValue(newwuser);
                            Toast.makeText(getApplicationContext(), "a verfifcation email have been sent to your Email account " , Toast.LENGTH_SHORT).show();
                            if (typestring.equals("Normal Type")){ ///// forwarding the users to the intendted pages:
                                fAuth.getCurrentUser().sendEmailVerification();
                                startActivity(new Intent(getApplicationContext(), Login_page.class));
                                finish(); }//inner iff
                            else{
                                fAuth.getCurrentUser().sendEmailVerification();
                                startActivity(new Intent(getApplicationContext(), Login_page.class));
                                finish(); }// for inner if (else)
                        } // if task successful
                        else{
                            Toast.makeText(register.this ,"Error occured! "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        } } });





            }// end on click
        });// b11 setonclicklistener

        // if the user have account & want to log in

        t77.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sending the user to log in page
                startActivity(new Intent(getApplicationContext(),Login_page.class));
                finish();

            }//onclick
        });//on click lister t77




    }//// on create
    private void retriveAndstoreTokens(){

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                String token = task.getResult();
                String  userid = fAuth.getUid();
                Token token1 = new Token(token);
                FirebaseDatabase.getInstance().getReference("Tokens").child(userid).setValue(token1);




            }//end o n co,plete
        });

    }//end method


    //method to diffrenate between the owner & normaltype:


    //*

}// end of class
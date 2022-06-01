package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andriod.smartparking1.Classes.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Login_page extends AppCompatActivity {

    ImageView M1;
    TextView t1, t2, t3, t5, t6, t7, t8;
    Button b1;
    ProgressBar p1;
    FirebaseAuth fAuth;
    FirebaseUser user;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_page);

        fAuth = FirebaseAuth.getInstance();
        M1 = findViewById(R.id.imageView2);
        progressDialog = new ProgressDialog(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        t1 = findViewById(R.id.textView5);
        t2 = findViewById(R.id.textView6);
        t3 = findViewById(R.id.editTextTextEmailAddress2);
        t5 = findViewById(R.id.editTextTextPassword2);
        t6 = findViewById(R.id.textView7);
        b1 = findViewById(R.id.button2);
        t7 = findViewById(R.id.textView8);
        t8 = findViewById(R.id.textView9);



        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edemail = new EditText(v.getContext());
                AlertDialog.Builder passwordreset = new AlertDialog.Builder(v.getContext());
                passwordreset.setTitle("Reset Password");
                passwordreset.setMessage("Enter your Email to Recive Reset Link");
                passwordreset.setView(edemail);
                passwordreset.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // i will extract the reset linkn & send the reset link

                        String mail= edemail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), " reset Link has successfuly been sent to your email account" , Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error! reset Link is Not sent "+ e.getMessage() , Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

                passwordreset.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialoug


                    }
                });

                passwordreset.create().show();
            }//end on click
        });




        // finsishing the indetfying of the compoments

        // handling the log in button
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
         public void onClick(View v) { // conditions such as in the register page
                String email = t3.getText().toString().trim();
                String password = t5.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    t3.setError(" email is required!!, please enter it.");   // show error message
                    return; }// end 1 if
                if (TextUtils.isEmpty(password)) {
                    t5.setError("Password is required!!, please enter it.");
                    return; }// end 2 if
                if (password.length() < 7) {   // checking the length of password for more secuirty.
                    t5.setError("Password should be at least 7 characters!!");
                    return; }// end if 2
                progressDialog = new ProgressDialog(Login_page.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading ...");
                progressDialog.show();   // showing the prgressbar:
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference refrence2 ;
                            fAuth=FirebaseAuth.getInstance();
                            user = fAuth.getCurrentUser();
                            refrence2 = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                            refrence2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    String usertype = snapshot.child("type").getValue().toString();

                                    if (usertype.equals("Normal Type")){
                                    if (fAuth.getCurrentUser().isEmailVerified()){
                                        if (task.isSuccessful()) {
                                            retriveAndstoreTokens();
                                            startActivity(new Intent(getApplicationContext(),MainPageNormal_user.class));
                                            progressDialog.dismiss();
                                            Toast.makeText(Login_page.this, "You Logged in successfuly!", Toast.LENGTH_SHORT).show();
                                            finish();
                                            //Toast.makeText(getContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();
                                        }

                                        // if (t5.getText().toString().equals(snapshot.child("passw").getValue().toString())){
                                           // retriveAndstoreTokens();
                                           // startActivity(new Intent(getApplicationContext(),MainPageNormal_user.class));
                                          //  progressDialog.dismiss();
                                          //  Toast.makeText(Login_page.this, "You Logged in successfuly!", Toast.LENGTH_SHORT).show();
                                           // finish();

                                      //  }//end if password correct
                                        else{
                                            progressDialog.dismiss();
                                            Toast.makeText(
                                                Login_page.this, "your password or email entered incorrectly!!", Toast.LENGTH_SHORT).show();}//end oassword incorrect
                                        }//end ifverifed
                                        else{
                                        progressDialog.dismiss();
                                            Toast.makeText(Login_page.this, "please verify your email first!!", Toast.LENGTH_SHORT).show(); } }//if1
                                    else{
                                        if (fAuth.getCurrentUser().isEmailVerified()){

                                            if (task.isSuccessful()) {
                                                retriveAndstoreTokens();
                                                startActivity(new Intent(getApplicationContext(),ParkownerMain_Page.class));
                                                progressDialog.dismiss();
                                                Toast.makeText(Login_page.this, "You Logged in successfuly!", Toast.LENGTH_SHORT).show();
                                                finish();
                                                //Toast.makeText(getContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();
                                            }
                                          //  if (t5.getText().toString().equals(snapshot.child("passw").getValue().toString())){
                                               // retriveAndstoreTokens();
                                               // startActivity(new Intent(getApplicationContext(),ParkownerMain_Page.class));
                                               // progressDialog.dismiss();
                                              //  Toast.makeText(Login_page.this, "You Logged in successfuly!", Toast.LENGTH_SHORT).show();
                                              //  finish();

                                           // }//end if password correct
                                            else{
                                                progressDialog.dismiss();
                                                Toast.makeText(Login_page.this, "your password or email entered incorrectly!!", Toast.LENGTH_SHORT).show();}//end oassword incorrect
                                                    }//end if
                                        else{
                                            progressDialog.dismiss();
                                            Toast.makeText(Login_page.this, "please verify your email first!!", Toast.LENGTH_SHORT).show(); } } }//ondatachnge
                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) { }//oncancelled
                            }); }//if1
                        else {

                            progressDialog.dismiss();
                            Toast.makeText(Login_page.this, "your password or email entered incorrectly!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }    }//onComplete
                     }); }//onclick
        });// b1 setonClicklistener



        t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sending the user to log in page
                startActivity(new Intent(getApplicationContext(), register.class));
                finish();
            }//onclick
        });//on click lister t8


    }//onCreate

///retrivivng & saving tokens

    private void retriveAndstoreTokens(){

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                String token = task.getResult();
                Token token1 = new Token(token);
                String  userid = fAuth.getUid();
                FirebaseDatabase.getInstance().getReference("Tokens").child(userid).setValue(token1);





            }//end o n co,plete
        });

    }//end method(


public void login(String userid){
    DatabaseReference refrence2 ;
    fAuth=FirebaseAuth.getInstance();
    user = fAuth.getCurrentUser();
    refrence2 = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
    refrence2.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {

            String usertype = snapshot.child("type").getValue().toString();
            if (usertype.equals("Normal Type")){
                if (fAuth.getCurrentUser().isEmailVerified()){
                    retriveAndstoreTokens();
                    startActivity(new Intent(getApplicationContext(),MainPageNormal_user.class));
                    finish();
                }//end if


            }//if1

            else{
                if (fAuth.getCurrentUser().isEmailVerified()){
                    retriveAndstoreTokens();
                    startActivity(new Intent(getApplicationContext(),ParkownerMain_Page.class));

                    finish();
                }//end if

            }//else
        }//ondatachnge

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }//oncancelled
    });////addd



}//end login

}//class
package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.andriod.smartparking1.Adapter.UserAdapter;
import com.andriod.smartparking1.Classes.Users_O;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class message_users extends AppCompatActivity {

        ArrayList <String > ownerids = new ArrayList<>();
    RecyclerView mainusers;
    UserAdapter userAdapter;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String userid ;
    String ownerid;
    ArrayList<Users_O> arrayListusers ;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_users);
        progressDialog = new ProgressDialog(message_users.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        auth =  FirebaseAuth.getInstance();


        mainusers = findViewById(R.id.mainuserresycal);
        mainusers.setHasFixedSize(true);

        mainusers.setLayoutManager(new LinearLayoutManager(this));
        arrayListusers=new ArrayList<Users_O>();

        userAdapter= new UserAdapter(message_users.this , arrayListusers);
       mainusers.setAdapter(userAdapter);


        // dtatbase:)))))) DatabaseReference reference = database.getReference().child("user");
        //reading from database
        readingbooked();





    }//end oncreate
    //reading owner info
    public void  puttingownerinfo(String ownerid2){
        DatabaseReference databaseReference2o = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference2o.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot2 : snapshot.getChildren()){

                   if (dataSnapshot2.getKey().equals(ownerid2)){
                       Users_O users_o = dataSnapshot2.getValue(Users_O.class);
                       if(arrayListusers.contains(users_o) == true)
                               break;
                      // arrayListusers.add(users_o);
                       else {arrayListusers.add(users_o);}
                   }//end if



               }//end for
System.out.println(arrayListusers.get(0).getNamme() +"iuiuiuiuiuiiuiuiuiuiuiuiuiuuiuiuiuiuiuiui");
                System.out.println(userAdapter.getItemCount()+" ///////////////////////////////// this shoub be one ");
                userAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }//end on datachange

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });//end reading database

    }//end method reading owner info

    public void readingbooked (){
        auth = FirebaseAuth.getInstance();
        userid = auth.getUid();
        //getting the ids of parkowner of the booked spots:
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("BookedPark").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ownerid = dataSnapshot.child("ownerid_").getValue().toString();
                    if (ownerids.contains(ownerid) != true){
                        ownerids.add(ownerid);
                        puttingownerinfo(ownerid);
                    }//end if


                }//end for

               // mainusers.setAdapter(userAdapter);

            }//ondatachange

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }//end on cancel
        });//end reading bookde park


    }//end reading booked
}//end class
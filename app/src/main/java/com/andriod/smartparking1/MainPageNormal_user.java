package com.andriod.smartparking1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;


import com.andriod.smartparking1.Classes.Token;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.NotNull;

public class MainPageNormal_user extends AppCompatActivity {
    Button b2;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String mUid = firebaseAuth.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.normalmain); //i think here


        // handling the bottom naviagtionn - ERROR SECOND LINE
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(navListner);
        //* bottomNavigationView.setOnItemSelectedListener(this);
        //* loadFragment(new main_frag());
      NavController navController = Navigation.findNavController(this,  R.id.fragmentContainerView3);
      NavigationUI.setupWithNavController(bottomNavigationView, navController);
        ////////////


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        //defining the logout button
        b2 = findViewById(R.id.button3);
        myRef.setValue("Hello, World!");

        //  handling the logout button
//*        b2.setOnClickListener(new View.OnClickListener() {
        //*    @Override
         //*   public void onClick(View v) {
             //*   FirebaseAuth.getInstance().signOut();
                //sending the user to log in page
             //*   startActivity(new Intent(getApplicationContext(), Login_page.class));
              //*  finish();
          //*  }//onclick
      //*  }); //be set on click listener
/////calling for tokkensss
        updateToken(FirebaseInstanceId.getInstance().getToken());


    }//oncreate

    @Override
    protected void onResume() {
        super.onResume();
    }

    ///for tokens sssss

    public void updateToken(String token ){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mtoken = new Token(token);
        databaseReference.child(mUid).setValue(mtoken);



    }//end update method






    // method for naviagation / switchinggg:
    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                    Fragment selectedfragment = null;
                    switch (item.getItemId()) {
                        case R.id.main_frag:
                            selectedfragment = new main_frag();
                            break;
                        case R.id.profile_frag:
                            selectedfragment = new profile_frag();
                            break;
                        case R.id.Vehicle_frag:
                            selectedfragment = new vehicle_frag();
                            break;
                    }//end switch

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView3
                            , selectedfragment).commit();
                    return true;
                }//on navv


            };


}//class
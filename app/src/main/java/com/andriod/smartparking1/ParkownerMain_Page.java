package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import static androidx.navigation.Navigation.findNavController;

public class ParkownerMain_Page extends AppCompatActivity {
 TextView fronttest2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkowner_main_page);
        //bottom  nevagtion setting up and making the pages changes
        BottomNavigationView bottomNav2 = findViewById(R.id.bottomNavigationView2);
        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        final NavController navController2 = navHostFragment.getNavController();

        //NavController navController2 = Navigation.findNavController(this,  R.id.fragmentContainerView);

        bottomNav2.setOnItemSelectedListener(navlistener2);
        NavigationUI.setupWithNavController(bottomNav2,navController2);
       // fronttest2 = findViewById(R.id.textView25);

        //bottomNav2.setOnItemSelectedListener(navlistener2);






    }//end oncreate
    private BottomNavigationView.OnNavigationItemSelectedListener navlistener2 =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                    Fragment SelectedFragment2 = null ;
                    switch (item.getItemId()){
                        case R.id.owner_main:
                            SelectedFragment2 = new owner_main();
                            break;

                        case R.id.owner_add:

                            SelectedFragment2 = new owner_add();

                            break;
                        case R.id.owner_prof:
                            SelectedFragment2 = new owner_prof();
                            break;
                    }//endswitch
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView
                            ,SelectedFragment2).commit();
                    return true;

                }
            };

}
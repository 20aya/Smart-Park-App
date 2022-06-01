package com.andriod.smartparking1;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class Barcode_shown extends AppCompatActivity {
    ImageView real ;
    String urlbarcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_shown);
        urlbarcode = getIntent().getStringExtra("barcode");

        real = findViewById(R.id.imageView22y);
        Uri uri = Uri.parse(urlbarcode);
        real.setImageURI(uri);
        //Glide.with(getApplicationContext()).load(urlbarcode).into(real);


    }
}
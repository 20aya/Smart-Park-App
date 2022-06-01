package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.andriod.smartparking1.Classes.Rate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class rateusDialog extends Dialog {
     float userRate =0;
FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
     String spot_id;


    public rateusDialog(@NonNull Context context , String spot_id) {

        super(context);
        this.spot_id = spot_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateus_dialog);
        final AppCompatButton ratenowbtn = findViewById(R.id.ratenowbtn);
        final AppCompatButton laterbtn = findViewById(R.id.laterbtn);
        final RatingBar ratingbar = findViewById(R.id.ratingbar2);
        final ImageView ratingimage = findViewById(R.id.ratingimage);
        ratenowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //svaing the rate in firebase :)
                String userid = firebaseAuth.getUid();
                FirebaseDatabase rootNode2 = FirebaseDatabase.getInstance();
                DatabaseReference refrence2 = rootNode2.getReference("Rating");
                Rate rate = new Rate(userid , spot_id , userRate);
                String []spotids = spot_id.split("/");

                 refrence2.child(spotids[0]).child(userid).setValue(rate);
                 spotids = null ;
                // inform user:0
                dismiss();
                Toast.makeText(getContext(), "Thank for your Rating!! :)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainPageNormal_user.class);
                getContext().startActivity(intent);



            }//end on click
        });// end on handling rate button
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating<=2){
                    ratingimage.setImageResource(R.drawable.sadsad);
                }//if
                else if (rating <=2){
                    ratingimage.setImageResource(R.drawable.yayy);
                }
                else if (rating <= 3){
                    ratingimage.setImageResource(R.drawable.hellllo21);
                }
                else if (rating <= 4){
                    ratingimage.setImageResource(R.drawable.hmmmm);
                }
                else if (rating <=5){
                    ratingimage.setImageResource(R.drawable.happy);
                }
                //animate emoji face
                animateImage(ratingimage);

                // selected rating by user
                userRate=rating;
            }
        });
        laterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                Intent intent = new Intent(getContext(), MainPageNormal_user.class);
                getContext().startActivity(intent);
            }
        });
    }
    private void animateImage (ImageView imageView){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1f,0,1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        imageView.startAnimation(scaleAnimation);
            }//animate
}
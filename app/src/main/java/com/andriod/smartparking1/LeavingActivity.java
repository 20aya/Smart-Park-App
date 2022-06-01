package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andriod.smartparking1.Adapter.Adapterleaving;
import com.andriod.smartparking1.Classes.BookedPark;
import com.andriod.smartparking1.Classes.captureAct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LeavingActivity extends AppCompatActivity  {
    RecyclerView recyclerView6 ;
    boolean dd = false ;
    ArrayList<BookedPark> bookedparkslist6 = new ArrayList<>() ;
    Adapterleaving booked_adapter6;
    FirebaseDatabase db6 ;
    DatabaseReference refsatabase6 ;
    //FirebaseAuth fauthh6 ;
    TextView scanparcode ;
    Calendar calendar = Calendar.getInstance();
    int cyear = calendar.get(Calendar.YEAR);
    int cmonth = calendar.get(Calendar.MONTH);
    int cday = calendar.get(Calendar.DAY_OF_MONTH);
    int cHour =calendar.get(Calendar.HOUR);
    int cMinute = calendar.get(Calendar.MINUTE);
    String parkid;
    FirebaseAuth fauthh6 = FirebaseAuth.getInstance();
    String userids = fauthh6.getUid();
    String bookedparid;
    String lang ;
    String lat;
    String ownerid;
    //boolean bool = false;
    //CodeScanner codescanner;
    //CodeScannerView codeScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        setContentView(R.layout.activity_leaving);
        recyclerView6 = findViewById(R.id.rec);
        recyclerView6.setHasFixedSize(true);
        recyclerView6.setLayoutManager(new LinearLayoutManager(this));
        bookedparkslist6 = new ArrayList<BookedPark>();
        booked_adapter6 = new Adapterleaving(LeavingActivity.this , bookedparkslist6);
        recyclerView6.setAdapter(booked_adapter6);
        scanparcode= findViewById(R.id.textViewharver);
        //scanparcode.setOnClickListener(this);


        db6= FirebaseDatabase.getInstance();
        refsatabase6 = db6.getReference().child("BookedPark").child(userids);
        System.out.println(userids + "plllllllllllllllllllllllllllllllllllllllllllllllllll");
        refsatabase6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int hourfrom ;
                int hour , hourto;
                int minow , minfrom , minto;
                DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat sdfe2= new SimpleDateFormat("HH:mm");
                Date date = new Date();
                sdf.format(date);
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    System.out.println("booker spot idddddd" + dataSnapshot.child("spotid_").getValue().toString()
                           + "plwease pleaese plees pleaes please please please please please ");
                  
                        parkid = dataSnapshot.child("spotid_").getValue().toString();
                        String ownerid = dataSnapshot.child("ownerid_").getValue().toString();
                        String time_from = dataSnapshot.child("time_from").getValue().toString();
                        hour = Integer.parseInt(sdfe2.format(date).substring(0, 2));
                        if(dataSnapshot.child("statusbooked").getValue().toString().equals("No Attend") != (true) ||
                                dataSnapshot.child("statusbooked").getValue().toString().equals("Left") != (true) ) {

                            //checking if the time from in 13,14.....
                            if (hour > 12 && time_from.substring(6, 8).equals("PM")) {
                                String timewithourr = time_from.substring(0, 2);
                                hourfrom = Integer.parseInt(timewithourr);
                                minfrom = Integer.parseInt(time_from.substring(3, 5));
                                hour = Integer.parseInt(sdfe2.format(date).substring(0, 2));
                                minow = Integer.parseInt(sdfe2.format(date).substring(3, 5));
                                hour = hour - 12;

                            }//end if checking m
                            else {
                                minow = Integer.parseInt(sdfe2.format(date).substring(3, 5));
                                minfrom = Integer.parseInt(time_from.substring(3, 5));
                                String timewithourr = time_from.substring(0, 2);
                                hourfrom = Integer.parseInt(timewithourr);
                                hour = Integer.parseInt(sdfe2.format(date).substring(0, 2));
                            }// end else
                            // hour2 = Integer.parseInt(sdfe2.format(date).substring(0,2));
                            System.out.println(hourfrom + " kk " + hour);
                            String time_to22 = dataSnapshot.child("time_to").getValue().toString();
                            //checking if the time from in 13,14..... for (time to)
                            if (hour > 12 && time_to22.substring(6, 8).equals("PM")) {
                                String timewithourrTO = time_to22.substring(0, 2);
                                hourto = Integer.parseInt(timewithourrTO);
                                minto = Integer.parseInt(time_to22.substring(3, 5));
                                hour = Integer.parseInt(sdfe2.format(date).substring(0, 2));
                                minow = Integer.parseInt(sdfe2.format(date).substring(3, 5));
                                hour = hour - 12;

                            }//end if checking m
                            else {
                                minow = Integer.parseInt(sdfe2.format(date).substring(3, 5));
                                minto = Integer.parseInt(time_to22.substring(3, 5));
                                String timewithourrTO = time_to22.substring(0, 2);
                                hourto = Integer.parseInt(timewithourrTO);
                                hour = Integer.parseInt(sdfe2.format(date).substring(0, 2));
                            }// end else

                            String day = dataSnapshot.child("day").getValue().toString();
                            String full_price = dataSnapshot.child("full_price").getValue().toString();
                            //  BookedPark hg = new BookedPark(userids,ownerid, parkid)

                            String m = dataSnapshot.child("day").getValue().toString().trim().replace("/", "-");
                            System.out.println(m);
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date dm = new Date();
                            try {
                                dm = dateFormat.parse(day);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            System.out.println(sdf.format(dm) + " love " + sdf.format(date));
                            System.out.println((hour >= hourfrom) + "hoursssssss" + " " + hour + " " + hourfrom);
                            System.out.println(sdf.format(dm).equals(sdf.format(date)));
                            if ((sdf.format(dm).equals(sdf.format(date))  )&& hour >= hourfrom && dataSnapshot.child("statusbooked").getValue().toString().equals("On Hold")) {

                                BookedPark hg6 = new BookedPark(userids, ownerid, parkid, time_from, time_to22, day, full_price, "Open"
                                ,"plate");

                                System.out.println(sdfe2.format(date) + " fully " + date + "comaprring yarrb");
                                DatabaseReference refrence44 = refsatabase6.child(dataSnapshot.child("spotid_").getValue().toString());
                                dataSnapshot.child("statusbooked").getRef().setValue("Open");
                                bookedparkslist6.add(hg6);
                                // System.out.println("aaaaaaaahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                                System.out.println("aaaaaaaahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");

                            }//end if day & time frommmm

                            if (dataSnapshot.child("statusbooked").getValue().toString().equals("Open")){
                                BookedPark hg6 = new BookedPark(userids, ownerid, parkid, time_from, time_to22, day, full_price, "Open", "plate");
                                bookedparkslist6.add(hg6);

                            }//end if open

                            //cheking time to to close the park
                            String [] noorday = day.split("/");
                            int smonth = Integer.parseInt(noorday[1]);
                            int sday = Integer.parseInt(noorday[0]);

                            int smin = Integer.parseInt(time_to22.substring(3,5));
                            System.out.println("asdfgh34" + cHour +" "+ hourto);
                            if ((cday>= sday) && (smonth<=cmonth+1   ) && dataSnapshot.child("statusbooked").getValue().toString().equals("Left")!= true ){
                                if (sday == cday && cHour> hourto )
                                    dataSnapshot.child("statusbooked").getRef().setValue("Not Attend");
                                else if (sday == cday && cHour== hourto && cMinute> smin)
                                    dataSnapshot.child("statusbooked").getRef().setValue("Not Attend");
                                else if (cday>sday && smonth==cmonth+1)
                                    dataSnapshot.child("statusbooked").getRef().setValue("Not Attend");
                                else if(smonth<cmonth+1){dataSnapshot.child("statusbooked").getRef().setValue("Not Attend");}
                                System.out.println("truelolgh" + (cday==sday) + (smonth==cmonth)  );

                            }//end if day Not attend

                        }//end checking status to
                }//end of for


                booked_adapter6.notifyDataSetChanged();

                progressDialog.dismiss();

            }//on datachange

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        /// my attempt // here when he click the button
        scanparcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator= new IntentIntegrator(LeavingActivity.this);
               intentIntegrator.setPrompt("For flash use volume up key");
               // set peep
                intentIntegrator.setBeepEnabled(true);
                //locked orientation
                intentIntegrator.setOrientationLocked(true);
                // set capture activity
                intentIntegrator.setCaptureActivity(captureAct.class);

                //intiate scan
                intentIntegrator.initiateScan();
                 //statleft();
            }


        });// on click scan button


    }// end of on create


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //intalize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,data);

        //check condition
        if (intentResult.getContents() != null){
            // when result is not null we will intaite dialog
            AlertDialog.Builder builder= new AlertDialog.Builder(LeavingActivity.this);
            int i = 0;
            while (i< bookedparkslist6.size()){
                String [] dds = bookedparkslist6.get(i).getSpotid_().split("/");
                if (intentResult.getContents().equals(dds[0])){ // if the the spot id = barcode id
                    parkid = intentResult.getContents();
                    statleft(userids , parkid); // method to change the state to leaving
                    builder.setTitle("you Left!"); //informing the user of leaving
                    builder.setMessage("You succefuly left the park with id: "+ userids +" "+intentResult.getContents() +"\n" + "\n Would you like to Rate this park!" );
                    builder.setPositiveButton(" Rate! ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            dd = true;
                            //*   statleft(userids , parkid);
                            rateusDialog RateusDialog = new rateusDialog(LeavingActivity.this, parkid) ;
                            RateusDialog.getWindow();
                           //*** RateusDialog.setCancelable(false);
                            RateusDialog.show();
                        }
                    });
                    //statleft();
                    builder.show();

                }//end if

                i++;
            }//end while
             if (i> bookedparkslist6.size()){
                 Toast.makeText(this , "Not the correct code :(" , Toast.LENGTH_LONG).show();

             }//end if

        }//end first if
        else {Toast.makeText(this , "unable to scan :(" , Toast.LENGTH_LONG).show();}
    }


    public void statleft(String user , String parkowner_park){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("BookedPark").child(user);

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int i =0 ;

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                       String [] dds2 = dataSnapshot.child("spotid_").getValue().toString().split("/");
                  System.out.println(parkowner_park + "*"+ dataSnapshot.child("spotid_").getValue().toString() + "  brrrrrrewq32") ;
                    if (parkowner_park.equals(dds2[0]) &&
                            dataSnapshot.child("statusbooked").getValue().toString().equals("Open")){
                       dataSnapshot.child("statusbooked").getRef().setValue("Left");


                        break;

                    }



                }//end of for
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });









    }//end of method





}
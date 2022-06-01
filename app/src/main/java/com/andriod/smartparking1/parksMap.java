package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andriod.smartparking1.Classes.Model;
import com.andriod.smartparking1.Classes.Picmark;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class parksMap extends AppCompatActivity implements OnMapReadyCallback  {
    String priceperhpurs;
    ArrayList<Picmark> backup ;
    int syear,sday,smonth;
    MapView bookmap;
    int hournow;
    int minnow;
    GoogleMap googlebookmap;
    ArrayList<Picmark> arrayList= new ArrayList<Picmark>();
    FirebaseDatabase firebaseDatabase33;
    DatabaseReference databaserefrence33;

    float result;
    int people ;

    ArrayList<Model> picurl_array ;

    String id_spot66;
    String status ;
    Button days;
    Button Times_from;
    Button Times_to , infmore;
    int hourr,minutee;
    TextView sar;
    Button search;
    ProgressDialog progressDialog;
    DatePickerDialog.OnDateSetListener setListener;
    Calendar calendar1n;
    private FusedLocationProviderClient mFusedLocationProviderClient33 = null; //
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parks_map);
        sar = findViewById(R.id.textView63);
        bookmap =findViewById(R.id.book_map);
        days = findViewById(R.id.button7);
        Times_from = findViewById(R.id.button8);
        Times_to = findViewById(R.id.button9);
        infmore=findViewById(R.id.button10);
        search = findViewById(R.id.button11);
        LocalDate localeDate = LocalDate.now();
        int noeday =localeDate.getDayOfMonth();

        Calendar calendar = Calendar.getInstance();
        int cyear = calendar.get(Calendar.YEAR);
        int cmonth = calendar.get(Calendar.MONTH);
        int cday = calendar.get(Calendar.DAY_OF_MONTH);
        int cHour =calendar.get(Calendar.HOUR);
        int cMinute = calendar.get(Calendar.MINUTE);

        String date = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm aa",Locale.getDefault()).format(new Date());
        days.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                days.setText("Day");
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        parksMap.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        syear = year;
                        smonth= month;
                        sday=dayOfMonth;
                        String sdate = sday+"/"+(smonth+1)+"/"+syear;
                        days.setText(sdate);



                    }
                },cyear,cmonth,cday
                );
                datePickerDialog.updateDate(syear,smonth,sday);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();

            }
        });

        Times_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get curent time in 12 hous format
                Times_to.setText("TO");
                String timen = new SimpleDateFormat("hh-mm aa" , Locale.getDefault()).format(new Date());
                //intialize time picker dialog
                TimePickerDialog timePickerDialogn = new TimePickerDialog(parksMap.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute2) {
                        // intialize hour & minute
                        hournow = hourOfDay;
                        minnow = minute2;
                        int shourn = hourOfDay;
                        int sminuten = minute2;
                        //imtalize calendar
                        calendar1n = Calendar.getInstance();
                        // get the date
                        if (days.getText().toString().equals("DAY")){
                            Toast.makeText(parksMap.this , "You should pick a day first!!!" , Toast.LENGTH_SHORT).show();

                        }//end if

                        else if (days.getText().toString()!= "DAY"){
                            String [] noor = days.getText().toString().split("/");
                            //get da from date
                            int sdayn = Integer.parseInt(noor[0]);
                            //set day onncalendar :
                            calendar1n.set(Calendar.DAY_OF_MONTH , sdayn);
                            //set hour on calendar
                            calendar1n.set(Calendar.HOUR_OF_DAY, shourn);
                            //set minute on calendar:
                            calendar1n.set(Calendar.MINUTE, sminuten);
                            //check coondtioin
                            if (calendar1n.getTimeInMillis() == Calendar.getInstance().getTimeInMillis()){
                                Toast.makeText(parksMap.this , "current time  selected!", Toast.LENGTH_SHORT).show();
                                Times_from.setText( DateFormat.format("hh:mm aa" , calendar1n)); }//end if
                            else if(smonth+1> cmonth+1){
                                Toast.makeText(parksMap.this ,  "Future time  selected!", Toast.LENGTH_SHORT).show();
                                Times_from.setText( DateFormat.format("hh:mm aa" , calendar1n));}
                            else if (smonth+1 == cmonth+1){
                                if(sday> noeday){
                                    Toast.makeText(parksMap.this , "Future time  selected!", Toast.LENGTH_SHORT).show();
                                    Times_from.setText( DateFormat.format("hh:mm aa" , calendar1n)); }//end inner if
                                else if (sday == noeday){
                                    Calendar datetime = Calendar.getInstance();
                                    Calendar c = Calendar.getInstance();
                                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    datetime.set(Calendar.MINUTE, minute2);
                                    if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                                        //it's after current
                                        int hour = hourOfDay % 12;
                                        Toast.makeText(getApplicationContext(), "Time selected", Toast.LENGTH_LONG).show();
                                        Times_from.setText( DateFormat.format("hh:mm aa" , calendar1n)); }
                                    else {
                                        Toast.makeText(parksMap.this , "NOT Reachable !!!! Past time  selected!", Toast.LENGTH_SHORT).show();
                                        Times_from.setText("FROM");}//end inner else
                                     }//end same day
                            }//else if
                            else {
                                Toast.makeText(parksMap.this , "NOT Reachable !!!! Past time  selected!", Toast.LENGTH_SHORT).show();
                                Times_from.setText("FROM");
                            }//end of else past time





                    }//end if not == day

                    }//end ontime set
                },cHour ,cMinute ,false);
                //showing time picker dialoug
                timePickerDialogn.show();
            }//eend onclick
        });//end button time from




        Times_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readingspotO();
                //get curent time in 12 hous format
                String timen2 = new SimpleDateFormat("hh-mm aa" , Locale.getDefault()).format(new Date());
                //intialize time picker dialog
                TimePickerDialog timePickerDialogn2 = new TimePickerDialog(parksMap.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute22) {
                        // intialize hour & minute
                        int shourn2 = hourOfDay;
                        int sminuten2 = minute22;
                        //imtalize calendar
                        Calendar calendar1n2 = Calendar.getInstance();
                        // get the date
                        if (days.getText().toString().equals("DAY") || Times_from.getText().toString().equals( "FROM" )){
                            Toast.makeText(parksMap.this , "You should fill the previod fields first!!!" , Toast.LENGTH_SHORT).show();

                        }//end if
                        else {

                            String [] noor2 = days.getText().toString().split("/");
                            //get da from date
                            int sdayn = Integer.parseInt(noor2[0]);
                            //set day onncalendar :
                            calendar1n2.set(Calendar.DAY_OF_MONTH , sdayn);
                            //set hour on calendar
                            calendar1n2.set(Calendar.HOUR_OF_DAY, shourn2);
                            //set minute on calendar:
                            calendar1n2.set(Calendar.MINUTE, sminuten2);
                            // set selected time on button :
                            Times_to.setText(DateFormat.format("hh:mm aa" , calendar1n2));
                            int ihour = Integer.parseInt(Times_from.getText().toString().substring(0, 2));
                            int i2hour = Integer.parseInt(Times_to.getText().toString().substring(0, 2));
                            int  imin = Integer.parseInt(Times_from.getText().toString().substring(3, Times_from.getText().toString().length()-3));
                            int i2min = Integer.parseInt(Times_to.getText().toString().substring(3, Times_to.getText().toString().length()-3));


                            String fromA = Times_from.getText().toString().substring(6, 8);
                            String mm2 = Times_to.getText().toString().substring(6, 8);
                            // Toast.makeText(parksMap.this, " jjjk"+ date+ " * " + calendar.getTimeInMillis() , Toast.LENGTH_LONG).show();
                            //check coondtioin
                            if (calendar1n2.getTimeInMillis() == Calendar.getInstance().getTimeInMillis()){
                                Toast.makeText(parksMap.this , "NOT Reachable !!!! Past time  selected!", Toast.LENGTH_SHORT).show();
                                Times_to.setText("TO");
                            }//end if

                            else if(calendar1n2.getTimeInMillis() > calendar1n.getTimeInMillis() && (((i2hour - ihour)>=1 && i2min >= imin)) ){

                                Toast.makeText(parksMap.this ,  "fututre time selected  ", Toast.LENGTH_LONG).show();
                            }//end else if if 1

                            else if (calendar1n2.getTimeInMillis() < calendar1n.getTimeInMillis() && ((i2hour - ihour)<1  || (i2min < imin))  ){
                                Toast.makeText(parksMap.this , "there should be at least a ONE HOUR parking paeriod!!!! ", Toast.LENGTH_LONG).show();
                                Times_to.setText("TO");

                            }// end if hours calculates

                            else if(calendar1n2.getTimeInMillis() < calendar1n.getTimeInMillis()){
                                Toast.makeText(parksMap.this , "NOT Reachable !!!! Past time  selected!", Toast.LENGTH_SHORT).show();
                                Times_to.setText("TO");
                            }//end of else past time

                        }//end else
                    }//end ontime set
                },cHour ,cMinute ,false);
                //showing time picker dialoug
                timePickerDialogn2.show();
            }//end on clickk
        });//end button time to


        CheckMyPremssion();
        Bundle mapViewBundle33 = null;
        //checking the premmisoiion to access location

        // right to diplaying the map screen
        if (savedInstanceState != null) {
            mapViewBundle33 = savedInstanceState.getBundle("MapViewBundleKey");
        }

        // Inflate the layout for this fragment
        bookmap.onCreate(mapViewBundle33);

        bookmap.getMapAsync(this);
        // Bundle mapViewBundle3 =
        //to show parks on map

        ////handling search button:}}

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Times_from.getText().toString().equals("FROM") ||
                        Times_to.getText().toString().equals("TO")){
                    Toast.makeText(getApplicationContext(), "fill the empty fields to view available parks", Toast.LENGTH_LONG).show();

                }//end iff

                else {
                    boolean accept;

                    arrayList.clear();
                    readingspotO();

                    googlebookmap.clear();

                    progressDialog = new ProgressDialog(parksMap.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Loading The Parks...");
                    progressDialog.show();
                    //   AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());


                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.imagepark);
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false);
                    DatabaseReference refrencebooking = FirebaseDatabase.getInstance().getReference("BookedPark");

                    refrencebooking.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot33) {
                            LatLng latLng44;
                            if (snapshot33.getChildrenCount() == 0) {
                                looping();
                            }//end of if databse empty
                            else {
                                for (DataSnapshot dataSnapshot : snapshot33.getChildren()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        for (int i = 0; i < arrayList.size(); i++) {
                                            System.out.println(arrayList.get(i).getSpot_id66().equals(dataSnapshot1.child("spotid_").getValue()));
                                            System.out.println(arrayList.get(i).getSpot_id66() + " " + dataSnapshot1.child("spotid_").getValue());
                                            String[] spotssid = dataSnapshot1.child("spotid_").getValue().toString().split("/");
                                            if (arrayList.get(i).getSpot_id66().equals(spotssid[0])) {
                                                if (dataSnapshot1.child("day").getValue().toString().equals(days.getText().toString())) {
                                                    arrayList.remove(i);
                                                    backup.remove(i);
                                                }
                                            }//end first if
                                        }//end of else

                                    }///end third for


                                }///end second for

                                for (int b = 0; b < arrayList.size(); b++) {
                                    // String []spotid2 = arrayList.get(b).getSpot_id66().toString().split("/");

                                    System.out.println(arrayList.get(b).getSpot_id66());
                                    /// System.out.println("noooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                                    System.out.println("this should excute 4 times ");
                                    double lat_44 = arrayList.get(b).getLat66();
                                    double long_44 = arrayList.get(b).getLong66();
                                    String pic_url44 = arrayList.get(b).getPic_urlll();
                                    latLng44 = new LatLng(lat_44, long_44);
                                    // taking the pic url
                                    /////////////////////////////////////////////////
                                    ////   System.out.println("mark num " + (i + 1));
                                    googlebookmap.addMarker(new MarkerOptions().position(latLng44).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).title("SPOT: " + (b + 1)));
                                    latLng44 = null;
                                    //break  ;


                                }//end for hs child

                                arrayList.clear();
                            }//else


                            Toast.makeText(getApplicationContext(), "Parks are ready! :)", Toast.LENGTH_LONG).show();
                            System.out.println("the endddddddddddddddd");
                            progressDialog.dismiss();
                            //progressDialog.dismiss();
                        }////on datachnge

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }/////on canceleed
                    });////add on event listener to the refrence database
                    /// readingtheparkingspot();

                }//end else all fields are filled
            }/////on click search button


        });/////set  search on click on click listener





    }///// on creatd

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {

        bookmap.onResume();
        googlebookmap = googleMap;
        CheckMyPremssion();
        //we are checking if we have the premission to display the map
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;

        }// end if

        googlebookmap.setMyLocationEnabled(true);
///here

        //end of on change
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        // end of event listener for database refrence
        /// });


        // now we will add on click listenesr for marks on map :>

        googlebookmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull @NotNull Marker marker) {


                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        parksMap.this, R.style.Theme_Design_BottomSheetDialog
                );
                View BottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.dialogprice, (LinearLayout) findViewById(R.id.bottomSheetContainer));

                RatingBar ratingBar2;
                TextView textprice, bytxt66, peopletxt;

                textprice = BottomSheetView.findViewById(R.id.textView62);
                bytxt66 = BottomSheetView.findViewById(R.id.textView90);
                peopletxt = BottomSheetView.findViewById(R.id.textView91);
                ratingBar2 = BottomSheetView.findViewById(R.id.ratingbar2);

                ratingBar2.setClickable(false);

                //textprice.setText(priceperhpurs);
                if (marker.getTitle() != null){
                    String markertitle = marker.getTitle();
                    char bb = markertitle.charAt(markertitle.length() - 1);
                    System.out.println(bb + "noooooooooooooorrrrrrr" + bb);
                    int indexx = Integer.parseInt(String.valueOf(bb));// get the last
                    System.out.println(indexx + "kkkkkk" + (indexx + 1) + "jhhjh");
                     id_spot66 = backup.get(indexx - 1).getSpot_id66();//** and the next l
                     textprice.setText(backup.get(indexx - 1).getPriceperhour().toString());
                    //getting the sum of ratings:

                    FirebaseDatabase firbase = FirebaseDatabase.getInstance();
                    DatabaseReference df = firbase.getReference("Rating").child(id_spot66);

                    df.addListenerForSingleValueEvent(new ValueEventListener() {
                        int i1=0;int i2=0;int i3=0;int i4=0;int i5=0 ; int people =0 ;
                        int result =0;
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            System.out.println("going to  the rating loop");
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                people = people+1;
                                System.out.println("inside rating loop");
                                if (dataSnapshot.child("value").getValue().toString().equals("5")){
                                    i5= i5+1;
                                    System.out.println("5555555555555555555555555555555555555555555555555555555555555555555");
                                }//end if
                                if (dataSnapshot.child("value").getValue().toString().equals("4")){
                                    i4 = i4+1;
                                }//end if
                                if (dataSnapshot.child("value").getValue().toString().equals("3")){
                                    i3 = i3+1;
                                }//end if
                                if (dataSnapshot.child("value").getValue().toString().equals("2")){
                                    i2 = i2+1;
                                }//end if
                                if (dataSnapshot.child("value").getValue().toString().equals("1")){
                                    i1 = i1+1;
                                }//end if



                            }//end for


                            if ((i1 == 0 && i2==0 && i3 == 0 && i4== 0 & i5==0)|| people ==0 ){
                                result= 0 ;
                                people =0;
                                float summ = result;
                                ratingBar2.setRating(summ);
                                ratingBar2.setSaveEnabled(true);
                                peopletxt.setText(people + " users");
                                //  Toast.makeText(getApplicationContext() , summ +"+" +people+ "ghghghghghghghghg", Toast.LENGTH_LONG).show();

                            }
                            else {
                                if (i5 >= i4 && i5 >= i3 && i5 >= i2 && i5 >= i1) {
                                    result = 5;
                                    float summ = result;
                                    ratingBar2.setRating(summ);
                                    ratingBar2.setSaveEnabled(true);
                                    peopletxt.setText(people + " users");
                                    //Toast.makeText(getApplicationContext() , summ +"+" +people+ "ghghghghghghghghg", Toast.LENGTH_LONG).show();
                                }//end if
                                else if (i4 >= i5 && i4 >= i3 && i4 >= i2 && i4 >= i1) {
                                    result = 4;
                                    float summ = result;
                                    ratingBar2.setRating(summ);
                                    ratingBar2.setSaveEnabled(true);
                                    peopletxt.setText(people + " users");
                                    //Toast.makeText(getApplicationContext() , summ +"+" +people+ "ghghghghghghghghg", Toast.LENGTH_LONG).show();
                                } else if (i3 >= i5 && i3 >= i4 && i3 >= i2 && i3 >= i1) {
                                    result = 3;
                                    float summ = result;
                                    ratingBar2.setRating(summ);
                                    ratingBar2.setSaveEnabled(true);
                                    peopletxt.setText(people + " users");
                                    // Toast.makeText(getApplicationContext() , summ +"+" +people+ "ghghghghghghghghg", Toast.LENGTH_LONG).show();
                                } else if (i2 >= i5 && i2 >= i3 && i2 >= i4 && i2 >= i1) {
                                    result = 2;
                                    float summ = result;
                                    ratingBar2.setRating(summ);
                                    ratingBar2.setSaveEnabled(true);
                                    peopletxt.setText(people + " users");
                                    //Toast.makeText(getApplicationContext() , summ +"+" +people+ "ghghghghghghghghg", Toast.LENGTH_LONG).show();
                                } else if (i1 >= i5 && i1 >= i3 && i1 >= i2 && i1 >= i4) {
                                    result = 1;
                                    float summ = result;
                                    ratingBar2.setRating(summ);
                                    ratingBar2.setSaveEnabled(true);
                                    peopletxt.setText(people + " users");
                                    // Toast.makeText(getApplicationContext() , summ +"+" +people+ "ghghghghghghghghg", Toast.LENGTH_LONG).show();
                                }

                            }//else


                        }//end of datachange

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }


                    });

                    System.out.println(result);
                    System.out.println(people +" peopleeeee");

                    /////////////////////////////////////////////////////

                    //   summ= 0 ;


                    BottomSheetView.findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(parksMap.this, "more information ", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                            String markertitle = marker.getTitle();
                            char bb = markertitle.charAt(markertitle.length() - 1);
                            System.out.println(bb + "noooooooooooooorrrrrrr" + bb);
                            int indexx = Integer.parseInt(String.valueOf(bb));// get the last
                            System.out.println(indexx + "kkkkkk" + (indexx + 1) + "jhhjh");
                            id_spot66 = backup.get(indexx - 1).getSpot_id66();
                            // textprice.setText(arrayList.get(indexx-1).getPriceperhour().toString());
                            Intent i = new Intent(parksMap.this, BookingdetailsActivity.class);
                            //we are passing the mrker title to new activity
                            i.putExtra("title", markertitle);
                            i.putExtra("idspot", id_spot66);
                            i.putExtra("day", days.getText().toString());
                            i.putExtra("Timeto", Times_to.getText().toString());
                            i.putExtra("Timefrom", Times_from.getText().toString());
                            i.putExtra("price", textprice.getText().toString());

                            startActivity(i);
                        }
                    });
                    bottomSheetDialog.setContentView(BottomSheetView);





                    bottomSheetDialog.show();



                }//end if marker :)


                return false;
            }
        });
    }// end of on map ready

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        //  getCurrentLocation();

        CheckMyPremssion();
        Bundle mapViewBundle33 = outState.getBundle("MapViewBundleKey");
        if (mapViewBundle33 == null) {
            mapViewBundle33 = new Bundle();
            outState.putBundle("MapViewBundleKey", mapViewBundle33);
        }

        bookmap.onSaveInstanceState(mapViewBundle33);

        //  usermapView.onSaveInstanceState(mapViewBundle2);
    } //on save instance method

    private void CheckMyPremssion() {
        // premmsion listener interface//Dexter is the library that will help us to make this task easy for handling runtime permissions in Android. Now we will see the implementation of this in our Android app.
        getCurrentLocation();

        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {


            // if access is accepted
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                //  isPremisionGranted = true;
                //  getCurrentLocation();
                // Toast.makeText(getActivity(), "Map ready", Toast.LENGTH_SHORT).show();

            }


            // if access  is denied
            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                // then going to the setting page for manullay providing access

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            // not accepted or denied
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();
            }
        }).check();
    }//check my premssion


    private void getCurrentLocation() {

        mFusedLocationProviderClient33 = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locat33 = mFusedLocationProviderClient33.getLastLocation();
        locat33.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Location> task) {
                if (task.isSuccessful()){

                    Location uloc33 = task.getResult();
                    if (uloc33 != null){
                        moveCamera(new LatLng(uloc33.getLatitude(), uloc33.getLongitude()),15);

                    }//inner if
                }//end if

                else{
                    Toast.makeText(getApplication(),"current location not found ",Toast.LENGTH_SHORT).show();
                    CheckMyPremssion();
                }//else

            }//onComplete


        }); // add on complete listener



    }// end of getcurrent location

    private void moveCamera(LatLng latLng, int i) {
        if (googlebookmap != null){
            googlebookmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,i));
            // googlebookmap.addMarker(CameraUpdateFactory.newLatLng(latLng));
            googlebookmap.addMarker(new MarkerOptions().position(latLng));
        }// end if

    }// end of move camera









    public void looping (){
        firebaseDatabase33 = FirebaseDatabase.getInstance();
        databaserefrence33 = firebaseDatabase33.getReference("ParkingSpot");
        // DatabaseReference zone1Ref = databaserefrence33
        databaserefrence33.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot33) {
                for (DataSnapshot dataSnapshot : snapshot33.getChildren()) {
                    double latt33, longg33;
                    String spot_id33;
                    String pic_url33;

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        priceperhpurs = dataSnapshot1.child("price").getValue().toString();
                        latt33 = (double) dataSnapshot1.child("latitude").getValue();
                        longg33 = (double) dataSnapshot1.child("longitude").getValue();
                        spot_id33 = (String) dataSnapshot1.child("spot_id").getValue();
                        status = dataSnapshot1.child("status").getValue().toString();
                        if (dataSnapshot1.hasChild("linkpic")) {
                            pic_url33 = (String) dataSnapshot1.child("linkpic").getValue();
                        }//end if
                        else {
//
                            pic_url33 = null;
                        }//end of else


                        //LatLng latLng = new LatLng(latt33 , longg33);
                        /// arrayList.add(latLng);
                        Picmark picmark1 = new Picmark(spot_id33, latt33, longg33, pic_url33, status, priceperhpurs);
                        arrayList.add(picmark1);
                        System.out.println(pic_url33);
                        System.out.println("ygygygygyygyggygygygygyygygy");


                    }// end of for

                }// end of for

                //for loop for arraylist
                // this is for adding a default pic for the non picturees one from drawble
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.imagepark);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false);


                //// we will make sure to dispaly the parks that are available for the user
                /// which is by taking the current time & day to check the booked parks are they done
                /// and bacome available again.

                // getting the current time & day :
                ////  Calendar calendar;
                ////  SimpleDateFormat simpleDateFormat2;
                ////  SimpleDateFormat simpleDateFormattime;
                ////  String Datatime;
                ////  String Dataday;
                ////   calendar = Calendar.getInstance();
                ////   simpleDateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
                ////   simpleDateFormattime = new SimpleDateFormat("HH-mm");
                ////   Datatime = simpleDateFormattime.format(calendar.getTime());
                ////    Dataday = simpleDateFormat2.format(calendar.getTime());
                ////    System.out.println(Dataday + ",,,,," + Datatime);
                ////   System.out.println("dfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfd");

                for (int b3 = 0; b3< arrayList.size(); b3++){
                    // if (dataSnapshot.hasChild(arrayList.get(b).getSpot_id66().toString())== false){
                    //   System.out.println(arrayList.get(b).getSpot_id66());
                    /// System.out.println("noooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                    System.out.println("this should excute 4 times ");
                    double lat_44 = arrayList.get(b3).getLat66();
                    double long_44 = arrayList.get(b3).getLong66();
                    String pic_url44 = arrayList.get(b3).getPic_urlll();
                    LatLng latLng44 = new LatLng(lat_44, long_44);
                    // taking the pic url
                    /////////////////////////////////////////////////
                    ////   System.out.println("mark num " + (i + 1));
                    googlebookmap.addMarker(new MarkerOptions().position(latLng44).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).title("SPOT: " + (b3 + 1)));
                    latLng44 = null;
                    //break  ;

                    //}//end if has child
                }//end for hs child

            }//end on data change looping
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }// end of looping

    public void readingspotO() {
           backup = new ArrayList<Picmark>();
        DatabaseReference re2 = FirebaseDatabase.getInstance().getReference("ParkingSpot");
        re2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        System.out.println(dataSnapshot1.child("spot_id").getValue().toString());
                        // Picmark spotd2 = dataSnapshot1.getValue(Picmark.class);
                        String idf = dataSnapshot1.child("spot_id").getValue().toString();
                        double lang = Double.parseDouble(dataSnapshot1.child("longitude").getValue().toString());
                        double latt = Double.parseDouble(dataSnapshot1.child("latitude").getValue().toString());
                        String price = dataSnapshot1.child("price").getValue().toString();
                        String stat = dataSnapshot1.child("status").getValue().toString();
                        String urll ;
                        if ((dataSnapshot1.hasChild("linkpic")) == false){
                            urll = null;
                        }
                        else{
                            urll = dataSnapshot1.child("linkpic").getValue().toString();
                        }

                        System.out.println("lalalaalalalalalallaalllaallaalalal" );
                        Picmark py = new Picmark(idf , latt , lang , urll , stat  , price);
                        arrayList.add(py);
                        backup.add(py);

                    }//end inner for



                }// end outer for

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }// end of reading

}//end of classs
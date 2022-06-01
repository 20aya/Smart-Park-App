package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andriod.smartparking1.Classes.BookedPark;
import com.andriod.smartparking1.databinding.ActivityBookingdetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import papaya.in.sendmail.SendMail;

public class BookingdetailsActivity extends AppCompatActivity {
TextView theader;
    String nm ;
    String ph ;

    String phonenumber  , nameowner ;
TextView textpriceh , texttimeh , textlocationh , completebook ;
ImageView park_pic , out_inpic ;
TextView textpricen , textlocationn , texttimefor, textimefrom ,texttypein_out , textrs;
TextView bookingbutton;
Dialog dialog21 ;
    String id_user_norm;
    String id_owner7;
private Spinner spinner;
ArrayList<String> vehicals ;
String realspotid;
    FirebaseAuth fAuth44 = FirebaseAuth.getInstance();
    FirebaseDatabase rootNode44;
    DatabaseReference refrence44;
    ArrayAdapter<String> arrayAdapter ;
    String spot_id_choosed;
// for database firebase
    FirebaseDatabase firebaseDatabase66;
    DatabaseReference databaserefrence66;
    TextView tday;
    String price_perhour;
    private AlarmManager alarmManager;
    PendingIntent pendingIntent;
    ActivityBookingdetailsBinding  activitybind;
    String resultt;
   // ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        activitybind= ActivityBookingdetailsBinding.inflate(getLayoutInflater());
        setContentView(activitybind.getRoot());
        CreateNotifactionChannel();
        

        // itailazing the compoments of the activity

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("notif_openpark" , "notif_openpark" , NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manger = getSystemService(NotificationManager.class);
            manger.createNotificationChannel(notificationChannel);


        }//checking the system for notifaction
        theader=findViewById(R.id.textView27);

        tday = findViewById(R.id.textView66);
        textpriceh = findViewById(R.id.textViewd2);
        texttimeh = findViewById(R.id.textViewd1);
        textlocationh = findViewById(R.id.textViewdvv);
        textpricen = findViewById(R.id.editTextNumberDecimaldd2);
        textlocationn = findViewById(R.id.textViewdv3);
        texttimefor = findViewById(R.id.editTextTimed3);
        textimefrom = findViewById(R.id.editTextTimed2);
        texttypein_out = findViewById(R.id.textView42);
        park_pic = findViewById(R.id.profile_image);
        out_inpic = findViewById(R.id.imageView1d);
        bookingbutton = findViewById(R.id.textViewar);
        textrs = findViewById(R.id.textView47);



        // we will get data from the map axctivity
        String title = getIntent().getStringExtra("title");
         spot_id_choosed = getIntent().getStringExtra("idspot");
        theader.setText(title);
        tday.setText(getIntent().getStringExtra("day"));
        textimefrom.setText(getIntent().getStringExtra("Timefrom"));
        texttimefor.setText(getIntent().getStringExtra("Timeto"));
        price_perhour = getIntent().getStringExtra("price"); // reciving th eprice from the previous pag
                                                                 // which was parks map,

                // reading from the firebase
        firebaseDatabase66 = FirebaseDatabase.getInstance();
        databaserefrence66 = firebaseDatabase66.getReference("ParkingSpot");
        // DatabaseReference zone1Ref = databaserefrence33
        databaserefrence66.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot33) {
                for (DataSnapshot dataSnapshot : snapshot33.getChildren()) {
                    double latt33, longg33;
                    //   String spot_id33;
                    String pic_url33 = null;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (spot_id_choosed.equals(dataSnapshot1.child("spot_id").getValue().toString())) {
                            //textpricen.setText(dataSnapshot1.child("price").getValue().toString());
                          //  texttimefor.setText(dataSnapshot1.child("timeTo").getValue().toString());
                          //  textimefrom.setText(dataSnapshot1.child("timefrom").getValue().toString());
                            calcpriceperhour();
                            textlocationn.setText(dataSnapshot1.child("longitude").getValue() + " , " + dataSnapshot1.child("latitude").getValue());

                             id_owner7 = dataSnapshot1.child("owner_id").getValue().toString();
                             realspotid = dataSnapshot1.child("spot_id").getValue().toString();
                            if (dataSnapshot1.hasChild("linkpic")) {
                                pic_url33 = (String) dataSnapshot1.child("linkpic").getValue();
                                Picasso.get().load(pic_url33).into(park_pic);
                            }//end if inner
                             if (pic_url33 == null) {
                                park_pic.setImageResource(R.drawable.imagepark);
                            }// end of  if else
                            if (dataSnapshot1.child("spot_type").getValue().toString().equals("Outdoor")){
                                texttypein_out.setText( dataSnapshot1.child("spot_type").getValue().toString());
                                out_inpic.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
                            }// end of if indoor

                            else if (dataSnapshot1.child("spot_type").getValue().toString().equals("Indoor")){
                                texttypein_out.setText( dataSnapshot1.child("spot_type").getValue().toString());
                                out_inpic.setImageResource(R.drawable.ic_baseline_account_balance_24);
                            }//end of else outdoor

                        } // end outer if
                    }//end of else


                }// end of for

            }// end of for



            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }

        });// end of event lisenr
progressDialog.dismiss();
bookingbutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        // for the dialog
        dialog21= new Dialog(BookingdetailsActivity.this);
        dialog21.setContentView(R.layout.bookspot_dailog);

        dialog21.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog21.setCancelable(true);
        dialog21.show();
        // now for the spinner in the dialog
        spinner =dialog21.findViewById(R.id.spinner);
        completebook= dialog21.findViewById(R.id.textView24);
        vehicals= new ArrayList<String>();

        
        reading_vehicles();
        completebook.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
            //    String spinner_data = spinner.getSelectedItem().toString();
                // saving abooke park & changing the stats of the park to booked:
                      if (spinner.getAdapter().getCount()==0) {


                          // set error message on spinner\
                         Toast.makeText(getApplicationContext(), " please  add your plate number in the vehicle page, to complete booking!!  ", Toast.LENGTH_LONG).show();


                          dialog21.dismiss();
                      }
                      else {
                          String spinner_data = spinner.getSelectedItem().toString();
                      rootNode44 = FirebaseDatabase.getInstance();
                      refrence44 = rootNode44.getReference("BookedPark");
                      String unid = refrence44.push().getKey(); // id to booiked spot
                      BookedPark p2 = new BookedPark(id_user_norm, id_owner7, realspotid + "/" + unid, textimefrom.getText().toString(), texttimefor.getText().toString()
                              , tday.getText().toString(), textpricen.getText().toString(), "On Hold" ,
                              spinner_data);
                      refrence44.child(id_user_norm).child(unid).setValue(p2);
                      //changing the status of the park t o booked :)
                      String booked = "booked";
                      DatabaseReference refrence123 = FirebaseDatabase.getInstance().getReference("ParkingSpot").child(id_owner7)
                              .child(spot_id_choosed);
                      Map<String, Object> Map = new HashMap<>();
                      Map.put("status", booked);
                      refrence123.updateChildren(Map);
                      gettokenss();
                      readingownerinfoforemail();


                      dialog21.dismiss();
                      //String []info = new String [2];
                      Toast.makeText(getApplicationContext(), " you completed booking!!! ", Toast.LENGTH_LONG).show();
                      Intent n2e = new Intent(BookingdetailsActivity.this, MainPageNormal_user.class);
                      n2e.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      getApplicationContext().startActivity(n2e);
                      finish();

                      // fter that we have to notifacte the owner & removeit from the map
                      try {
                          setAlarm();
                      } catch (ParseException e) {
                          e.printStackTrace();
                      }


//Toast.makeText(getApplicationContext(), id_owner7+ "  mnmnmnmnmnmnmn" , Toast.LENGTH_SHORT).show();


                  }//end else not null


            }//on click  complete boooking button





        });// setting on click listener complet booking


    }
});


        }// end of oncreate



    public void reading_vehicles() {
        id_user_norm = fAuth44.getUid();
        rootNode44 = FirebaseDatabase.getInstance();
        refrence44 = rootNode44.getReference("Plates");
        DatabaseReference zone1Ref = refrence44.child(id_user_norm);
        zone1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot22) {
                // plate_vehicle p3;
               // vehicals=new ArrayList<String>();

                for (DataSnapshot dataSnapshot: snapshot22.getChildren()) {

                    //    plate_vehicle  p3 = dataSnapshot.getValue(plate_vehicle.class);
                    String platamv = dataSnapshot.child("platenum").getValue().toString();
                    vehicals.add(platamv);



                }//end of for

                arrayAdapter=new ArrayAdapter<>(BookingdetailsActivity.this , android.R.layout.simple_spinner_dropdown_item,vehicals);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);


                // for to not repeting plates :)
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }




    private void calcpriceperhour() {
        double hoursss= 0;
         String hourfromwithm = textimefrom.getText().toString();
        String hourfrom = hourfromwithm.substring(0 , 2);
        double hourfromdouble = Double.parseDouble(String.valueOf(hourfrom));// get the last
        System.out.println(hourfromdouble);
        String hourtowithm = texttimefor.getText().toString();
        String hourtostring2 = hourtowithm.substring(0 , 2);
        String mmfrom = hourfromwithm.substring(hourfromwithm.length()-2,hourfromwithm.length()-1);
        String mmtoo = hourtowithm.substring(hourtowithm.length()-2 , hourtowithm.length()-1 );
        System.out.println("ljkjkjkjkjkjkjkjkjkjkjk" +  (hourfromwithm.length()-2 )+"="+ (hourfromwithm.length()-1 ));
        double hourtodouble  = Double.parseDouble(String.valueOf(hourtostring2));
       char  charhourfromfirsst = hourfrom.charAt(0);
       char charhourfromsecond = hourfrom.charAt(1);
       char charhour2first = hourtostring2.charAt(0);
       char charhour2second = hourtostring2.charAt(1);

        // this condition for the case of  am & pm & samehours
       System.out.println("popopopopo" + mmfrom  + mmtoo + hourtodouble + hourfromdouble);
        if (mmfrom.equals("A") && mmtoo.equals("P")  && hourtodouble == hourfromdouble ){
            hoursss = 12 ; }//end 1 if


        // this condition for the case of  pm & pm & to > from
        else if (hourtodouble > hourfromdouble && mmfrom.equals(mmtoo)){
            hoursss = hourtodouble - hourfromdouble; }// end if 2

        else if (hourtodouble > hourfromdouble && mmfrom != mmtoo ){
               if (mmfrom.equals("A") && mmtoo.equals("P") &&hourtodouble > hourfromdouble ){
                   hoursss =  (hourfromdouble - hourtodouble) * -1 ; }//endif
        }// end if 3
        else if (hourfromdouble > hourtodouble  && mmfrom != mmtoo ){
           hoursss =   12 -  (  hourfromdouble- hourtodouble);
       }// end if 4
        double priceee = Double.parseDouble(String.valueOf(price_perhour));
                textpricen.setText(hoursss* priceee +" " );
                hoursss = 0; }// end of clack price per hour :)

    private void CreateNotifactionChannel() {
        // check the andriod running
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "foxandroidReminderChannel";
            String description = "Channel for alarm manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("foxandroid", name , importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }///end if



    }// end of method channel



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAlarm() throws ParseException {
        String []timesss = tday.getText().toString().split("/");
           // String year = (String) tday.getText().toString().substring(5 , tday.getText().toString().length());
        String year = timesss[2];
            int yearint = Integer.parseInt(year);
            //String month = tday.getText().toString().substring(3,4);
              String month = timesss[1];
            int monthint = Integer.parseInt(month);
           // String day = tday.getText().toString().substring(0 ,2 );
        String day = timesss[0];
            int dayint =  Integer.parseInt(day);
            String hous = textimefrom.getText().toString().substring(0, 2);
            int housint =Integer.parseInt(hous);
            String min = textimefrom.getText().toString().substring(3, 5);
            int minint = Integer.parseInt(min);
            System.out.println(year +" "+ month +" "+ day +"now to int" + hous +"m."+ min);
       // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
          Intent i =new Intent(this , AlarmReciver.class);
          pendingIntent = PendingIntent.getBroadcast(this , 0 , i , 0);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, yearint);
            calendar.set(Calendar.MONTH , monthint);
            calendar.set(Calendar.DAY_OF_MONTH ,  dayint );
            calendar.set(Calendar.HOUR_OF_DAY,  housint );
           calendar.set(Calendar.MINUTE ,  minint);
           System.out.println(housint +minint+"time:::");
          // System.out.println(calendar.getTime());
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP  ,calendar.getTimeInMillis()
                ,AlarmManager.INTERVAL_DAY , pendingIntent);
        Toast.makeText(this , "alarm seucccessfuly got set ", Toast.LENGTH_SHORT).show();
        Timer timer = new Timer();
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        hous = housint +12 +"";
        String no = year+"-"+month+"-" +day+" " +hous+":"+min;
       Date date = dateFormat.parse(no);
       Date date2 = dateFormat.parse("2022-02-17 18:16");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                 System.out.println("happy new yearrrrr" );
                 System.out.println("love love love please workkkkkkkkk");
                 //notifacting user when park is open :)
                NotificationCompat.Builder builder= new NotificationCompat.Builder(BookingdetailsActivity.this , "notif_openpark");
                builder.setContentTitle("Spot is open!");
                builder.setContentText("The time will be " +
                        "counted from Now!");
                builder.setSmallIcon(R.drawable.ic_baseline_local_car_wash_24);
                builder.setAutoCancel(true);
                NotificationManagerCompat managercompat = NotificationManagerCompat.from(BookingdetailsActivity.this);
                managercompat.notify(1 , builder.build());
                 timer.cancel();
            }
        };//timer task
       timer.schedule(task , date);

        // }

    }// end of set alarm method



























/*
    public String gettokenss(String id ){


        DatabaseReference reftokes = FirebaseDatabase.getInstance().getReference("Tokens");
        reftokes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (id.equals(dataSnapshot.getKey())){
                        resultt = dataSnapshot.child("token").getValue().toString();
                    }


                }//end for
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return  resultt;
    }//end getting the token
*/


    public void readingownerinfoforemail() {

        DatabaseReference downers = FirebaseDatabase.getInstance().getReference("Users").child(id_owner7);
        downers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //*Toast.makeText(getApplicationContext(), snapshot.child("phonenum").getValue().toString() + "nmnmnmnmn", Toast.LENGTH_SHORT).show();
               ph = snapshot.child("phonenum").getValue().toString();
               //* Toast.makeText(getApplicationContext(), phonenumber + "nmnmnmnmn", Toast.LENGTH_SHORT).show();
                nm = snapshot.child("namme").getValue().toString();

               SendMail mail = new SendMail("smartparking.appteam@gmail.com", "noor123123",
                       fAuth44.getCurrentUser().getEmail(),
                       "You have booked a new Spot! ",
                       " A new spot is booked from:"+ textimefrom.getText().toString()+" , To: "+ texttimefor.getText().toString()
                               + " . On date: " + tday.getText().toString()+ " with the full price: " +textpricen.getText().toString() +"R.S " +"."+
                               "owner of spot: " + snapshot.child("namme").getValue().toString()+" , " + " Owner Contacts: " +  snapshot.child("phonenum").getValue().toString() +" .");
                           mail.execute();


          // info[0] = snapshot.child("phonenum").getValue().toString();
        // info[1]= snapshot.child("namme").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    //    SendMail mail = new SendMail("smartparking.appteam@gmail.com", "noor123123",
      //          fAuth44.getCurrentUser().getEmail(),
        //        "You have booked a new Spot! ",
              //  " A new spot is booked from:"+ textimefrom.getText().toString()+" , To: "+ texttimefor.getText().toString()
                    //    + " . On date: " + tday.getText().toString()+ " with the full price: " +textpricen.getText().toString() +"R.S " +"."+
                    //    "owner of spot: " + nm+" , " + " Owner Contacts: " +  ph +" .");
       // mail.execute();


    }


    public void gettokenss(){
       // String resulttt = "";

        DatabaseReference reftokes = FirebaseDatabase.getInstance().getReference("Tokens");
        reftokes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (id_owner7.equals(dataSnapshot.getKey())){
                        String resulttt = dataSnapshot.child("token").getValue().toString();
                        String token222 = resulttt;
                        System.out.println("Tokkkkkkenssssssssssssssssssssssss" + token222);
                       //* Toast.makeText(getApplicationContext(), "tokens:" + token222, Toast.LENGTH_SHORT).show();
                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                                token222, "New Booker!", "Someone Booked Your Spot, click & chek it",
                                getApplicationContext(), BookingdetailsActivity.this);

                        notificationsSender.SendNotifications();

                    }


                }//end for
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
       //new soluationnnnnn


    }//end getting the token
}// end of class
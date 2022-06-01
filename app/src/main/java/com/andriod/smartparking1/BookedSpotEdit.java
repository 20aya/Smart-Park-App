package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookedSpotEdit extends AppCompatActivity {
    String price, location , from , to , day , owned , phone , ownerid , spotid, userid;
    TextView header1 , header2 , Timetxt , daytxt  , important,fromheader ,toheader ,bcancel , bupdate;
    Button   buttonday , buttonfrom , buttonto ;
    SimpleDateFormat sdf;
    ProgressDialog progressDialog;
    ImageView driveimage;
    int syear,sday,smonth;
    int hournow;
    int minnow;
    String  price_perhour = " ";
   String comingprice ;
    DatePickerDialog.OnDateSetListener setListener;
    Calendar calendar1n;
    boolean result;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookedspot_edit);

////getting the values forwarded from the other class
       comingprice = getIntent().getStringExtra("priceperhour");
        price = getIntent().getStringExtra("fullprice");
        location = getIntent().getStringExtra("location");
        userid = getIntent().getStringExtra("userid");
        ownerid = getIntent().getStringExtra("ownerid");
        spotid = getIntent().getStringExtra("spotid");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        day = getIntent().getStringExtra("day");
        phone = getIntent().getStringExtra("phone");
        owned = getIntent().getStringExtra("owned");
//////////////////////////////////////////////////////////////

        //to get the ccurrent date
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        LocalDate localeDate = LocalDate.now();
        int noeday =localeDate.getDayOfMonth();
        /// intalizing viewss////////////////////
        bcancel = findViewById(R.id.textView77u);
        bupdate = findViewById(R.id.button32);
        important = findViewById(R.id.textView50);
        header1 = findViewById(R.id.textView10rrrr3);
        header2 = findViewById(R.id.textView104);
        Timetxt = findViewById(R.id.textView564r);
        daytxt = findViewById(R.id.textView22o);
       fromheader = findViewById(R.id.textView10);
       toheader= findViewById(R.id.textView51);
        buttonfrom = (Button)findViewById(R.id.button19);
        buttonto = (Button)findViewById(R.id.button20);
        buttonday = (Button)findViewById(R.id.button18);

        driveimage = findViewById(R.id.imageView22ovv);


        //setting the previous values for the day & time:
        //Toast.makeText(getApplicationContext(), day+" " + to , Toast.LENGTH_LONG).show();
       // System.out.println(day+" " + to+"dfghjkl;';lkjhgfdsdfghjkl;lkjhgfdsdfghjkl;';lkjhgfdsdfghjkl;lkhgfdsdfghjkl");
        buttonday.setText(day);
        buttonfrom.setText(from);
        buttonto.setText(to);


        ////getting the days hours valuess
        Calendar calendar = Calendar.getInstance();
        int cyear = calendar.get(Calendar.YEAR);
        int cmonth = calendar.get(Calendar.MONTH);
        int cday = calendar.get(Calendar.DAY_OF_MONTH);
        int cHour =calendar.get(Calendar.HOUR_OF_DAY);
        int cMinute = calendar.get(Calendar.MINUTE);
        buttonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            BookedSpotEdit.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            syear = year;
                            smonth= month;
                            sday=dayOfMonth;
                            String sdate = sday+"/"+(smonth+1)+"/"+syear;
                            buttonday.setText(sdate);



                        }
                    },cyear,cmonth,cday
                    );
                    datePickerDialog.updateDate(syear,smonth,sday);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                    datePickerDialog.show();

            }//end on click day
        });//end handling day button



        ///starting to handle from button (start)

        buttonfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 buttonto.setText("END");
                //get curent time in 12 hous format
                String timen = new SimpleDateFormat("hh-mm aa" , Locale.getDefault()).format(new Date());
                //intialize time picker dialog
                TimePickerDialog timePickerDialogn = new TimePickerDialog(BookedSpotEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute2) {
                        // intialize hour & minute
                        int shourn = hourOfDay;
                        int sminuten = minute2;
                        hournow = hourOfDay;
                        minnow = minute2;
                        //imtalize calendar
                        calendar1n = Calendar.getInstance();
                        // get the date
                        if (buttonday.getText().toString().equals("DAY")){
                            Toast.makeText(BookedSpotEdit.this , "You should pick a day first!!!" , Toast.LENGTH_SHORT).show();

                        }//end if
                        else if(buttonday.getText().toString()!= "DAY"){
                            String [] noor = buttonday.getText().toString().split("/");
                            //get da from date
                            int sdayn = Integer.parseInt(noor[0]);
                            smonth = Integer.parseInt(noor[1]);
                            //set day onncalendar :
                            calendar1n.set(Calendar.DAY_OF_MONTH , sdayn);
                            //set hour on calendar
                            calendar1n.set(Calendar.HOUR_OF_DAY, shourn);
                            //set minute on calendar:
                            calendar1n.set(Calendar.MINUTE, sminuten);
                            //check coondtioin
                            if (calendar1n.getTimeInMillis() == Calendar.getInstance().getTimeInMillis()){
                                Toast.makeText(BookedSpotEdit.this , "current time  selected!", Toast.LENGTH_SHORT).show();
                                buttonfrom.setText( DateFormat.format("hh:mm aa" , calendar1n));
                            }//end if
                            //  else if(calendar1n.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()){
                            //      Toast.makeText(parksMap.this , "Future time  selected!", Toast.LENGTH_SHORT).show();

                            // }//end else if if 1

                            else if(smonth+1> cmonth+1){
                                Toast.makeText(BookedSpotEdit.this ,  "Future time  selected!", Toast.LENGTH_SHORT).show();
                                buttonfrom.setText( DateFormat.format("hh:mm aa" , calendar1n));}

                            else if (smonth+1 == cmonth+1){

                                if(sday> noeday){

                                    Toast.makeText(BookedSpotEdit.this , "Future time  selected!", Toast.LENGTH_SHORT).show();
                                    buttonfrom.setText( DateFormat.format("hh:mm aa" , calendar1n));
                                }//end inner if

                                else if (sday == noeday){
                                    Calendar datetime = Calendar.getInstance();
                                    Calendar c = Calendar.getInstance();
                                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    datetime.set(Calendar.MINUTE, minute2);
                                    if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                                        //it's after current
                                        int hour = hourOfDay % 12;
                                        // Times_from.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                        //     minute2, hourOfDay < 12 ? "am" : "pm"));
                                        Toast.makeText(getApplicationContext(), "Time selected", Toast.LENGTH_LONG).show();
                                        buttonfrom.setText( DateFormat.format("hh:mm aa" , calendar1n));
                                    }
                                    else {
                                        Toast.makeText(BookedSpotEdit.this , "NOT Reachable !!!! Past time  selected!", Toast.LENGTH_SHORT).show();
                                        buttonfrom.setText("START");}//end inner else
                                }//end same day

                            }//else if
                            else {
                                Toast.makeText(BookedSpotEdit.this , "NOT Reachable !!!! Past time  selected!", Toast.LENGTH_SHORT).show();
                                buttonfrom.setText("START");
                            }//end of else past time





                        }//end else
                    }//end ontime set
                },cHour ,cMinute ,false);
                //showing time picker dialoug
                timePickerDialogn.show();

            }//end on click from
        });//end handling from button



        ////handling to button (END)

        buttonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get curent time in 12 hous format
                String timen2 = new SimpleDateFormat("hh-mm aa" , Locale.getDefault()).format(new Date());
                //intialize time picker dialog
                TimePickerDialog timePickerDialogn2 = new TimePickerDialog(BookedSpotEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute22) {
                        // intialize hour & minute
                        int shourn2 = hourOfDay;
                        int sminuten2 = minute22;
                        //imtalize calendar
                        Calendar calendar1n2 = Calendar.getInstance();
                        // get the date
                        if (buttonday.getText().toString().equals("DAY") || buttonfrom.getText().toString().equals( "START" )){
                            Toast.makeText(BookedSpotEdit.this , "You should fill the previod fields first!!!" , Toast.LENGTH_SHORT).show();

                        }//end if
                        else {

                            String [] noor2 =buttonday.getText().toString().split("/");
                            //get da from date
                            int sdayn = Integer.parseInt(noor2[0]);
                            //set day onncalendar :
                            calendar1n2.set(Calendar.DAY_OF_MONTH , sdayn);
                            //set hour on calendar
                            calendar1n2.set(Calendar.HOUR_OF_DAY, shourn2);
                            //set minute on calendar:
                            calendar1n2.set(Calendar.MINUTE, sminuten2);
                            // set selected time on button :
                            buttonto.setText(DateFormat.format("hh:mm aa" , calendar1n2));
                            int ihour = Integer.parseInt(buttonfrom.getText().toString().substring(0, 2));
                            int i2hour = Integer.parseInt(buttonto.getText().toString().substring(0, 2));
                            int  imin = Integer.parseInt(buttonfrom.getText().toString().substring(3, buttonfrom.getText().toString().length()-3));
                            int i2min = Integer.parseInt(buttonto.getText().toString().substring(3, buttonto.getText().toString().length()-3));


                            String fromA = buttonfrom.getText().toString().substring(6, 8);
                            String mm2 = buttonto.getText().toString().substring(6, 8);
                            // Toast.makeText(parksMap.this, " jjjk"+ date+ " * " + calendar.getTimeInMillis() , Toast.LENGTH_LONG).show();
                            //check coondtioin
                            if (calendar1n2.getTimeInMillis() == Calendar.getInstance().getTimeInMillis()){
                                Toast.makeText(BookedSpotEdit.this , "NOT Reachable !!!! Past time  selected!", Toast.LENGTH_SHORT).show();
                                buttonto.setText("END");
                            }//end if

                            else if(calendar1n2.getTimeInMillis() > calendar1n.getTimeInMillis() && (((i2hour - ihour)>=1 && i2min >= imin)) ){

                                Toast.makeText(BookedSpotEdit.this ,  "fututre time selected  ", Toast.LENGTH_LONG).show();
                            }//end else if if 1

                            else if (calendar1n2.getTimeInMillis() < calendar1n.getTimeInMillis() && ((i2hour - ihour)<1  || (i2min < imin))  ){
                                Toast.makeText(BookedSpotEdit.this , "there should be at least a ONE HOUR parking paeriod!!!! ", Toast.LENGTH_LONG).show();
                                buttonto.setText("END");

                            }// end if hours calculates

                            else if(calendar1n2.getTimeInMillis() < calendar1n.getTimeInMillis()){
                                Toast.makeText(BookedSpotEdit.this , "NOT Reachable !!!! Past time  selected!", Toast.LENGTH_SHORT).show();
                                buttonto.setText("END");
                            }//end of else past time
                        }//end else
                    }//end ontime set
                },cHour ,cMinute ,false);
                //showing time picker dialoug
                timePickerDialogn2.show();
            }//end to on click
        });//end handling to button



        ///handling the cancel button::::

        bcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (getApplicationContext(), MainPageNormal_user.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            }//end onclick
        });//end handling cancel button


        ///handling the update button .. first we need to check if the day
        //they choosed id=s available or booked :

        bupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first creating database refrence

                if (buttonfrom.getText().toString().equals("START")|| buttonto.getText().toString().equals("END")||
                        buttonfrom.getText().toString().equals("DAY")){
                    Toast.makeText(getApplicationContext(), "You need to fill the full values", Toast.LENGTH_LONG).show();

                }//end if checking everything is filled

                else {
                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("BookedPark");
                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String databaseid = dataSnapshot.getKey();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    //cheking if the user havent changed anything
                                    if (dataSnapshot1.child("spotid_").getValue().toString().equals(spotid) && databaseid.equals(userid)
                                            && dataSnapshot1.child("day").getValue().toString().equals(buttonday.getText())
                                            && dataSnapshot1.child("time_from").getValue().toString().equals(buttonfrom.getText()) &&
                                            dataSnapshot1.child("time_to").getValue().toString().equals(buttonto.getText())
                                            && dataSnapshot1.child("full_price").getValue().toString().equals(price)) {
                                        progressDialog = new ProgressDialog(BookedSpotEdit.this);
                                        progressDialog.setCancelable(false);
                                        progressDialog.setMessage("Loading...");
                                        progressDialog.show();
                                        Toast.makeText(getApplicationContext(), "No changes made", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(getApplicationContext(), MainPageNormal_user.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getApplicationContext().startActivity(intent);

                                        break;

                                    }//end if

                                    ///cheking if th user havent changed the day but changed the time then we willhave to call the method price
                                    if (dataSnapshot1.child("spotid_").getValue().toString().equals(spotid) && databaseid.equals(userid)
                                            && dataSnapshot1.child("day").getValue().toString().equals(buttonday.getText().toString())
                                            && (dataSnapshot1.child("time_from").getValue().toString().equals(buttonfrom.getText().toString()) != true ||
                                            dataSnapshot1.child("time_to").getValue().toString().equals(buttonto.getText().toString()) != true) &&
                                            dataSnapshot1.child("full_price").getValue().toString().equals(price)) {
                                        progressDialog = new ProgressDialog(BookedSpotEdit.this);
                                        progressDialog.setCancelable(false);
                                        progressDialog.setMessage("Updating...");
                                        progressDialog.show();
                                        //method to calculate new price & update  the new tiimes & price in database
                                        double newpricefull = calcandsave(buttonfrom.getText().toString(), buttonto.getText().toString());
                                        dataSnapshot1.child("full_price").getRef().setValue(newpricefull);
                                        dataSnapshot1.child("time_from").getRef().setValue(buttonfrom.getText().toString());
                                        dataSnapshot1.child("time_to").getRef().setValue(buttonto.getText().toString());
                                        progressDialog.dismiss();
                                        // Toast.makeText(getApplicationContext(), "Time has changed! ", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), MainPageNormal_user.class);
                                        getApplicationContext().startActivity(intent);
                                        break;
                                    }//end if 2

                                    ////different day with different pricesss because different times :)
                                    if (dataSnapshot1.child("spotid_").getValue().toString().equals(spotid) && databaseid.equals(userid)
                                            && (dataSnapshot1.child("day").getValue().toString().equals(buttonday.getText().toString()) != true)
                                            && dataSnapshot1.child("full_price").getValue().toString().equals(price)) {
                                        //first checking if the day is booked :
                                        boolean stat = checkbookedday(ref1);

                                        if (stat == true) {

                                            progressDialog = new ProgressDialog(BookedSpotEdit.this);
                                            progressDialog.setCancelable(false);
                                            progressDialog.setMessage("Updating...");
                                            progressDialog.show();
                                            //method to calculate new price & update  the new tiimes & price in database
                                            double newpricefull = calcandsave(buttonfrom.getText().toString(), buttonto.getText().toString());
                                            dataSnapshot1.child("full_price").getRef().setValue(newpricefull);
                                            dataSnapshot1.child("time_from").getRef().setValue(buttonfrom.getText().toString());
                                            dataSnapshot1.child("time_to").getRef().setValue(buttonto.getText().toString());
                                            dataSnapshot1.child("day").getRef().setValue(buttonday.getText().toString());
                                            progressDialog.dismiss();
                                            //   Toast.makeText(getApplicationContext(), "Time & day  have changed! ", Toast.LENGTH_SHORT).show();
                                            break;
                                        }//end if available

                                        else if (result == false) {
                                            Toast.makeText(getApplicationContext(), "OOps! someone already booked At the same time or day", Toast.LENGTH_LONG).show();
                                            buttonday.setText("DAY");
                                            buttonfrom.setText("START");
                                            buttonto.setText("END");
                                            break;


                                        }//else day booked


                                    }//end 3 if


                                }//endd for loop


                            }//end outer if


                        }//end on datachane


                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }//end oncancel
                    });//end refsnapshot

                }//else everything is filled
            }//end onclick
        });//end handling update button



    }//end oncreate


    //method to calculate price & save new values
    public double calcandsave(String  time_from, String time_to) {
        //first before alculatine we need to find the price per hour in databse

price_perhour = comingprice;



        double hoursss= 0;
        String hourfromwithm = time_from;

        String hourfrom = hourfromwithm.substring(0 , 2);
        //hourfrom = hourfrom.replace(":" , ".");
        System.out.println("hhhhooouurrrssssssssssssssssssssssssssssssssss   " +ownerid+" "+price_perhour);
        System.out.println(hourfrom);
        //String hourfrom = textimefrom.getText().charAt(textimefrom.getText().toString().length() - 1);
        double hourfromdouble = Double.parseDouble(String.valueOf(hourfrom));// get the last
        System.out.println(hourfromdouble);

        String hourtowithm = time_to;
        String hourtostring2 = hourtowithm.substring(0 , 2);

        String mmfrom = hourfromwithm.substring(hourfromwithm.length()-2,hourfromwithm.length()-1);
        String mmtoo = hourtowithm.substring(hourtowithm.length()-2 , hourtowithm.length()-1 );
        //  String mmfrom = "nn";
        // String mmtoo = "mm";
        System.out.println("ljkjkjkjkjkjkjkjkjkjkjk" +  (hourfromwithm.length()-2 )+"="+ (hourfromwithm.length()-1 ));
        double hourtodouble  = Double.parseDouble(String.valueOf(hourtostring2));
        char  charhourfromfirsst = hourfrom.charAt(0);
        char charhourfromsecond = hourfrom.charAt(1);
        char charhour2first = hourtostring2.charAt(0);
        char charhour2second = hourtostring2.charAt(1);
        if (charhourfromfirsst == charhour2first & charhourfromsecond == charhour2second  ){
            hoursss = 12 ;
        }//end 1 if

        else if (hourtodouble > hourfromdouble && mmfrom.equals(mmtoo)){
            hoursss = hourtodouble - hourfromdouble;
            //hoursss = 10;
        }// end if 2

        else if (hourtodouble > hourfromdouble && mmfrom != mmtoo ){

            hoursss = (  12 ) + (hourtodouble - hourfromdouble);

        }// end if 3
        else if (hourfromdouble > hourtodouble  && mmfrom != mmtoo ){
            hoursss =   12 -  (  hourfromdouble- hourtodouble);
            //  hoursss =  hourfromdouble - hourtodouble;
        }// end if 4
        double priceee = Double.parseDouble(String.valueOf(price_perhour));

        return hoursss* priceee ;






    }//end method calccc

    public boolean checkbookedday(DatabaseReference ref1){

          int numofchildren = 0 ;
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String []ids = spotid.split("/");
                    String ownerspot =  ids[0];
                    //String []dataspot = dataSnapshot.child("spot_id").getValue().toString().split("/");
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        String []dataspot = dataSnapshot1.child("spotid_").getValue().toString().split("/");
                                 if (dataspot[0].equals(ids[0])){
                                     if (dataSnapshot1.child("day").getValue().toString().equals(buttonday.getText().toString())){
                                         result = false ;
                                        // Toast.makeText(getApplicationContext(), "OOps! someone already booked At the same time or day",Toast.LENGTH_LONG).show();
                                       //  buttonday.setText("DAY");
                                       //  buttonfrom.setText("START");
                                       //  buttonto.setText("END");
                                       //  break;
                                     }//end if same day
                                     else{result = true;}

                                 }//if owner spot (same spot )

                    }//endinner for

                }//end outer for

                if (result == true){




                }//end day available


            }//end on datachange

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }//end cancel
        });//end ref



    return  result;
    }//end checkking day











}//end class
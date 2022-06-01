package com.andriod.smartparking1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.andriod.smartparking1.Classes.BookedPark;
import com.andriod.smartparking1.Delete_bookedspot;
import com.andriod.smartparking1.R;
import com.andriod.smartparking1.BookedSpotEdit;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class booked_Adapter extends RecyclerView.Adapter<booked_Adapter.viewholding> {

    HashMap <String , String> pricehour = new HashMap<>();
String priceperhour;
    Context context3 ;
    ArrayList<BookedPark> booked_list ;
    DatabaseReference refowners;
    Calendar calendar = Calendar.getInstance();
    int cyear = calendar.get(Calendar.YEAR);
    int cmonth = calendar.get(Calendar.MONTH);
    int cday = calendar.get(Calendar.DAY_OF_MONTH);
    int cHour =calendar.get(Calendar.HOUR);
    int cMinute = calendar.get(Calendar.MINUTE);
    //String spot_iddd;
    String price , location , timeto , timefrom  , picurl ;
    boolean stre = false;

    public booked_Adapter(Context context3, ArrayList<BookedPark> booked_list) {
        this.booked_list = booked_list;
        this.context3 = context3;
    }

    @NonNull
    @NotNull
    @Override
    public booked_Adapter.viewholding onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       View v4 = LayoutInflater.from(context3).inflate(R.layout.item21 , parent , false);

        return new viewholding(v4);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull booked_Adapter.viewholding holder, int position) {

        BookedPark p1 = booked_list.get(position);
      //  holder.t12phonetext.setText(p1.booker_id);
        String owneriddd= p1.getOwnerid_();
       // spot_iddd = p1.getSpotid_();
        //getting the ids & reading from the firebase + setting them to textviews
        FirebaseDatabase database44;
        DatabaseReference refrence54;
        database44 = FirebaseDatabase.getInstance();
        refrence54 = database44.getReference("BookedPark");
        refowners = refrence54.child(p1.getBooker_id());
        int i = 0;
        readingparkinfoo(holder , p1.getSpotid_() , p1.getBooker_id() , refrence54 );

        refowners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                //    String ids[]= dataSnapshot.child("spotid_").getValue().toString().split("/");
                 //   String spottid , keyspot;

                    if (p1.getSpotid_().equals(dataSnapshot.child("spotid_").getValue().toString())){
                        if (dataSnapshot.child("statusbooked").getValue().toString()== null){
                            holder.t2stattext.setText("Left");
                        }//end if

                        else{

                            ////checking if the time has passed to leave :(
                            checkleavingtime(dataSnapshot.child("spotid_").getValue().toString(), p1.getBooker_id());
                           // if (stre== false)
                            holder.t2stattext.setText(dataSnapshot.child("statusbooked").getValue().toString());///}
                             holder.t2daytext.setText(dataSnapshot.child("day").getValue().toString());}//end if stre
                       // if (stre== true){
                           // DatabaseReference refrence44 = refowners.child(dataSnapshot.child("spotid_").getValue().toString());
                            //dataSnapshot.child("statusbooked").getRef().setValue("Not Attend");
                           // holder.t2stattext.setText("Not Attend");
                            holder.t2stattext.setText(dataSnapshot.child("statusbooked").getValue().toString());
                            stre = false;
                       //}//end stre true
                        }//end else
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        holder.t2stattext.setText(refrence54.child(p1.getSpotid_()).child("statusbooked").toString());
        holder.t2daytext.setText(refrence54.child(p1.getSpotid_()).child("day").toString());

      // while(i <= booked_list.size()){

      // }//end while

      //  DatabaseReference refspots= refowners.child(spot_iddd);

            // reding the info from owners \
        refrence54 = database44.getReference("Users").child( owneriddd);
        refrence54.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                holder.t10ownertext.setText(snapshot.child("namme").getValue().toString());
                holder.t12phonetext.setText(snapshot.child("phonenum").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        ///////checking the period time/day to decide whether deleting / edit bookd spot acceptable or not
        int dayint , monthint , yearint ;
        String arraylist[] = p1.getDay().split("/");
        dayint = Integer.parseInt(arraylist[0]);
        monthint = Integer.parseInt(arraylist[1]);
        yearint = Integer.parseInt(arraylist[2]);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH, dayint);
        calendar1.set(Calendar.MONTH, monthint);
        calendar1.set(Calendar.YEAR, yearint);
        Calendar calendar1now = Calendar.getInstance();

        LocalDate localeDate = LocalDate.now();
        int noeday =localeDate.getDayOfMonth();

        int noyear = localeDate.getYear();
        if(monthint > cmonth+1 ||(monthint == cmonth+1 && cday < dayint)

       ){
         //enabling button cancellll

         //   Toast.makeText(context3.getApplicationContext(),(monthint > Calendar.MONTH )+" - "
         //  + (monthint == Calendar.MONTH && calendar1.get(Calendar.DAY_OF_MONTH) > noeday) + " - ", Toast.LENGTH_LONG).show();
            holder.canclbitton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context3.getApplicationContext(),cday +" "+  noeday+ "m " + cmonth +" " +monthint, Toast.LENGTH_LONG).show();
                    Intent n2 = new Intent(context3, Delete_bookedspot.class);
                    n2.putExtra("userid", p1.getBooker_id());
                    n2.putExtra("spotid", p1.getSpotid_());
                    n2.putExtra("ownerid" , p1.getOwnerid_());
                    n2.putExtra("from", p1.getTime_from());
                    n2.putExtra("to" , p1.getTime_to());
                    n2.putExtra("day", p1.getDay());
                    n2.putExtra("fullprice", p1.getFull_price());
                     n2.putExtra("location" ,holder.t4locationtext.getText() );
                     n2.putExtra("phone" , holder.t12phonetext.getText());
                     n2.putExtra("owned" , holder.t10ownertext.getText());
                   n2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context3.startActivity(n2);
                }//end on click
            });//end cancel button



            //enabling button cancellll
            holder.Editbookingbuttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent n2e = new Intent(context3, BookedSpotEdit.class);
                    n2e.putExtra("priceperhour", priceperhour);
                    n2e.putExtra("userid", p1.getBooker_id());
                    n2e.putExtra("spotid", p1.getSpotid_());
                    n2e.putExtra("ownerid" , p1.getOwnerid_());
                    n2e.putExtra("from", p1.getTime_from());
                    n2e.putExtra("to" , p1.getTime_to());
                    n2e.putExtra("day", p1.getDay());
                    n2e.putExtra("fullprice", p1.getFull_price());
                    n2e.putExtra("location" ,holder.t4locationtext.getText() );
                    n2e.putExtra("phone" , holder.t12phonetext.getText());
                    n2e.putExtra("owned" , holder.t10ownertext.getText());
                   n2e.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context3.startActivity(n2e);
                }//end on click
            });//end cancel button

        }//end if checking timess

        else {


            holder.canclbitton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context3.getApplicationContext(),cday +" "+  dayint+ "m " + cmonth+1 +" " +monthint, Toast.LENGTH_LONG).show();
                    Toast.makeText(context3 , (yearint == Calendar.YEAR)+ " "+ "time passed to cancel your booking for this spot!!",Toast.LENGTH_LONG).show();
                }
            });//end camcel button



            holder.Editbookingbuttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context3 , (yearint == Calendar.YEAR)+ " "+ "time passed to edit your booking for this spot!!",Toast.LENGTH_LONG).show();
                }
            });
        }//end else
    }// onBindviewholder

    public  void readingparkinfoo (booked_Adapter.viewholding holder , String spot_iddd , String booker , DatabaseReference refrence54){
        refowners = refrence54.child(booker);
        refowners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot22) {
                // plate_vehicle p3;
                System.out.println("chhhhhhhhhhhangeeeeee");
                for (DataSnapshot dataSnapshot: snapshot22.getChildren()) {
//                    System.out.println("lolololololololololololololololololololololo"+dataSnapshot.child("spot_id").getValue().toString() );
                    //  if (dataSnapshot.child(""))
                  if (dataSnapshot.child("spotid_").getValue().toString().equals(spot_iddd)){
                    holder.t2pricetext.setText(dataSnapshot.child("full_price").getValue().toString() +"  " + "SAR $");
                    System.out.println(dataSnapshot.child("full_price").getValue().toString()+"nooooooooooooooooooooooooooooooooooooooooooorrrrrrrr");
                    System.out.println();





                    holder.t6timefromtext.setText(dataSnapshot.child("time_from").getValue().toString());
                   holder.t8timetotext.setText( dataSnapshot.child("time_to").getValue().toString());

                ///   if (dataSnapshot.hasChild("linkpic")){
                  ///     Glide.with(context3).load(dataSnapshot.child("linkpic").getValue().toString()).into(holder.imagepark3); }// end if
                  ///  else{
                  ///     holder.imagepark3.setImageResource(R.drawable.spoticon);}// end of else

                      // reading the pics & lat /long
                      DatabaseReference refrence54 = FirebaseDatabase.getInstance().getReference("ParkingSpot");
                      refowners = refrence54.child(dataSnapshot.child("ownerid_").getValue().toString());
                      refowners.addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                              for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                  String ids [] = spot_iddd.split("/");
                                  if (dataSnapshot.child("spot_id").getValue().toString().equals(ids[0])){
                                      holder.t4locationtext.setText( dataSnapshot.child("longitude").getValue().toString()
                                              + " , " + dataSnapshot.child("latitude").getValue().toString());


                                         if (dataSnapshot.hasChild("linkpic")){
                                           Glide.with(context3).load(dataSnapshot.child("linkpic").getValue().toString()).into(holder.imagepark3); }// end if
                                       else{
                                          holder.imagepark3.setImageResource(R.drawable.spoticon);}// end of else
                                     priceperhour = dataSnapshot.child("price").getValue().toString();


                                  }//end if
                              }//end of for
                          }

                          @Override
                          public void onCancelled(@NonNull @NotNull DatabaseError error) {

                          }
                      });
                     }//end if


                }//end of for
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }


    public void checkleavingtime(String spotid6, String userid44){
        DatabaseReference refsatabase6 = FirebaseDatabase.getInstance().getReference().child("BookedPark").child(userid44);
        refsatabase6.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                //getting the current time & day:
                DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat sdfe2= new SimpleDateFormat("HH:mm");
                Date date = new Date();
                String daynow = sdf.format(date);
                int hournow = Integer.parseInt(sdfe2.format(date).substring(0, 2));
                int  hour2;
                int hourtimeto ;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    if (spotid6.equals(dataSnapshot.child("spotid_").getValue().toString())) {
                        //  parkid = dataSnapshot.child("spotid_").getValue().toString();
                        String ownerid = dataSnapshot.child("ownerid_").getValue().toString();
                        String time_to = dataSnapshot.child("time_to").getValue().toString();
                        hour2 = Integer.parseInt(sdfe2.format(date).substring(0, 2));
                        if (hour2 > 12 && time_to.substring(6, 8).equals("PM")) {
                            String timewithourr = time_to.substring(0, 2);
                            hourtimeto = Integer.parseInt(timewithourr);
                            hour2 = Integer.parseInt(sdfe2.format(date).substring(0, 2));
                            hour2 = hour2 - 12;
                        }//end if checking m
                        else {
                            String timewithourr_timeto = time_to.substring(0, 2);
                            hourtimeto = Integer.parseInt(timewithourr_timeto);
                            hour2 = Integer.parseInt(sdfe2.format(date).substring(0, 2));
                        }// end else
                        // hour2 = Integer.parseInt(sdfe2.format(date).substring(0,2));
                        System.out.println(hourtimeto + " kk " + hour2);
                        String time_too = dataSnapshot.child("time_to").getValue().toString();
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
                        System.out.println((hour2 >= hourtimeto) + "hoursssssss" + " " + hour2 + " " + hourtimeto);
                        System.out.println(sdf.format(dm).equals(sdf.format(date)));
                        String [] noorday = day.split("/");
                        int smonth = Integer.parseInt(noorday[1]);
                        int sday = Integer.parseInt(noorday[0]);

                     int smin = Integer.parseInt(time_to.substring(3,5));
                        System.out.println("truelolgh22" + (cday==sday) + smonth +" " + cmonth  );
                        if ((cday>sday|| cday==sday) && (smonth<cmonth+1 ||smonth==cmonth+1  ) && dataSnapshot.child("statusbooked").getValue().toString().equals("Left")!= true ){
                                if (sday == cday && cHour> hourtimeto )
                            dataSnapshot.child("statusbooked").getRef().setValue("Not Attend");
                               else if (sday == cday && cHour== hourtimeto && cMinute> smin)
                                    dataSnapshot.child("statusbooked").getRef().setValue("Not Attend");
                               else if (cday>sday && smonth==cmonth+1)
                                    dataSnapshot.child("statusbooked").getRef().setValue("Not Attend");

                            // System.out.println("aaaaaaaahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                            System.out.println("truelolgh" + (cday==sday) + (smonth==cmonth)  );
                            //  BookedPark hg6 = new BookedPark(userids , ownerid, parkid,time_from , time_to , day , full_price , "Open");

                            // bookedparkslist6.add(hg6);
                        }//end if day Not attend

                        else if(smonth<cmonth+1&& dataSnapshot.child("statusbooked").getValue().toString().equals("Left")!= true){
                            dataSnapshot.child("statusbooked").getRef().setValue("Not Attend");}

                          if ( dataSnapshot.child("statusbooked").getValue().toString().equals("Not Attend") != true){
                                     int hourfrom ,minfrom , minow;
                              String time_from = dataSnapshot.child("time_from").getValue().toString();
                              int hour = Integer.parseInt(sdfe2.format(date).substring(0, 2));
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
                              System.out.println(sdfe2.format(date) + " fully " + sdf.format(date) + "comaprring yarrb");
                              if ((sdf.format(dm).equals(sdf.format(date))  )&& hour >= hourfrom && dataSnapshot.child("statusbooked").getValue().toString().equals("On Hold")
                                      && dataSnapshot.child("statusbooked").getValue().toString().equals("Left")==false) {



                                  System.out.println(sdfe2.format(date) + " fully " + date + "comaprring yarrb");
                                  DatabaseReference refrence44 = refsatabase6.child(dataSnapshot.child("spotid_").getValue().toString());
                                  dataSnapshot.child("statusbooked").getRef().setValue("Open");}//end changing to open if














                          }//end if to make it open







                    }//end outer if

                }//end for

            }///end on data change

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });//end value listenrt


    }//end checking leaving time
    @Override
    public int getItemCount() {
        return booked_list.size();
    }

    public static class viewholding extends RecyclerView.ViewHolder {

        // refering to alll the elements :

        TextView t1price , t2pricetext , t3location , t4locationtext , t5timefrom ,
                t6timefromtext, t7timeto , t8timetotext , t9owner , t10ownertext, t11phoneown
                , t12phonetext , t2day , t2daytext , t2stat , t2stattext
                ,canclbitton , Editbookingbuttn;

        ImageView imagepark3 ;

        public viewholding(@NonNull @NotNull View itemView) {
            super(itemView);
            Editbookingbuttn = itemView.findViewById(R.id.textView77t);
            t1price = itemView.findViewById(R.id.textView50rrr);
            t2pricetext= itemView.findViewById(R.id.textView51rrr);
            t3location = itemView.findViewById(R.id.textView45ttrrrr);
            t4locationtext = itemView.findViewById(R.id.textView46ttrrrr);
            t5timefrom = itemView.findViewById(R.id.textView564r);
            t6timefromtext = itemView.findViewById(R.id.textView57ee);
            t7timeto = itemView.findViewById(R.id.khgisss);
            t8timetotext =itemView.findViewById(R.id.sdgkdbs);
            t9owner = itemView.findViewById(R.id.textView52df);
            t10ownertext= itemView.findViewById(R.id.textView53kj);
            t11phoneown = itemView.findViewById(R.id.textView54lk);
            t12phonetext = itemView.findViewById(R.id.oohg);
            imagepark3 = itemView.findViewById(R.id.imageView10tt);
            t2day = itemView.findViewById(R.id.textView22o);
            t2daytext = itemView.findViewById(R.id.textView30kkfgh);
            t2stat = itemView.findViewById(R.id.textView32);
            t2stattext = itemView.findViewById(R.id.textView33);
            canclbitton = itemView.findViewById(R.id.textView77u);


        }



    }// end of the inner class
}// end of class

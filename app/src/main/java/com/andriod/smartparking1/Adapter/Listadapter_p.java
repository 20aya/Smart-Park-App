package com.andriod.smartparking1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andriod.smartparking1.Barcode_shown;
import com.andriod.smartparking1.Classes.BookedPark;
import com.andriod.smartparking1.Delete_spotO;
import com.andriod.smartparking1.Edit_Spot;
import com.andriod.smartparking1.Classes.Parkingspot;
import com.andriod.smartparking1.R;
import com.andriod.smartparking1.Showbookeduser;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Listadapter_p extends RecyclerView.Adapter<Listadapter_p.MyViewHolder> {
    boolean check  ;
    Context context ;
    ArrayList <Parkingspot> listofspots ;

    public Listadapter_p(Context context , ArrayList <Parkingspot> listofspots) {
        this.context= context;
        this.listofspots = listofspots;

    }// end of constructer
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view2 = LayoutInflater.from(context).inflate(R.layout.list_item_owner,parent,false);
        // handling the edit button
       // startActivity(new Intent( ((Activity) context).getApplication() , Edit_spot.class));
        return  new MyViewHolder(view2);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
          // <<<we have to set the items to valuess>>>>
        Parkingspot ps1;
        ps1 = listofspots.get(position);
        System.out.println("outttttttttttt" + ps1.getPrice());
        //holder.txtstat.setText(ps1.getStatus());

        holder.txtnamesspot.setText("park spot" );
        holder.txtlocation.setText(ps1.getLongitude() + " , "+ps1.getLatitude());
        boolean ava=  checkavailbalitiy(ps1 , holder);
        holder.imageava.setImageResource(R.drawable.available);
      //*  if (ava== true){
     //*   holder.imageava.setImageResource(R.drawable.booked);}//end of if
     //*   else if (ava== false) {
       //*     holder.imageava.setImageResource(R.drawable.available);
      //*  }// end of else
       // check= true;
        if (ps1.getLinkpic()!= null){
            Glide.with(context).load(listofspots.get(position).getLinkpic()).into(holder.imagspotpic);}// end if
        else{
            holder.imagspotpic.setImageResource(R.drawable.spoticon);}// end of else
       // holder.txtTto.setText(ps1.getTimeTo());
     //   holder.txtTfrom.setText(ps1.getTimefrom());
        holder.txttprrice.setText(ps1.getPrice());
        holder.txtTypee.setText(ps1.getSpot_type());
        //Glide.with(context).load(listofspots.get(position).getParcodeurl()).into(holder.parcodeimage);

        // handling the edit button
        holder.editspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //*  Intent intent = new Intent();
               //* methodStartActivity(context);

              //  FragmentTransaction fragmentTransaction = getSu


               // Bundle bundle = new Bundle();
                Intent intent22 = new Intent(context, Edit_Spot.class);
                intent22.putExtra("price", ps1.getPrice() );
                intent22.putExtra("type", ps1.getSpot_type());
               intent22.putExtra("piclink" , ps1.getLinkpic());
               intent22.putExtra("spotid" , ps1.getSpot_id());


                context.startActivity(intent22);

            }
        });

        holder.parcodeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Barcode_shown.class);
                intent.putExtra( "barcode" , ps1.getParcodeurl());
                context.startActivity(intent);
            }
        });// end parcode image

        if (ps1.getStatus()== "avaliable"){
            holder.imageava.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent22 = new Intent(context, Showbookeduser.class);

                    String []spotids = ps1.getSpot_id().split("/");

                    context.startActivity(intent22);

                }
            });



        }// end if stat
       if (ps1.getStatus()!= "avaliable"){
           holder.imageava.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent22 = new Intent(context, Showbookeduser.class);
                   intent22.putExtra("ownerid", ps1.getOwner_id() );

                   intent22.putExtra("spotid" , ps1.getSpot_id());
                   //intent22.putExtra("bookerid" , );

                   String []spotids = ps1.getSpot_id().split("/");

                   context.startActivity(intent22);

               }
           });



       }// end if stat

        holder.delettt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent n2 = new Intent(context, Delete_spotO.class);
            n2.putExtra("ownerid", ps1.getOwner_id());
            n2.putExtra("spotid", ps1.getSpot_id());
            n2.putExtra("piclink" , ps1.getLinkpic());
            n2.putExtra("price", ps1.getPrice());
            n2.putExtra("type" , ps1.getSpot_type());
                n2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(n2);

            }
        });// end of handling deleete button
    }// on Bind view holder


    public boolean checkavailbalitiy(Parkingspot ps1, MyViewHolder holder){

        DatabaseReference red = FirebaseDatabase.getInstance().getReference("BookedPark");

        red.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    for (DataSnapshot dataSnapshot1n : dataSnapshot.getChildren()){

                        String [] idssw =dataSnapshot1n.child("spotid_").getValue().toString().split("/");
                        System.out.println("noor" +idssw[0].equals(ps1) + "/" + idssw[0] + " /"+ ps1  );
                        if (idssw[0].equals(ps1.getSpot_id())) {
                            System.out.println(  dataSnapshot1n.child("statusbooked").getValue().toString()+"lkjhg22" + (dataSnapshot1n.child("statusbooked").getValue().toString().equals("On Hold")==true||
                                    dataSnapshot1n.child("statusbooked").getValue().toString().equals("Open")==true)   );
                            if(  dataSnapshot1n.child("statusbooked").getValue().toString().equals("On Hold")==true ||
                                    dataSnapshot1n.child("statusbooked").getValue().toString().equals("Open")==true ){
                                System.out.println( "lkjhg3" + dataSnapshot1n.child("statusbooked").getValue().toString()+ "/");
                                holder.imageava.setImageResource(R.drawable.booked);
                                break;

                            }//end if





                        }//outer if



                    }//end inner for
                }//end for outer


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });//end firebase

        System.out.println(check +" check12313");
        return  check;



    }//END CHECKING AVAILABLITY
    @Override
    public int getItemCount() {

        // here too returing all the items
        return listofspots.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtlocation , txtnamesspot, txtstat , txtTfrom, txtTto, txtTypee ,txttprrice;
        ImageView imageava , imagspotpic , parcodeimage;
        TextView editspot , delettt;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            txtlocation = itemView.findViewById(R.id.textView31);
            txtnamesspot = itemView.findViewById(R.id.textView28);
            delettt = itemView.findViewById(R.id.textView77u);
            //txtstat = itemView.findViewById(R.id.textView35);
            imageava = itemView.findViewById(R.id.imageView9);
            imagspotpic = itemView.findViewById(R.id.imageView3);
            txtTypee = itemView.findViewById(R.id.textView39);
          //  txtTfrom = itemView.findViewById(R.id.textView32);
           // txtTto = itemView.findViewById(R.id.textView34);
            txttprrice = itemView.findViewById(R.id.textView37);
            editspot = itemView.findViewById(R.id.textView77);
            parcodeimage = itemView.findViewById(R.id.imageView22b);

        }
    }//end of inner class



}// end of class


package com.andriod.smartparking1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;


import com.andriod.smartparking1.Adapter.Listadapter_p;
import com.andriod.smartparking1.Classes.Parkingspot;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.jetbrains.annotations.NotNull;



import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link owner_main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class owner_main extends Fragment {
    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerView;
    ListView Llstview;
    ProgressDialog progressDialog;

    DatabaseReference refdata4;
    Listadapter_p palist ;
    ArrayList <Parkingspot> arrlistpark;
    FirebaseAuth fauthh2;

    TextView text255;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public owner_main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment owner_prof.
     */
    // TODO: Rename and change types and number of parameters
    public static owner_main newInstance(String param1, String param2) {
        owner_main fragment = new owner_main();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_owner_main, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
      //checking user type for making sure no normal accounts
        fauthh2 = FirebaseAuth.getInstance();
        String userid2 = fauthh2.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid2);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.child("type").getValue().toString().equals("Normal Type")){
                    Intent intent = new Intent(getActivity() , Login_page.class);
                    startActivity(intent);
                    getActivity().finish();


                }//end if
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });//end checking user


        text255 = view.findViewById(R.id.textView25);
        recyclerView =  view.findViewById(R.id.listview_item);
        fauthh2  =FirebaseAuth.getInstance();
        String userid22 = fauthh2.getUid();
        refdata4 = FirebaseDatabase.getInstance().getReference("ParkingSpot");
        DatabaseReference zone1Ref = refdata4.child(userid22);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrlistpark = new ArrayList<Parkingspot>();
        palist = new Listadapter_p(getActivity() , arrlistpark);
        recyclerView.setAdapter(palist);
////////////////////////////////////////////////////////////////////////////////////
       /*
        FirebaseRecyclerOptions <Parkingspot> options =
                new FirebaseRecyclerOptions.Builder<Parkingspot>().setQuery(zone1Ref,
                        new SnapshotParser<Parkingspot>() {
                            @NonNull
                            @NotNull
                            @Override
                            public Parkingspot parseSnapshot(@NonNull @NotNull DataSnapshot snapshot) {
                               Parkingspot pu = snapshot.getValue(Parkingspot.class);
                                 pu.setSpot_id( snapshot.getKey());

                                return pu;
                            }
                        }).build();

        adapter = new FirebaseRecyclerAdapter(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position, @NonNull @NotNull Object model) {

                Listadapter_p.MyViewHolder vh = (Listadapter_p.MyViewHolder) holder;
                Parkingspot ps1;
                ps1 = (Parkingspot) model;
                System.out.println("outttttttttttt" + ps1.price);
                //holder.txtstat.setText(ps1.getStatus());

                vh.txtnamesspot.setText("park spot" );
                vh.txtlocation.setText(ps1.getLongitude() + " , "+ps1.getLatitude());
                if (ps1.getStatus().equals("avaliable")){
                    vh.imageava.setImageResource(R.drawable.available);}//end of if
                else {
                    vh.imageava.setImageResource(R.drawable.booked);
                }// end of else
                if (ps1.getLinkpic()!= null){
                    Glide.with(getContext()).load(ps1.getLinkpic()).into(vh.imagspotpic);}// end if
                else{
                   vh.imagspotpic.setImageResource(R.drawable.spoticon);}// end of else
                // holder.txtTto.setText(ps1.getTimeTo());
                //   holder.txtTfrom.setText(ps1.getTimefrom());
                vh.txttprrice.setText(ps1.getPrice());
                vh.txtTypee.setText(ps1.getSpot_type());
                //Glide.with(context).load(listofspots.get(position).getParcodeurl()).into(holder.parcodeimage);

                // handling the edit button
                vh.editspot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        //** methodStartActivity(context);
                        // ps1
                        //dialog
                        //editdialog.show();
                        Edit_spot editdialog = new  Edit_spot(getContext() , ps1.getPrice() , ps1.getSpot_type()  , ps1.getLinkpic()
                                , ps1.getSpot_id());
                        editdialog.getWindow();
                        editdialog.setCancelable(true);
                        editdialog.show();

                        //  String valuepr = editdialog.getPricedchange();
                        // boolean valuetype = editdialog.getTypechanged();
                        //  holder.txttprrice.setText(valuepr);
                        //    if (valuetype == true ){
                        //       holder.txtTypee.setText("Indoor");
                        //     }//end of if
                        //  else {holder.txtTypee.setText("Outdoor"); }



                        //startActivity(new Intent( ((Activity) context).getApplication() , Edit_spot.class));
                    }
                });

                vh.parcodeimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Barcode_shown.class);
                        intent.putExtra( "barcode" , ps1.getParcodeurl());
                        getContext().startActivity(intent);
                    }
                });// end parcode image


                vh.delettt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });// end of handling deleete button
            }/// end this class bindviewholder

            @NonNull
            @NotNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view2 = LayoutInflater.from(getContext()).inflate(R.layout.list_item_owner,parent,false);
                // handling the edit button
                // startActivity(new Intent( ((Activity) context).getApplication() , Edit_spot.class));
                return  new Listadapter_p.MyViewHolder(view2);
            }



            @Override
            public void onDataChanged() {
                Toast.makeText(getContext(), "Spot got changed!", Toast.LENGTH_SHORT).show();

            }



        };
        recyclerView.setAdapter(adapter);














*/
        /////////////////////////////////////////////////////////////////////
        zone1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot22) {
                Parkingspot spotd = new Parkingspot();
               // will fitch all the dataa of parks
                for (DataSnapshot dataSnapshot: snapshot22.getChildren()){



                     String parkkid = dataSnapshot.getValue().toString();
                    Parkingspot spotd2 = dataSnapshot.getValue(Parkingspot.class);
                    System.out.println("yarbbb mmnnnkll" +parkkid);
                     arrlistpark.add(spotd2);


                }//end for

                palist.notifyDataSetChanged();
                progressDialog.dismiss();
                System.out.println("looovvvvvvvvvvvveeeeee");
               // System.out.println(arrlistpark.get(0).getSpot_type() + arrlistpark.get(0).getStatus());

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        TextView ter = view.findViewById(R.id.textView53);
if (arrlistpark.size()==0){
     ter = view.findViewById(R.id.textView53);
}//end if
        else{
            ter.setText("");
}






        // refering to all the items
        /*
       imagevieww = view.findViewById(R.id.profile_pic);
       imagevAB = view.findViewById(R.id.imageView4);
       t555 = view.findViewById(R.id.msgtime);
       T7779 = view.findViewById(R.id.lastMessage);
       t566 = view.findViewById(R.id.personName);
*/
        return  view;

    }//oncreate view



 }// end of class
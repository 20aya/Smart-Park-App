package com.andriod.smartparking1;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andriod.smartparking1.Adapter.vehicle_list_adapter;
import com.andriod.smartparking1.Classes.plate_vehicle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
//import com.google.firebase.ml.vision.FirebaseVision;
//import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.text.FirebaseVisionText;
//import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
//import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
//import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link vehicle_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class vehicle_frag extends Fragment {
    Bitmap imageBitmap;
    TextView oo;
    private String mParam1;
    private String mParam2;
    TextView ADD;
    Dialog dialog;
    TextView cambutton;
    EditText numtt;
    Button savebutton ;
    FirebaseAuth fAuth44 = FirebaseAuth.getInstance();
    FirebaseDatabase rootNode44;
    DatabaseReference refrence44;

    RecyclerView recyclerView2;
    ArrayList<plate_vehicle>platelists;
    vehicle_list_adapter vehicle_list_adapter2;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private static final int Request_camera_code=100;
//for cam
static final int REQUEST_IMAGE_CAPTURE = 1;


    public vehicle_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment history_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static vehicle_frag newInstance(String param1, String param2) {
        vehicle_frag fragment = new vehicle_frag();
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
        View view = inflater.inflate(R.layout.fragment_vehicle_frag,container,false);
         progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        ADD = view.findViewById(R.id.textView24);
        recyclerView2 = view.findViewById(R.id.vehc_rec);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        platelists=new ArrayList<plate_vehicle>();
        vehicle_list_adapter2 = new vehicle_list_adapter(getActivity() , platelists);
        recyclerView2.setAdapter(vehicle_list_adapter2);


        Reading_vehicles_firebase();


       //oo = view.findViewById(R.id.textView44);
      //  oo.setText("pppppp");
        //onclick litsnr for add button
        ADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new Dialog(getActivity());
                dialog.setContentView(R.layout.vehical_info);

                dialog.getWindow().setBackgroundDrawableResource(R.drawable.background2);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.show();
                //NOW FOR THE BUTTON INTHE DIALOG
                cambutton = dialog.findViewById(R.id.textView23);
                numtt= dialog.findViewById(R.id.noortext);
                savebutton = dialog.findViewById(R.id.button6);
                //for the cammera premisson
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!=
                        PackageManager.PERMISSION_GRANTED){
                    //to ask the premission
                    ActivityCompat.requestPermissions(getActivity(),new String []{
                            Manifest.permission.CAMERA
                    },Request_camera_code);

                }//if

                cambutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       //we will call our crop image Activity that we will get from the library

                        captureimage();
                      //  detecttext();

                    }




                });

                // handling the save buutton too!!
                // handling the save buuutoon to save it in firebase :)
                savebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (numtt.getText().toString().equals("")){

                            Toast.makeText(getActivity(), "please fill the empty field!!", Toast.LENGTH_LONG).show();
                        }//end if
                        else {
                            String platenum = numtt.getText().toString();
                            String id_user_norm = fAuth44.getUid();
                            rootNode44 = FirebaseDatabase.getInstance();
                            refrence44 = rootNode44.getReference("Plates");
                            String plate_id2 = refrence44.push().getKey();
                            plate_vehicle newv = new plate_vehicle(id_user_norm, plate_id2, platenum);

                            refrence44.child(id_user_norm).child(plate_id2).setValue(newv);

                            platelists.clear();
                            vehicle_list_adapter2 = new vehicle_list_adapter(getActivity(), platelists);
                            recyclerView2.setAdapter(vehicle_list_adapter2);
                            // Reading_vehicles_firebase();

                            dialog.dismiss();

                            Toast.makeText(getActivity(), "your vehicle saved successfully!", Toast.LENGTH_SHORT).show();
                        }//end else if no empty
                    }//on click save button
                });// save button

            }// on click add
        });// add button on click listener




        return view ;

    }

    private void Reading_vehicles_firebase() {
        
        String id_user_norm = fAuth44.getUid();
        rootNode44 = FirebaseDatabase.getInstance();
        refrence44 = rootNode44.getReference("Plates");
        DatabaseReference zone1Ref = refrence44.child(id_user_norm);
        zone1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot22) {
                // plate_vehicle p3;

                for (DataSnapshot dataSnapshot: snapshot22.getChildren()) {

           //    plate_vehicle  p3 = dataSnapshot.getValue(plate_vehicle.class);
                 String platamv = dataSnapshot.child("platenum").getValue().toString();
                 String veh_id = dataSnapshot.child("plate_id").getValue().toString();
              plate_vehicle  p3 = new plate_vehicle(id_user_norm , veh_id,platamv);
                System.out.println("loooooooooooooooooooove" + platamv);
                platelists.add(p3);


                 }//end of for
                vehicle_list_adapter2.notifyDataSetChanged();
                progressDialog.dismiss();
               // for to not repeting plates :)
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }// end of reding vehicle firebase


    ///the picture that has been takennnn
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            //assign the picture that has been taken to Bitmap & from Bitmap to firebassevisin image
            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap( imageBitmap);
            FirebaseVision  firebaseVision= FirebaseVision.getInstance();
            FirebaseVisionTextRecognizer recognizer = firebaseVision.getOnDeviceTextRecognizer();
            // processing the image by creating a task object
            Task<FirebaseVisionText> task = recognizer.processImage(firebaseVisionImage);////hereee
            task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    if (firebaseVisionText.getText()!= null){
                String s= firebaseVisionText.getText(); // extract the plate number
                System.out.println(s);
                 numtt.setText(s);  // we willl set it to the edit text
                         }
                    else {
                        Toast.makeText(getActivity(), "Error occured:" , Toast.LENGTH_SHORT).show(); }
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getActivity(), "Error occured:"+ e.getMessage() , Toast.LENGTH_SHORT).show();
                }
            });
        }// end of if
    }

    private void captureimage() {
        // for capturing imagen - opening camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

          if (takePictureIntent.resolveActivity(getActivity().getPackageManager())!= null) {
              try {
                  startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
              } catch (Exception e) {
                  // display error state to the user
                  Toast.makeText(getActivity(), "you need to provide a camera access premission", Toast.LENGTH_LONG).show();
                  //for the cammera premisson
                  if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                          == PackageManager.PERMISSION_DENIED)
                      ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, Request_camera_code);

                //  dialog.dismiss();


              }//end of catch
          }
    }//end of capture image



    }// end of class


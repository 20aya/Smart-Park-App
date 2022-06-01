package com.andriod.smartparking1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

public class main_frag extends Fragment implements OnMapReadyCallback {

   // private FusedLocationProviderClient mFusedLocationProviderClient22 = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MapView usermapView;
    GoogleMap mapuser;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    TextView parkmap , booked,leaving , message;
    private FusedLocationProviderClient mFusedLocationProviderClient22 = null; // to track user location
    public main_frag() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static main_frag newInstance(String param1, String param2) {
        main_frag fragment = new main_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

 public void onMapReady(GoogleMap map2) {
     usermapView.onResume();
     mapuser = map2;
     CheckMyPremssion();
//we are checking if we have the premission to display the map
     if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
             != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
             Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         return;

     }// end if


     mapuser.setMyLocationEnabled(true);
 }//onmapready


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String userid2 = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid2);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.child("type").getValue().toString().equals("Owner Type")){
                    Intent intent = new Intent(getActivity() , Login_page.class);
                    startActivity(intent);
                    getActivity().finish();


                }//end if
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });//end checking user
        // Inflate the layout for this fragment
        View view2 = inflater.inflate(R.layout.fragment_main_frag, container, false);
       //checking wether the account is for the normal user or not :



        parkmap = view2.findViewById(R.id.textView40);
        leaving=view2.findViewById(R.id.textView41);
        booked=view2.findViewById(R.id.textView35);
        message = view2.findViewById(R.id.textView40tyy);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent44 = new Intent(getActivity(),message_users.class);
                startActivity(intent44);
            }
        });
        leaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent21 = new Intent(getActivity(),LeavingActivity.class);
                startActivity(intent21);
            }
        });
        //for booked spots button
        booked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), BookedSpotsfrag.class);
                startActivity(intent2);
            }
        });
        parkmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the map activity
                parksmap();
            }
        });

        usermapView=view2.findViewById(R.id.user_map);
        CheckMyPremssion();
        Bundle mapViewBundle22 = null;
        //checking the premmisoiion to access location

        // right to diplaying the map screen
        if (savedInstanceState != null) {
            mapViewBundle22 = savedInstanceState.getBundle("MapViewBundleKey");
        }

        // Inflate the layout for this fragment
        usermapView.onCreate(mapViewBundle22);

        usermapView.getMapAsync(this);
       // Bundle mapViewBundle3 =
        return view2;
    }// end of on create view
public void parksmap(){
  Intent intent = new Intent(this.getActivity(),parksMap.class);
  startActivity(intent);
}//end parks map

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //CheckMyPremssion();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }// end of on create of the bundle




    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        //  getCurrentLocation();

      ///***  CheckMyPremssion();
        Bundle mapViewBundle22 = outState.getBundle("MapViewBundleKey");
        //usermapView.onSaveInstanceState(mapViewBundle22);


        if (mapViewBundle22 == null) {
            mapViewBundle22 = new Bundle();
            outState.putBundle("MapViewBundleKey", mapViewBundle22);
          //  usermapView.onSaveInstanceState(mapViewBundle22);
            //usermapView.onSaveInstanceState(mapViewBundle22);
        }

        usermapView.onSaveInstanceState(mapViewBundle22);


       //******* usermapView.onSaveInstanceState(mapViewBundle22);

      //  usermapView.onSaveInstanceState(mapViewBundle2);
    } //on save instance method


    private void CheckMyPremssion() {
        // premmsion listener interface//Dexter is the library that will help us to make this task easy for handling runtime permissions in Android. Now we will see the implementation of this in our Android app.
        getCurrentLocation();

        Dexter.withContext(getActivity()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {


            // if access is accepted
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
              //  isPremisionGranted = true;
             //   getCurrentLocation();
                // Toast.makeText(getActivity(), "Map ready", Toast.LENGTH_SHORT).show();

            }


            // if access  is denied
            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                // then going to the setting page for manullay providing access

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), "");
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

        mFusedLocationProviderClient22 = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locat22 = mFusedLocationProviderClient22.getLastLocation();
        locat22.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Location> task) {
                if (task.isSuccessful()){

                    Location uloc22 = task.getResult();
                    if (uloc22 != null){
                        moveCamera(new LatLng(uloc22.getLatitude(), uloc22.getLongitude()),15);

                    }//inner if
                }//end if

                else{
                    Toast.makeText(getActivity(),"current location not found ",Toast.LENGTH_SHORT).show();
                    CheckMyPremssion();
                }//else

            }//onComplete


        }); // add on complete listener



    }// end of getcurrent location

    private void moveCamera(LatLng latLng, int i) {
        if (mapuser != null){
     mapuser.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,i));
        mapuser.addMarker(new MarkerOptions().position(latLng).title("your location! :)"));}// end if

    }// end of move camera
}//end of class
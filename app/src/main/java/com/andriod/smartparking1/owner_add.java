package com.andriod.smartparking1;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.andriod.smartparking1.Classes.Model;
import com.andriod.smartparking1.Classes.Parkingspot;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import static android.app.Activity.RESULT_OK;
import static androidx.browser.customtabs.CustomTabsClient.getPackageName;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


//com.google.android.gms.maps.MapView
public class owner_add extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener, LocationListener, GoogleMap.OnCameraMoveStartedListener  {
    boolean isPremisionGranted;
    MapView mapView; 
    int zoom = 15;
    EditText searchmap;
    ProgressDialog progressDialog;
    GoogleMap Mmap;
    ImageView test ;
    ProgressDialog progressDialog2;
    //String parcodeurl = "";

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseDatabase rootNode2;
    DatabaseReference refrence2;
    // to upload the pic to storage
    FirebaseStorage firebaseStorage ;
    StorageReference storageReference;

   // LatLng userlat = new LatLng(21.543333, 39.172779);
    private FusedLocationProviderClient mFusedLocationProviderClient = null; // to track user location

    String spot_id2;
    // for the time variabless:
       TextView Etfrom , EtTo ;
       int from_Hour , from_Minute , To_Hour , To_Minute;

    // dialog
    Dialog dialog ;

       // button add park
       Button addb ;
       // taking the other informations of park:
      EditText pricetext ;
      SwitchCompat switch_type ;
      double finlatiude , finlongat;

      // for taking images
      Uri imageuri ;
      String pic_url;
      String parcodeurl;
    private final  int PREMISSION_CODE = 1001;
    private  final int IMAGE_PICK_CODE = 1000;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public owner_add() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment owner_history.
     */
    // TODO: Rename and change types and number of parameters
    public static owner_add newInstance(String param1, String param2) {
        owner_add fragment = new owner_add();
        Bundle args = new Bundle();
       // args.putString(ARG_PARAM1, param1);
       // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    //widgets


    @Override
    public void onCreate(Bundle savedInstanceState) {

        // search loc
        // getCurrentLocation();
        super.onCreate(savedInstanceState);
        // getCurrentLocation();
        CheckMyPremssion();
        if (getArguments() != null) {
          //  userlat = new LatLng(savedInstanceState.getLong("keylon", 0), savedInstanceState.getLong("keylat", 0));

          //  mParam1 = getArguments().getString(ARG_PARAM1);
          //  mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    //init method for searhching
    public void init() {
        Log.d(TAG, "init: initializing");
        searchmap.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyevent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE ||
                        keyevent.getAction() == KeyEvent.ACTION_DOWN
                        || keyevent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //execute our method for searching
                    geoLocate();

                }
                return false;
            }
        });
    }

    // this method for searching (not required)
    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");
        String searchstring = searchmap.getText().toString();
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchstring, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate" + e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geoLocate : Found a location :" + address.toString());
            //Toast.makeText(this,address.toString(),Toast.LENGTH_SHORT).show();
            //moveCamera( new LatLng(address.getLatitude(),address.getLongitude()),15f);

        }//if
    }// end of geo locate method


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_owner_add, container, false);
        mapView = view.findViewById(R.id.owner_map);
        searchmap = (EditText) view.findViewById(R.id.editTextTextPersonName2);



        pricetext = view.findViewById(R.id.editTextNumberDecimal2);
        switch_type = view.findViewById(R.id.switchCompat);
        test = view.findViewById(R.id.imageView21);
        //

        progressDialog2 = new ProgressDialog(getActivity());
        //AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        progressDialog2.setCancelable(false);
        progressDialog2.setMessage("Loading...");

        // intalizing the add button
        addb = view.findViewById(R.id.button5);

        Bundle mapViewBundle = null;
        //checking the premmisoiion to access location
        CheckMyPremssion();
        // right to diplaying the map screen
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey");
        }

        // Inflate the layout for this fragment
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);


        // Handling the add button :)
        addb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // showing the camera dialog :
                //intalizing dialog

                if (pricetext.getText().toString().equals("")|| searchmap.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "please fill the empty fields", Toast.LENGTH_LONG).show();

                }//end if empty fields
                else if (pricetext.getText().toString().equals("0.00")||pricetext.getText().toString().equals("0") ||
                        pricetext.getText().toString().equals("0.")|| pricetext.getText().toString().equals(".")){
                    pricetext.setError("price can't be zero");
                    Toast.makeText(getActivity(), "please specify a price ", Toast.LENGTH_LONG).show();

                }
                else {
                    dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.cameradialog);

                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.ic_baseline_camera_enhance_24);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(true);
                    dialog.show();
                    Button Nopic = dialog.findViewById(R.id.btn_cancel);
                    Nopic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();


                            progressDialog2.show();
                            try {
                                Saving_spotinfo_Nopic();
                            } catch (IOException | WriterException e) {
                                e.printStackTrace();
                            }
                        }

                    }); // set on click listner for cancel buton

                    Button addpic = dialog.findViewById(R.id.btn_okay);

                    addpic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressDialog2.show();

                            //taking the address in words & convert it to lat & long
                            Geocoder geocoder3 = new Geocoder(getActivity(), Locale.getDefault());
                            String adressForLatling2 = searchmap.getText().toString();//taking the adress from edit text
                            List<Address> addreses4 = new ArrayList<Address>();
                            try {
                                addreses4 = geocoder3.getFromLocationName(adressForLatling2, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //converting to lat & long
                            finlongat = addreses4.get(0).getLongitude();
                            finlatiude = addreses4.get(0).getLatitude();
                            //////////////////////////////////////////
                            imagemethod();

                            dialog.dismiss();
                            //   Toast.makeText(getActivity(),"pic :)" ,Toast.LENGTH_SHORT).show();
                        }


                    });/// add pic handling


                }//end else empty
            } // on click for add buton

        }); // end of addb button set on click listener

        // italizing the storage firebase
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        progressDialog2.dismiss();
        return view;

    }// END ON CREATE VIEW

    //for generating par code :)
    public void generateparcode() throws WriterException, FileNotFoundException {
        String data = spot_id2;
        MultiFormatWriter multiFormatWriter= new MultiFormatWriter();
        // saving the spot id in the barcode to create it
        BitMatrix bitMatrix = multiFormatWriter.encode(data.toString(), BarcodeFormat.QR_CODE,500,500);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();// from library zxihng to create a barcode
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();   // converting it to uri to save it in firebase
        bitmap.compress(Bitmap.CompressFormat.PNG , 100 , bytes);
        //converting to file to get uri
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File mFileTemp = null;
        String root= getActivity().getDir("my_sub_dir", getContext().MODE_PRIVATE).getAbsolutePath();
        File myDir = new File(root + "/Img");
        if(!myDir.exists()){
            myDir.mkdirs(); }
        try { mFileTemp=File.createTempFile(imageFileName,".jpg",myDir.getAbsoluteFile()); } catch (IOException e1) {
            e1.printStackTrace(); }
        if ( mFileTemp != null) {
            FileOutputStream fout;
            try { fout = new FileOutputStream( mFileTemp);
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, fout);
                fout.flush(); } catch (Exception e) {
                e.printStackTrace(); }

            Uri uri=Uri.fromFile( mFileTemp); // converting the file to object uri to have the url of the bas
                                              //bar code
            parcodeurl = uri.toString(); } }//end parcode
















    private void CheckMyPremssion() {
        // premmsion listener interface//Dexter is the library that will help us to make this task easy for handling runtime permissions in Android. Now we will see the implementation of this in our Android app.
        // getCurrentLocation();
        if (isPremisionGranted) {
            getCurrentLocation();
        }
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {


            // if access is accepted
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPremisionGranted = true;
                getCurrentLocation();
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


    // get the current location of the user
    private void getCurrentLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

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
        Task<Location> locat = mFusedLocationProviderClient.getLastLocation();
        locat.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Location> task) {
                if (task.isSuccessful()){

                    Location uloc = task.getResult();
                    if (uloc != null){
                        moveCamera(new LatLng(uloc.getLatitude(), uloc.getLongitude()),zoom);

                    }//inner if
                }//end if

                else{
                    Toast.makeText(getActivity(),"current location not found ",Toast.LENGTH_SHORT).show();
                    CheckMyPremssion();
                }//else

            }//onComplete
        }); // add on complete listener


    }// end of currentlocation


    // Now we have to zoom to the user location:
     private void moveCamera(LatLng latLng , float zoom1){
      if (Mmap != null){
          Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom1));}



     }// end move camera method


    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
      //  getCurrentLocation();
        CheckMyPremssion();
        Bundle mapViewBundle = outState.getBundle("MapViewBundleKey");
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle("MapViewBundleKey", mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    } //on save instance method


    @Override
    public void onResume() {
        super.onResume();
        getCurrentLocation();
        mapView.onResume();

    }

    @Override
    public void onStart() {

        super.onStart();
        mapView.onStart();
        getCurrentLocation();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("marker"));
        //to display my location i have to preform the premission check
         Mmap= map ;
        // mapView.onResume();
         CheckMyPremssion();

         //CheckMyPremssion();
         // this if condition to check if we have the premission to displayy the map
        // we are  checking the access coarse location/ access fine location /
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; // if we dont have the prem we will return tooncreate method

        }// end if

        Mmap.setMyLocationEnabled(true);
        Mmap.setOnCameraMoveListener(this);
        //map.setOnCameraMoveListener(this);
        Mmap.setOnCameraIdleListener(this);
        Mmap.setOnCameraMoveStartedListener(this);


        //userlat= getDeviceLocation(map);
        //map.addMarker(new MarkerOptions().position(userlat).title("Your location"));
        Mmap.getUiSettings().setAllGesturesEnabled(true);

        // to be able to search for loc
        init();
    }




    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        // to get the current location
     Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
     // we need aaraylist to save it & show it as street name etc...
        ArrayList<Address> addreses = new ArrayList<Address>();
        // for any errors
        try {
           addreses= (ArrayList) geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
          // this.finlatiude= location.getLatitude();
           //this.finlongat = location.getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }//catchh

        setadress (addreses.get(0));
    }//onlocationchanged

    private void setadress(Address adress) {
        // decoding & showing the address in a text + cheking if they are null or  not
        if (adress != null){
            if (adress.getAddressLine(0)!=null){

             searchmap.setText(adress.getAddressLine(0));

            }//inner if

            if (adress.getAddressLine(1)!=null){
                searchmap.setText(searchmap.getText().toString()+ adress.getAddressLine(1).toString());

            }//inner if 2


        }//if
    }//setadress


    @Override
    public void onCameraMoveStarted(int i) {

    }






    @Override
    public void onCameraIdle() {
        // to get the current location
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        // we need aaraylist to save it & show it as street name etc...
        List<Address> addreses = new ArrayList<Address>();
        // for any errors
       // GoogleMap Mmap;
       // Mmap = OnMapReady();
        try {
            addreses = geocoder.getFromLocation(Mmap.getCameraPosition().target.latitude,Mmap.getCameraPosition().target.longitude,1);
            setadress (addreses.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }//catchh
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }//catch2

    }//on Camera ID LISTENERR


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onCameraMove() {

    }


    // saving to firebase partt :)

    public void Saving_spotinfo_Nopic() throws IOException, WriterException {
        String owner_idd2 = fAuth.getUid();
        String Switch_type4;
        if (switch_type.isChecked()== true){ Switch_type4 = "Indoor"; }//if first
        else{ Switch_type4 ="Outdoor"; }//else
        String status_park ="available";
        String pricce = (String) pricetext.getText().toString().trim();
        Geocoder geocoder2 = new Geocoder(getActivity(),Locale.getDefault());
        String adressForLatling = searchmap.getText().toString();
        List<Address> addreses2 = new ArrayList<Address>();
        addreses2 = geocoder2.getFromLocationName(adressForLatling , 1);
        this.finlongat = addreses2.get(0).getLongitude();
        this.finlatiude = addreses2.get(0).getLatitude();
           rootNode2 = FirebaseDatabase.getInstance();
           refrence2 = rootNode2.getReference("ParkingSpot");
           spot_id2 = refrence2.push().getKey();
       generateparcode();
        Parkingspot new_spot = new Parkingspot(spot_id2,owner_idd2,Switch_type4,status_park,pricce,
                this.finlatiude,this.finlongat ,parcodeurl );
        refrence2.child(owner_idd2).child(spot_id2).setValue(new_spot);
           // here should be a beautiful dialog
        progressDialog2.dismiss();
           Toast.makeText(getActivity()," spot added successfully!!" , Toast.LENGTH_SHORT).show();
    }// end of saving method without  a picture :)







  public void imagemethod(){
        // first checking the premission
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

               if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
               == PackageManager.PERMISSION_DENIED){
                        // permission is not granted, gonna request it
                   String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                   // show a popup for premission
                  requestPermissions(permissions, PREMISSION_CODE );
                  

               }// end of inner if

               else {
                   // premission already granted
                     Pickimagefromgallery();
               }// end of inner eles

           }// end of if

      else {
          // mobile os is lesser than marshememloww
               Pickimagefromgallery();
           }// end of outer else

  }// end of image method

    private void Pickimagefromgallery() {

        Intent Iintent = new Intent(Intent.ACTION_PICK);   // create intent to pick image :)
        Iintent.setType("image/*"); // taking all the images type
        startActivityForResult(Iintent ,IMAGE_PICK_CODE); //forwardinng the owner to the pictures page
    }// end of pick image from gallery method              //* gallery*

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
       // HANDLING THE PICKED IMAGE


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PREMISSION_CODE:{
                if (grantResults.length >0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED){
                    // premission was granted
                    Pickimagefromgallery();
                }// end of if
                else {
                    // premission was denied
                    Toast.makeText(getActivity(),"permission was denied :(" ,Toast.LENGTH_SHORT).show();

                }// end of else
            }// END OF CASE 1

        }// end of switch

    }// end of on request premission result method

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
         if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE ){
             //uri object that take the url of the picked picture
             imageuri = data.getData();// taking the image url
             uploadpicturee();  // to upload the image in firebase storage
         }// END OF IF
        super.onActivityResult(requestCode, resultCode, data);
    }




    public void saving_finalYes_pic() throws IOException, WriterException {

        FirebaseDatabase rootNode2;
        DatabaseReference refrence2;
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        rootNode2 = FirebaseDatabase.getInstance();
        refrence2 = rootNode2.getReference("ParkingSpot");
        rootNode2 = FirebaseDatabase.getInstance();
        refrence2 = rootNode2.getReference("ParkingSpot");
        String owner_idd2 = fAuth.getUid();
        String Switch_type4;

        //getting the values from the inputs fields
        if (switch_type.isChecked()== true){
            Switch_type4 = "Indoor"; }//if first
        else{ Switch_type4 ="Outdoor"; }//else
        String status_park =" ";
        String pricce = (String) pricetext.getText().toString().trim();
         spot_id2 = refrence2.push().getKey(); // created an new id to the parking spot
          generateparcode(); //called generate par code
        Parkingspot new_spot2 = new Parkingspot(spot_id2,owner_idd2,Switch_type4,status_park,pricce,
                this.finlatiude,this.finlongat, pic_url , parcodeurl);
        //saving it in firebase
        refrence2.child(owner_idd2).child(spot_id2).setValue(new_spot2);
        progressDialog2.dismiss();
        Toast.makeText(getActivity() , " spot got added succefuly" , Toast.LENGTH_SHORT).show();
    }// end of  method


    // method to upload in firebase storage
    private void uploadpicturee() {

        // first checking if the imGE URI is not null
        if (imageuri != null ) {

            // new storage ref
            StorageReference fileref = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageuri));
            fileref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               Model model = new Model(uri.toString());// creating object model to save the url of picture
                              pic_url = model.getImage_Url();
                               Toast.makeText(getActivity(),"picture uploaded succeessfuly :)",Toast.LENGTH_SHORT).show();

                               try {
                                   saving_finalYes_pic(); // we callled to save the park with picture url
                               } catch (IOException | WriterException e) {
                                  e.printStackTrace();
                               }

                           }
                       });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getActivity(), "uploading failed!",Toast.LENGTH_SHORT ).show();
                }
            });




        }// end if
        else{
            Toast.makeText(getActivity(), "please select image!", Toast.LENGTH_SHORT).show();
        }// end of else

    }// end of upload method

    private String getFileExtension(Uri imageuri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageuri));
    }//endof getfileextension method





}//class
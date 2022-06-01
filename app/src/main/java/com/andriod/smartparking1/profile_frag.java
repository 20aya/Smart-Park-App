package com.andriod.smartparking1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_frag extends Fragment {
TextView bedite;
    FirebaseAuth fAuth ;
    Button b65 ;// for logout button

    TextView textUsername2 , textUsername23 ,textUserEmail2, textUserPhone2 ;
    //to fetch data
    FirebaseAuth fAuth55;
    FirebaseUser user55;
    TextView txtchengepass;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static profile_frag newInstance(String param1, String param2) {
        profile_frag fragment = new profile_frag();
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


        }//saved




    }//oncreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_frag, container, false);
        b65 = (Button) view.findViewById(R.id.button3);

        // handling logout button
        b65.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "Logout Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), Login_page.class));
                getActivity().finish();
               // getActivity().finish();
            }
        });




        // intalizing text vies to show user infoo
        textUsername2 = view.findViewById(R.id.textViewhar17);
        textUsername23 = view.findViewById(R.id.textViewhar19);
        textUserEmail2 = view.findViewById(R.id.textViewhar13);
        textUserPhone2 = view.findViewById(R.id.textViewhar21);
        bedite = view.findViewById(R.id.textViewhar23);

        bedite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent221= new Intent(getActivity(),editprofile.class);
                intent221.putExtra("name" , textUsername23.getText().toString());
                intent221.putExtra("phone" ,textUserPhone2.getText().toString());
                intent221.putExtra("email" , textUserEmail2.getText().toString());
                startActivity(intent221);



            }
        });

        //taking the values from firebase

        DatabaseReference refrence55 ;
        fAuth55=FirebaseAuth.getInstance();
        user55= fAuth55.getCurrentUser();
        refrence55 = FirebaseDatabase.getInstance().getReference().child("Users").child(user55.getUid());

        refrence55.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String name44 = snapshot.child("namme").getValue().toString();
                String emaill44 = snapshot.child("emaill").getValue().toString();
                String phone44 = snapshot.child("phonenum").getValue().toString();

                textUserEmail2.setText(emaill44);
                textUsername2.setText(name44);
                textUsername23.setText(name44);
                textUserPhone2.setText(phone44);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }



            });

        return view;}// end of view method


}// classs
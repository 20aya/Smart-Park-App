package com.andriod.smartparking1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link owner_prof#newInstance} factory method to
 * create an instance of this fragment.
 */
public class owner_prof extends Fragment {
    Button b777; // logout button
    TextView textUsername1 , textUsername2 ,textUserEmail, textUserPhone , editprof;
    //to fetch data
    FirebaseAuth fAuth44;
    FirebaseUser user44;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public owner_prof() {
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
    public static owner_prof newInstance(String param1, String param2) {
        owner_prof fragment = new owner_prof();
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
        View view =  inflater.inflate(R.layout.fragment_owner_prof, container, false);
        b777 = (Button) view.findViewById(R.id.button4);
        textUsername1 = view.findViewById(R.id.textView17);
        textUsername2 = view.findViewById(R.id.textView19);
        textUserEmail = view.findViewById(R.id.textView13);
        textUserPhone = view.findViewById(R.id.textView21);
        editprof= view.findViewById(R.id.textView23);

        editprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent221= new Intent(getActivity(),editprofile.class);
                intent221.putExtra("name" , textUsername2.getText().toString());
                intent221.putExtra("phone" , textUserPhone.getText().toString());
                intent221.putExtra("email" , textUserEmail.getText().toString());
                startActivity(intent221);



            }
        });
        //HANDLING THE LOGOUT BUTTON
        b777.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "Logout Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), Login_page.class));
                getActivity().finish();

            }
        });

        DatabaseReference refrence44 ;
        fAuth44=FirebaseAuth.getInstance();
        user44= fAuth44.getCurrentUser();
        refrence44 = FirebaseDatabase.getInstance().getReference().child("Users").child(user44.getUid());

        refrence44.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String name44 = snapshot.child("namme").getValue().toString();
                String emaill44 = snapshot.child("emaill").getValue().toString();
                String phone44 = snapshot.child("phonenum").getValue().toString();

                textUserEmail.setText(emaill44);
                textUsername1.setText(name44);
                textUsername2.setText(name44);
                textUserPhone.setText(phone44);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });









        return view;

    }//oncreate view




}//class
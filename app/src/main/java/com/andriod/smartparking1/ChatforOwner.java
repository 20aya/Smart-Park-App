package com.andriod.smartparking1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andriod.smartparking1.Adapter.MessageAdapter;
import com.andriod.smartparking1.Classes.Messages;
import com.andriod.smartparking1.Classes.Users_O;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatforOwner extends AppCompatActivity {
    String recivername, reciverid;
    ImageView imageView , sendbtn;
    TextView nametxt;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    String userid;
    String senderRoom , reciverRoom;

    EditText messagetxt;
    RecyclerView recyclerViewforowner;
    ArrayList<Messages> messagesArrayList ;
    MessageAdapter Adapter;

    boolean notifyy = false;

    String bodynotif , titlenotify , usertokennotify;
    String resultt ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatfor_owner);

        recivername = getIntent().getStringExtra("name");
        reciverid = getIntent().getStringExtra("userid");
        imageView = findViewById(R.id.profileimagg);
        nametxt = findViewById(R.id.textView92);
        nametxt.setText(recivername);
        sendbtn = findViewById(R.id.sendbtnn);
        messagetxt = findViewById(R.id.txtedit);
        firebaseAuth= FirebaseAuth.getInstance();
        userid = firebaseAuth.getUid();
        senderRoom = userid+ reciverid;
        reciverRoom = reciverid +userid;


        recyclerViewforowner = findViewById(R.id.messageadaptter);
        messagesArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewforowner.setLayoutManager(linearLayoutManager);
        Adapter = new MessageAdapter(ChatforOwner.this,messagesArrayList);
        recyclerViewforowner.setAdapter(Adapter);

        // showing the previous messages for the sender
        DatabaseReference refshow = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom).child("messages");

        refshow.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    System.out.println(messages.getSenderID() + "Firsttt msggggg");
                    messagesArrayList.add(messages);
                    // i retrived the messages and i added them in the arraylist

                }//end for
                Adapter.notifyDataSetChanged();



            }//end on datachange

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });// end reading previois





        ///////handing sending messaging //////////////////////////////

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyy = true ;
                String message = messagetxt.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(getApplicationContext(), "please enter valid message. " , Toast.LENGTH_LONG).show();
                    return;
                }//end if
                else {
                    messagetxt.setText("");
                    //getting the time of message
                    Date date = new Date();
                    Messages messages = new Messages(message, userid, date.getTime());
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages").push()
                            .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override //adding the message in the reciever and sender room
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                            firebaseDatabase.getReference().child("chats").child(reciverRoom).child("messages").push()
                                    .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                }//end on complete
                            });//inner add on complete listener

                            showprevious();
                            //new solution
                            /*
                            String token222 = gettokenss();// token is like a key to work with the user device
                            Toast.makeText(getApplicationContext(), " 11tokens:" + token222 +" reciver: " +reciverid, Toast.LENGTH_SHORT).show();
                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                                    token222, "New Message!", messages.getMessage(),
                                    getApplicationContext(), ChatforOwner.this);

                            notificationsSender.SendNotifications();
*/
                        }//on complete first
                    });//add on complete listener

                    DatabaseReference reftokes = FirebaseDatabase.getInstance().getReference("Tokens");
                    reftokes.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                if (reciverid.equals(dataSnapshot.getKey())){
                                    resultt = dataSnapshot.child("token").getValue().toString();

                                }


                            }//end for
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                    //new solution

                    String token222 = resultt; // token is like a key to work with the user device
                   //** Toast.makeText(getApplicationContext(), "22tokens:" + token222 +" reciver: " +reciverid, Toast.LENGTH_SHORT).show();
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                            token222, "New Message!", messages.getMessage(),
                            getApplicationContext(), ChatforOwner.this);

                    notificationsSender.SendNotifications();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                }//else





            }//end on clcik
        });//end handling send button




        /////////////////////////////tokens///////////////////



    }//end on create





    public String gettokenss(){
      resultt= "k";
        DatabaseReference reftokes = FirebaseDatabase.getInstance().getReference("Tokens");
        reftokes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (reciverid.equals(dataSnapshot.getKey())){
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



    public void  addingchatone(){





                Adapter.notifyDataSetChanged();








    }//end addingc chat


    public  void showprevious(){
        //////////////////showing previous messages
        DatabaseReference refshow = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom).child("messages");

        refshow.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Messages messages = dataSnapshot.getValue(Messages.class);
                    System.out.println(messages.getSenderID() + "Firsttt msggggg");
                    messagesArrayList.add(messages);


                }//end for
                Adapter.notifyDataSetChanged();



            }//end on datachange

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });// end reading previois




    }//end previous
}//end on class
package com.andriod.smartparking1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andriod.smartparking1.Classes.Messages;
import com.andriod.smartparking1.R;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context ;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND =1;
    int ITEM_RECIVE =2;

FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
String userid;
    public MessageAdapter(Context context,ArrayList <Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
        this.userid= firebaseAuth.getUid();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item, parent,false);
            return new senderViewholder(view);

        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_layout_item, parent,false);
            return new ResiverViewholder(view);

        }

    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = (messagesArrayList.get(position));
        if (userid.equals(messages.getSenderID())) {
            return ITEM_SEND;
        }else{
            return ITEM_RECIVE;
        }
    }





    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
      Messages message = messagesArrayList.get(position);
      if ( holder.getClass()==senderViewholder.class){
          senderViewholder viewholder=(senderViewholder) holder;
          viewholder.tv1.setText(message.getMessage());


      }else {
          ResiverViewholder viewholder=(ResiverViewholder) holder;
          viewholder.tv.setText(message.getMessage());

      }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }


    class senderViewholder extends RecyclerView.ViewHolder{
        ImageView image1;
        TextView tv1;

        public senderViewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            image1 = itemView.findViewById(R.id.profile_image);
            tv1 = itemView.findViewById(R.id.ayann);
            userid = firebaseAuth.getUid();
            System.out.println(userid + "construccterrrrrrr");
        }
    }
    class ResiverViewholder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView tv;


        public ResiverViewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_imageaya);
            tv = itemView.findViewById(R.id.ayaaa);
        }
    }

}

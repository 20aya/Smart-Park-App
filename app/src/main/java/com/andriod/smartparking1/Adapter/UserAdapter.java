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

import com.andriod.smartparking1.ChatforOwner;
import com.andriod.smartparking1.R;
import com.andriod.smartparking1.Classes.Users_O;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder33> {
    Context context;
    ArrayList <Users_O> arrayListusers;

    public UserAdapter(Context context, ArrayList<Users_O> arrayListusers) {
        this.context = context;
        this.arrayListusers = arrayListusers;
    }//end constructer

    public UserAdapter.Viewholder33 onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view2 = LayoutInflater.from(context).inflate(R.layout.item_user_row,parent,false);
        // handling the edit button

        return  new UserAdapter.Viewholder33(view2);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder33 holder, int position) {

           Users_O users_o = arrayListusers.get(position);

           System.out.println(users_o.getNamme());
            holder.textname.setText(users_o.getNamme());
           holder.textstat.setText("Hey there im renting a park spot. ");
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context , ChatforOwner.class);
                   //key
                   intent.putExtra("name", users_o.getNamme());
                   intent.putExtra("userid" , users_o.getUid());

                   context.startActivity(intent);
               }//end on click
           });// end itemview on click listenrer

    }//end onbindviewholder

    @Override
    public int getItemCount() {

        return this.arrayListusers.size();
    }
    public static class Viewholder33 extends RecyclerView.ViewHolder{
        TextView textstat,textname;
        ImageView imageView;



        public Viewholder33(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img456321);
             textname = itemView.findViewById(R.id.username);
            textstat = itemView.findViewById(R.id.usersatas);

            // System.out.println("UYTUYTUYTUYTUTYTUTYUTTYTUTYUTYTUTYTUTYTUYTUTYTUTYTUTYTUTTUYTUTYUTUTYUTTUUTUTUTUTUU");
        }

    }//class
}

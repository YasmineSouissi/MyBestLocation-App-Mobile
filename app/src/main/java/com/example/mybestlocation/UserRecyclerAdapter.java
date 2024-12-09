package com.example.mybestlocation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybestlocation.ui.home.MapsActivity;

import java.util.ArrayList;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {

    private Context context;
    private ArrayList<User> userList;


    public UserRecyclerAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.emailTextView.setText(user.getEmail());
        holder.phoneTextView.setText(user.getPhoneNumber());
        int id=user.getId();

        // Ajouter une action au bouton btn_dial
        holder.btnDial.setOnClickListener(v -> {
            String phoneNumber = user.getPhoneNumber();
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(dialIntent);
        });

        holder.btn_location.setOnClickListener(v -> {
            Intent intent = new Intent(context, MapsActivity.class);

            intent.putExtra("user_id", user.id);
            Log.d("MapsActivity", "User ID sent: " + id);
            intent.putExtra("user_name", id);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, phoneTextView;
        ImageView btnDial,btn_location;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_name);
            emailTextView = itemView.findViewById(R.id.tv_email);
            phoneTextView = itemView.findViewById(R.id.tv_phone);
            btnDial = itemView.findViewById(R.id.iv_dial);
            btn_location=itemView.findViewById(R.id.iv_location);
        }
    }
}

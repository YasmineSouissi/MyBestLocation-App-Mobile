package com.example.mybestlocation.ui.home;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybestlocation.Position;
import com.example.mybestlocation.R;

import java.util.ArrayList;

public class Account extends AppCompatActivity {
    ImageView img_back;
    Button btn_locations,btn_contacts;
    ArrayList<Position> data = new ArrayList<>();
    TextView tv_name,tv_mail,tv_num;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account); // Ajoutez cette ligne

        img_back = findViewById(R.id.img_back);
        btn_locations = findViewById(R.id.btn_loc);
        btn_contacts= findViewById(R.id.button_contacts_ac);
        tv_name = findViewById(R.id.tv_nom_acc);
        tv_mail = findViewById(R.id.tv_mail_acc);
        tv_num = findViewById(R.id.tv_numero_acc);


        // Récupérer les données de l'intent
        Intent intent_i = getIntent();
        String username = intent_i.getStringExtra("name");
        String email = intent_i.getStringExtra("email");
        String phone = intent_i.getStringExtra("phonenumber");
        int user_id = intent_i.getIntExtra("id",0);
        Log.d("Account", "Info recues de main " + username+user_id+email);


        tv_name.setText(username);
        tv_mail.setText(email);
        tv_num.setText(phone);


        btn_locations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Account.this, Locations.class);
                startActivity(intent);
            }
        });
        btn_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Account.this, Contacts.class);
//                startActivity(intent);
                Intent intent = new Intent(Account.this, Contacts.class);
               startActivity(intent);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account.this.finish(); // Ferme l'activité
            }
        });
    }


}
package com.example.mybestlocation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.mybestlocation.ui.home.Account;
import com.example.mybestlocation.ui.home.Contacts;
import com.example.mybestlocation.ui.home.HomeFragment;
import com.example.mybestlocation.ui.home.Locations;
import com.example.mybestlocation.ui.home.Login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybestlocation.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupérer les informations de l'utilisateur passées par l'Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phonenumber = intent.getStringExtra("phonenumber");
        int userId = intent.getIntExtra("id", 0);
        Log.d("MainActivity", "Info recues " + username+userId);


        // Créer un nouveau fragment et passer les données
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", username);
        bundle.putString("email", email);
        bundle.putString("phonenumber", phonenumber);
        Log.d("MainActivity", "Info envoye a home usesrid" +userId);
        bundle.putInt("id", userId);
        Log.d("MainActivity", "Info envoye a home " + username+userId+bundle.getString("id"));

        homeFragment.setArguments(bundle);

        // Afficher le fragment dans le FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();

        FloatingActionButton btnAccount = findViewById(R.id.btn_account_ic);
        // Définir le OnClickListener
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, Account.class);
                intent1.putExtra("name", username);
                intent1.putExtra("email", email);
                intent1.putExtra("phonenumber", phonenumber);
                intent1.putExtra("id", userId);

                startActivity(intent1);

            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        FloatingActionButton btnContacts = findViewById(R.id.btn_contact_ic);
        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, Contacts.class);
                intent1.putExtra("name", username);
                intent1.putExtra("email", email);
                intent1.putExtra("phonenumber", phonenumber);
                intent1.putExtra("id", userId);

                startActivity(intent1);
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        FloatingActionButton btnLogout = findViewById(R.id.btn_logout_home);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        FloatingActionButton btnLocations = findViewById(R.id.btn_locations_ic);
        btnLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, Locations.class);
                intent1.putExtra("name", username);
                intent1.putExtra("email", email);
                intent1.putExtra("phonenumber", phonenumber);
                intent1.putExtra("id", userId);

                startActivity(intent1);
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
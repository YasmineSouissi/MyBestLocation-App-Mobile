package com.example.mybestlocation.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybestlocation.Config;
import com.example.mybestlocation.JSONParser;
import com.example.mybestlocation.R;
import com.example.mybestlocation.User;
import com.example.mybestlocation.UserRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserRecyclerAdapter adapter;
    private ArrayList<User> userData = new ArrayList<>();
    private ImageView btn_back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // Initialisation du RecyclerView et de l'adaptateur
        recyclerView = findViewById(R.id.recycler_view_contacts);
        btn_back = findViewById(R.id.img_back_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserRecyclerAdapter(this, userData);
        recyclerView.setAdapter(adapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Récupérer les données de l'intent
        Intent intent_i = getIntent();
        String username = intent_i.getStringExtra("name");
        String email = intent_i.getStringExtra("email");
        String phone = intent_i.getStringExtra("phonenumber");
        int user_id = intent_i.getIntExtra("id",0);
        Log.d("Contacts", "Info recues de main " + username+user_id+email);

        // Lancer le téléchargement
        new Download().execute();
    }

    // Classe interne pour le téléchargement des utilisateurs
    public class Download extends AsyncTask<Void, Void, Void> {
        private static final String TAG_DOWNLOAD = "UserDownloadTask";
        AlertDialog alertDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG_DOWNLOAD, "onPreExecute: Démarrage du téléchargement des utilisateurs");

            // Afficher une boîte de dialogue de chargement
            AlertDialog.Builder dial = new AlertDialog.Builder(Contacts.this);
            dial.setTitle("Téléchargement");
            dial.setMessage("Veuillez patienter...");
            alertDialog = dial.create();
            alertDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG_DOWNLOAD, "doInBackground: Début du téléchargement des données utilisateurs");
            JSONParser jsonParser = new JSONParser();
            JSONObject response = jsonParser.makeRequest(Config.URL_get_all_users);

            try {
                if (response != null) {
                    int success = response.getInt("success");
                    if (success > 0) {
                        JSONArray userArray = response.getJSONArray("users");
                        for (int i = 0; i < userArray.length(); i++) {
                            JSONObject userObject = userArray.getJSONObject(i);
                            int id = userObject.getInt("id");
                            String name = userObject.getString("name");
                            String email = userObject.getString("email");
                            String phoneNumber = userObject.getString("phone_number");
                            String password = userObject.getString("password");
                            userData.add(new User(id,name, email, phoneNumber, password));
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG_DOWNLOAD, "doInBackground: Erreur JSON", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG_DOWNLOAD, "onPostExecute: Téléchargement terminé");

            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }

            // Mise à jour des données de l'adaptateur
            adapter.notifyDataSetChanged();
        }
    }
}

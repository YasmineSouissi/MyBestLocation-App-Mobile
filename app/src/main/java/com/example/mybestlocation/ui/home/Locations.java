package com.example.mybestlocation.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybestlocation.Config;
import com.example.mybestlocation.JSONParser;
import com.example.mybestlocation.Position;
import com.example.mybestlocation.PositionRecyclerAdapter;
import com.example.mybestlocation.PositionServices;
import com.example.mybestlocation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Locations extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PositionRecyclerAdapter adapter;
    private ArrayList<Position> data = new ArrayList<>();
    private PositionServices positionServices= new PositionServices();
    private ImageView btn_back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        // Initialiser RecyclerView
        recyclerView = findViewById(R.id.recycler_view_locations);
        btn_back=findViewById(R.id.img_back_locations);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PositionRecyclerAdapter(this, data);
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
        Log.d("Locations", "Info recues de main " + username+user_id+email);

        // Lancer le téléchargement des données
        new Download().execute();
    }

    // Classe interne Download pour effectuer la tâche AsyncTask
    public class Download extends AsyncTask<Void, Void, Void> {
        private static final String TAG_DOWNLOAD = "DownloadTask";
        AlertDialog alertDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG_DOWNLOAD, "onPreExecute: Démarrage de la tâche de téléchargement des positions");

            // Afficher la boîte de dialogue
            AlertDialog.Builder dial = new AlertDialog.Builder(Locations.this);
            dial.setTitle("Téléchargement");
            dial.setMessage("Veuillez patienter...");
            alertDialog = dial.create();
            alertDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG_DOWNLOAD, "doInBackground: Début du téléchargement des données");
            JSONParser jsonParser = new JSONParser();
            JSONObject response = jsonParser.makeRequest(Config.URL_get_all);
            Log.d(TAG_DOWNLOAD, "doInBackground: Réponse reçue: " + response);

            try {
                if (response != null) {
                    int success = response.getInt("success");
                    if (success > 0) {
                        JSONArray tabposition = response.getJSONArray("positions");
                        for (int i = 0; i < tabposition.length(); i++) {
                            JSONObject ligne = tabposition.getJSONObject(i);
                            int idp = ligne.getInt("idposition");
                            Double longitude = ligne.getDouble("longitude");
                            Double latitude = ligne.getDouble("latitude");
                            String pseudo = ligne.getString("pseudo");
                            data.add(new Position(idp, longitude, latitude, pseudo));
                            Log.d(TAG_DOWNLOAD, "doInBackground: Position ajoutée: " + pseudo);
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG_DOWNLOAD, "doInBackground: Erreur lors de l'analyse JSON", e);
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

            // Notifier l'adaptateur que les données ont changé
            adapter.notifyDataSetChanged();
        }
    }
}

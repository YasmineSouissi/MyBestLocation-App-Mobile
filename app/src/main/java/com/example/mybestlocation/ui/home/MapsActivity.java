package com.example.mybestlocation.ui.home;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybestlocation.Config;
import com.example.mybestlocation.JSONParser;
import com.example.mybestlocation.Position;
import com.example.mybestlocation.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private int userId;
    private double latitude, longitude;
    private GoogleMap mMap;
    private int positionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Récupérer l'ID de l'utilisateur passé via l'intent
        userId = getIntent().getIntExtra("user_id", -1);
        Log.d("MapsActivity from contacts", "User ID received: " + userId);
        positionId = getIntent().getIntExtra("position_id", -1);
        Log.d("MapsActivity from locations", "position ID received: " + positionId);

        // Vérifier si l'ID de l'utilisateur est valide
        if (userId != -1) {
            // Lancer la tâche AsyncTask pour récupérer la position
            new GetUserPositionTask().execute(userId);
        }

        if (positionId != -1) {
            // Lancer la tâche AsyncTask pour récupérer la position
            new GetPositionTask().execute(positionId);
        }


        // Initialiser la carte
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    // Callback de la carte lorsque la carte est prête à être utilisée
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    // AsyncTask pour récupérer la position de l'utilisateur
    private class GetUserPositionTask extends AsyncTask<Integer, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "Fetching user position...");
        }

        @Override
        protected JSONObject doInBackground(Integer... params) {
            int userId = params[0];

            // Effectuer la requête pour récupérer la position de l'utilisateur
            JSONParser jsonParser = new JSONParser();
            Log.d("MapsActivity", "User ID: " + userId);

            String url = Config.URL_get_position_by_user + "?user_id=" + userId;

            // Effectuer la requête HTTP et récupérer la réponse
            return jsonParser.makeRequest(url);
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);

            // Vérifier si la réponse est nulle
            if (response == null) {
                Log.e(TAG, "Response is null");
                Toast.makeText(MapsActivity.this, "Failed to get user position", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Vérifier la réussite de la réponse
                int success = response.getInt("success");

                if (success == 1) {
                    // Récupérer les données de la position
                    JSONObject positionData = response.getJSONObject("position");
                    latitude = positionData.getDouble("latitude");
                    longitude = positionData.getDouble("longitude");

                    Log.d(TAG, "Position retrieved: Lat = " + latitude + ", Lon = " + longitude);

                    // Ajouter un marqueur à la position récupérée
                    LatLng userLocation = new LatLng(latitude, longitude);
                    if (mMap != null) {
                        mMap.addMarker(new MarkerOptions().position(userLocation).title("User Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                    }

                } else {
                    Log.e(TAG, "No position found for user ID: " + userId);
                    Toast.makeText(MapsActivity.this, "No position found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing JSON", e);
                Toast.makeText(MapsActivity.this, "Error fetching position", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class GetPositionTask extends AsyncTask<Integer, Void, Position> {

        private static final String TAG = "GetPositionTask";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "Fetching position...");
        }

        @Override
        protected Position doInBackground(Integer... params) {
            int positionId = params[0]; // Récupérer l'ID de l'utilisateur passé en paramètre

            // URL de l'API qui récupère la position de l'utilisateur
            String url = Config.URL_get_position + "?position_id=" + positionId;
            Log.d(TAG, "URL: " + url);

            // Créer une instance de JSONParser pour envoyer la requête et récupérer la réponse
            JSONParser jsonParser = new JSONParser();

            // Effectuer la requête HTTP
            JSONObject response = jsonParser.makeRequest(url);

            // Vérifier la réponse
            if (response != null) {
                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        // Extraire les données de latitude et longitude
                        JSONObject positionData = response.getJSONObject("position");
                        double latitude = positionData.getDouble("latitude");
                        double longitude = positionData.getDouble("longitude");
                        String pseudo=positionData.getString("pseudo");
                        // Retourner un objet Position
                        return new Position(longitude,latitude,pseudo);
                    } else {
                        Log.e(TAG, "No position found for position ID: " + userId);
                        return null;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON", e);
                    return null;
                }
            } else {
                Log.e(TAG, "Response is null");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Position position) {
            super.onPostExecute(position);

            // Vérifier si la position est nulle
            if (position == null) {
                Toast.makeText(MapsActivity.this, "Failed to get position", Toast.LENGTH_SHORT).show();
                return;
            }

            // Récupérer latitude et longitude de l'objet Position
            double latitude = position.getLatitude();
            double longitude = position.getLongitude();

            Log.d(TAG, "Position retrieved: Lat = " + latitude + ", Lon = " + longitude);

            // Ajouter un marqueur à la position récupérée sur la carte
            LatLng userLocation = new LatLng(latitude, longitude);
            if (mMap != null) {
                mMap.addMarker(new MarkerOptions().position(userLocation).title("position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            }
        }
    }

}

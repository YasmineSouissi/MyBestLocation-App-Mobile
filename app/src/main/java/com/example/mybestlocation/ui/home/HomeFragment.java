package com.example.mybestlocation.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.mybestlocation.MainActivity;
import com.example.mybestlocation.Position;
import com.example.mybestlocation.PositionServices;
import com.example.mybestlocation.R;
import com.example.mybestlocation.databinding.FragmentHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "HomeFragment";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final long REFRESH_INTERVAL = 30000; // 30 secondes

    MapView mapView;
    FragmentHomeBinding binding;
    Position userPosiiton;
    private String username;
    private String email;
    private int userId_recue;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private Handler locationUpdateHandler;
    private Runnable locationUpdateRunnable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Récupérer les arguments passés depuis MainActivity
        if (getArguments() != null) {
            username = getArguments().getString("name", "Unknown User");
            email = getArguments().getString("email", "Unknown Email");
            userId_recue = getArguments().getInt("id", 0);
            Log.d(TAG, "onCreateView: Received username: " + username);
            Log.d(TAG, "onCreateView: Received email: " + email);
        }

        // Initialisation de la carte
        try {
            Log.d(TAG, "MapsInitializer: Initializing...");
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, "MapsInitializer: Error", e);
            e.printStackTrace();
        }

        // Initialisation de MapView
        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); // Obtenir la carte

        // Initialisation de FusedLocationProviderClient pour obtenir la position actuelle
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialisation du Handler pour mettre à jour la position périodiquement
        locationUpdateHandler = new Handler();

        // Créer le Runnable pour la mise à jour périodique
        locationUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                updateLocation();  // Mettre à jour la position
                locationUpdateHandler.postDelayed(this, REFRESH_INTERVAL);  // Réexécuter après 30 secondes
            }
        };

        return root;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Vérifier si la permission de localisation est accordée
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            updateLocation(); // Mettre à jour la position dès que la carte est prête
        } else {
            // Si la permission n'est pas accordée, demander la permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

        }
        // Ajouter un écouteur de clic sur la carte
        googleMap.setOnMapClickListener(latLng -> {
            // Ouvrir la fenêtre de dialogue pour enregistrer la position
            showSaveLocationDialog(latLng);
        });

    }
    private void showSaveLocationDialog(LatLng latLng) {

        // Créer un LayoutInflater pour gonfler le fichier XML
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Gonfler le layout dialog_save_location.xml
        View dialogView = inflater.inflate(R.layout.dialog_save_location, null);
        // Récupérer l'EditText depuis le layout inflaté
        EditText input = dialogView.findViewById(R.id.editTextLocationName);

        // Créer une fenêtre de dialogue
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Save Location")
                .setMessage("Enter a name for the location")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    // Récupérer le nom de la position saisi
                    String locationName = input.getText().toString();

                    if (!locationName.isEmpty()) {
                        // Ajouter un marqueur à la carte
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(locationName));

                        // Sauvegarder la position dans la base de données
                        new AddPositionTask(locationName, latLng.latitude, latLng.longitude, userId_recue).execute();
                    } else {
                        Toast.makeText(requireContext(), "Please enter a location name", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void updateLocation() {
        // Utilisez requireContext() pour obtenir le contexte à partir du fragment
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si la permission n'est pas accordée, demandez la permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Récupérer la dernière position connue
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        String pseudo = "current position of: " + username;
                        userPosiiton = new Position(location.getLongitude(), location.getLatitude(), pseudo, userId_recue);

                        // Ajouter ou mettre à jour un marqueur à la position actuelle
                        googleMap.clear(); // Clear previous markers
                        googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));

                        // Déplacer la caméra à la position actuelle et zoomer
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                        // Appel pour envoyer la position sur le serveur
                        new AddPositionTask(pseudo, location.getLatitude(), location.getLongitude(), userId_recue).execute();
                    } else {
                        Toast.makeText(requireContext(), "Failed to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        locationUpdateHandler.postDelayed(locationUpdateRunnable, REFRESH_INTERVAL); // Démarrer le rafraîchissement périodique
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        locationUpdateHandler.removeCallbacks(locationUpdateRunnable); // Arrêter le rafraîchissement périodique
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        locationUpdateHandler.removeCallbacks(locationUpdateRunnable); // Enlever le callback
        binding = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocation(); // Permission accordée, récupérer la position actuelle
            } else {
                Toast.makeText(requireContext(), "Permission denied. Unable to access location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AddPositionTask extends AsyncTask<Void, Void, Boolean> {
        private final String pseudo;
        private final double latitude;
        private final double longitude;
        private final int userid;

        public AddPositionTask(String pseudo, double latitude, double longitude, int userid_p) {
            this.pseudo = pseudo;
            this.latitude = latitude;
            this.longitude = longitude;
            this.userid = userid_p;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                PositionServices positionServices = new PositionServices();
                return positionServices.addPosition(pseudo, latitude, longitude, userid);
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(getContext(), "Position ajoutée avec succès !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Échec de l'ajout de la position.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

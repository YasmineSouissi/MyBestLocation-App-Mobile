package com.example.mybestlocation;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class UserServices {

    public UserServices() {}

    public boolean addUser(User newUser) {
        boolean isSuccess = false;
        JSONParser jsonParser = new JSONParser();

        // Construire l'URL avec les paramètres nécessaires (POST)
        String url = Config.URL_add_user+"?name="+newUser.getName()+"&email="+newUser.getEmail()+"&phone_number="+newUser.getPhoneNumber()+"&password="+newUser.getPassword();

        // Créer un objet JSON pour envoyer les données au serveur
        JSONObject userParams = new JSONObject();
        try {

            // Effectuer la requête POST
            JSONObject response = jsonParser.makeRequest(url);

            // Vérifier la réponse
            if (response != null) {
                int success = response.getInt("success");
                if (success == 1) {
                    Log.d("USER_UPLOAD", "User added successfully.");
                    isSuccess = true;
                } else {
                    Log.d("USER_UPLOAD", "Failed to add user: " + response.getString("message"));
                }
            }
        } catch (Exception e) {
            Log.e("USER_UPLOAD", "Error adding user", e);
        }

        return isSuccess;
    }


    public User get_user_by_email(String emailParam) {
        User user = null;  // Initialise l'utilisateur comme null pour gérer les cas où aucun utilisateur n'est trouvé
        JSONParser jsonParser = new JSONParser();

        // Construire l'URL avec l'email en paramètre (GET)
        String urlWithEmail = Config.URL_get_user_by_email + "?email=" + emailParam;

        // Effectuer la requête
        JSONObject response = jsonParser.makeRequest(urlWithEmail);

        try {
            if (response != null) {
                int success = response.getInt("success");

                // Si l'utilisateur est trouvé
                if (success > 0) {
                    JSONObject userObject = response.getJSONObject("user");

                    // Récupérer les informations de l'utilisateur
                    int id = userObject.getInt("id");
                    String name = userObject.getString("name");
                    String email = userObject.getString("email");
                    String password = userObject.getString("phone_number");
                    String phoneNumber = userObject.getString("password");

                    // Créer l'objet User
                    user = new User(id,name, email, phoneNumber, password);
                    Log.d("USER_DOWNLOAD", "Utilisateur récupéré: id : "+user.getId() + name);
                    Log.d("USER_DOWNLOAD", "mdp récupéré: " + phoneNumber);
                } else {
                    Log.d("USER_DOWNLOAD", "Aucun utilisateur trouvé avec cet email");
                }
            }
        } catch (JSONException ex) {
            Log.e("USER_DOWNLOAD", "Erreur lors de l'analyse JSON", ex);
        }

        return user; // Retourner l'utilisateur ou null si aucun n'est trouvé
    }
}

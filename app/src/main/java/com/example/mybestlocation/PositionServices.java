    package com.example.mybestlocation;

    import android.util.Log;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.HashMap;

    public class PositionServices {
        public PositionServices() {}
        private static final String TAG = "POSITION_SERVICES";

        /**
         * Supprime une position en fonction de son ID.
         *
         * @param positionId l'ID de la position à supprimer
         * @return true si la position a été supprimée avec succès, false sinon
         */
        public boolean deletePosition(int positionId) {
            JSONParser jsonParser = new JSONParser();
            String url = Config.URL_delete_position + "?id=" + positionId;

            JSONObject response = jsonParser.makeRequest(url);
            if (response == null) {
                Log.e("POSITION_SERVICES", "Réponse nulle du serveur");
                return false;
            }

            try {
                int success = response.getInt("success");
                return success == 1;
            } catch (JSONException e) {
                Log.e("POSITION_SERVICES", "Erreur JSON lors de la suppression", e);
                return false;
            }
        }
        /**
         * Récupère la position d'un utilisateur par son ID.
         *
         * @param userId L'ID de l'utilisateur
         * @return L'objet Position de l'utilisateur ou null si la récupération échoue.
         */
        public Position getPositionByUser(int userId) {
            JSONParser jsonParser = new JSONParser();
            String url = Config.URL_get_position_by_user + "?user_id=" + userId;

            // Effectuer la requête HTTP
            JSONObject response = jsonParser.makeRequest(url);
            if (response == null) {
                Log.e("POSITION_SERVICES", "Réponse nulle du serveur");
                return null;
            }

            try {
                int success = response.getInt("success");
                if (success == 1) {
                    // Récupérer les données de la position depuis la réponse JSON
                    JSONObject positionData = response.getJSONObject("position");
                    int idPosition = positionData.getInt("idposition");
                    String pseudo = positionData.getString("pseudo");
                    double latitude = positionData.getDouble("latitude");
                    double longitude = positionData.getDouble("longitude");

                    // Créer un objet Position avec les données récupérées
                    return new Position(idPosition, longitude, latitude, pseudo);
                } else {
                    Log.e("POSITION_SERVICES", "Aucune position trouvée pour l'utilisateur avec ID: " + userId);
                    return null;
                }
            } catch (JSONException e) {
                Log.e("POSITION_SERVICES", "Erreur JSON lors de la récupération de la position", e);
                return null;
            }

        }


        /**
         * Ajoute une nouvelle position dans la base de données.
         *
         * @param pseudo    Le pseudo associé à la position.
         * @param latitude  La latitude de la position.
         * @param longitude La longitude de la position.
         * @return true si la position a été ajoutée avec succès, false sinon.
         */
        public boolean addPosition(String pseudo, double latitude, double longitude, int idUser) {
            JSONParser jsonParser = new JSONParser();

            // URL de votre service PHP pour ajouter une position
            String url = Config.URL_add_position;

            // Paramètres envoyés avec la requête POST
            HashMap<String, String> params = new HashMap<>();
            params.put("pseudo", pseudo);
            params.put("latitude", String.valueOf(latitude));
            params.put("longitude", String.valueOf(longitude));
            params.put("userid", String.valueOf(idUser)); // Ajout de l'id_user

            // Effectuer la requête POST avec les paramètres
            JSONObject response = jsonParser.makeRequest(url + "?pseudo=" + pseudo + "&latitude=" + latitude + "&longitude=" + longitude + "&userid=" + idUser);

            // Vérifier la réponse
            if (response == null) {
                Log.e(TAG, "Réponse nulle du serveur lors de l'ajout de la position");
                return false;
            }

            try {
                int success = response.getInt("success");
                return success == 1; // Si le succès est 1, la position a été ajoutée
            } catch (JSONException e) {
                Log.e(TAG, "Erreur JSON lors de l'ajout de la position", e);
                return false;
            }
        }


    }

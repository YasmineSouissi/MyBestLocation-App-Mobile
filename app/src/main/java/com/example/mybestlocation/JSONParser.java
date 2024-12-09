package com.example.mybestlocation;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class JSONParser {

    String charset = "UTF-8";
    HttpURLConnection conn;
    DataOutputStream wr;
    StringBuilder result;
    URL urlObj;
    JSONObject jObj = null;
    StringBuilder sbParams;
    String paramsString;

    public JSONObject makeRequest(String url) {
        BufferedReader reader = null;

        try {
            urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.connect();

            // Lire la réponse
            InputStream in = new BufferedInputStream(conn.getInputStream());
            reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("JSON Parser", "result: " + result.toString());

            // Traitement pour extraire le JSON du texte de la réponse
            String response = result.toString();
            // Vérifier si la réponse contient un JSON valide
            if (response != null && response.contains("{")) {
                // Chercher la première accolade '{' et commencer la chaîne à partir de là
                String jsonString = response.substring(response.indexOf("{"));
                // Conversion de la chaîne en JSONObject
                jObj = new JSONObject(jsonString);
            } else {
                Log.e("JSONParser", "La réponse ne contient pas de JSON valide.");
            }

        } catch (IOException | JSONException e) {
            Log.e("JSON Parser", "Erreur lors de la requête ou du parsing: " + e.toString());
        } finally {
            // Fermeture des ressources
            try {
                if (reader != null) reader.close();
                if (conn != null) conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jObj;
    }

    public JSONObject makeHttpRequest(String url, String method, HashMap<String, String> params) {
        sbParams = new StringBuilder();
        if (params != null) {
            int i = 0;
            for (String key : params.keySet()) {
                try {
                    if (i != 0) {
                        sbParams.append("&");
                    }
                    sbParams.append(key).append("=").append(URLEncoder.encode(params.get(key), charset));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }

        BufferedReader reader = null;

        try {
            if (method.equals("POST")) {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", charset);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.connect();

                paramsString = sbParams.toString();
                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();

            } else if (method.equals("GET")) {
                if (sbParams.length() != 0) {
                    url += "?" + sbParams.toString();
                }
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Charset", charset);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.connect();
            }

            // Lire la réponse
            InputStream in = new BufferedInputStream(conn.getInputStream());
            reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("JSON Parser", "result: " + result.toString());

            // Traitement pour extraire le JSON du texte de la réponse
            String response = result.toString();
            // Vérifier si la réponse contient un JSON valide
            if (response != null && response.contains("{")) {
                // Chercher la première accolade '{' et commencer la chaîne à partir de là
                String jsonString = response.substring(response.indexOf("{"));
                // Conversion de la chaîne en JSONObject
                jObj = new JSONObject(jsonString);
            } else {
                Log.e("JSONParser", "La réponse ne contient pas de JSON valide.");
            }

        } catch (IOException | JSONException e) {
            Log.e("JSON Parser", "Erreur lors de la requête ou du parsing: " + e.toString());
        } finally {
            // Fermeture des ressources
            try {
                if (wr != null) wr.close();
                if (reader != null) reader.close();
                if (conn != null) conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jObj;
    }
}

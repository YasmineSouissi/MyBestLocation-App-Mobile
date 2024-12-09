package com.example.mybestlocation.ui.home.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mybestlocation.MainActivity;
import com.example.mybestlocation.R;
import com.example.mybestlocation.User;
import com.example.mybestlocation.UserServices;
import com.example.mybestlocation.ui.home.MapsActivity;

public class SignUp extends AppCompatActivity {

    private EditText edEmail, edPassword, edVerifyPassword, ednom, edphone;
    private Button btnSignUp, btnCancel;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialisation des vues
        edEmail = findViewById(R.id.ed_email_signup);
        ednom = findViewById(R.id.ed_nom_signup);
        edPassword = findViewById(R.id.ed_mdp_signup);
        edVerifyPassword = findViewById(R.id.ed_verfi_mdp_signup);
        edphone = findViewById(R.id.ed_phone_number_signup);

        btnSignUp = findViewById(R.id.btn_sign_up_signup);
        btnCancel = findViewById(R.id.btn_annuler_signup);

        // Ajout d'un événement de clic pour le bouton "Sign Up"
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                String verifyPassword = edVerifyPassword.getText().toString();
                String nom=ednom.getText().toString();
                String phone=edphone.getText().toString();
                // Ici vous pouvez ajouter la logique pour enregistrer l'utilisateur
                // Exemple de validation des champs
                if (email.isEmpty() || password.isEmpty() || verifyPassword.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(verifyPassword)) {
                    Toast.makeText(SignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    User newUser= new User(nom,email,password,phone);
                    new AddUserTask(newUser).execute();
                    Toast.makeText(SignUp.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Ajout d'un événement de clic pour le bouton "Quit"
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retourner à la page précédente ou fermer l'activité
                finish();
            }
        });
    }


    private class AddUserTask extends AsyncTask<Void, Void, Boolean> {
        private final User user;
        private final String TAG="AddUserSignup";

        public AddUserTask(User user) {
            this.user=user;
            Log.d(TAG, "AddUserTask: Initialisation avec name=" + user);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: Préparation pour l'ajout de l'utilisateur");
            Toast.makeText(SignUp.this, "Envoi des informations utilisateur en cours...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: Début de l'appel à UserServices.addUser");
            try {
                UserServices userServices = new UserServices();
                // Appel de la méthode addUser pour ajouter l'utilisateur
                boolean result = userServices.addUser(user);
                this.user.setId(userServices.get_user_by_email(this.user.getEmail()).getId());
                Log.d(TAG, "doInBackground: User final  = " + this.user);

                Log.d(TAG, "doInBackground: Résultat de addUser = " + result);
                return result;
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Erreur lors de l'ajout de l'utilisateur", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                Log.d(TAG, "onPostExecute: Utilisateur ajouté avec succès !");
                Toast.makeText(SignUp.this, "Utilisateur ajouté avec succès !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this, MainActivity.class);

                intent.putExtra("name", this.user.getName());
                intent.putExtra("email", this.user.getEmail());
                intent.putExtra("phonenumber", this.user.getPhoneNumber());
                intent.putExtra("id", this.user.getId());
                Log.d(TAG, "User sent: " + this.user);
                //String username = intent.getStringExtra("name");
                //        String email = intent.getStringExtra("email");
                //        String phonenumber = intent.getStringExtra("phonenumber");
                //        int userId = intent.getIntExtra("id", 0);
                SignUp.this.finish();
                startActivity(intent);
            } else {
                Log.e(TAG, "onPostExecute: Échec de l'ajout de l'utilisateur");
                Toast.makeText(SignUp.this, "Échec de l'ajout de l'utilisateur.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
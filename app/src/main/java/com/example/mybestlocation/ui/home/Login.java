package com.example.mybestlocation.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybestlocation.Config;
import com.example.mybestlocation.MainActivity;
import com.example.mybestlocation.R;
import com.example.mybestlocation.User;
import com.example.mybestlocation.UserServices;
import com.example.mybestlocation.ui.home.ui.SignUp;

public class Login extends AppCompatActivity {

    Button btn_login, btn_signup;
    ImageView img_back;
    EditText ed_email, ed_mdp;
    String email, mdp;
    User user;
    UserServices userServices = new UserServices();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_sign_up_login);
        ed_email = findViewById(R.id.ed_email_login);
        ed_mdp = findViewById(R.id.ed_mdp_login);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = ed_email.getText().toString();
                mdp = ed_mdp.getText().toString();
                new LoginTask().execute(email); // Exécute la tâche async
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);

            }
        });

    }

    // AsyncTask pour gérer la connexion utilisateur en arrière-plan
    private class LoginTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            String email = params[0];
            return userServices.get_user_by_email(email); // Récupère l'utilisateur par email
        }

        @Override
        protected void onPostExecute(User result) {
            super.onPostExecute(result);
            if (result != null) {
                Log.d("Login", "Entered Password: " + mdp);
                Log.d("Login", "Stored Password: " + result.getPassword());

                if (mdp.equals(result.getPassword())) {
                    // Transférer les informations à MainActivity via Intent
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("name", result.getName());
                    intent.putExtra("email", result.getEmail());
                    intent.putExtra("phonenumber", result.getPhoneNumber());
                    intent.putExtra("id", result.getId());
                    Log.d("Login", "Info envoyes: " + result.getName()+result.getId());

                    Login.this.finish();
                    startActivity(intent);

                } else {
                    Log.d("Login", "Wrong email or password");
                }
            } else {
                Log.d("Login", "No user found with the given email");
            }
        }


    }
}

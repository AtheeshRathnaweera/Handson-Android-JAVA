package com.example.emenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.emenu.utils.JWTTokenHandler;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("app-log", "launcher activity started");
        checkTheJWTValidity();
    }

    private void checkTheJWTValidity() {
        SharedPreferences sharedPreferences = getSharedPreferences("loggedInData", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.saved_jwt_key_name), "");
        String email = sharedPreferences.getString(getString(R.string.saved_email_key_name), "");

        boolean validateResult = JWTTokenHandler.validateJwt(token, email);

        Log.i("app-log", "login validate result : " + validateResult);

        if (validateResult) {
            //redirect to main if toke is valid
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }
}

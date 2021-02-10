package com.example.emenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emenu.sqlitedb.DatabaseHandler;
import com.example.emenu.utils.EditTextValidator;
import com.example.emenu.utils.JWTTokenHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton, registerButton;
    private EditText emailText, passwordText;

    private DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.buttonLogin);
        registerButton = findViewById(R.id.buttonRegister);

        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        setClickListeners();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void setClickListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<EditText> checkNullResult = EditTextValidator.checkNull(Arrays.asList(emailText, passwordText));

                if (checkNullResult.size() == 0) {
                    Properties authProp = db.authenticateUser(emailText.getText().toString(), passwordText.getText().toString());

                    if (authProp.getProperty("success").equals("1")) {

                        String generatedJwt = JWTTokenHandler.createANew(emailText.getText().toString());

                        // get the activity shared preferences
                        SharedPreferences sharedPreferences = getSharedPreferences("loggedInData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.saved_jwt_key_name), generatedJwt);
                        editor.putString(getString(R.string.saved_email_key_name), emailText.getText().toString());
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        EditText editText = (EditText) findViewById(R.id.editText);
//                        String message = editText.getText().toString();
//                        intent.putExtra(EXTRA_MESSAGE, message);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), authProp.getProperty("message"), Toast.LENGTH_SHORT).show();
                    }
                } else {

                    String toastMessage = checkNullResult.get(0).getTag().toString() + " field is required !";
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


}

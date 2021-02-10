package com.example.emenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emenu.sqlitedb.DatabaseHandler;
import com.example.emenu.pojos.User;
import com.example.emenu.utils.EditTextValidator;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText emailText, passwordText;

    private DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.buttonRegister);

        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        setClickListeners();

    }

    private void setClickListeners() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<EditText> checkNullResult = EditTextValidator.checkNull(Arrays.asList(emailText, passwordText));
                List<EditText> checkInvalidEmail = EditTextValidator.checkInvalidEmail(Arrays.asList(emailText));

                if (checkNullResult.size() == 0 && checkInvalidEmail.size() == 0) {
                    Properties regProp = db.registerUser(new User(emailText.getText().toString(), passwordText.getText().toString()));

                    if (!regProp.get("rowNumber").equals("-1")) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), regProp.getProperty("message"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String toastMessage = "";

                    if (checkNullResult.size() > 0) {
                        toastMessage = checkNullResult.get(0).getTag().toString() + " field is required !";
                    } else if (checkInvalidEmail.size() > 0) {
                        toastMessage = "Invalid email for "+checkInvalidEmail.get(0).getTag().toString();
                    }
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

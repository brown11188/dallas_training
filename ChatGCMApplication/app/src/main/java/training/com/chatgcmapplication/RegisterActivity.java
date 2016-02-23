package training.com.chatgcmapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import training.com.common.AppConfig;
import training.com.database.DatabaseHelper;
import training.com.model.Users;
import training.com.services.RegistrationIdManager;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userName;
    private EditText password;
    private String regId;
    private RegistrationIdManager registrationIdManager;
    private DatabaseHelper databaseHelper;
    String getUsername;
    String getPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText) findViewById(R.id.txt_userName_register);
        password = (EditText) findViewById(R.id.txt_password_register);

        Button btnRegist = (Button) findViewById(R.id.btn_register);

        btnRegist.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        getUsername = userName.getText().toString();
        getPassword = password.getText().toString();
        doRegister(getUsername, getPassword);


    }

    private void createUser(String username, String password,String regId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        Users user = databaseHelper.getUser(username);
        if (user.getUserName() == null) {
            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "password must be more than 6 character", Toast.LENGTH_SHORT).show();
            } else {
                password = databaseHelper.storePassword(password);
                user = new Users();
                user.setUserName(username);
                user.setPassword(password);
                user.setRegistrationId(regId);
                databaseHelper.addUser(user);
                Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Your username is already exist", Toast.LENGTH_SHORT).show();
        }
    }

    private void doRegister(final String username, final String password) {
        registrationIdManager = new RegistrationIdManager(this, AppConfig.SENDER_ID);
        registrationIdManager.registerIfNeeded(new RegistrationIdManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                createUser(username, password, registrationId);
            }
            @Override
            public void onFailure(String ex) {
                Log.i("Registration Id", "Register Fail");
            }
        });
    }
}



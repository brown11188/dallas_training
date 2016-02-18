package training.com.chatgcmapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import training.com.common.AppConfig;
import training.com.database.DatabaseHelper;
import training.com.model.Users;
import training.com.services.RegistrationIdManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText) findViewById(R.id.txt_username);
        pass = (EditText) findViewById(R.id.txt_password);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        Button btnRegist = (Button) findViewById(R.id.btn_register_login);
        btnLogin.setOnClickListener(this);
        btnRegist.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {

    }

    private void saveUser(Users user) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("loginPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId", user.getUserId());
        editor.putString("userName", user.getUserName());
        editor.putString("registration_id", user.getRegistrationId());
        editor.apply();
    }

    private void doLogin(String userName, String password) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        Users user = Users.getInstance();
        user = databaseHelper.checkLogin(userName, password);
        if (user.getUserId()==0) {
            Toast.makeText(getApplicationContext(), "Username or password is Wrong !", Toast.LENGTH_SHORT).show();
        } else {
            saveUser(user);
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                String userName = name.getText().toString();
                String password = pass.getText().toString();
                doLogin(userName, password);
                break;
            case R.id.btn_register_login:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
        }
    }

}

package training.com.chatgcmapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;


import training.com.common.AppConfig;
import training.com.database.DatabaseHelper;
import training.com.model.Users;
import training.com.services.RegistrationIdManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_register;
    private Button btn_contact;
    // private Button btn_logout;
    //private Button btn_logout
    private DatabaseHelper databaseHelper;
    private Users user, user1;
    private SharedPreferences preferences;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getApplicationContext().getSharedPreferences("loginPref", MODE_PRIVATE);
        int id = preferences.getInt("userId", 0);
        AppConfig.USER_ID = id;
        AppConfig.USER_NAME = preferences.getString("userName", null);
        AppConfig.REG_ID = preferences.getString("registration_id", null);
        if (id == 0) {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_contact = (Button) findViewById(R.id.btn_contact);
        // btn_logout = (Button) toolbar.findViewById(R.id.btn_out);
        btn_register.setOnClickListener(this);
        btn_contact.setOnClickListener(this);

//        user = setDefaultUserValue();
//        user1 = setDefaultUserValue1();
//
//        databaseHelper.addUser(user);
//        databaseHelper.addUser(user1);
        //btn_logout.setOnClickListener(this);


    }

    private Users setDefaultUserValue() {
        Users users = new Users();
        users.setUserId(1);
        users.setUserName("Harold");
        users.setPassword("123123");
        users.setRegistrationId(AppConfig.HAROLD_KEY);
        return users;
    }

    private Users setDefaultUserValue1() {
        Users users = new Users();
        users.setUserId(2);
        users.setUserName("Hawk");
        users.setPassword("123123");
        users.setRegistrationId(AppConfig.HAWK_KEY);
        return users;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_register:
                RegistrationIdManager registrationIdManager = new RegistrationIdManager(this, AppConfig.SENDER_ID);
                registrationIdManager.registerIfNeeded(new RegistrationIdManager.RegistrationCompletedHandler() {
                    @Override
                    public void onSuccess(String registrationId, boolean isNewRegistration) {
                        Log.i("Registration Id", registrationId);
                    }

                    @Override
                    public void onFailure(String ex) {
                        Log.i("Registration Id", "Register Fail");
                    }
                });
                break;
            case R.id.btn_contact:
                intent = new Intent(MainActivity.this, HomePageActivity.class);
                MainActivity.this.startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import training.com.common.AppConfig;
import training.com.common.RetrofitCallBackUtil;
import training.com.common.RetrofitGenerator;
import training.com.dao.RESTDatabaseDAO;
import training.com.database.DatabaseHelper;
import training.com.model.Users;
import training.com.services.RegistrationIdManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name;
    private EditText pass;
    private RetrofitCallBackUtil retrofitCallBackUtil = new RetrofitCallBackUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("loginPref", MODE_PRIVATE);
        int id = preferences.getInt("userId", 0);
        AppConfig.USER_ID = id;
        AppConfig.USER_NAME = preferences.getString("userName", null);
        AppConfig.REG_ID = preferences.getString("registration_id", null);
        if (id != 0) {
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }
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
        RetrofitGenerator retrofitGenerator = new RetrofitGenerator();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(AppConfig.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        RESTDatabaseDAO service = retrofit.create(RESTDatabaseDAO.class);
        Call<Users> callUser = service.getUser(userName, password);
        callUser.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccess()) {
                    Users user = response.body();
                    saveUser(user);
                    Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Username or password is Wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Please, check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
//       boolean check = retrofitCallBackUtil.login(userName, password, service, getApplicationContext());
//        if (check) {
//            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
//            startActivity(intent);
//        } else {
//            Toast.makeText(getApplicationContext(), "Username or password is Wrong !", Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

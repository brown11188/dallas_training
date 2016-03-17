package training.com.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import training.com.dao.RESTDatabaseDAO;
import training.com.dao.RetrofitResponseCallBack;
import training.com.model.Message;
import training.com.model.Users;

/**
 * Created by enclaveit on 3/10/16.
 */
public class RetrofitCallBackUtil {


    public void getUserByNameRetrofit(String name, RESTDatabaseDAO service, final RetrofitResponseCallBack retrofitResponseCallBack) {
        Call<Users> callUser = service.getUser(name);

        callUser.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccess()) {
                    retrofitResponseCallBack.onSuccessUser(response.body());
                    Log.i("Success", "addMessageToServerRetrofit " + response.body());
                } else {
                    Log.i("Success", "Not addMessageToServerRetrofit " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                retrofitResponseCallBack.onFailure();
                Log.i("Fail", "addMessageToServerRetrofit " + t.toString());
            }
        });
    }


    public void addMessageToServer(String message, int user_id,int sender_id, RESTDatabaseDAO service) {
        Call<Void> call = service.addMessage(message, user_id, sender_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccess()) {
                    Log.i("Success", "addMessageToServer " + response.message());
                } else {
                    Log.i("Success", "Not addMessageToServer " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("Fail", "addMessageToServer " + t.toString());
            }
        });
    }

    public void getLastTenMessageCallBack(int user_id, int sender_id, int offset_number, RESTDatabaseDAO service, final RetrofitResponseCallBack retrofitResponseCallBack) {
        Call<ArrayList<Message>> call = service.getLastTenMessage(user_id, sender_id, offset_number);
        call.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                if (response.isSuccess()) {
                    retrofitResponseCallBack.onSuccessMessages(response.body());
                    Log.i("Success", "getLastTenMessageCallBack " + response.body().size());
                } else {
                    Log.i("Success", "Not getLastTenMessageCallBack " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                retrofitResponseCallBack.onFailure();
                Log.i("Fail", "getLastTenMessageCallBack " + t.toString());
            }
        });
    }

    public void addNewUser(String username, String password, String registrationId, RESTDatabaseDAO service) {
        Call<ResponseBody> userCall = service.regist(username, password, registrationId);
        userCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("test", String.valueOf(t));
            }
        });
    }

    public boolean login(String username, String password, RESTDatabaseDAO service, final Context context) {
        final boolean[] check = {false};
        Call<Users> callUser = service.getUser(username, password);
        callUser.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccess()) {
                    Users user = response.body();
                    saveUser(user, context);
                    check[0] = true;
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.i("login", "oh shit \n" + t);
                Toast.makeText(context.getApplicationContext(), "Please, check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        return check[0];
    }

    private void saveUser(Users user, Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("loginPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId", user.getUserId());
        editor.putString("userName", user.getUserName());
        editor.putString("registration_id", user.getRegistrationId());
        editor.apply();
    }


}

package training.com.common;

import android.util.Log;

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
    private int global_userId;

    public int getUserByNameRetrofit(String name, RESTDatabaseDAO service) {
        Call<Users> callUser = service.getUser(name);

        callUser.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccess()) {
                    global_userId = response.body().getUserId();
                    Log.i("Success", "addMessageToServerRetrofit " + global_userId);
                } else {
                    Log.i("Success", "Not addMessageToServerRetrofit " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.i("Fail", "addMessageToServerRetrofit " + t.toString());
            }
        });
        return global_userId;
    }

    public void addMessageToServerRetrofit(String message, int user_id, RESTDatabaseDAO service) {

        Call<Void> callMessage = service.addMessage(message, AppConfig.USER_ID, user_id);
        callMessage.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccess()) {
                    Log.i("Success", "addMessageToServerRetrofit " + response.message());
                } else {
                    Log.i("Success", "Not addMessageToServerRetrofit " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("Fail", "addMessageToServerRetrofit " + t.toString());
            }
        });
    }

    public void addMessageToServer(String message, int user_id, RESTDatabaseDAO service) {
        Call<Void> call = service.addMessage(message, user_id, AppConfig.USER_ID);
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
                    retrofitResponseCallBack.onSuccess(response.body());
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


}

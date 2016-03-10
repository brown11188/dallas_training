package training.com.common;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import training.com.dao.RESTDatabaseDAO;
import training.com.model.Users;

/**
 * Created by enclaveit on 3/10/16.
 */
public class RetrofitCallBackUtil {
    private int global_userId;

    public int  getUserByNameRetrofit(String name, RESTDatabaseDAO service) {
        Call<Users> callUser = service.getUser(name);

        callUser.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccess()) {
                    global_userId = response.body().getUserId();
                    Log.i("Success", "Good " + global_userId);
                } else {
                    Log.i("Success", "Not good " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.i("Fail", "Failure " + t.toString());
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
                    Log.i("Success", "Good " + response.message());
                } else {
                    Log.i("Success", "Not good " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("Fail", "Failure " + t.toString());
            }
        });
    }

    public void addMessageToServer(String message, int user_id, RESTDatabaseDAO service) {
        Call<Void> call = service.addMessage(message, user_id, AppConfig.USER_ID);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccess()) {
                    Log.i("Success", "Good " + response.message());
                } else {
                    Log.i("Success", "Not good " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("Fail", "Failure " + t.toString());
            }
        });
    }

}

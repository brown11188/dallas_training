package training.com.dao;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;
import training.com.model.Users;

/**
 * Created by enclaveit on 3/1/16.
 */
public interface RESTDatabaseDAO {

    @GET("list")
    Call<ArrayList<Users>> getUsers(@Query("userName") String username);
}

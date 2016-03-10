package training.com.dao;

import java.util.ArrayList;
import java.util.Date;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import training.com.model.Message;
import training.com.model.Users;

/**
 * Created by enclaveit on 3/1/16.
 */
public interface RESTDatabaseDAO {

    @GET("list")
    Call<ArrayList<Users>> getUsers(@Query("userName") String username);

    @GET("getlastmessage")
    Call<Message> getLastMessage(@Query("user_id") int user_id,
                                 @Query("sender_id") int sender_id);

    @GET("addmessage")
    Call<Void> addMessage(@Query("content") String content,
                             @Query("user_id") int user_id,
                             @Query("sender_id") int sender_id);

    @GET("login")
    Call<Users> getUser(@Query("userName") String username,
                        @Query("password") String password);
    @GET("add")
    Call<String> regist(@Query("userName") String username,
                       @Query("password") String password,
                       @Query("registrationId") String registration_id);

}

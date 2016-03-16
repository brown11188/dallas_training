package training.com.fragment;


import android.app.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import training.com.adapter.ContactListFragmentAdapter;
import training.com.chatgcmapplication.ChatActivity;
import training.com.chatgcmapplication.R;
import training.com.common.AppConfig;
import training.com.common.RetrofitGenerator;
import training.com.dao.RESTDatabaseDAO;
import training.com.database.DatabaseHelper;
import training.com.model.Users;

/**
 * Created by hawk on 27/01/2016.
 */
public class ContactFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<Users> users = new ArrayList<>();
    private ContactListFragmentAdapter contactsAdapter;
    private  ListView list_contact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_contact, container, false);
        list_contact = (ListView) rootView.findViewById(R.id.list_contact);
        list_contact.setOnItemClickListener(this);
        RetrofitGenerator retrofitGenerator = new RetrofitGenerator();
        Retrofit client = retrofitGenerator.createRetrofit();
        RESTDatabaseDAO service = client.create(RESTDatabaseDAO.class);

        Call<ArrayList<Users>> call = service.getUsers(AppConfig.USER_NAME);
        call.enqueue(new Callback<ArrayList<Users>>() {
            @Override
            public void onResponse(Call<ArrayList<Users>> call, retrofit2.Response<ArrayList<Users>> response) {
                if (response.isSuccess()) {
                    Log.i("In retrofit method success", response.body() + "");
                    users = response.body();
                    contactsAdapter = new ContactListFragmentAdapter(getActivity(), users);
                    list_contact.setAdapter(contactsAdapter);

                } else {
                    Log.i("In retrofit method failure contact", response.body() + "");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Users>> call, Throwable t) {
                Log.i("In retrofit method failure", "Fail roi nha contact a");
            }

        });
        return rootView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent chatIntent = new Intent(getActivity(), ChatActivity.class);

        chatIntent.putExtra("regId", users.get(position).getRegistrationId());
        chatIntent.putExtra("titleName", users.get(position).getUserName());
        chatIntent.putExtra("userId", users.get(position).getUserId());
        chatIntent.putExtra("name", AppConfig.USER_NAME);
        startActivity(chatIntent);
    }

//


}

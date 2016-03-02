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

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import training.com.adapter.ContactListFragmentAdapter;
import training.com.chatgcmapplication.ChatActivity;
import training.com.chatgcmapplication.R;
import training.com.common.AppConfig;
import training.com.dao.RESTDatabaseDAO;
import training.com.database.DatabaseHelper;
import training.com.model.Users;

/**
 * Created by hawk on 27/01/2016.
 */
public class ContactFragment extends Fragment implements AdapterView.OnItemClickListener {

    private DatabaseHelper databaseHelper;
    private ArrayList<Users> users = new ArrayList<>();
    private ContactListFragmentAdapter contactsAdapter;
    private  ListView list_contact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());

        View rootView = inflater.inflate(R.layout.list_contact, container, false);
        list_contact = (ListView) rootView.findViewById(R.id.list_contact);


        list_contact.setOnItemClickListener(this);
        Retrofit client = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RESTDatabaseDAO service = client.create(RESTDatabaseDAO.class);

        Call<ArrayList<Users>> call = service.getUsers(AppConfig.USER_NAME);
        call.enqueue(new Callback<ArrayList<Users>>() {
            @Override
            public void onResponse(retrofit.Response<ArrayList<Users>> response) {
                if (response.isSuccess()) {

                    users = response.body();
                    contactsAdapter = new ContactListFragmentAdapter(getActivity(), users);
                    list_contact.setAdapter(contactsAdapter);

                } else {
                    Log.i("In retrofit method failure", response.body() + "");
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("In retrofit method failure", "Fail roi nha");
            }
        });
        return rootView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent chatIntent = new Intent(getActivity(), ChatActivity.class);

        chatIntent.putExtra("regId", databaseHelper.getUsers().get(position).getRegistrationId());
        chatIntent.putExtra("titleName", databaseHelper.getUsers().get(position).getUserName());
        chatIntent.putExtra("userId", databaseHelper.getUsers().get(position).getUserId());
        chatIntent.putExtra("name", AppConfig.USER_NAME);
        startActivity(chatIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        contactsAdapter.notifyDataSetChanged();
    }
}

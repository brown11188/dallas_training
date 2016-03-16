package training.com.chatgcmapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.LinearProgressButton;

import java.util.ArrayList;
import java.util.List;


import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import training.com.adapter.ContactListFragmentAdapter;
import training.com.common.AppConfig;
import training.com.common.MorphinButtonCreator;
import training.com.common.ProgressGenerator;
import training.com.common.RetrofitGenerator;
import training.com.dao.RESTDatabaseDAO;
import training.com.database.DatabaseHelper;
import training.com.model.Users;
import training.com.services.MessageSender;
import training.com.services.MessageSenderContent;

public class MessageSendingActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.txtContent)
    EditText txtContent;
    private MultiAutoCompleteTextView txtContacts;
    private String[] test;
    private LinearProgressButton btnMorph;
    private MorphinButtonCreator buttonCreator;
    private Context context;
    private RESTDatabaseDAO service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_sending2);
        ButterKnife.bind(this);
        txtContacts = (MultiAutoCompleteTextView) findViewById(R.id.txtContacts);
        btnMorph = (LinearProgressButton) findViewById(R.id.btnSendAll);
        btnMorph.setOnClickListener(this);
        context = getApplication();
        buttonCreator = new MorphinButtonCreator();
        RetrofitGenerator retrofitGenerator = new RetrofitGenerator();
        Retrofit client = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(retrofitGenerator.gsonDateDeserializerGenerator()))
                .build();
        service = client.create(RESTDatabaseDAO.class);

        Call<ArrayList<Users>> call = service.getUsers(AppConfig.USER_NAME);
        call.enqueue(new Callback<ArrayList<Users>>() {
            @Override
            public void onResponse(Call<ArrayList<Users>> call, retrofit2.Response<ArrayList<Users>> response) {
                if (response.isSuccess()) {
                    Users[] user_array = new Users[response.body().size()];
                    user_array = response.body().toArray(user_array);
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, user_array);
                    txtContacts.setAdapter(adapter);
                    txtContacts.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                    Log.i("In retrofit method success contact", user_array[1].getUserName() + "");


                } else {
                    Log.i("In retrofit method failure contact", response.body() + "");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Users>> call, Throwable t) {
                Log.i("In retrofit method failure", "Fail roi nha contact a");
            }

        });
        buttonCreator.morphToSquare(btnMorph, 0, context);
    }

    @Override
    public void onClick(View v) {
        if (txtContacts.getText().length() > 0 && txtContent.getText().length() > 0) {
            buttonCreator.onMorphButton1Clicked(btnMorph, context);
            final MessageSenderContent messageSenderContent = new MessageSenderContent();
            final List<String> registration_ids = new ArrayList<>();
            final MessageSender messageSender = new MessageSender();
            String[] contacts = txtContacts.getText().toString().split(",");

            for (String contact : contacts) {
                Call<Users> call = service.getUser(contact.trim());
                call.enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        registration_ids.add(response.body().getRegistrationId());
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {

                    }
                });
            }
            messageSenderContent.setRegIds(registration_ids);
            messageSenderContent.createData(AppConfig.USER_NAME, txtContent.getText().toString());
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    messageSender.sendPost(messageSenderContent);
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    finish();
                }
            }.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please fill all required field.", Toast.LENGTH_SHORT).show();
        }
    }


}

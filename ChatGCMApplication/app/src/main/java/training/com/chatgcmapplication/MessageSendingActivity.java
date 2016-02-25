package training.com.chatgcmapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import training.com.common.AppConfig;
import training.com.database.DatabaseHelper;
import training.com.services.MessageSender;
import training.com.services.MessageSenderContent;

public class MessageSendingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtContent;
    private MultiAutoCompleteTextView txtContacts;
    private Button btnSendAll;
    private String[] contacts;
    private DatabaseHelper databaseHelper;
    private String[] array = {"Hawk", "Harold"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_sending2);
        txtContacts = (MultiAutoCompleteTextView) findViewById(R.id.txtContacts);
        txtContent = (TextView) findViewById(R.id.txtContent);
        btnSendAll = (Button) findViewById(R.id.btnSendAll);
        btnSendAll.setOnClickListener(this);
        databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
        txtContacts.setAdapter(adapter);
        txtContacts.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    @Override
    public void onClick(View v) {
        final MessageSenderContent messageSenderContent = new MessageSenderContent();
        List<String> registration_ids = new ArrayList<>();
        final MessageSender messageSender = new MessageSender();
        contacts = txtContacts.getText().toString().split(",");
        for (String contact : contacts) {
            String regId = databaseHelper.getUser(contact.trim()).getRegistrationId();
            registration_ids.add(regId);
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
    }
}

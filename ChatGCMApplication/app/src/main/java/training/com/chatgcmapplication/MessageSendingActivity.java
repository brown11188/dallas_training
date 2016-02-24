package training.com.chatgcmapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import training.com.common.AppConfig;
import training.com.database.DatabaseHelper;
import training.com.services.MessageSender;
import training.com.services.MessageSenderContent;

public class MessageSendingActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView txtContacts, txtContent;
    private Button btnSendAll;
    private String[] contacts;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_sending2);
        txtContacts =(TextView) findViewById(R.id.txtContacts);
        txtContent = (TextView) findViewById(R.id.txtContent);
        btnSendAll = (Button) findViewById(R.id.btnSendAll);
        btnSendAll.setOnClickListener(this);
        databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        final MessageSenderContent messageSenderContent = new MessageSenderContent();
        List<String> registration_ids = new ArrayList<>();
        final MessageSender messageSender = new MessageSender();
        contacts = txtContacts.getText().toString().split(",");
        for(String contact: contacts){
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
        }.execute();
    }
}

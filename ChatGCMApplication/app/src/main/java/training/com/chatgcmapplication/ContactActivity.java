package training.com.chatgcmapplication;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContactActivity extends Activity {
    private final String TAB_RECENT = "Recent chat";
    private final String TAB_CONTACTS = "Contact List";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }
}

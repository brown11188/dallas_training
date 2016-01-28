package training.com.chatgcmapplication;

import android.app.Activity;
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

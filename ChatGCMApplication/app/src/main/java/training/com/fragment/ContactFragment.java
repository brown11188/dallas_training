package training.com.fragment;


import android.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import training.com.adapter.ContactListFragmentAdapter;
import training.com.chatgcmapplication.ChatActivity;
import training.com.chatgcmapplication.R;
import training.com.common.AppConfig;
import training.com.database.DatabaseHelper;
import training.com.model.Users;

/**
 * Created by hawk on 27/01/2016.
 */
public class ContactFragment extends Fragment implements AdapterView.OnItemClickListener {

    private DatabaseHelper databaseHelper;
    private ContactListFragmentAdapter contactsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());

        View rootView = inflater.inflate(R.layout.list_contact, container, false);
        ArrayList<Users> users= databaseHelper.getUsers();
        ListView list_contact =(ListView)rootView.findViewById(R.id.list_contact);
        contactsAdapter = new ContactListFragmentAdapter(getActivity(), users);
        list_contact.setAdapter(contactsAdapter);
        list_contact.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent chatIntent = new Intent(getActivity(), ChatActivity.class);

        chatIntent.putExtra("regId",databaseHelper.getUsers().get(position).getRegistrationId());
        chatIntent.putExtra("titleName",databaseHelper.getUsers().get(position).getUserName());
        chatIntent.putExtra("userId",databaseHelper.getUsers().get(position).getUserId());
        chatIntent.putExtra("name", AppConfig.USER_NAME);
        startActivity(chatIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        contactsAdapter.notifyDataSetChanged();
    }
}

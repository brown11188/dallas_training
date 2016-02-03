package training.com.fragment;


import android.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import training.com.adapter.ContactListFragmentAdapter;
import training.com.chatgcmapplication.ChatActivity;
import training.com.chatgcmapplication.R;
import training.com.database.DatabaseHelper;
import training.com.model.Users;

/**
 * Created by hawk on 27/01/2016.
 */
public class ContactFragment extends Fragment implements AdapterView.OnItemClickListener {

    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        View rootView = inflater.inflate(R.layout.list_contact, container, false);

        ArrayList<Users> users= databaseHelper.getUsers();
        ListView list_contact =(ListView)rootView.findViewById(R.id.list_contact);
        list_contact.setAdapter(new ContactListFragmentAdapter(getActivity(), users));
        list_contact.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent chatIntent = new Intent(getActivity(), ChatActivity.class);

        chatIntent.putExtra("regId",databaseHelper.getUsers().get(position).getRegistrationId());
        chatIntent.putExtra("titleName",databaseHelper.getUsers().get(position).getUserName());
        chatIntent.putExtra("userId",databaseHelper.getUsers().get(position).getUserId());
        chatIntent.putExtra("name","Harold");
        startActivity(chatIntent);
    }
}

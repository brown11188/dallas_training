package training.com.fragment;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import training.com.adapter.ContactListFragmentAdapter;
import training.com.chatgcmapplication.R;
import training.com.contact.Contact;

/**
 * Created by hawk on 27/01/2016.
 */
public class ContactFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_contact, container, false);

        ArrayList<Contact> contacts = getListContact();
        ListView list_contact =(ListView)rootView.findViewById(R.id.list_contact);
        list_contact.setAdapter(new ContactListFragmentAdapter(getActivity(), contacts));
        return rootView;
    }

    private ArrayList<Contact> getListContact(){
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        Contact contact = new Contact();

        contact.setUserName("Hawk Tran");
        contact.setLastMsg("this is my last message");
        contacts.add(contact);

        contact = new Contact();
        contact.setUserName("Huy Bui");
        contact.setLastMsg("this is my last message");
        contacts.add(contact);

        return contacts;
    }

}

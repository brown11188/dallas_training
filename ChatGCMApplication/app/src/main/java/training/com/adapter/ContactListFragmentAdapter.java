package training.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import training.com.chatgcmapplication.R;
import training.com.contact.Contact;

/**
 * Created by hawk on 27/01/2016.
 */
public class ContactListFragmentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Contact> listContact;

    public ContactListFragmentAdapter(Context context, ArrayList<Contact> listContact) {
        this.context = context;
        this.listContact = listContact;
    }

    @Override
    public int getCount() {
        return listContact.size();
    }

    @Override
    public Object getItem(int position) {
        return listContact.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_contact,null);
        }
        TextView tv_userName = (TextView)convertView.findViewById(R.id.tv_userName);
        TextView tv_lasMsg = (TextView)convertView.findViewById(R.id.tv_lastMsg);

        tv_userName.setText(listContact.get(position).getUserName());
        tv_lasMsg.setText(listContact.get(position).getLastMsg());

        return convertView;
    }
}

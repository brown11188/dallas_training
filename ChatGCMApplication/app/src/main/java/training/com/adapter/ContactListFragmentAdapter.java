package training.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import training.com.chatgcmapplication.R;
import training.com.common.AppConfig;
import training.com.database.DatabaseHelper;
import training.com.model.Message;
import training.com.model.Users;

/**
 * Created by hawk on 27/01/2016.
 */
public class ContactListFragmentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Users> listContact;
    private DatabaseHelper databaseHelper;

    public ContactListFragmentAdapter(Context context, ArrayList<Users> listContact) {
        this.context = context;
        this.listContact = listContact;
        databaseHelper = DatabaseHelper.getInstance(context);
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
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_contact, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
            viewHolder.tv_lastMessage = (TextView) convertView.findViewById(R.id.tv_lastMsg);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Users objectItem = listContact.get(position);
        Message message = databaseHelper.getLastMessage(objectItem.getUserId(), AppConfig.USER_ID);
        viewHolder.tv_userName.setText(objectItem.getUserName());
        viewHolder.tv_lastMessage.setText(message.getMessage());

        return convertView;
    }

    static class ViewHolder {
        TextView tv_userName;
        TextView tv_lastMessage;
    }
}



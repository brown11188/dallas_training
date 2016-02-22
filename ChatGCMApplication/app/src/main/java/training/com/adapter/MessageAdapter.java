package training.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import training.com.chatgcmapplication.R;
import training.com.common.AppConfig;
import training.com.database.DatabaseHelper;
import training.com.model.Message;

/**
 * Created by enclaveit on 2/17/16.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    private Context context;
    private ArrayList<Message> messages;
    private DatabaseHelper databaseHelper;
    private Message message;

    @Override
    public void add(Message object) {
        messages.add(object);
        super.add(object);
    }

    public MessageAdapter(Context context, int textViewResouceId, ArrayList<Message> messages) {
        super(context, textViewResouceId);
        this.context = context;
        this.messages = messages;
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public int getCount() {
        return messages.size();
    }

    public Message getItem(int index) {
        return messages.get(index);
    }

    public long getItemId(int index) {
        return index;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageViewHolder messageViewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_item, parent, false);
            messageViewHolder = new MessageViewHolder();
            messageViewHolder.tv_userName = (TextView) convertView.findViewById(R.id.tv_chatName);
            messageViewHolder.tv_message = (TextView) convertView.findViewById(R.id.tv_messageContent);
            convertView.setTag(messageViewHolder);
        } else {
            messageViewHolder = (MessageViewHolder) convertView.getTag();
        }
        message = messages.get(position);
        String username = databaseHelper.getUserByUserId(message.getUserId()).getUserName();
        if (message.getUserId() == AppConfig.USER_ID) {
            messageViewHolder.tv_message.setTextColor(Color.parseColor("#0066ff"));
            messageViewHolder.tv_userName.setTextColor(Color.parseColor("#0066ff"));
        } else {
            messageViewHolder.tv_message.setTextColor(Color.parseColor("#000000"));
            messageViewHolder.tv_userName.setTextColor(Color.parseColor("#000000"));
        }
        messageViewHolder.tv_userName.setText(username);
        messageViewHolder.tv_message.setText(message.getMessage());

        return convertView;
    }

    static class MessageViewHolder {
        TextView tv_message;
        TextView tv_userName;
    }


}

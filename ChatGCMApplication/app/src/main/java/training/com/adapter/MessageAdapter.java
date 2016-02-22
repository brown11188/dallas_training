package training.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import training.com.chatgcmapplication.R;
import training.com.common.AppConfig;
import training.com.model.Message;

/**
 * Created by enclaveit on 2/17/16.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    private Context context;
    private ArrayList<Message> messages;

    @Override
    public void add(Message object) {
        messages.add(object);
        super.add(object);
    }

    public MessageAdapter(Context context, int textViewResouceId, ArrayList<Message> messages) {
        super(context, textViewResouceId);
        this.context = context;
        this.messages = messages;
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_item, parent, false);
            messageViewHolder = createViewHolder(convertView);
            convertView.setTag(messageViewHolder);
        } else {
            messageViewHolder = (MessageViewHolder) convertView.getTag();
        }
        Message message = messages.get(position);
        setAlignment(messageViewHolder, message.getUserId());
        messageViewHolder.txtMessage.setText(message.getMessage());
        messageViewHolder.txtInfo.setText(message.getExpiresTime()+"");

        return convertView;
    }

    private void setAlignment(MessageViewHolder holder, int user_id) {
        if (user_id == AppConfig.USER_ID) {
            holder.contentWithBG.setBackgroundResource(R.drawable.my_textview_border);

            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            linearParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(linearParams);

            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(relativeParams);

            linearParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            linearParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(linearParams);

            linearParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            linearParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(linearParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.friend_textview_border);

            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            linearParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(linearParams);

            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(relativeParams);

            linearParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            linearParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(linearParams);

            linearParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            linearParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(linearParams);
        }
    }

    private MessageViewHolder createViewHolder(View v) {
        MessageViewHolder holder = new MessageViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        return holder;
    }

    private static class MessageViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }


}

package training.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import training.com.chatgcmapplication.R;
import training.com.common.AppConfig;
import training.com.model.Message;

/**
 * Created by enclaveit on 2/17/16.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    private Context context;
    private ArrayList<Message> messages;
    private ArrayList<String> editMessages = new ArrayList<String>();
    private HashMap<String, Integer> emotions = new HashMap<String, Integer>();

    @Override
    public void add(Message object) {
        messages.add(object);
        super.add(object);
    }

    public void insertToTheFirst(List<Message> object) {
        messages.addAll(0, object);
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
        final MessageViewHolder messageViewHolder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        emotions.put(":D", R.drawable.smile);
        emotions.put(":(", R.drawable.sad);
        emotions.put(":((", R.drawable.cry);
        emotions.put(":@", R.drawable.angry);

        fillMessages();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_item, parent, false);
            messageViewHolder = createViewHolder(convertView);
            convertView.setTag(messageViewHolder);
        } else {
            messageViewHolder = (MessageViewHolder) convertView.getTag();
        }
        final Message message = messages.get(position);
        setAlignment(messageViewHolder, message.getUserId());
        if (message.getMessage().contains("https://drive.google.com/uc?")) {
            final String[] array = message.getMessage().split("=");
            messageViewHolder.txtMessage.setVisibility(View.GONE);
            messageViewHolder.contentWithBG.setBackground(null);
            messageViewHolder.img_messageImage.setVisibility(View.VISIBLE);
            messageViewHolder.txtInfo.setText(message.getExpiresTime() + "");
            File file = new File(Environment.getExternalStorageDirectory() + "/Image-" + array[1] + ".jpg");
            if (!file.exists()) {
                Picasso.Builder builder = new Picasso.Builder(context);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.i("Picasso error", exception.getMessage() + ",");
                    }
                });

                final Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        messageViewHolder.img_messageImage.setImageBitmap(bitmap);
                        saveImage(bitmap, array[1]);
                        Log.i("Message content in target success", message.getMessage());
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.i("Message content in target fail", message.getMessage());
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.i("Message content in target prepare", message.getMessage());
                    }
                };
                messageViewHolder.img_messageImage.setTag(target);
                Picasso.with(context).cancelRequest(target);
                builder.build()
                        .load(message.getMessage())
                        .resize(250, 250)
                        .centerInside()
                        .into(target);
            }
            else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Image-" + array[1] + ".jpg");
                messageViewHolder.img_messageImage.setImageBitmap(bitmap);
            }
        } else {
            messageViewHolder.img_messageImage.setVisibility(View.GONE);
            messageViewHolder.txtMessage.setVisibility(View.VISIBLE);
            messageViewHolder.contentWithBG.setBackgroundResource(R.drawable.my_textview_border);
            messageViewHolder.txtMessage.setText(getEmotionText(getContext(), message.getMessage()));
            messageViewHolder.txtInfo.setText(message.getExpiresTime() + "");
        }


        return convertView;
    }

    private void fillMessages() {
        for (Map.Entry<String, Integer> entry : emotions.entrySet()) {
            editMessages.add(entry.getKey());

        }
    }

    private Spannable getEmotionText(Context context, String text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        int index;
        for (index = 0; index < builder.length(); index++) {
            for (Map.Entry<String, Integer> entry : emotions.entrySet()) {
                int length = entry.getKey().length();
                if (index + length > builder.length())
                    continue;
                if (builder.subSequence(index, index + length).toString().equals(entry.getKey())) {
                    builder.setSpan(new ImageSpan(context, entry.getValue()), index, index + length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    index += length - 1;
                    break;
                }
            }
        }
        return builder;
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
        holder.img_messageImage = (ImageView) v.findViewById(R.id.img_messageImage);
        return holder;
    }

    private static class MessageViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public ImageView img_messageImage;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }


    private void saveImage(Bitmap finalBitmap, String imageId) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + imageId + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

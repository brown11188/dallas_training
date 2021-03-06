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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import training.com.chatgcmapplication.R;
import training.com.common.AppConfig;
import training.com.common.RetrofitGenerator;
import training.com.dao.RESTDatabaseDAO;
import training.com.model.Message;
import training.com.model.Users;

/**
 * Created by hawk on 27/01/2016.
 */
public class ContactListFragmentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Users> listContact;
    private RetrofitGenerator retrofitGenerator;

    public ContactListFragmentAdapter(Context context, ArrayList<Users> listContact) {
        this.context = context;
        this.listContact = listContact;
        retrofitGenerator = new RetrofitGenerator();
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
        final ViewHolder viewHolder;
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
        final Users objectItem = listContact.get(position);


        Retrofit client = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(retrofitGenerator.gsonDateDeserializerGenerator()))
                .build();

        RESTDatabaseDAO service = client.create(RESTDatabaseDAO.class);

        final Call<Message> call = service.getLastMessage(objectItem.getUserId(), AppConfig.USER_ID);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccess()) {
                    viewHolder.tv_lastMessage.setText(response.body().getMessage());
                    Log.i("In retrofit method success message", response.body().getExpiresTime().toString().trim());

                } else {
                    Log.i("In retrofit method failure", response.body() + "");
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.i("In retrofit method failure message", t.toString());
            }


        });
        viewHolder.tv_userName.setText(objectItem.getUserName());
        return convertView;
    }

    static class ViewHolder {
        TextView tv_userName;
        TextView tv_lastMessage;
    }
}



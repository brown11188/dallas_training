package training.com.services;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import training.com.common.AppConfig;

/**
 * Created by hawk on 29/01/2016.
 */
public class MessageSender {
    public void sendPost(MessageSenderContent content) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection connection = null;
        try {
            URL gcmAPI = new URL(AppConfig.GCM_API);
            connection = (HttpURLConnection) gcmAPI.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "key=" + AppConfig.API_KEY);
            connection.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());

            mapper.writeValue(dataOutputStream, content);

            dataOutputStream.flush();
            dataOutputStream.close();

            int responseCode = connection.getResponseCode();
            Log.i("Request Status", "This is response status from server: " + responseCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

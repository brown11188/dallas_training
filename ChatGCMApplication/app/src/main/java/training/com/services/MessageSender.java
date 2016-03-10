package training.com.services;

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
    private int responseCode;


    public boolean sendPost(MessageSenderContent content) {

        HttpURLConnection connection;
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

            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (responseCode == 200) {
            Log.i("Request Status", "This is success response status from server: " + responseCode);
            return true;
        } else {
            Log.i("Request Status", "This is failure response status from server: " + responseCode);
            return false;
        }
    }
}
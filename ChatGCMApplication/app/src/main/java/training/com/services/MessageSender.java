package training.com.services;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import training.com.common.AppConfig;

/**
 * Created by hawk on 29/01/2016.
 */
public class MessageSender {
    public void sendPost(String apiKey,MessageSenderContent content) {
        HttpURLConnection connection = null;
        try {
            URL gcmAPI = new URL(AppConfig.GCM_API);
            connection = (HttpURLConnection) gcmAPI.openConnection();
            connection.getDoOutput();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "key="+apiKey);
            connection.connect();

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(dataOutputStream, content);

            dataOutputStream.flush();
            dataOutputStream.close();

            int responseCode = connection.getResponseCode();
            Log.i("Request Status", "This is response status from server: " + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

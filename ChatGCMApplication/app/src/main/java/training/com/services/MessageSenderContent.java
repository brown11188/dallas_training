package training.com.services;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by hawk on 29/01/2016.
 */
public class MessageSenderContent implements Serializable {
    private List<String> registration_ids;
    private Map<String, String> data;

    public void addRegId(String regId){
        if (registration_ids==null){
            registration_ids = new LinkedList<String>();
            registration_ids.add(regId);
        }
    }

    public void createData(String title,String message) {
        if (data == null)
            data = new HashMap<String, String>();

        data.put("title", title);
        data.put("message", message);
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public List<String> getRegIds() {
        return registration_ids;
    }

    public void setRegIds(List<String> regIds) {
        this.registration_ids = regIds;
    }

    @Override
    public String toString() {
        return "MessageSenderContent{" +
                "registration_ids=" + registration_ids +
                ", data=" + data +
                '}';
    }

}

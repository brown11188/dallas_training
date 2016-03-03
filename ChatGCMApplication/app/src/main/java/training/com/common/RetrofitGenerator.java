package training.com.common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import training.com.dao.RESTDatabaseDAO;

/**
 * Created by enclaveit on 3/3/16.
 */
public class RetrofitGenerator {

    public RESTDatabaseDAO createRetrofit() {
        Retrofit client = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return  client.create(RESTDatabaseDAO.class);
    }
}

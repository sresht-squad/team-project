import android.app.Application;

import com.parse.Parse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //LATER: let parse know about the model class subclass
//        ParseObject.registerSubclass(____.class);

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        //config the parse
        //get it running & connect
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("restauranteur")
                .clientKey("jesuskellynamitasresht")
                .server("http://fbu-restauranteur.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }
}
package api;

import android.content.Context;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by shitul on 5/3/17.
 */
public class MovieBaseUrl {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.themoviedb.org/3/movie/")
                    .build();
        }
        return retrofit;
    }
}

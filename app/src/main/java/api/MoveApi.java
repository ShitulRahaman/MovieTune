package api;

import model.MoveDetailsObject;
import model.PageObject;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by shitul on 5/2/17.
 */
public interface MoveApi {
    @GET("now_playing")
    Call<PageObject> getNewReleasePageObject(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("top_rated")
    Call<PageObject> getTopRatedPageObject(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("upcoming")
    Call<PageObject> getUpComingPageObject(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("{id}")
    Call<MoveDetailsObject> getMovieDetails(
            @Path("id") int id,
            @Query("api_key") String apiKey
    );

    @GET("{id}/similar")
    Call<PageObject> getSamilarPageObject(@Path("id") int id, @Query("api_key") String apiKey, @Query("page") int pageIndex);
}

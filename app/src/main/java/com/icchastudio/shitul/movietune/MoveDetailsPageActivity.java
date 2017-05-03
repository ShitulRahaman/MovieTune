package com.icchastudio.shitul.movietune;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import UtilityManager.Utility;
import adapter.MovieGridRecyclerViewAdapter;
import api.MoveApi;
import api.MovieBaseUrl;
import model.Genres;
import model.MoveDetailsObject;
import model.PageObject;
import model.ProductionCompany;
import model.ProductionCountriy;
import model.SpokenLanguagesObject;
import model.ThumbMovieObject;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by shitul on 5/3/17.
 */
public class MoveDetailsPageActivity extends AppCompatActivity implements MovieGridRecyclerViewAdapter.ItemClickListener {

    ImageView imageView;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    MovieGridRecyclerViewAdapter adapter;
    List<ThumbMovieObject> thumbMovieObjects;
    int movieID;
    ImageButton navigationButton;
    TextView toolbarTitle;
    TextView title;
    TextView year;
    TextView genres;
    TextView voteAvg;
    TextView popularity;

    TextView description;
    TextView productionComapnay;
    TextView buget;
    TextView productionContry;
    TextView productionLanguge;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        ThumbMovieObject thumbMovieObject = (ThumbMovieObject) i.getSerializableExtra("MyClass");

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.movie_details_page);
        movieID = thumbMovieObject.getId();
        initUI();

        thumbMovieObjects = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rvNumbers);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieGridRecyclerViewAdapter(
                this, thumbMovieObjects);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

    }

    void initUI() {
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        title = (TextView) findViewById(R.id.title);
        year = (TextView) findViewById(R.id.year);
        genres = (TextView) findViewById(R.id.catagory);
        voteAvg = (TextView) findViewById(R.id.vote);
        popularity = (TextView) findViewById(R.id.popularity);

        description = (TextView) findViewById(R.id.details);
        productionComapnay = (TextView) findViewById(R.id.poductionCompany);
        buget = (TextView) findViewById(R.id.budgetAmount);
        productionContry = (TextView) findViewById(R.id.countries);
        productionLanguge = (TextView) findViewById(R.id.languageName);
        navigationButton = (ImageButton) findViewById(R.id.backbutton);

        navigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MoveDetailsPageActivity.this, MovieTuneMainActivity.class);

                startActivity(i);
                finish();
            }
        });

        getVediosDetails();
    }


    public String getImageFullPath(String imageName) {
        return Utility.imageBasePath + "" + imageName + "?api_key=" + Utility.apiKey;
    }


    private void getVediosDetails() {

        final ProgressDialog loading = ProgressDialog.show(this, "Fetching Data", "Please wait...f", false, false);


        MoveApi service = MovieBaseUrl.getClient(this).create(MoveApi.class);

        Call<MoveDetailsObject> call = service.getMovieDetails(movieID, Utility.apiKey);


        call.enqueue(new Callback<MoveDetailsObject>() {
            @Override
            public void onResponse(Response<MoveDetailsObject> response, Retrofit retrofit) {
                Log.d("results", "totalPage " + response.body());
                loading.hide();
                loadData(response.body());


            }

            @Override
            public void onFailure(Throwable t) {
                loading.hide();
                Log.e("results", "" + t.getMessage());
            }
        });
    }


    void loadData(MoveDetailsObject moveDetailsObject) {
        toolbarTitle.setText(moveDetailsObject.getTitle());
        title.setText(moveDetailsObject.getTitle());
        imageView = (ImageView) findViewById(R.id.moviePoster);

        Picasso.with(this).load(getImageFullPath(moveDetailsObject.getBackdropPath())).into(imageView);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = format.parse(moveDetailsObject.getReleaseDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);


            year.setText("(" + calendar.get(Calendar.YEAR) + ")");


        } catch (ParseException e) {

            e.printStackTrace();
        }

        String genreStringPick = "";
        for (Genres genres : moveDetailsObject.getGenres()) {
            genreStringPick = genreStringPick + "" + genres.getName() + ",";
        }

        genres.setText(genreStringPick);
        voteAvg.setText(moveDetailsObject.getVoteAverage() + "");
        popularity.setText(new DecimalFormat("##.##").format(moveDetailsObject.getPopularity()) + "%");


        description.setText(moveDetailsObject.getOverview());
        if (moveDetailsObject.getProductionCompanies().size() > 0) {
            String productionCompanyStringPick = moveDetailsObject.getProductionCompanies().get(0).getName();

            productionComapnay.setText(productionCompanyStringPick);
        }
        buget.setText("$" + moveDetailsObject.getBudget());
        if (moveDetailsObject.getProductionCountries().size() > 0) {
            String productionContryStringPick = moveDetailsObject.getProductionCountries().get(0).getName();


            productionContry.setText(productionContryStringPick);
        }
        if (moveDetailsObject.getSpokenLanguages().size() > 0) {
            String languageContryStringPick = moveDetailsObject.getSpokenLanguages().get(0).getName();

            productionLanguge.setText(languageContryStringPick);

        }
        movieID = moveDetailsObject.getId();
        getVedios();
    }

    @Override
    public void onItemClick(View view, int position) {
        getVediosDetails();
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            Log.d("...........", "First position : " + firstVisibleItemPosition + "total " + totalItemCount + "child " + visibleItemCount);
            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= Utility.PAGE_SIZE) {
                    currentPage++;
                    getVedios();

                }
            }
        }
    };


    private void getVedios() {

        final ProgressDialog loading = ProgressDialog.show(this, "Fetching Data", "Please wait...", false, false);
        isLoading = true;

        MoveApi service = MovieBaseUrl.getClient(this).create(MoveApi.class);

        Call<PageObject> call = service.getSamilarPageObject(movieID, Utility.apiKey, currentPage);


        call.enqueue(new Callback<PageObject>() {
            @Override
            public void onResponse(Response<PageObject> response, Retrofit retrofit) {
                Log.d("results", "totalPage " + response.body().getTotalPages() + " s " + adapter.getItemCount());
                loading.hide();
                isLoading = false;
                currentPage = response.body().getPage();
                if (currentPage == response.body().getTotalPages())
                    isLastPage = true;

                thumbMovieObjects.addAll(response.body().getResults());
                adapter.setmData(thumbMovieObjects);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Throwable t) {
                loading.hide();
                Log.e("results", "" + t.getMessage());
            }
        });
    }
}

package fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icchastudio.shitul.movietune.MoveDetailsPageActivity;
import com.icchastudio.shitul.movietune.R;

import java.util.ArrayList;
import java.util.List;

import UtilityManager.Utility;
import adapter.MovieGridRecyclerViewAdapter;
import api.MoveApi;
import api.MovieBaseUrl;
import model.PageObject;
import model.ThumbMovieObject;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by shitul on 5/2/17.
 */
public class TopRated extends Fragment implements MovieGridRecyclerViewAdapter.ItemClickListener {

    MovieGridRecyclerViewAdapter adapter;
    GridLayoutManager layoutManager;
    List<ThumbMovieObject> thumbMovieObjects;
    RecyclerView recyclerView;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.top_rated, container, false);

        thumbMovieObjects = new ArrayList<>();
        // set up the RecyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvNumbers);
        layoutManager = new GridLayoutManager(getContext(), Utility.COLOUM_NO);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieGridRecyclerViewAdapter(getContext(), thumbMovieObjects);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        getVedios();
        return rootView;
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

    @Override
    public void onItemClick(View view, int position) {
        Intent i = new Intent(getActivity(), MoveDetailsPageActivity.class);
        i.putExtra("MyClass", thumbMovieObjects.get(position));
        startActivity(i);

    }


    private void getVedios() {

        final ProgressDialog loading = ProgressDialog.show(getContext(), "Fetching Data", "Please wait...", false, false);
        isLoading = true;

        MoveApi service = MovieBaseUrl.getClient(getContext()).create(MoveApi.class);

        Call<PageObject> call = service.getTopRatedPageObject(Utility.apiKey, currentPage);


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
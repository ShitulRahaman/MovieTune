package model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shitul on 5/3/17.
 */
public class PageObject {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<ThumbMovieObject> results;

    @SerializedName("dates")
    private Dates dates;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ThumbMovieObject> getResults() {
        return results;
    }

    public void setResults(List<ThumbMovieObject> results) {
        this.results = results;
    }

    public Dates getDates() {
        return dates;
    }

    public void setDates(Dates dates) {
        this.dates = dates;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}

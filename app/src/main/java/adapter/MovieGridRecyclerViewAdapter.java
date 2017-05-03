package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.icchastudio.shitul.movietune.R;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import UtilityManager.Utility;
import model.ThumbMovieObject;

/**
 * Created by shitul on 5/2/17.
 */
public class MovieGridRecyclerViewAdapter extends RecyclerView.Adapter<MovieGridRecyclerViewAdapter.ViewHolder>  {

    private List<ThumbMovieObject> mData ;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;


    public MovieGridRecyclerViewAdapter(Context context, List<ThumbMovieObject> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
    }

    public void setmData(List<ThumbMovieObject> mData1){
        mData=mData1;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.layout_grid_cell, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ThumbMovieObject movie = mData.get(position);
        Picasso.with(context).load(getImageFullPath(movie.getPosterPath())).into(holder.movieImageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView movieImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            movieImageView = (ImageView) itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public ThumbMovieObject getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }



 public String getImageFullPath(String imageName){
     return Utility.imageBasePath+""+imageName+"?api_key="+Utility.apiKey;
 }



}


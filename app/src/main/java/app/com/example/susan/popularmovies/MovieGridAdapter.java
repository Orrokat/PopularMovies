package app.com.example.susan.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by susan on 4/27/2016.
 * custom class extending ArrayAdapter to handle non-standard data type
 * based on code from android-custom-arrayadapter-gridview exapmle
 */
public class MovieGridAdapter extends ArrayAdapter<MovieData> {
    private final String LOG_TAG = "MovieGridAdapter";
    /**
     * Custom constructor since there is no Image adapter in the base class
     * @param context  current context so we can inflate the layout
     * @param movieList  list of movie data which includes the poster id
     */
    public MovieGridAdapter(Activity context, List<MovieData> movieList)
    {
        super(context, 0, movieList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieData moviedata = getItem(position);

        //if this view has not been inflated yet, inflate it
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_list_item, parent, false);
        }

        ImageView posterView = (ImageView) convertView.findViewById(R.id.movie_poster_image);
        Picasso.with(getContext()).load(buildImageUrl(moviedata.posterId)).into(posterView);

        return convertView;

    }
    public static String buildImageUrl(String posterId){
        String imageSize = "w185";
        String imageUrl = "http://image.tmdb.org/t/p/" + imageSize + posterId;
        return imageUrl;
    }


}

package app.com.example.susan.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Fragment class for the main activity screen.
 * Retrieve data from themoviedb
 * Format the data into an array of MovieData items
 * Display the movie posters in a grid using Picasso
 * When a poster is clicked, launch to detail activity
 */
public class MovieListFragment extends Fragment {
    private MovieGridAdapter movieGridAdapter;
    private final String LOG_TAG = "MovieListFragment";
    public MovieListFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<MovieData> movieList = new ArrayList<MovieData>();

        movieGridAdapter = new MovieGridAdapter(getActivity(), movieList);
        final View rootView = inflater.inflate(R.layout.movie_list_fragment, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.movie_poster_grid);
        gridView.setAdapter(movieGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailIntent = new Intent(getActivity(), MovieDetailActivity.class);
                MovieData aMovie = movieGridAdapter.getItem(i);
                detailIntent.putExtra(getString(R.string.bundle_movie_id), aMovie.movieId);
                detailIntent.putExtra(getString(R.string.bundle_title), aMovie.movieTitle);
                detailIntent.putExtra(getString(R.string.bundle_release_date), aMovie.releaseDate);
                detailIntent.putExtra(getString(R.string.bundle_average), aMovie.voteAverage);
                detailIntent.putExtra(getString(R.string.bundle_overview), aMovie.plotSynopsis);
                detailIntent.putExtra(getString(R.string.bundle_poster_id), aMovie.posterId);
                startActivity(detailIntent);}
        });

        return rootView;
    }
    public void onStart() {
        super.onStart();
        updateMovies();
    }
    private void updateMovies() {

        FetchMovieDataTask fetch = new FetchMovieDataTask();
        fetch.execute(null, null, null);
    }

    public class FetchMovieDataTask extends AsyncTask<String, Void, MovieData[]> {
        @Override
        protected MovieData[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;
            try {
                // Construct the URL for the query
                UriBuilder builder = new UriBuilder();
                URL url = new URL(builder.getURL());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                     return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {

                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(MovieData[] results)
        {
            if(results != null) {
                movieGridAdapter.clear();
                for (MovieData oneMovie : results
                        ) {
                    movieGridAdapter.add(oneMovie);

                }
            }
        }
    }

    private MovieData[] getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String TMD_RESULTS = "results";
        final String TMD_MOVIE_ID = "id";
        final String TMD_TITLE = "original_title";
        final String TMD_SYNOPSIS = "overview";
        final String TMD_RELEASE_DATE = "release_date";
        final String TMD_POSTER_PATH = "poster_path";
        final String TMD_VOTE_AVERAGE = "vote_average";
        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(TMD_RESULTS);

        MovieData[] movieResults = new MovieData[movieArray.length()];
        for(int i = 0; i < movieArray.length(); i++) {

            JSONObject aMovie = movieArray.getJSONObject(i);
            String id = aMovie.getString(TMD_MOVIE_ID);
            String title = aMovie.getString(TMD_TITLE);
            String synopsis = aMovie.getString(TMD_SYNOPSIS);
            String releaseDate = aMovie.getString(TMD_RELEASE_DATE);
            String posterPath = aMovie.getString(TMD_POSTER_PATH);
            String voteAverage = aMovie.getString(TMD_VOTE_AVERAGE);
            movieResults[i] = new MovieData(id, title, releaseDate,
                    posterPath, voteAverage, synopsis);
        }

        return movieResults;

    }
    private class UriBuilder {

        public static final String APIID = "&api_key=";
        public static final String SORT_BY = "?sort_by=";
        public static final String POPULAR ="0";


        private String getURL() {

            SharedPreferences sharedPref =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortBy = sharedPref.getString((getString(R.string.pref_name_sort_by)), POPULAR);

            String sortOrder;
            if(sortBy.equals(POPULAR)){
                sortOrder = getString(R.string.sort_popular);
            }else{
                sortOrder = getString(R.string.sort_average);
            }

            String finalURL = getString(R.string.basic_URL)
                    + SORT_BY + sortOrder
                    + APIID + BuildConfig.THE_MOVIE_DB_API_KEY;
            return finalURL;


        }
    }

}



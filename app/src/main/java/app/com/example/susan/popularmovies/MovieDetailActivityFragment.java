package app.com.example.susan.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Movie Detail Fragment receives data through the intent bundle for a single
 * movie and displays the data to the user
 */
public class MovieDetailActivityFragment extends Fragment {
    private final String LOG_TAG = "MovieDetailActivityFrag";
    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        TextView vTitle = (TextView)rootView.findViewById(R.id.et_movie_title);
        ImageView vPoster = (ImageView) rootView.findViewById(R.id.iv_movie_poster);
        TextView vReleaseDate = (TextView)rootView.findViewById(R.id.et_release_date);
        TextView vVoteAverage = (TextView)rootView.findViewById(R.id.et_vote_average);
        TextView vSynopsis = (TextView)rootView.findViewById(R.id.et_synopsis);
        Intent detailIntent = getActivity().getIntent();
        Bundle bundle = detailIntent.getExtras();

        if(bundle != null){
            vTitle.setText((String) bundle.get(getString(R.string.bundle_title)));
            String posterId = (String) bundle.get(getString(R.string.bundle_poster_id));
            Picasso.with(getContext()).load(MovieGridAdapter.buildImageUrl(posterId)).into(vPoster);
            vReleaseDate.setText((String) bundle.get(getString(R.string.bundle_release_date)));
            vVoteAverage.setText((String) bundle.get(getString(R.string.bundle_average)));
            vSynopsis.setText((String) bundle.get(getString(R.string.bundle_overview)));
        }
        return rootView;
    }
}

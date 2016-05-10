package app.com.example.susan.popularmovies;

/**
 * Created by susan on 4/28/2016.
 * Container class to hold a single instance of movie data
 */
public class MovieData {
    String movieId;
    String movieTitle;
    String releaseDate;
    String posterId;
    String voteAverage;
    String plotSynopsis;


    public MovieData(String movieId, String movieTitle, String releaseDate,
                     String posterId, String voteAverage, String plotSynopsis) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.releaseDate = releaseDate;
        this.posterId = posterId;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }



}

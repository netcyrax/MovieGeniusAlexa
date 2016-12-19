package core;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.MovieSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Holds the non-Alexa specific logic.
 */
public class MovieGenieAlexaCore {

    private static final Logger log = LoggerFactory.getLogger(MovieGenieAlexaCore.class);
    private static final String EMPTY_STRING = "";

    private static MovieGenieAlexaCore INSTANCE;
    private static int MAX_MOVIES_NUM = 5;

    private final Gson gson;

    private MovieGenieAlexaCore() {
        gson = new Gson();
    }

    public static MovieGenieAlexaCore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MovieGenieAlexaCore();
        }
        return INSTANCE;
    }

    /**
     * Converts a list of movies to JSON.
     */
    public String toJson(List<MovieSet> movies) {
        return gson.toJson(movies);
    }

    /**
     * Converts a JSON to list of movies.
     */
    public List<MovieSet> toMovies(Optional<String> json) {
        if (!json.isPresent()) {
            return Lists.newArrayList();
        }
        return gson.fromJson(json.get(), new TypeToken<ArrayList<MovieSet>>() {
        }.getType());
    }

    /**
     * Given a movie genre returns a number of movies matching this criteria.
     */
    public List<MovieSet> queryMovies(String genre) {
        List<MovieSet> result = Lists.newArrayList();
        String url = Config.queryUrl(genre);
        log.info("url: " + url);
        String json = getJson(url);
        log.info("response: " + json);
        List<MovieSet> movies = gson.fromJson(json, new TypeToken<ArrayList<MovieSet>>() {
        }.getType());

        // Shuffle and return MAX_MOVIES_NUM movies
        if (movies != null && movies.size() > MAX_MOVIES_NUM) {
            Collections.shuffle(movies);
            for (int i = 0; i < Math.min(movies.size(), MAX_MOVIES_NUM); i++) {
                result.add(movies.get(i));
            }
        }
        return result;
    }

    private String getJson(String urlRaw) {
        try {
            return Unirest.get(urlRaw)
                    .asString().getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return EMPTY_STRING;
    }
}

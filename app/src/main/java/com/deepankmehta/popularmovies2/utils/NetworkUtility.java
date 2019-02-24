/*
 * Copyright 2019 Deepank Mehta. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * You may not use this file; except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * Distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * Limitations under the License.
 *
 */

package com.deepankmehta.popularmovies2.utils;

import android.content.Context;
import android.net.Uri;

import com.deepankmehta.popularmovies2.Movie;
import com.deepankmehta.popularmovies2.Review;
import com.deepankmehta.popularmovies2.Trailer;
import com.deepankmehta.popularmovies2.adapters.TrailerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtility {

    static String BASE_URL = "https://api.themoviedb.org/3/movie";
    static String PARAM_API_KEY = "api_key";
    static String API_KEY = "enter_your_api_key";
    static String PARAM_LANGUAGE = "language";
    static String LANGUAGE = "en-US";
    static String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
    static String RESULTS = "results";
    static String POSTER_PATH = "poster_path";
    static String TITLE = "title";
    static String VOTE = "vote_average";
    static String OVERVIEW = "overview";
    static String RELEASE_DATE = "release_date";
    static String ID = "id";
    static String VIDEOS = "videos";
    static String TRAILER_KEY = "key";
    static String TRAILER_NAME = "name";
    static String REVIEWS = "reviews";
    static String REVIEW_AUTHOR = "author";
    static String REVIEW_CONTENT = "content";

    public static URL buildUrl(String searchQuery) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(searchQuery)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildTrailerUrl(int id) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(String.valueOf(id))
                .appendEncodedPath(VIDEOS)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static URL buildReviewUrl(int id) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(String.valueOf(id))
                .appendEncodedPath(REVIEWS)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Movie[] getMovieJson(Context context, String json) throws JSONException {
        JSONObject movieJson = new JSONObject(json);
        JSONArray movieArray = movieJson.getJSONArray(RESULTS);
        Movie[] movieResults = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            String poster_path;
            String title;
            String date;
            String rate;
            String overview;
            int id;

            Movie movie = new Movie();
            poster_path = movieArray.getJSONObject(i).getString(POSTER_PATH);
            title = movieArray.getJSONObject(i).getString(TITLE);
            date = movieArray.getJSONObject(i).getString(RELEASE_DATE);
            rate = movieArray.getJSONObject(i).getString(VOTE);
            overview = movieArray.getJSONObject(i).getString(OVERVIEW);
            id = movieArray.getJSONObject(i).getInt(ID);

            movie.setPoster(POSTER_BASE_URL + poster_path);
            movie.setTitle(title);
            movie.setDate(date);
            movie.setVote(rate);
            movie.setOverview(overview);
            movie.setId(id);

            movieResults[i] = movie;
        }
        return movieResults;
    }

    public static Trailer[] getTrailerJson(Context context, String json) throws JSONException {
        JSONObject trailerJson = new JSONObject(json);
        JSONArray trailerArray = trailerJson.getJSONArray(RESULTS);
        Trailer[] trailerResults = new Trailer[trailerArray.length()];

        for (int i = 0; i < trailerArray.length(); i++) {
            String key;
            String name;
            Trailer trailer = new Trailer();
            key = trailerArray.getJSONObject(i).getString(TRAILER_KEY);
            name = trailerArray.getJSONObject(i).getString(TRAILER_NAME);
            trailer.setKey(key);
            trailer.setName(name);
            trailerResults[i] = trailer;
        }
        return trailerResults;
    }

    public static Review[] getReviewJson(Context context, String json) throws JSONException {
        JSONObject reviewJson = new JSONObject(json);
        JSONArray reviewArray = reviewJson.getJSONArray(RESULTS);
        Review[] reviewResults = new Review[reviewArray.length()];

        for (int i = 0; i < reviewArray.length(); i++) {
            String author;
            String content;
            Review review = new Review();
            author = reviewArray.getJSONObject(i).getString(REVIEW_AUTHOR);
            content = reviewArray.getJSONObject(i).getString(REVIEW_CONTENT);
            review.setAuthor(author);
            review.setContent(content);
            reviewResults[i] = review;
        }
        return reviewResults;
    }
}
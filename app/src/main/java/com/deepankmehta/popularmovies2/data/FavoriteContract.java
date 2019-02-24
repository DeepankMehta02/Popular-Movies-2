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

package com.deepankmehta.popularmovies2.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {

    public static final String AUTHORIY = "com.deepankmehta.popularmovies2.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORIY);
    public static final String PATH_FAVOTITES = "favorites";

    public static final class FavoriteAdd implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOTITES).build();
        public static final String TABLE_NAME = "favorites";

        public static final String MOVIE_ID = "movieId";
        public static final String MOVIE_NAME = "movieName";
        public static final String MOVIE_POSTER = "moviePoster";
        public static final String MOVIE_RATE = "movieRate";
        public static final String MOVIE_RELEASE = "movieRelease";
        public static final String MOVIE_OVERVIEW = "movieOverview";
    }
}

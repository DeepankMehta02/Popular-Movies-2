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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites";
    private static final int DATABASE_VERSION = 1;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                FavoriteContract.FavoriteAdd.TABLE_NAME + " (" +
                FavoriteContract.FavoriteAdd._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteContract.FavoriteAdd.MOVIE_ID + " INTEGER NOT NULL," +
                FavoriteContract.FavoriteAdd.MOVIE_NAME + " TEXT NOT NULL," +
                FavoriteContract.FavoriteAdd.MOVIE_POSTER + " TEXT NOT NULL," +
                FavoriteContract.FavoriteAdd.MOVIE_RELEASE + " TEXT NOT NULL," +
                FavoriteContract.FavoriteAdd.MOVIE_RATE + " TEXT NOT NULL," +
                FavoriteContract.FavoriteAdd.MOVIE_OVERVIEW + " TEXT NOT NULL" + " ); ";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteAdd.TABLE_NAME);
        onCreate(db);
    }
}

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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class FavoriteContentProvider extends ContentProvider {

    private static final int FAVORITES = 1;
    private static final int FAVORITES_WITH_ID = 2;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteContract.AUTHORIY, FavoriteContract.PATH_FAVOTITES, FAVORITES);
        uriMatcher.addURI(FavoriteContract.AUTHORIY, FavoriteContract.PATH_FAVOTITES + "/#", FAVORITES_WITH_ID);
        return uriMatcher;
    }

    private FavoriteDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new FavoriteDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match) {
            case FAVORITES:
                returnCursor = db.query(FavoriteContract.FavoriteAdd.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};

                returnCursor = db.query(FavoriteContract.FavoriteAdd.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
                default:
                    throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITES:
                return "vnd.android.cursor.dir" + "/" + FavoriteContract.AUTHORIY + "/" + FavoriteContract.PATH_FAVOTITES;
            case FAVORITES_WITH_ID:
                return "vnd.android.cursor.item" + "/" + FavoriteContract.AUTHORIY + "/" + FavoriteContract.PATH_FAVOTITES;
                default:
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITES:
                long id = db.insert(FavoriteContract.FavoriteAdd.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoriteContract.FavoriteAdd.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert rown into " + uri);
                }
                break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int favoriteDeleted;

        switch (match) {
            case FAVORITES:
                favoriteDeleted = db.delete(FavoriteContract.FavoriteAdd.TABLE_NAME, selection, selectionArgs);
                break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (favoriteDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        } return favoriteDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int favoriteUpdated;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                favoriteUpdated = mDbHelper.getWritableDatabase().update(
                        FavoriteContract.FavoriteAdd.TABLE_NAME, values, "_id=?", new String[]{id}
                );
                break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (favoriteUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        } return favoriteUpdated;
    }
}

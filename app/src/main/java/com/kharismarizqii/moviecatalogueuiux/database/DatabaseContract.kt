package com.kharismarizqii.moviecatalogueuiux.database

import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.kharismarizqii.moviecatalogueuiux"
    const val SCHEME = "content"

    internal class FavoriteMovieColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite_movie"
            const val ID = "_id"
            const val TITLE = "title"
            const val OVERVIEW = "overview"
            const val RATING = "rating"
            const val RELEASE = "release"
            const val POSTER_PATH = "poster_path"
            const val BACKDROP_PATH = "backdrop_path"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }

    internal class FavoriteTVColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite_tv"
            const val ID = "_id"
            const val TITLE = "title"
            const val OVERVIEW = "overview"
            const val RATING = "rating"
            const val RELEASE = "release"
            const val POSTER_PATH = "poster_path"
            const val BACKDROP_PATH = "backdrop_path"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }

    fun getColumnString(cursor: Cursor, columName: String): String{
        return cursor.getString(cursor.getColumnIndex(columName))
    }
    fun getColumnInt(cursor: Cursor, columName: String): Int{
        return cursor.getInt(cursor.getColumnIndex(columName))
    }
    fun getColumDouble(cursor: Cursor, columName: String): Double{
        return cursor.getDouble(cursor.getColumnIndex(columName))
    }
}
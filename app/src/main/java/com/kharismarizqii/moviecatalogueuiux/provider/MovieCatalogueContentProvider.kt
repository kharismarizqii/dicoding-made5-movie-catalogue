package com.kharismarizqii.moviecatalogueuiux.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.kharismarizqii.moviecatalogueuiux.database.DatabaseContract.AUTHORITY
import com.kharismarizqii.moviecatalogueuiux.database.DatabaseContract.FavoriteMovieColumns.Companion.CONTENT_URI
import com.kharismarizqii.moviecatalogueuiux.database.DatabaseContract.FavoriteMovieColumns.Companion.TABLE_NAME
import com.kharismarizqii.moviecatalogueuiux.database.FavoriteMovieHelper

class MovieCatalogueContentProvider : ContentProvider() {

    companion object{
        private const val MOVIE = 1
        private const val MOVIE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var movieHelper: FavoriteMovieHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", MOVIE_ID)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when(MOVIE_ID) {
            sUriMatcher.match(uri) -> movieHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (MOVIE){
            sUriMatcher.match(uri) -> movieHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        movieHelper = FavoriteMovieHelper.getInstance(context as Context)
        movieHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        Log.e("uri","uri: $uri")
        val cursor: Cursor?
        when(sUriMatcher.match(uri)){
            MOVIE -> cursor = movieHelper.queryAll()
            MOVIE_ID -> cursor = movieHelper.queryById(uri.pathSegments.toString())
            else -> cursor = null
        }
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}

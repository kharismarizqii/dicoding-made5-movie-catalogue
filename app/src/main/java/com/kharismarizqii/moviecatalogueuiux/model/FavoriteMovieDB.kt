package com.kharismarizqii.moviecatalogueuiux.model

import android.database.Cursor
import android.os.Parcelable
import com.kharismarizqii.moviecatalogueuiux.database.DatabaseContract
import com.kharismarizqii.moviecatalogueuiux.database.DatabaseContract.FavoriteMovieColumns.Companion.ID
import com.kharismarizqii.moviecatalogueuiux.database.DatabaseContract.getColumDouble
import com.kharismarizqii.moviecatalogueuiux.database.DatabaseContract.getColumnInt
import com.kharismarizqii.moviecatalogueuiux.database.DatabaseContract.getColumnString
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteMovieDB(
    var id: Int = 0,
    var title: String? = null,
    var rating: Double = 0.0,
    var overview: String? = null,
    var releaseDate: String? = null,
    var posterPath: String? = null,
    var backdropPath: String? = null
):Parcelable {
    constructor(cursor: Cursor) : this(){
        this.id = getColumnInt(cursor, ID)
        this.title = getColumnString(cursor, DatabaseContract.FavoriteMovieColumns.TITLE)
        this.rating = getColumDouble(cursor, DatabaseContract.FavoriteMovieColumns.RATING)
        this.overview = getColumnString(cursor, DatabaseContract.FavoriteMovieColumns.OVERVIEW)
        this.releaseDate = getColumnString(cursor, DatabaseContract.FavoriteMovieColumns.RELEASE)
        this.posterPath = getColumnString(cursor, DatabaseContract.FavoriteMovieColumns.POSTER_PATH)
        this.backdropPath = getColumnString(cursor, DatabaseContract.FavoriteMovieColumns.BACKDROP_PATH)
    }
}
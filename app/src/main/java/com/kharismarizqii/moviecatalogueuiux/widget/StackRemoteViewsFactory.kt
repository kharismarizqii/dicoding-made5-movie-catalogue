package com.kharismarizqii.moviecatalogueuiux.widget

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Binder
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.kharismarizqii.moviecatalogueuiux.R
import com.kharismarizqii.moviecatalogueuiux.database.DatabaseContract.FavoriteMovieColumns.Companion.CONTENT_URI
import com.kharismarizqii.moviecatalogueuiux.helper.MappingHelper
import com.kharismarizqii.moviecatalogueuiux.model.FavoriteMovieDB
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.concurrent.ExecutionException

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory{

    private val mWidgetItems = ArrayList<FavoriteMovieDB>()
    private var pathPoster = "https://image.tmdb.org/t/p/w300"
    private var cursor: Cursor? = null

    override fun onCreate() {
        cursor = mContext.contentResolver.query(CONTENT_URI, null, null, null, null)
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun onDataSetChanged() {
        if (cursor != null){
            Log.d("isi cursor", "cursor null:")

            cursor?.close()
        }

        Log.d("isi cursor", "cursor:")

        val identityToken = Binder.clearCallingIdentity()
//        val handlerThread = HandlerThread("DataObserver")
//        handlerThread.start()
//        val handler = Handler(handlerThread.looper)
//        val myObserver = object : ContentObserver(handler) {
//            override fun onChange(self: Boolean) {
//                loadMoviesAsync()
//            }
//        }
//
//        mContext.contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        cursor= mContext.contentResolver?.query(CONTENT_URI, null, null, null, null)

        Binder.restoreCallingIdentity(identityToken)
    }

    private fun loadMoviesAsync(){
        GlobalScope.launch(Dispatchers.Main) {

            val deferredMovies = async(Dispatchers.IO) {
                Log.e("uri","uri: $CONTENT_URI")
                cursor= mContext.contentResolver?.query(CONTENT_URI, null, null, null, null)
                Log.d("isi cursor", "cursor: ${cursor?.count}")
                cursor?.let { MappingHelper.mapCursorToArrayList(it) }
            }
            val movies = deferredMovies.await()
            Log.d("isi cursor", "movies.size: ${movies?.size}")
            if (movies?.size!! >0){
                mWidgetItems.addAll(movies)
            } else{
                mWidgetItems.addAll(ArrayList())
            }

        }
        Log.d("isi cursor", "cursor2:")

    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        val favorite: FavoriteMovieDB = getItem(position)

        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        var bitmap: Bitmap? = null
        try {
            bitmap = Glide.with(mContext)
                .asBitmap()
                .load(pathPoster+favorite.posterPath)
                .submit()
                .get()
        }catch (e: InterruptedException){
            e.printStackTrace()
        } catch (e: ExecutionException){
            e.printStackTrace()
        }

        rv.setImageViewBitmap(R.id.imageView, bitmap)

        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    private fun getItem(position: Int): FavoriteMovieDB {
        if (!cursor?.moveToPosition(position)!!){
            throw IllegalStateException("Position invalid")
        }
        return FavoriteMovieDB(cursor!!)
    }

    override fun getCount(): Int {
        return cursor!!.count
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {

    }

}
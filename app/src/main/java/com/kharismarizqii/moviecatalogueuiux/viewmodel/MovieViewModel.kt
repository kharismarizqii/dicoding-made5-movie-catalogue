package com.kharismarizqii.moviecatalogueuiux.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.kharismarizqii.moviecatalogueuiux.fragment.MovieFragment
import com.kharismarizqii.moviecatalogueuiux.model.MovieDB
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var listMovieDB: ArrayList<MovieDB>

    companion object {
        private val TAG = MovieViewModel::class.java.simpleName
    }
    private val listMovies = MutableLiveData<ArrayList<MovieDB>>()

    private fun getLang(): String{
        var currentLang = Locale.getDefault().toString()
        Log.d(TAG, "getLang: $currentLang")
        if(currentLang.equals("in_ID")){
            currentLang = "id-ID"
        }
        return currentLang
    }

    internal fun setMovies(type : Int, query: String){
        val current = type
        Log.d(TAG, "getMovie: Mulai... $current")
        listMovieDB = ArrayList<MovieDB>()
        val lang = getLang()
        var url = ""
        when{
            current == 1 ->{
                url = "https://api.themoviedb.org/3/movie/now_playing?language=$lang&api_key=${MovieFragment.APP_ID}"
            }
            current == 2 ->{
                url = "https://api.themoviedb.org/3/search/movie?api_key=${MovieFragment.APP_ID}&language=$lang&query=$query"
            }
        }
        Log.d("URL", "url: $url")
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener{ response ->
            try {
                val jsonArray: JSONArray = response.getJSONArray("results")
                for (position in 0 until jsonArray.length()-1){
                    val data: JSONObject = jsonArray.getJSONObject(position)
                    val movieDB = MovieDB(
                        data.getInt("id"),
                        data.getString("title"),
                        data.getDouble("vote_average"),
                        data.getString("overview"),
                        data.getString("release_date"),
                        data.getString("poster_path"),
                        data.getString("backdrop_path")
                    )
                    listMovieDB.add(movieDB)
                }
                listMovies.postValue(listMovieDB)
                Log.d(TAG, "listMovieDB1: "+listMovieDB)
            }catch (e: JSONException){
                e.printStackTrace()
                Log.d(TAG, "error: $e")
            }

        },Response.ErrorListener {response:VolleyError->
            Log.d("errorslider", " : $response")
        })
        Volley.newRequestQueue(getApplication()).add(jsonObjectRequest)
    }

    internal fun getMovies(): LiveData<ArrayList<MovieDB>>{
        return listMovies
    }
}
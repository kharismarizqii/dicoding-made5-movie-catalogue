package com.kharismarizqii.moviecatalogueuiux.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kharismarizqii.moviecatalogueuiux.BuildConfig
import com.kharismarizqii.moviecatalogueuiux.DetailMovieActivity
import com.kharismarizqii.moviecatalogueuiux.MainActivity
import com.kharismarizqii.moviecatalogueuiux.R
import com.kharismarizqii.moviecatalogueuiux.adapter.MovieAdapter
import com.kharismarizqii.moviecatalogueuiux.model.MovieDB
import com.kharismarizqii.moviecatalogueuiux.viewmodel.MovieViewModel


/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    private lateinit var rvMovie: RecyclerView
    private lateinit var prBar: ProgressBar
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var adapter: MovieAdapter
    private var querySearch: String? = null

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        internal const val APP_ID = BuildConfig.TMDB_API_KEY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        querySearch = savedInstanceState?.getString("query")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("query", querySearch)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("URL", "query: $query")
                Toast.makeText(context, query, Toast.LENGTH_SHORT).show()
                querySearch = query
                showRecyclerCardView(querySearch)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMovie = view.findViewById(R.id.rv_movie)
        prBar = view.findViewById(R.id.progressBar)

        setHasOptionsMenu(true)
        showRecyclerCardView(querySearch)
        rvMovie.setHasFixedSize(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            prBar.visibility = View.VISIBLE
        } else {
            prBar.visibility = View.GONE
        }
    }

    private fun showRecyclerCardView(query: String?) {
        adapter = MovieAdapter()
        adapter.notifyDataSetChanged()

        rvMovie.layoutManager = LinearLayoutManager(context)
        rvMovie.adapter = adapter

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        if (query == null) {
            movieViewModel.setMovies(1, "")
        } else {
            movieViewModel.setMovies(2, query)
        }

        showLoading(true)


        movieViewModel.getMovies().observe(this, androidx.lifecycle.Observer { movieItems ->
            if (movieItems != null) {
                adapter.setData(movieItems)
                showLoading(false)
            }
        })

        adapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: MovieDB) {
                showSelectedData(data)
            }
        })
    }

    private fun showSelectedData(movie: MovieDB) {

        val moveObjectIntent = Intent(activity, DetailMovieActivity::class.java)
        moveObjectIntent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movie)
        startActivity(moveObjectIntent)
    }

}

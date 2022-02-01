package com.example.android.githubsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.moshi.Moshi
import com.squareup.moshi.JsonAdapter
import com.example.android.githubsearch.data.GitHubRepo
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : AppCompatActivity() {
    private val apiBaseUrl = "https://api.github.com"
    private val tag = "MainActivity"

    private val repoAdapter = GitHubRepoListAdapter()

    private lateinit var requestQueue: RequestQueue

    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        val searchBoxET: EditText = findViewById(R.id.et_search_box)
        val searchBtn: Button = findViewById(R.id.btn_search)

        searchErrorTV = findViewById(R.id.tv_search_error)
        loadingIndicator = findViewById(R.id.loading_indicator)

        searchResultsListRV = findViewById(R.id.rv_search_results)
        searchResultsListRV.layoutManager = LinearLayoutManager(this)
        searchResultsListRV.setHasFixedSize(true)

        searchResultsListRV.adapter = repoAdapter

        searchBtn.setOnClickListener {
            val query = searchBoxET.text.toString()
            if (!TextUtils.isEmpty(query)) {
                doRepoSearch(query)
                searchResultsListRV.scrollToPosition(0)
            }
        }
    }

    fun doRepoSearch(q: String, sort: String = "stars") {
        val url = "$apiBaseUrl/search/repositories?q=$q&sort=$sort"

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<GitHubSearchResults> =
            moshi.adapter(GitHubSearchResults::class.java)

        val req = StringRequest(
            Request.Method.GET,
            url,
            {
                var results = jsonAdapter.fromJson(it)
                Log.d(tag, results.toString())
                repoAdapter.updateRepoList(results?.items)
                loadingIndicator.visibility = View.INVISIBLE
                searchResultsListRV.visibility = View.VISIBLE
            },
            {
                Log.d(tag, "Error fetching from $url: ${it.message}")
                loadingIndicator.visibility = View.INVISIBLE
                searchErrorTV.visibility = View.VISIBLE
            }
        )

        loadingIndicator.visibility = View.VISIBLE
        searchResultsListRV.visibility = View.INVISIBLE
        searchErrorTV.visibility = View.INVISIBLE
        requestQueue.add(req)
    }

    private data class GitHubSearchResults(
        val items: List<GitHubRepo>
    )
}
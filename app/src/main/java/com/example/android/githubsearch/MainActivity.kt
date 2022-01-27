package com.example.android.githubsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.moshi.Moshi
import com.squareup.moshi.JsonAdapter
import com.example.android.githubsearch.data.GitHubRepo
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : AppCompatActivity() {
    private val apiBaseUrl = "https://api.github.com"
    private val tag = "MainActivity"

    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        val searchBoxET: EditText = findViewById(R.id.et_search_box)
        val searchBtn: Button = findViewById(R.id.btn_search)

        val searchResultsListRV: RecyclerView = findViewById(R.id.rv_search_results)
        searchResultsListRV.layoutManager = LinearLayoutManager(this)
        searchResultsListRV.setHasFixedSize(true)

        val adapter = GitHubRepoListAdapter()
        searchResultsListRV.adapter = adapter

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
                Log.d(tag, it)
            },
            {
                Log.d(tag, "Error fetching from $url: ${it.message}")
            }
        )

        requestQueue.add(req)
    }

    private data class GitHubSearchResults(
        val items: List<GitHubRepo>
    )
}
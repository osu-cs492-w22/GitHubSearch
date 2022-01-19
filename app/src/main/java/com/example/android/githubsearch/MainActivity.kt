package com.example.android.githubsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.githubsearch.data.GitHubRepo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dummySearchResults = listOf<GitHubRepo>(
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results")
        )

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
                adapter.updateSearchResults(dummySearchResults)
                searchBoxET.setText("")
                searchResultsListRV.scrollToPosition(0)
            }
        }
    }
}
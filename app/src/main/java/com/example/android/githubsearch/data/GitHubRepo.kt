package com.example.android.githubsearch.data

import com.squareup.moshi.Json

data class GitHubRepo(
    @Json(name = "full_name") val name: String,
    val description: String,
    @Json(name = "html_url") val url: String,
    @Json(name = "stargazers_count") val stars: Int
)

package com.example.androidnews

import android.util.Log
import com.google.android.gms.common.server.converter.StringToIntConverter
import okhttp3.Address
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject

object NewsManager {
    val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)
        okHttpClient = builder.build()
    }


    const val logD = "NewsManager"

    /**
     * A modular search api
     * Fetches News based on provided [NewsAPI]
     * @param newsAPI_Key
     * @param query used for the search
     * @param country default value is us
     * @param category default value is business
     * @param newsAPI changes API Source possible options are
     * EVERYTHING_SEARCH, TOP_HEADLINES, SOURCES
     * @return a list of News Objects
     */
    enum class NewsAPI(api: String) {
        EVERYTHING_SEARCH("everythingSearch"),
        TOP_HEADLINES("topHeadlines"),
        SOURCES("sources")
    }

    fun fetchHeadlinesNews(newsAPI_Key: String, queryCategory: String): List<News> {
        val newsList = mutableListOf<News>()
        //build the request
        var request: Request = Request.Builder()
            .url("https://newsapi.org/v2/top-headlines?country=us&category=${queryCategory}")
            .header("Authorization", "$newsAPI_Key")
            .build()

        //call the request
        val response = okHttpClient.newCall(request).execute()

        //retrieve the json body
        val responseString = response.body?.string()

        //check response is not empty or null
        if (response.isSuccessful && !responseString.isNullOrEmpty()) {
            //parse json string
            val json = JSONObject(responseString)

            val articles = json.getJSONArray("articles")
            //parse as News Object to headlines
            for (i in 0 until articles.length()) {
                var currNews = articles.getJSONObject(i)
                val title = currNews.getString("title")
                val thumbnailUrl = currNews.getString("urlToImage")
                val source = currNews.getJSONObject("source").getString("name")
                val url = currNews.getString("url")
                val content = currNews.getString("content")
                newsList.add(News(title, thumbnailUrl, source, url, content))
            }
        }

        return newsList
    }


    fun fetchMapNews(newsAPI_Key: String, address: List<android.location.Address>): List<News> {
        val newsList = mutableListOf<News>()
        //build the request
        var request: Request = when {
            address?.first()?.adminArea.isNullOrEmpty() -> {
                return emptyList()
            }
            address?.first()?.countryCode == "US" -> {
                Request.Builder()
                    .url("https://newsapi.org/v2/everything?qInTitle=${address?.first()?.adminArea}")
                    .header("Authorization", "$newsAPI_Key")
                    .build()
            }
            else -> {
                Request.Builder()
                    .url("https://newsapi.org/v2/everything?qInTitle=${address?.first()?.countryName}")
                    .header("Authorization", "$newsAPI_Key")
                    .build()
            }
        }
        //call the request
        val response = okHttpClient.newCall(request).execute()
        //retrieve the json body
        val responseString = response.body?.string()
        //check response is not empty or null
        if (response.isSuccessful && !responseString.isNullOrEmpty()) {
            //parse json string
            val json = JSONObject(responseString)
            val articles = json.getJSONArray("articles")
            //parse as News Object to headlines
            for (i in 0 until articles.length()) {
                var currNews = articles.getJSONObject(i)
                val title = currNews.getString("title")
                val thumbnailUrl = currNews.getString("urlToImage")
                val source = currNews.getJSONObject("source").getString("name")
                val url = currNews.getString("url")
                val content = currNews.getString("content")
                newsList.add(News(title, thumbnailUrl, source, url, content))
            }
        }
        return newsList
    }

    fun fetchQuickSearchQuery(newsAPI_Key: String, query: String): List<News> {
        val newsList = mutableListOf<News>()
        //build the request
        var request: Request =
            Request.Builder()
                .url("https://newsapi.org/v2/everything?q=${query}")
                .header("Authorization", "$newsAPI_Key")
                .build()
        //call the request
        val response = NewsManager.okHttpClient.newCall(request).execute()
        //retrieve the json body
        val responseString = response.body?.string()
        //check response is not empty or null
        if (response.isSuccessful && !responseString.isNullOrEmpty()) {
            //parse json string
            val json = JSONObject(responseString)
            val articles = json.getJSONArray("articles")
            //parse as News Object to headlines
            for (i in 0 until articles.length()) {
                var currNews = articles.getJSONObject(i)
                val title = currNews.getString("title")
                val thumbnailUrl = currNews.getString("urlToImage")
                val source = currNews.getJSONObject("source").getString("name")
                val url = currNews.getString("url")
                val content = currNews.getString("content")
                newsList.add(News(title, thumbnailUrl, source, url, content))
            }
        }
        return newsList
    }

    fun fetchSources(newsAPI_Key: String, category: String): List<Source> {
        val sourcesList = mutableListOf<Source>()
        //build the request
        var request: Request =
            Request.Builder()
                .url("https://newsapi.org/v2/sources?category=${category}&apiKey=$newsAPI_Key")
                .header("Authorization", "$newsAPI_Key")
                .build()
        //call the request
        val response = NewsManager.okHttpClient.newCall(request).execute()
        //retrieve the json body
        val responseString = response.body?.string()
        //check response is not empty or null
        if (response.isSuccessful && !responseString.isNullOrEmpty()) {
            //parse json string
            val json = JSONObject(responseString)
            val sources = json.getJSONArray("sources")
            //parse as News Object to headlines
            for (i in 0 until sources.length()) {
                var currSource = sources.getJSONObject(i)
                val title = currSource.getString("name")
                val content = currSource.getString("description")
                sourcesList.add(Source(title, content, false))
            }
        } else {
            return emptyList()
        }
        return sourcesList
    }

    fun fetchAdvancedSearchQuery(
        newsAPI_Key: String,
        query: String,
        queryListSources: ArrayList<String>,
    ): MutableList<News> {
        val newsList = mutableListOf<News>()
        //build the request
        var request: Request =
            Request.Builder()
                .url(
                    "https://newsapi.org/v2/everything?q=${query}&sources=${
                        queryListSources.toString().replace("[", "").replace("]", "")
                    }"
                )
                .header("Authorization", "$newsAPI_Key")
                .build()
        //call the request
        val response = NewsManager.okHttpClient.newCall(request).execute()
        //retrieve the json body
        val responseString = response.body?.string()
        //check response is not empty or null
        if (response.isSuccessful && !responseString.isNullOrEmpty()) {
            //parse json string
            val json = JSONObject(responseString)
            val articles = json.getJSONArray("articles")
            //parse as News Object to headlines
            for (i in 0 until articles.length()) {
                var currNews = articles.getJSONObject(i)
                val title = currNews.getString("title")
                val thumbnailUrl = currNews.getString("urlToImage")
                val source = currNews.getJSONObject("source").getString("name")
                val url = currNews.getString("url")
                val content = currNews.getString("content")
                newsList.add(News(title, thumbnailUrl, source, url, content))
            }
        }
        return newsList
    }
}

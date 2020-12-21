package com.example.androidnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_results.*
import org.jetbrains.anko.doAsync

class ResultsActivity : AppCompatActivity() {
    lateinit var results_newsRecycler: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newsAPIKey = getString(R.string.news_API_key)
        setContentView(R.layout.activity_results)
        results_newsRecycler = findViewById(R.id.results_newsRecycler)


        var query = intent.getStringExtra("quickSearchQuery")
        // Perform quick search
        if (query != null) {
            this.title = "Quick Search Results for $query"
            doAsync {
                val news: List<News> =
                    NewsManager.fetchQuickSearchQuery(newsAPIKey, query.toString())
                val newsAdapter = NewsAdapter(news)
                runOnUiThread {
                    results_newsRecycler.adapter = newsAdapter
                    results_newsRecycler.layoutManager = LinearLayoutManager(this@ResultsActivity)
                }
            }
        }
        // perform advance search
        else{
            query = intent.getStringExtra("advancedSearchQuery").toString()
            var category =  intent.getStringExtra("category").toString()
            var queryListSources:ArrayList<String> = intent.getStringArrayListExtra("queryListSources") as ArrayList<String>
            Toast.makeText(this, category.toString(),Toast.LENGTH_SHORT)
            Toast.makeText(this, queryListSources.toString(),Toast.LENGTH_SHORT)
            Log.d("ResultsActivity()", category)
            Log.d("ResultsActivity()", queryListSources.toString())
            this.title = "Advanced Search Results for $query"

            doAsync {
                val news: List<News> =
                    NewsManager.fetchAdvancedSearchQuery(newsAPIKey, query, queryListSources)


                val newsAdapter = NewsAdapter(news)
                runOnUiThread {
                    results_newsRecycler.adapter = newsAdapter
                    results_newsRecycler.layoutManager = LinearLayoutManager(this@ResultsActivity)
                }
            }
        }
    }
}
package com.example.androidnews

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync

class HomeScreenActivity : AppCompatActivity() {
    private lateinit var termSearch: SearchView
    private lateinit var quickSearch: Button
    private lateinit var advancedSearch: Button
    private lateinit var viewMap: Button
    private lateinit var headlinesRecyclerView: RecyclerView
    private lateinit var categorySpinner: Spinner
    private val logD = "HomeScreenActivity"

    companion object {
        var categoryList =
            arrayOf(
                "Business",
                "Entertainment",
                "General",
                "Health",
                "Science",
                "Sports",
                "Technology"
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newsAPIKey = getString(R.string.news_API_key)

        var sharedPrefs = getSharedPreferences("android-news", Context.MODE_PRIVATE)
        setContentView(R.layout.activity_home_screen)

        quickSearch = findViewById(R.id.quickSearch)
        advancedSearch = findViewById(R.id.advancedSearch)
        termSearch = findViewById(R.id.termSearch)
        viewMap = findViewById(R.id.viewMap)
        headlinesRecyclerView = findViewById(R.id.headlinesRecycler)
        categorySpinner = findViewById(R.id.categorySpinner)

        val savedQuery = sharedPrefs.getString("SAVED_QUERY", "")

        quickSearch.isEnabled = savedQuery!!.isNotBlank()
        advancedSearch.isEnabled = savedQuery!!.isNotBlank()

        //load previous query
        termSearch.setQuery(savedQuery, false)

        //listen input change termSearch
        termSearch.setOnSearchClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    ResultsActivity::class.java
                ).putExtra("quickSearchQuery", termSearch.query.toString())
            )
            sharedPrefs.edit().putString("SAVED_QUERY", termSearch.query.toString()).apply()
        }
        termSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //Save search
                startActivity(
                    Intent(
                        applicationContext,
                        ResultsActivity::class.java
                    ).putExtra("quickSearchQuery", termSearch.query.toString())
                )
                sharedPrefs.edit().putString("SAVED_QUERY", query).apply()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                quickSearch.isEnabled = !termSearch.query.isBlank()
                advancedSearch.isEnabled = !termSearch.query.isBlank()
                if (termSearch.query.isBlank()) {
                    sharedPrefs.edit().putString("SAVED_QUERY", "").apply()
                }
                else {
                    sharedPrefs.edit().putString("SAVED_QUERY", newText).apply()

                }

                return advancedSearch.isEnabled
            }
        })

        quickSearch.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    ResultsActivity::class.java
                ).putExtra("quickSearchQuery", termSearch.query.toString())
            )
            sharedPrefs.edit().putString("SAVED_QUERY", termSearch.query.toString()).apply()
        }

        advancedSearch.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    AdvancedSearchActivity::class.java
                ).putExtra("advancedSearchQuery", termSearch.query.toString())
            )
            sharedPrefs.edit().putString("SAVED_QUERY", termSearch.query.toString()).apply()
        }

        viewMap.setOnClickListener {
            Log.d("HomeScreenActivity", "viewMaps Clicked()")
            startActivity(Intent(applicationContext, MapsActivity::class.java))
        }


        val categorySpinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        categorySpinner.adapter = categorySpinnerAdapter
        var selectedCategory: Int = sharedPrefs.getInt("SAVED_SPINNER_USER_DEFAULT_POSITION", 0)

        categorySpinner.setSelection(selectedCategory)
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sharedPrefs.edit().putInt("SAVED_SPINNER_USER_DEFAULT_POSITION", position).apply()
                selectedCategory = position
                Log.d(logD, categoryList[selectedCategory])
                doAsync {
                    val news: List<News> =
                        NewsManager.fetchHeadlinesNews(newsAPIKey, categoryList[selectedCategory])
                    val newsAdapter = NewsAdapter(news)
                    runOnUiThread {
                        headlinesRecyclerView.adapter = newsAdapter
                        headlinesRecyclerView.layoutManager =
                            LinearLayoutManager(this@HomeScreenActivity)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}


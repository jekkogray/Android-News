package com.example.androidnews

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync

interface OnItemClick {
    fun onClick(value: String) {

    }
}

class AdvancedSearchActivity : AppCompatActivity() {
    private lateinit var categorySpinner: Spinner
    private lateinit var seeResultsButton: Button
    private lateinit var sourcesRecyclerView: RecyclerView
    private lateinit var selectSourcesText: TextView
    private lateinit var selectedTextView: TextView
    private var selectedCategoryIndex = 0
    var selectedSources = arrayListOf<String>()
    private lateinit var sourceAdapter: SourcesAdapter

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
        val preferences = getSharedPreferences("android-news", Context.MODE_PRIVATE)
        val newsAPIKey = getString(R.string.news_API_key)
        setContentView(R.layout.activity_advanced_search)
        categorySpinner = findViewById(R.id.categorySpinner)
        sourcesRecyclerView = findViewById(R.id.sourcesRecycler)
        selectSourcesText = findViewById(R.id.selectSourcesText)
        seeResultsButton = findViewById(R.id.seeResultsButton)
        selectedTextView = findViewById(R.id.selectedTextView)
        seeResultsButton.isEnabled = false

        // Get query intent
        var query = intent.getStringExtra("advancedSearchQuery").toString()
        if (!query.isNullOrEmpty()) {
            this.title = "Advanced Search for $query"
        }

        // Initialize Spinner Adapter
        val categorySpinnerAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                AdvancedSearchActivity.categoryList
            )

        // Initialize category spinner
        categorySpinner.adapter = categorySpinnerAdapter
        categorySpinner.setSelection(selectedCategoryIndex)

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedTextView.text = "0 Selected"
                selectSourcesText.text = "Sources (select at least 1)"
                selectedCategoryIndex = position
                doAsync {
                    val sources: List<Source> =
                        NewsManager.fetchSources(newsAPIKey, categoryList[selectedCategoryIndex])
                    sourceAdapter = SourcesAdapter(sources)
                    runOnUiThread {
                        selectedSources = sourceAdapter.getCheckList()
                        sourcesRecyclerView.adapter = sourceAdapter
                        sourcesRecyclerView.layoutManager =
                            LinearLayoutManager(this@AdvancedSearchActivity)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        seeResultsButton.setOnClickListener {
            var sendQueryIntent = Intent(this@AdvancedSearchActivity, ResultsActivity::class.java)
                .putExtra("advancedSearchQuery", query.toString())
                .putExtra("category", categorySpinner.selectedItem.toString())
                .putStringArrayListExtra("queryListSources", sourceAdapter.getCheckList())
            startActivity(sendQueryIntent)
        }
    }

    protected override fun onResume() {
        super.onResume()

        // Watch for change in checkList
        var intentFilter_ALERT_CHANGE = IntentFilter("ALERT_CHANGE")
        var receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // Update view from received intent
                selectedTextView.text = intent?.getStringExtra("checkChange")
            }
        }

        // Empty List
        var intentFilter_ALERT_EMPTY_CHECK_LIST = IntentFilter("ALERT_EMPTY_CHECK_LIST")
        var receiver2 = object : BroadcastReceiver() {
            // Update view from received intent
            override fun onReceive(context: Context?, intent: Intent?) {
                // Update view from received intent
                selectSourcesText.text = intent?.getStringExtra("emptyCheckList")
            }
        }
        // Check source selection
        var intentFilter_ALERT_RESULTS_BUTTON = IntentFilter("ALERT_RESULTS_BUTTON")
        var receiver3 = object : BroadcastReceiver() {
            // Update view from received intent
            override fun onReceive(context: Context?, intent: Intent?) {
                seeResultsButton.isEnabled = intent!!.getBooleanExtra("resultsButton", false)
            }
        }
        this.registerReceiver(receiver, intentFilter_ALERT_CHANGE)
        this.registerReceiver(receiver2, intentFilter_ALERT_EMPTY_CHECK_LIST)
        this.registerReceiver(receiver3, intentFilter_ALERT_RESULTS_BUTTON)
    }
}
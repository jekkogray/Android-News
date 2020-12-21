package com.example.androidnews

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.rows_news.*
import org.jetbrains.anko.doAsync
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var localNews: CardView
    private lateinit var localNewsRecycler: RecyclerView
    private lateinit var localMapResultsScreen: TextView

    private val logD = "MapsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        localNews = findViewById(R.id.mapsLocalNewsCard)
        localMapResultsScreen = findViewById(R.id.maps_titleMapResultsScreen)
        localNews.visibility = View.GONE
        localNewsRecycler = findViewById(R.id.localNewsRecycler)
        this.title = "News by Location"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        // Load saved coordinates
        val sharedPrefs: SharedPreferences =
            getSharedPreferences("android-news", Context.MODE_PRIVATE)
        var latitude = sharedPrefs.getString("SAVED_LATITUDE", "0.0")!!.toDouble()
        var longitude = sharedPrefs.getString("SAVED_LONGITUDE", "0.0")!!.toDouble()
        var loadCoordinates = LatLng(latitude, longitude)

        mMap = googleMap
        val newsAPIKey = getString(R.string.news_API_key)
        var news: List<News> = listOf()

        doAsync {
            // Load Geocoder object
            val geocode = Geocoder(this@MapsActivity)
            // Get address from default coordinates
            val results: List<Address> = try {
                geocode.getFromLocation(
                    loadCoordinates.latitude,
                    loadCoordinates.longitude,
                    10
                )
            } catch (exception: Exception) {
                Log.e("MapsActivity", "Geocoder failed:", exception)
                listOf()
            }

            // Cannot find address
            if (results == null || results == emptyList<Address>()) {
                localMapResultsScreen.text = "Cannot Load Current Address"
            } else {
                localMapResultsScreen.text = "Results for ${results.first().adminArea}"
                news = NewsManager.fetchMapNews(newsAPIKey, results)
            }
            val newsAdapter = NewsAdapter(news)
            runOnUiThread {
                sharedPrefs.edit().putString("SAVED_LATITUDE", loadCoordinates.latitude.toString())
                    .apply()
                sharedPrefs.edit()
                    .putString("SAVED_LONGITUDE", loadCoordinates.longitude.toString())
                    .apply()

                if (results.size != 0) {
                    mMap.addMarker(
                        MarkerOptions().position(loadCoordinates).title(results.first().adminArea)
                    )
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLng(loadCoordinates))
                localNews.visibility = View.VISIBLE
                localNewsRecycler.adapter = newsAdapter
                localNewsRecycler.layoutManager = LinearLayoutManager(
                    this@MapsActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }

        mMap.setOnMapLongClickListener { coord: LatLng ->
            mMap.clear()
            doAsync {
                // Load Geocoder object
                val geocode = Geocoder(this@MapsActivity)
                sharedPrefs.edit().putString("SAVED_LATITUDE", coord.latitude.toString()).apply()
                sharedPrefs.edit().putString("SAVED_LONGITUDE", coord.longitude.toString()).apply()
                // Get address from press
                val results: List<Address> = try {
                    geocode.getFromLocation(
                        coord.latitude,
                        coord.longitude,
                        10
                    )
                } catch (exception: Exception) {
                    Log.e("MapsActivity", "Geocoder failed:", exception)
                    listOf()
                }

                // Cannot find address
                if (results == null || results == emptyList<Address>()) {
                    localMapResultsScreen.text = "Cannot Load Current Address"
                } else {
                    news = NewsManager.fetchMapNews(newsAPIKey, results)
                }
                val newsAdapter = NewsAdapter(news)
                when {
                    results.first().countryName == "United States" && results.first().adminArea != null && (news != null || news != emptyList<News>()) -> {
                        localMapResultsScreen.text = "Results for ${results.first().adminArea}"
                        runOnUiThread {
                            mMap.addMarker(
                                MarkerOptions().position(coord).title(results[0].adminArea)
                            )
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(coord))
                            Log.e("MapsActivity", "Results response received generating news")
                            localNews.visibility = View.VISIBLE
                            localNewsRecycler.adapter = newsAdapter
                            localNewsRecycler.layoutManager = LinearLayoutManager(
                                this@MapsActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }

                    results.first().countryName != null && (news != null || news != emptyList<News>()) -> {
                        localMapResultsScreen.text =
                            "Results for ${results.first().countryName}"
                        runOnUiThread {
                            mMap.addMarker(
                                MarkerOptions().position(coord).title(results[0].adminArea)
                            )
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(coord))
                            Log.e("MapsActivity", "Results response received generating news")
                            localNews.visibility = View.VISIBLE
                            localNewsRecycler.adapter = newsAdapter
                            localNewsRecycler.layoutManager = LinearLayoutManager(
                                this@MapsActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                    results.first().countryName == null -> {
                        localMapResultsScreen.text =
                            "No results for Ocean"
                        runOnUiThread {
                            mMap.addMarker(
                                MarkerOptions().position(coord).title(results[0].adminArea)
                            )
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(coord))
                            Log.e("MapsActivity", "Results response received generating news")
                            localNews.visibility = View.VISIBLE
                            localNewsRecycler.adapter = newsAdapter
                            localNewsRecycler.layoutManager = LinearLayoutManager(
                                this@MapsActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                    else -> {
                        localMapResultsScreen.text =
                            "No results for Ocean"
                        runOnUiThread {
                            mMap.addMarker(
                                MarkerOptions().position(coord).title(results[0].adminArea)
                            )
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(coord))
                            Log.e("MapsActivity", "Results response received generating news")
                            localNews.visibility = View.VISIBLE
                            localNewsRecycler.adapter = newsAdapter
                            localNewsRecycler.layoutManager = LinearLayoutManager(
                                this@MapsActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                }

            }
        }
    }
}

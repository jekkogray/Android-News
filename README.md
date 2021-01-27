# Android-News

## About

<p>
Android News is a simple app to aggregate news from multiple sources. It can be used to view top headlines for broad categories, perform specific term searches, view news from specific sources, and view local news in different areas of the world!</p>
<p>Requires Android 6.0 +</p>

The APK to run SpotThat on your own Android device is available <a href="https://github.com/jekkogray/Android-News/blob/main/build/APK/Android-News-demo.apk" download> here.</a>

## Android-News Screenshots

#### [Home Activity](https://github.com/jekkogray/Android-News/blob/main/AndroidNews/app/src/main/java/com/example/androidnews/HomeScreenActivity.kt)

<img src="/readme-imgs/HomeActivity.png" alt="Home Activity" width=200/>
<blockquote>
<p>
<b>Home Activity</b> displays the top headlines and a search bar to find news. The top headlines can also be changed based on the provided categories of the NewsAPI. Additionally, the top headlines saves the previous categories and makes a network API call to NewsAPI.
</p>
</blockquote>

<img src="/readme-imgs/HomeActivity-search.png" alt="Home Activity Search" width=200/>
<blockquote>
<p>
<b>Home Activity Search</b> users can perform either a quick search or an advanced search based on the query.
</p>
</blockquote>

#### [Maps Activity](https://github.com/jekkogray/Android-News/blob/main/AndroidNews/app/src/main/java/com/example/androidnews/MapsActivity.kt)

<img src="/readme-imgs/MapActivity.png" alt="Maps Activity" width=200/>
<blockquote>
<p><b>Maps Activity</b> view the map and allow users to view news based on the location. Tapping on the news opens a new browser with news.</p> 
</blockquote>

#### [Results Activity](https://github.com/jekkogray/Android-News/blob/main/AndroidNews/app/src/main/java/com/example/androidnews/ResultsActivity.kt)

<img src="/readme-imgs/ResultsActivity.png" alt="Results Activity" width=200/>
<blockquote>
<p>
<b>Results Activity</b> the results activity queries and loads news from the NewsAPI asynchronously. 
</p>
</blockquote>

#### [Advanced Search Activity](https://github.com/jekkogray/Android-News/blob/main/AndroidNews/app/src/main/java/com/example/androidnews/AdvancedSearchActivity.kt)

<img src="/readme-imgs/AdvancedSearchActivity.png" alt="AdvancedSearch Activity" width=200/>
<blockquote>
<p>
<b>Advanced Search Activity</b> allows users to query based on a given source and category. In addition, you can tap on multiple sources from the same category to aggregate the results of the news. 
</p>
</blockquote>

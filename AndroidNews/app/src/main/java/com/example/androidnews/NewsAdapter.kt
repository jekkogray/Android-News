package com.example.androidnews

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class NewsAdapter(private val news: List<News> ): RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

    /**
     * @property itemView the view that sets the context similar to setContentView()
     * Extends RecyclerView.Adapter<NewsAdapter.ViewHolder>() this means the view is consisted of ViewHolder objects
     */
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.newsCard)
        val title: TextView = itemView.findViewById(R.id.title)
        val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        val source: TextView = itemView.findViewById(R.id.source)
        val content: TextView = itemView.findViewById(R.id.content)
    }

    /**
     * @return a ViewHolder object from an inflated xml file. This is an object consisting of
     * one view --> one card view technically.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemView: View = layoutInflater.inflate(R.layout.rows_news, parent, false)
        return ViewHolder(itemView)
    }

    /**
     * @param holder this is the given ViewHolder object from above use as a context reference.
     * Allows for independent context for each holder.
     * @return loads data in a newsCard
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // load data into a row
        val currentNews = news[position]
        holder.title.text = currentNews.title
        holder.source.text = currentNews.source
        holder.content.text = currentNews.content
        holder.cardView.setOnClickListener{
            var intUrl: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentNews.url))
            it.context.startActivity(intUrl)
        }
        // see the process in network and cache
        Picasso.get().setIndicatorsEnabled(true)
        // create thumbnail
        Picasso.get()
            .load(currentNews.thumbnailUrl)
            .into(holder.thumbnail)
    }

    /**
     * This determines how much is going to be generated as a whole.
     * The adapter determines how much can be seen at the same time.
     * @return the size of list rendered
     */
    override fun getItemCount(): Int {
        return news.size
    }


}
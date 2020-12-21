package com.example.androidnews

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlin.coroutines.coroutineContext

class SourcesAdapter(private val sources: List<Source>) :
    RecyclerView.Adapter<SourcesAdapter.ViewHolder>() {

    private var pendingCheckedList: ArrayList<String> = ArrayList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.sourceCard)
        val titleSource: TextView = itemView.findViewById(R.id.titleSource)
        val content: TextView = itemView.findViewById(R.id.content)
        var checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemView: View = layoutInflater.inflate(R.layout.rows_sources, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // load data into a row
        val currentSource = sources[position]
        holder.titleSource.text = currentSource.titleSource
        holder.content.text = currentSource.content
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = currentSource.isChecked
        holder.checkBox.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            currentSource.isChecked = isChecked
            if (holder.checkBox.isChecked) {
                pendingCheckedList.add(currentSource.titleSource)
                if (this.checkListSize() > 0) {
                    // broadcast changes to the AdvancedSearchActivity
                    compoundButton.context.sendBroadcast(
                        Intent("ALERT_RESULTS_BUTTON").putExtra(
                            "resultsButton",
                            true
                        )
                    )
                    compoundButton.context.sendBroadcast(
                        Intent("ALERT_EMPTY_CHECK_LIST").putExtra(
                            "emptyCheckList",
                            "Sources"
                        )
                    )
                }
                compoundButton.context.sendBroadcast(
                    Intent("ALERT_CHANGE").putExtra(
                        "checkChange",
                        "${this.checkListSize()} Selected"
                    )
                )
            } else {
                pendingCheckedList.remove(currentSource.titleSource)
                //send broadcast when empty list
                if (this.checkListSize() == 0) {
                    compoundButton.context.sendBroadcast(
                        Intent("ALERT_RESULTS_BUTTON").putExtra(
                            "resultsButton",
                            false
                        )
                    )
                    compoundButton.context.sendBroadcast(
                        Intent("ALERT_EMPTY_CHECK_LIST").putExtra(
                            "emptyCheckList",
                            "Sources (select at least 1)"
                        )
                    )
                }
                compoundButton.context.sendBroadcast(
                    Intent("ALERT_CHANGE").putExtra(
                        "checkChange",
                        "${this.checkListSize()} Selected"
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return sources.size
    }

    public fun getCheckList(): ArrayList<String> {
        return pendingCheckedList
    }

    public fun checkListSize(): Int {
        return pendingCheckedList.size
    }

}
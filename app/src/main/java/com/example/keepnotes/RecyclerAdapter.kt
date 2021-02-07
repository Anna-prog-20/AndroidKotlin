package com.example.keepnotes

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(
    private var notes: ArrayList<Notes>,
    var onItemClickCallback: IRVOnItemClick
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item, parent,
            false
        )
        return ViewHolder(view, onItemClickCallback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (notes != null) {
            holder.setTextToTextView(notes!![position].text)
            holder.setTopicToTextView(notes!![position].topic)
            holder.setOnClickForItem(notes!![position].topic)
        }
    }

    override fun getItemCount(): Int {
        return notes?.size ?: 0
    }

    class ViewHolder(itemView: View, val onItemClickCallback:IRVOnItemClick) : RecyclerView.ViewHolder(itemView) {
        private val itemTopic: TextView
        private val itemText: TextView
        fun setTextToTextView(text: String?) {
            itemText.text = text
        }

        fun setTopicToTextView(text: String?) {
            itemTopic.text = text
        }

        fun setOnClickForItem(text: String?) {
            itemTopic.setOnClickListener(View.OnClickListener {
                onItemClickCallback.onItemClicked(text)
            })
            itemText.setOnClickListener(View.OnClickListener {
                 onItemClickCallback.onItemClicked(text)
            })
            itemView.setOnClickListener(View.OnClickListener {
                onItemClickCallback.onItemClicked(text)
            })
        }

        init {
            itemText = itemView.findViewById(R.id.itemTextView)
            itemTopic = itemView.findViewById(R.id.itemTopic)
        }
    }

}
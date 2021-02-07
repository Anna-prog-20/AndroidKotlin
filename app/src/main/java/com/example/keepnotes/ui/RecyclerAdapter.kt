package com.example.keepnotes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.keepnotes.R
import com.example.keepnotes.databinding.ItemBinding
import com.example.keepnotes.model.Note


class RecyclerAdapter(var onItemClickCallback: IRVOnItemClick) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item, parent,
            false
        )
        return ViewHolder(
            view,
            onItemClickCallback
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])
        holder.setOnClickForItem(notes[position].topic)
    }

    override fun getItemCount(): Int = notes.size ?: 0

    class ViewHolder(itemView: View, val onItemClickCallback: IRVOnItemClick) :
        RecyclerView.ViewHolder(itemView) {
        val ui: ItemBinding = ItemBinding.bind(itemView)

        fun bind(note: Note) {
            ui.itemTopic.text = note.topic
            ui.itemText.text = note.text
            (itemView as CardView).setCardBackgroundColor(note.color)
        }

        fun setOnClickForItem(text: String?) {
            ui.itemTopic.setOnClickListener(View.OnClickListener {
                onItemClickCallback.onItemClicked(text)

            })
            ui.itemText.setOnClickListener(View.OnClickListener {
                onItemClickCallback.onItemClicked(text)
            })
            itemView.setOnClickListener(View.OnClickListener {
                onItemClickCallback.onItemClicked(text)
            })
        }
    }

}
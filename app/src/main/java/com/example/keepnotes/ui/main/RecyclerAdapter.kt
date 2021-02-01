package com.example.keepnotes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.keepnotes.R
import com.example.keepnotes.databinding.ItemBinding
import com.example.keepnotes.model.Note


class RecyclerAdapter(val onItemClickCallback: IRVOnItemClick) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var noteSelected: Note? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item, parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ui: ItemBinding = ItemBinding.bind(itemView)

        fun bind(note: Note) {
            note.run {
                ui.itemTopic.text = topic
                ui.itemText.text = text
                itemView.setBackgroundColor(color)
                itemView.setOnClickListener { onItemClickCallback.onItemClicked(this) }
                itemView.setOnLongClickListener {
                    noteSelected = this
                    itemView.showContextMenu()
                    true
                }
            }

        }

    }

}
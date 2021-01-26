package com.example.keepnotes.model

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.ContextCompat.getColor
import com.example.keepnotes.R

@SuppressLint("ResourceType")
fun initArrayList(context: Context, count: Int): List<Note> {
    var i = 1
    val arrayList: MutableList<Note> = mutableListOf()
    for (i in 1..count) {
        arrayList.add(
            if (i <= 5) {
                Note(
                    topic = "Заголовок №" + i,
                    text = "Мои первые записки!!!",
                    color = getColor(context, R.color.color_yello)
                )
            } else {
                Note(
                    topic = "Заголовок №" + i,
                    text = "Уже намного лучше - вывели много много заметок!!!",
                    color = getColor(context, R.color.color_violet)
                )
            }
        )
    }
    return arrayList
}
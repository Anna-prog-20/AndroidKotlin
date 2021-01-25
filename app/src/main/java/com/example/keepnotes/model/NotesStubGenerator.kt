package com.example.keepnotes.model

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.ContextCompat.getColor
import com.example.keepnotes.R

@SuppressLint("ResourceType")
fun initArrayList(context: Context, count: Int): MutableList<Note> {
    var i = 1
    val arrayList: MutableList<Note> = mutableListOf()
    while (i <= count) {
        if (i <= 5)
            arrayList.add(
                Note(
                    topic = "Заголовок №" + i,
                    text = "Мои первые записки!!!",
                    color = getColor(context, R.color.color_yello)
                )
            )
        else
            arrayList.add(
                Note(
                    topic = "Заголовок №" + i,
                    text = "Уже намного лучше - вывели много много заметок!!!",
                    color = getColor(context, R.color.color_violet)
                )
            )
        i++;
    }
    return arrayList
}
package com.example.keepnotes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MainActivity() : AppCompatActivity(), IRVOnItemClick {
    val arrayList: ArrayList<Notes> = ArrayList(1)
    private var listNotes: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listNotes = findViewById(R.id.listNotes)

        initArrayList(20)
        setupRecyclerView(arrayList)
    }

    private fun initArrayList(count: Int) {
        var i = 1
        while(i <= count){
            if (i <=5)
                arrayList.add(Notes("Заголовок №" + i, "Мои первые записки!!!"))
            else
                arrayList.add(Notes("Заголовок №" + i, "Уже намного лучше - вывели много много заметок!!!"))
            i++;
        }
    }

    private fun setupRecyclerView(arrayList: ArrayList<Notes>) {
        val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        val adapter = RecyclerAdapter(arrayList, this)
        listNotes?.layoutManager = layoutManager
        listNotes?.adapter = adapter
    }

    override fun onItemClicked(itemText: String?) {
        Toast.makeText(this, "Заметка с темой - $itemText!", Toast.LENGTH_LONG).show()
    }

}
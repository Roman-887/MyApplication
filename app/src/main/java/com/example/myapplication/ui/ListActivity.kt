package com.example.myapplication.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.adapter.PhotoAdapter
import com.example.myapplication.R
import com.example.myapplication.data.Preferences

class ListActivity : AppCompatActivity() {

    private  var recyclerView: RecyclerView? = null
    private  var adapter: PhotoAdapter? = null
    private val preferences by lazy { Preferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        recyclerView = findViewById(R.id.recyclerView)
        val numberOfColumns = 3
        val gridLayoutManager = GridLayoutManager(this, numberOfColumns)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }

        val photoList = preferences.getSavedPhotos().sorted()
        adapter = PhotoAdapter(this, photoList)
        recyclerView?.adapter = adapter
    }
}
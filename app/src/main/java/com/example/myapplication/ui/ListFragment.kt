package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Preferences
import com.example.myapplication.ui.adapter.PhotoAdapter

class ListFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var adapter: PhotoAdapter? = null
    private var preferences: Preferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = Preferences(requireContext())

        recyclerView = view.findViewById(R.id.recyclerView)
        val numberOfColumns = 3
        val gridLayoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = 1
        }

        val photoList = preferences?.getSavedPhotos()?.sorted()
        adapter = photoList?.let { PhotoAdapter(requireContext(), it) }
        recyclerView?.adapter = adapter
    }
}
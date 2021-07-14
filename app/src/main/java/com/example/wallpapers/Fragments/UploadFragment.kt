package com.example.wallpapers.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mywallpaper.adapter.CategoryAdapter
import com.example.mywallpaper.adapter.WallpaperAdapter
import com.example.mywallpaper.model.CategoryModel
import com.example.mywallpaper.model.WallpaperModel
import com.example.wallpapers.R
import com.example.wallpapers.databinding.FragmentMainBinding
import com.example.wallpapers.databinding.FragmentUploadBinding


class UploadFragment : Fragment() {
    private lateinit var categoryAdapter: CategoryAdapter
    var list = ArrayList<CategoryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.cate_recyclerview)
        list.add(CategoryModel(R.drawable.nature,"Nature"))
        list.add(CategoryModel(R.drawable.food,"Food"))
        list.add(CategoryModel(R.drawable.people,"People"))
        list.add(CategoryModel(R.drawable.r,"Red"))
        list.add(CategoryModel(R.drawable.g,"Green"))
        list.add(CategoryModel(R.drawable.yellow,"Yellow"))
        list.add(CategoryModel(R.drawable.white,"White"))
        list.add(CategoryModel(R.drawable.black,"Black"))
        list.add(CategoryModel(R.drawable.dark,"Dark"))
        list.add(CategoryModel(R.drawable.sea,"Sea"))
        list.add(CategoryModel(R.drawable.lifestyle,"Lifestyle"))
        list.add(CategoryModel(R.drawable.artandculture,"Art & Culture"))
        list.add(CategoryModel(R.drawable.bright_colors,"Bright Colors"))
        list.add(CategoryModel(R.drawable.space,"Space"))
        list.add(CategoryModel(R.drawable.dark_and_moody,"Dark & Moody"))
        list.add(CategoryModel(R.drawable.fantasy,"Fantasy"))
        list.add(CategoryModel(R.drawable.landscape,"Landscape"))
        list.add(CategoryModel(R.drawable.motorcycle,"Motorcycle"))
        list.add(CategoryModel(R.drawable.sport,"Sport"))
        list.add(CategoryModel(R.drawable.beautiful,"Beautiful"))
        list.add(CategoryModel(R.drawable.car,"Car"))

        categoryAdapter = CategoryAdapter(requireContext(), list)
        recyclerView.adapter = categoryAdapter
        var gridLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = gridLayoutManager


    }


}
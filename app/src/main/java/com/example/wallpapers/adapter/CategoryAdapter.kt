package com.example.mywallpaper.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mywallpaper.model.CategoryModel
import com.example.mywallpaper.model.WallpaperModel
import com.example.wallpapers.Activities.FullActivity
import com.example.wallpapers.Activities.category
import com.example.wallpapers.R

class CategoryAdapter(val context:Context, var categoryModelList:List<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_item , parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
    val list = categoryModelList.get(position)
       holder.imageView.setImageResource(list.originalUrl)
        holder.cardView.setOnClickListener {
           val intent = Intent(context, category::class.java)
            intent.putExtra("title",list.title)
            context.startActivity(intent)
        }
        holder.textview.setText(list.title)
    }

    override fun getItemCount(): Int {
       return categoryModelList.size
    }

    class CategoryViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
      val imageView:ImageView = itemView.findViewById(R.id.CateimageViewItem)
        val textview: TextView = itemView.findViewById(R.id.CateTextViewItem)
        val cardView: CardView = itemView.findViewById(R.id.cate_card)

    }
}
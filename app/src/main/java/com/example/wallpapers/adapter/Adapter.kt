package com.example.mywallpaper.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.example.mywallpaper.model.WallpaperModel
import com.example.wallpapers.Activities.FullActivity
import com.example.wallpapers.R

class Adapter(val context:Context, var wallpaperModelList:List<WallpaperModel>) : RecyclerView.Adapter<Adapter.WallpaperViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item , parent, false)
        return WallpaperViewHolder(view)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
    val list = wallpaperModelList.get(position)
        Log.i("tag", "onBindViewHolder: ${list.originalUrl}")
       Glide.with(context).load(list.mediumUrl)
           .listener(object :RequestListener<Drawable>{
               override fun onLoadFailed(
                   e: GlideException?,
                   model: Any?,
                   target: Target<Drawable>?,
                   isFirstResource: Boolean
               ): Boolean {
                   holder.lottieAnimationView.visibility = View.GONE
                   return false
               }

               override fun onResourceReady(
                   resource: Drawable?,
                   model: Any?,
                   target: Target<Drawable>?,
                   dataSource: DataSource?,
                   isFirstResource: Boolean
               ): Boolean {

                   holder.lottieAnimationView.visibility = View.GONE
                   return false
               }
           })
           .into(holder.imageView)
        holder.imageView.setOnClickListener {

           val intent = Intent(context,FullActivity::class.java)
            intent.putExtra("photographer",list.photographerName)
            intent.putExtra("avg_color",list.avg_color)
            intent.putExtra("photographerUrl",list.photographerUrl)
            intent.putExtra("originalUrl",list.originalUrl)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
       return wallpaperModelList.size
    }

    class WallpaperViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
      val imageView:ImageView = itemView.findViewById(R.id.imageViewItem)
        val lottieAnimationView = itemView.findViewById<LottieAnimationView>(R.id.lottie_anim)
    }
}
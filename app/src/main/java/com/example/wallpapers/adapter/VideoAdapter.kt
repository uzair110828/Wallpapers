package com.example.wallpapers.adapter


import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.wallpapers.Activities.FullScreenVideo
import com.example.wallpapers.Fragments.VideosFragment
import com.example.wallpapers.R
import com.example.wallpapers.model.VideoModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.video.VideoListener
import de.hdodenhof.circleimageview.CircleImageView


class VideoAdapter(private val mVideoItems: List<VideoModel>, val context: Context) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.show_videos, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.setVideoData(mVideoItems.get(position),context)
        holder.imageView.setOnClickListener {
            val intent = Intent(context,FullScreenVideo::class.java)
            intent.putExtra("url",mVideoItems.get(position).url)
            intent.putExtra("user_url",mVideoItems.get(position).user_url)
            intent.putExtra("name",mVideoItems.get(position).name)
            intent.putExtra("image",mVideoItems.get(position).image)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mVideoItems.size
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var lottieAnimationView: LottieAnimationView
          var imageView: ImageView = itemView.findViewById(R.id.videoViewItem)
     var  cardView: CardView = itemView.findViewById(R.id.videoCard)
        fun setVideoData(videoModel: VideoModel, context: Context) {

            lottieAnimationView = itemView.findViewById(R.id.lottie_anim)
            cardView = itemView.findViewById(R.id.videoCard)
            Glide.with(context).load(videoModel.image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        lottieAnimationView.visibility = View.GONE
                        return false
                    }
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        lottieAnimationView.visibility = View.GONE
                        return false
                    }

                })
                .into(imageView)





        }

    }

}
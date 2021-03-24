package com.example.wallpapers.Activities

import android.app.DownloadManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.wallpapers.R
import com.example.wallpapers.databinding.ActivityFullScreenVideoBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

class FullScreenVideo : AppCompatActivity() {
    private lateinit var playerView: PlayerView
    private lateinit var binding: ActivityFullScreenVideoBinding
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private lateinit var name: String
    private lateinit var url: String
    private lateinit var user_url: String
    private lateinit var image: String
    private lateinit var progressBar: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playerView = findViewById(R.id.videoView)
        progressBar = findViewById(R.id.lottie)
        url = intent.getStringExtra("url")
        user_url = intent.getStringExtra("user_url")
        name = intent.getStringExtra("name")
        image = intent.getStringExtra("image")
        binding.vUserName.setText(name)
        binding.vuserUrl.setText(user_url)
        Glide.with(this).load(image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.lottie.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.lottie.visibility = View.GONE
                    return false
                }

            })
            .into(binding.videoImage)
        initializePlayer()


    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this).build()
        playerView.player = player
        val mediaItem = MediaItem.fromUri(url)
        player!!.setMediaItem(mediaItem)
        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.prepare()
        player!!.repeatMode = Player.REPEAT_MODE_ONE


        val listener = player!!.addListener(object : Player.EventListener {
            override fun onLoadingChanged(isLoading: Boolean) {
                super.onLoadingChanged(isLoading)
//               progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                when (state) {
                    Player.STATE_READY -> {
                        progressBar.visibility = View.GONE
                        player!!.playWhenReady = true
                    }
                    Player.STATE_BUFFERING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    Player.STATE_IDLE -> {
                        player!!.retry()
                    }
                }

            }
        })

    }


    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player?.release()
            player = null
        }
    }

    fun downloadVideos() {
        val downloadManager: DownloadManager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        var uri = Uri.parse(url)
        var request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            uri.getLastPathSegment()
        );
        downloadManager.enqueue(request)

        Toast.makeText(this, "Downloading Start", Toast.LENGTH_SHORT).show()
    }

    fun downloadVideo(view: View) {
        downloadVideos()
    }


    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }
}
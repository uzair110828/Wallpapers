package com.example.wallpapers.Activities

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.Context
import android.content.IntentFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.wallpapers.Check_Network.network_receiver
import com.example.wallpapers.R
import com.example.wallpapers.databinding.ActivityFullBinding
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.glide.transformations.BlurTransformation

class FullActivity :AppCompatActivity(), network_receiver.ConnectivityReceiverListener {

    private var snackBar: Snackbar? = null
    private lateinit var originalUrl: String
    private lateinit var photographer: String
    private lateinit var photographerUrl: String
    private lateinit var binding: ActivityFullBinding
    private lateinit var photoView: PhotoView
    private lateinit var progressBar:LottieAnimationView
    private lateinit var avg_color:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityFullBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerReceiver(network_receiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        photoView = findViewById(R.id.photoView)
        progressBar = findViewById(R.id.progress_bar)
        originalUrl = intent.getStringExtra("originalUrl")
        photographer = intent.getStringExtra("photographer")
        photographerUrl = intent.getStringExtra("photographerUrl")
        avg_color = intent.getStringExtra("avg_color")

        Glide.with(this).load(originalUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

            })
            .into(photoView)
        Glide.with(this).load(originalUrl)
            .into(binding.photographerImage)
        binding.photographerName.setText(photographer)
        binding.photographerUrl.setText(photographerUrl)




    }

    fun SetWallpaper() {
        val wallpaperManager = WallpaperManager.getInstance(this)

        var bitmap = (photoView.getDrawable() as BitmapDrawable).bitmap
        wallpaperManager.setBitmap(bitmap)
        Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show()
    }

    fun downloadWallpaper() {
        val downloadManager: DownloadManager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        var uri = Uri.parse(originalUrl)
        var request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            uri.getLastPathSegment()
        );
        downloadManager.enqueue(request)

        Toast.makeText(this, "Downloading Start", Toast.LENGTH_SHORT).show()
    }

    fun btnDownload(view: View) {downloadWallpaper()}
    fun btnSet(view: View) {SetWallpaper()}

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        network_receiver.connectivityReceiverListener = this
    }
    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackBar = Snackbar.make(findViewById(R.id.layout), "You are offline.", Snackbar.LENGTH_LONG) //Assume "rootLayout" as the root layout of every activity.
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE

            snackBar?.show()
        } else {
            snackBar?.dismiss()
            Glide.with(this).load(originalUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                })
                .into(photoView)
            Glide.with(this).load(originalUrl)
                .into(binding.photographerImage)
            binding.photographerName.setText(photographer)
            binding.photographerUrl.setText(photographerUrl)

        }
    }


}
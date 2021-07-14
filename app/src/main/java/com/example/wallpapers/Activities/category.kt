package com.example.wallpapers.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mywallpaper.adapter.Adapter
import com.example.mywallpaper.adapter.WallpaperAdapter
import com.example.mywallpaper.model.WallpaperModel
import com.example.wallpapers.R
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject

class category : AppCompatActivity() {
    private var title_query: String? = null

    private lateinit var wallpaperAdapter: Adapter
    private var snackBar: Snackbar? = null
    var list = ArrayList<WallpaperModel>()
    var pageNumber = 1
    var isScrolling = false
    private var currenctItems: Int? = null
    private var totalItems: Int? = null
    private var scrollOutItems: Int? = null
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        title_query = intent.getStringExtra("title")
        recyclerView = findViewById(R.id.show_recycler_view)
        Log.i("tag", "onCreate: $title_query")

        wallpaperAdapter = Adapter(this, list)
        recyclerView.adapter = wallpaperAdapter
        var gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                currenctItems = gridLayoutManager.childCount

                totalItems = gridLayoutManager.itemCount

                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition()

                if (isScrolling && (currenctItems!! + scrollOutItems!! == totalItems)) {
                    isScrolling = false
                    fetchWallpaper(title_query)
                }
            }
        })
        fetchWallpaper(title_query)
    }
    fun fetchWallpaper(query: String?) {
            val request = object : StringRequest(
                Request.Method.GET,
                "https://api.pexels.com/v1/search/?page=$pageNumber&per_page=80&query=${query?.toLowerCase()}",
                object : Response.Listener<String> {
                    override fun onResponse(response: String?) {
                        try {
                            var jsonObject = JSONObject(response)

                            var jsonArray = jsonObject.getJSONArray("photos")

                            val length = jsonArray.length()

                            for (i in 0 until length) {
                                var objects = jsonArray.getJSONObject(i)

                                var id = objects.getInt("id")
                                var photographer = objects.getString("photographer")
                                var photographer_url = objects.getString("photographer_url")
                                val avg_color = objects.getString("avg_color")


                                var objectImages = objects.getJSONObject("src")
                                var originalUrl = objectImages.getString("original")
                                var mediumUrl = objectImages.getString("medium")
                                Log.i("tag", "onCreate: $originalUrl")

                                var model = WallpaperModel(
                                    id,
                                    photographer,
                                    photographer_url,
                                    avg_color,
                                    originalUrl,
                                    mediumUrl
                                )
                                list.add(model)
                            }
                            wallpaperAdapter.notifyDataSetChanged()
                            pageNumber++

                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@category,
                                "${e.message.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                },
                Response.ErrorListener {
                    Toast.makeText(this, "something went wrong ${it.message.toString()}", Toast.LENGTH_SHORT).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    var param = HashMap<String, String>()
                    param.put(
                        "Authorization",
                        "563492ad6f917000010000011e11b50e2cd74b8e9729ddcf23232f3a"
                    )
                    return param

                }
            }


            val requestQueue: RequestQueue = Volley.newRequestQueue(this)
            requestQueue.add(request)

        }

}
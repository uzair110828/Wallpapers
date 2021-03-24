package com.example.wallpapers.Fragments

import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mywallpaper.adapter.WallpaperAdapter
import com.example.mywallpaper.model.WallpaperModel
import com.example.wallpapers.Check_Network.network_receiver
import com.example.wallpapers.R
import com.example.wallpapers.databinding.FragmentMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject

class MainFragment : Fragment() , network_receiver.ConnectivityReceiverListener {
    private lateinit var binding: FragmentMainBinding
    private lateinit var wallpaperAdapter: WallpaperAdapter
    private var snackBar: Snackbar? = null

    var list =ArrayList<WallpaperModel>()
    var pageNumber = 1
    var isScrolling = false
    private  var currenctItems:Int?=null
    private  var totalItems:Int?=null
    private  var scrollOutItems:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onStart() {
        super.onStart()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        requireActivity().registerReceiver(network_receiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        wallpaperAdapter = WallpaperAdapter(requireContext(), list)
        binding.recyclerView.adapter  = wallpaperAdapter
        var gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.layoutManager= gridLayoutManager
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                    fetchWallpaper()
                }
            }
        })
        fetchWallpaper()

    }
    fun fetchWallpaper() {
        val request = object : StringRequest(Request.Method.GET,
            "https://api.pexels.com/v1/curated/?page=" + pageNumber + "&per_page=80",
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
                            requireContext(),
                            "${e.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            },
            Response.ErrorListener {

            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                var param = HashMap<String, String>()
                param.put(
                    "Authorization",
                    "563492ad6f917000010000011e11b50e2cd74b8e9729ddcf23232f3a"
                )
                return param

            }
        }


        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(request)

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        network_receiver.connectivityReceiverListener = this
    }
    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackBar = Snackbar.make(binding.consLayout, "You are offline.", Snackbar.LENGTH_LONG) //Assume "rootLayout" as the root layout of every activity.
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackBar?.show()
        } else {
            snackBar?.dismiss()
            fetchWallpaper()

        }
    }


}
package com.example.wallpapers.Fragments

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
import com.example.wallpapers.R
import com.example.wallpapers.adapter.VideoAdapter
import com.example.wallpapers.databinding.FragmentVideosBinding
import com.example.wallpapers.model.VideoModel
import org.json.JSONException
import org.json.JSONObject

class VideosFragment : Fragment() {
    private val TAG: String = "tag"
    private  var pageNumber:Int = 1
    var isScrolling = false
    private  var currenctItems:Int?=null
    private  var totalItems:Int?=null
    private  var scrollOutItems:Int?=null
    private lateinit var binding: FragmentVideosBinding
    var videoItems= ArrayList<VideoModel>()
    private lateinit var adapter:VideoAdapter
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
        return inflater.inflate(R.layout.fragment_videos, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVideosBinding.bind(view)
        fetchWallpaper()
        adapter= VideoAdapter(videoItems, activity?.applicationContext!!)
        val gridLayoutManager=GridLayoutManager(requireContext(),2)
        binding.viewPagerVideos.layoutManager = gridLayoutManager
        binding.viewPagerVideos.adapter = adapter
        binding.viewPagerVideos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    }
    fun fetchWallpaper() {
        val request = object : StringRequest(Request.Method.GET,
            "https://api.pexels.com/videos/popular/?page=" + pageNumber + "  &per_page=80",
            object : Response.Listener<String> {
                override fun onResponse(response: String?) {
                    try {
                        var jsonObject = JSONObject(response)

                        var jsonArray = jsonObject.getJSONArray("videos")

                        val length = jsonArray.length()

                        for (i in 0 until length) {
                            var objects = jsonArray.getJSONObject(i)


                            var id = objects.getInt("id")

                            var url = objects.getString("url")

                            var image = objects.getString("image")



                            var users = objects.getJSONObject("user")
                            var user_name = users.getString("name")

                            var user_url = users.getString("url")

                            var videoFiles = objects.getJSONArray("video_files")


                            val videofileObject = videoFiles.getJSONObject(1)

                            val quality = videofileObject.getString("quality")

                            val link = videofileObject.getString("link")



                            var v = VideoModel(user_name , link,user_url,image)

                            videoItems.add(v)

                        }
                        adapter.notifyDataSetChanged()
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
}
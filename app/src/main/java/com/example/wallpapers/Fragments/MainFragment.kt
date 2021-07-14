package com.example.wallpapers.Fragments

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mywallpaper.adapter.WallpaperAdapter
import com.example.mywallpaper.model.WallpaperModel
import com.example.wallpapers.Check_Network.network_receiver
import com.example.wallpapers.R
import com.example.wallpapers.databinding.FragmentMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject


class MainFragment : Fragment(), network_receiver.ConnectivityReceiverListener {
    private lateinit var binding: FragmentMainBinding
    private lateinit var wallpaperAdapter: WallpaperAdapter
    private var snackBar: Snackbar? = null
    var list = ArrayList<WallpaperModel>()
    var pageNumber = 1
    var isScrolling = false
    private var currenctItems: Int? = null
    private var totalItems: Int? = null
    private var check_query: String? = null
    private var scrollOutItems: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
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
        requireActivity().registerReceiver(
            network_receiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        wallpaperAdapter = WallpaperAdapter(requireContext(), list)
        binding.recyclerView.adapter = wallpaperAdapter
        var gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.layoutManager = gridLayoutManager
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
                    fetchWallpaper(check_query)
                }
            }
        })
        fetchWallpaper(null)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (check_query.isNullOrEmpty()) {
                    requireActivity().finish()
                } else {
                    check_query = null
                    list.clear()
                    binding.recyclerView.adapter!!.notifyDataSetChanged()
                    fetchWallpaper(check_query)
                    Toast.makeText(
                        requireContext(),
                        "Press Back again to Exit.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


    }

    fun fetchWallpaper(query: String?) {
        if (query.isNullOrEmpty()) {
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


            val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
            requestQueue.add(request)

        } else {
            val request = object : StringRequest(Request.Method.GET,
                "https://api.pexels.com/v1/search/?page=$pageNumber&per_page=80&query=${query.toLowerCase()}",
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
                    Toast.makeText(
                        requireContext(),
                        "something went wrong ${it.message.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
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


            val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
            requestQueue.add(request)

        }
    }

    private fun View.closeKeyboard() {

        val manager: InputMethodManager? =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        manager?.hideSoftInputFromWindow(windowToken, 0)

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuItem = menu.findItem(R.id.search_icon)
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.queryHint = "Enter Category e.g Nature"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                check_query = query
                list.clear()
                if (check_query!!.isNotEmpty()) {
                    binding.recyclerView.adapter!!.notifyDataSetChanged()
                    fetchWallpaper(check_query)
                }
                Log.i("tag", "onQueryTextSubmit: ${check_query.toString()}")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        searchView.closeKeyboard()
        super.onCreateOptionsMenu(menu, inflater)

    }


    override fun onResume() {
        super.onResume()
        network_receiver.connectivityReceiverListener = this
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackBar = Snackbar.make(
                binding.consLayout,
                "You are offline.",
                Snackbar.LENGTH_LONG
            ) //Assume "rootLayout" as the root layout of every activity.
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackBar?.show()
        } else {
            snackBar?.dismiss()
            fetchWallpaper(check_query)

        }
    }


}
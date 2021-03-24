package com.example.wallpapers.Fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.wallpapers.R
import com.example.wallpapers.databinding.FragmentUploadBinding
import mehdi.sakout.aboutpage.AboutPage


class UploadFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        var view =  inflater.inflate(R.layout.fragment_upload, container, false)
        val typeface = Typeface.createFromAsset(requireActivity().assets, "font/ubuntu_regular.ttf")

        return AboutPage(requireContext())
            .isRTL(false)
            .enableDarkMode(false)
            .setCustomFont(typeface) // or Typeface
            .setDescription(getString(R.string.main_string))
            .setImage(R.drawable.wall)
            .addGroup("Connect with us")
            .addEmail("uzerarif12@gmail.com"," contact with wallpaper's team")
            .addFacebook("umair.khan.10420321")
            .addGitHub("uzair110828")
            .addInstagram("uzair__arif_")
            .addGroup("This app is created by Uzair Arif.")
            .create()
    }


}
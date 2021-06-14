package com.azatberdimyradov.photoplan.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.azatberdimyradov.photoplan.R
import com.azatberdimyradov.photoplan.databinding.FragmentImageBinding
import com.bumptech.glide.Glide

class ImageFragment: Fragment(R.layout.fragment_image) {

    private val args: ImageFragmentArgs by navArgs()
    private lateinit var binding: FragmentImageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentImageBinding.bind(view)

        binding.apply {
            val image = args.image
            Glide.with(requireContext())
                .load(image.imageUrl)
                .into(fragmentImageImageView)
        }
    }

}
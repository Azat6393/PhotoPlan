package com.azatberdimyradov.photoplan.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azatberdimyradov.photoplan.R
import com.azatberdimyradov.photoplan.adapters.LocationAdapter
import com.azatberdimyradov.photoplan.databinding.FragmentLocationBinding
import com.azatberdimyradov.photoplan.models.Image
import com.azatberdimyradov.photoplan.models.Location
import com.azatberdimyradov.photoplan.models.Section
import com.azatberdimyradov.photoplan.utils.READ_STORAGE
import com.azatberdimyradov.photoplan.utils.Resource
import com.azatberdimyradov.photoplan.utils.checkPermission
import com.azatberdimyradov.photoplan.viewmodels.LocationViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class LocationFragment: Fragment(R.layout.fragment_location), LocationAdapter.ItemListener {

    private lateinit var binding: FragmentLocationBinding
    private lateinit var locationAdapter: LocationAdapter
    private val viewModel: LocationViewModel by viewModels()
    private var myLocation: Location? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLocationBinding.bind(view)
        locationAdapter = LocationAdapter(this)

        binding.apply {
            recyclerView.apply {
                adapter = locationAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            floatingActionButton.setOnClickListener {
                val uuid = UUID.randomUUID().toString()
                viewModel.addLocation(Location(uuid,"", emptyList()))
            }
            sectionEditText.addTextChangedListener {
                viewModel.changeSectionName(it.toString())
            }
        }
        observeData()
    }

    fun observeData(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenStarted {
                viewModel.locations.collect {
                    when(it){
                        is Resource.Success -> {
                            it.data?.let { locations ->
                                locationAdapter.submitList(locations)
                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        }
                        is Resource.Empty -> {

                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenStarted {
                viewModel.sectionName.collect {
                    when(it){
                        is Resource.Success -> {
                            it.data?.let { sectionName ->
                                binding.sectionEditText.setText(sectionName.sectionName)
                            }
                        }
                        is Resource.Error -> {
                            println(it.message)
                        }
                        is Resource.Empty -> {

                        }
                    }
                }
            }
        }
    }

    override fun addPhoto(location: Location) {
        if(checkPermission(READ_STORAGE)){
            myLocation = location
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent,2)
        }
    }

    override fun onTextChanged(locationName: String, location: Location) {
        viewModel.updateLocation(locationName,location)
    }

    override fun onImageClicked(image: Image) {
        val action = LocationFragmentDirections.actionLocationFragmentToImageFragment(image)
        findNavController().navigate(action)
    }

    override fun onClickDeleteButton(deleteList: List<Image>, location: Location) {
        viewModel.deleteImages(deleteList,location)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2
            && resultCode == Activity.RESULT_OK && data != null
        ) {
            val imageUri = data.data
            imageUri?.let {
                viewModel.addImage(it, myLocation!!)
            }
        }
    }

}
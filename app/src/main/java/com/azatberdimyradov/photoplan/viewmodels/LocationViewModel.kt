package com.azatberdimyradov.photoplan.viewmodels

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azatberdimyradov.photoplan.models.Image
import com.azatberdimyradov.photoplan.models.Location
import com.azatberdimyradov.photoplan.models.Section
import com.azatberdimyradov.photoplan.repository.PhotoPlanRepository
import com.azatberdimyradov.photoplan.utils.Resource
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel @ViewModelInject constructor(
    private val repository: PhotoPlanRepository
) : ViewModel() {

    private val _locations = MutableStateFlow<Resource<List<Location>>>(Resource.Empty())
    val locations: StateFlow<Resource<List<Location>>> = _locations

    private val _sectionName = MutableStateFlow<Resource<Section>>(Resource.Empty())
    val sectionName: StateFlow<Resource<Section>> = _sectionName

    init {
        getLocations()
        getSectionName()
    }

    fun addImage(uri: Uri, location: Location) = viewModelScope.launch {
        repository.addImage(uri, location)
    }

    fun addLocation(location: Location) = viewModelScope.launch {
        repository.addLocation(location)
    }

    fun changeSectionName(sectionName: String) = viewModelScope.launch {
        repository.changeSectionName(sectionName)
    }

    fun updateLocation(locationName: String, location: Location) = viewModelScope.launch {
        repository.updateLocationName(locationName, location)
    }

    fun deleteImages(deleteList: List<Image>, location: Location) = viewModelScope.launch {
        repository.deleteImages(deleteList, location)
    }

    private fun getLocations() = viewModelScope.launch {
        repository.getLocations()
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _locations.value = Resource.Error(error.message.toString())
                }
                value?.let { data ->
                    val location = data.toObjects<Location>()
                    _locations.value = Resource.Success(location)
                }
            }
    }

    private fun getSectionName() = viewModelScope.launch {
        repository.getSectionName()
            .addOnSuccessListener {
                val sectionName = it.toObject<Section>()
                _sectionName.value = Resource.Success(sectionName)
            }
            .addOnFailureListener {
                _sectionName.value = Resource.Error(it.message)
            }
    }

}
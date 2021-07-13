package com.azatberdimyradov.photoplan.repository

import android.net.Uri
import com.azatberdimyradov.photoplan.db.FirebaseDatabase
import com.azatberdimyradov.photoplan.models.Image
import com.azatberdimyradov.photoplan.models.Location
import com.azatberdimyradov.photoplan.models.Section
import com.azatberdimyradov.photoplan.utils.IMAGES
import com.azatberdimyradov.photoplan.utils.LOCATIONS
import com.azatberdimyradov.photoplan.utils.SECTION_NAME
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PhotoPlanRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {

    fun deleteImages(deleteList: List<Image>, location: Location) =
        firebaseDatabase.deleteImagesFromStorage(deleteList, location)

    fun addImage(uri: Uri, location: Location) =
        firebaseDatabase.addImageToFirebaseStorage(uri, location)

    fun updateLocationName(locationName: String, location: Location) =
        firebaseDatabase.updateLocationNameFromFirestore(locationName, location)

    fun getLocations() = firebaseDatabase.getLocationsFromFirestore()

    fun getSectionName() = firebaseDatabase.getSectionNameFromFirestore()

    fun addLocation(location: Location) = firebaseDatabase.addLocationToFirestore(location)

    fun changeSectionName(sectionName: String) =
        firebaseDatabase.changeSectionNameFromFirestore(sectionName)
}
package com.azatberdimyradov.photoplan.repository

import android.net.Uri
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


class PhotoPlanRepository {

    fun deleteImagesFromStorage(deleteList: List<Image>, location: Location){
        val path = Firebase.storage.reference
            .child(IMAGES)

        deleteList.forEach {
            path.child("${it.id}.jpg").delete()
        }
        deleteImagesFromFirestore(deleteList, location)
    }

    private fun deleteImagesFromFirestore(deleteList: List<Image>, location: Location){
        val newImageList = location.imageList?.minus(deleteList)
        Firebase.firestore
            .collection(LOCATIONS)
            .document(location.id!!)
            .set(location.copy(imageList = newImageList))
    }

     fun addImageToStorage(uri: Uri, location: Location) {
        val uuid = UUID.randomUUID()
        val path = Firebase.storage.reference
            .child(IMAGES)
            .child("${uuid.toString()}.jpg")

           path.putFile(uri)
            .addOnSuccessListener {
                path.downloadUrl.addOnSuccessListener {
                    addImageToFirestore(it.toString(), uuid.toString(),location)
                }
            }.addOnFailureListener {
                println(it.message)
            }
    }

    private fun addImageToFirestore(uri: String, id: String, location: Location) {
        val imageList = location.imageList!!.toMutableList()
        imageList.add(Image(id,uri))
        Firebase.firestore
            .collection(LOCATIONS)
            .document(location.id!!)
            .set(location.copy(imageList = imageList))
    }

    fun updateLocationName(locationName: String, location: Location) =
        Firebase.firestore
            .collection(LOCATIONS)
            .document(location.id!!)
            .set(location.copy(locationName = locationName))


    fun getLocations() =
        Firebase.firestore
            .collection(LOCATIONS)

    fun getSectionName() =
        Firebase.firestore
            .collection(SECTION_NAME)
            .document(SECTION_NAME)
            .get()

    fun addLocation(location: Location) {
        location.id?.let {
            Firebase.firestore
                .collection(LOCATIONS)
                .document(it)
                .set(location)
        }
    }

    fun changeSectionName(sectionName: String) {
        Firebase.firestore
            .collection(SECTION_NAME)
            .document(SECTION_NAME)
            .set(Section(sectionName))
    }
}
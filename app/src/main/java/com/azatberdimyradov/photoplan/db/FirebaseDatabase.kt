package com.azatberdimyradov.photoplan.db

import android.net.Uri
import com.azatberdimyradov.photoplan.models.Image
import com.azatberdimyradov.photoplan.models.Location
import com.azatberdimyradov.photoplan.models.Section
import com.azatberdimyradov.photoplan.utils.IMAGES
import com.azatberdimyradov.photoplan.utils.LOCATIONS
import com.azatberdimyradov.photoplan.utils.SECTION_NAME
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDatabase @Inject constructor(
    private val mFirestore: FirebaseFirestore,
    private val mStorage: FirebaseStorage
){

    fun deleteImagesFromStorage(deleteList: List<Image>, location: Location){
        val path = mStorage.reference
            .child(IMAGES)

        deleteList.forEach {
            path.child("${it.id}.jpg").delete()
        }
        deleteImagesFromFirestore(deleteList, location)
    }

    private fun deleteImagesFromFirestore(deleteList: List<Image>, location: Location){
        val newImageList = location.imageList?.minus(deleteList)
        mFirestore
            .collection(LOCATIONS)
            .document(location.id!!)
            .set(location.copy(imageList = newImageList))
    }

    fun addImageToFirebaseStorage(uri: Uri, location: Location) {
        val uuid = UUID.randomUUID()
        val path = mStorage.reference
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
        mFirestore
            .collection(LOCATIONS)
            .document(location.id!!)
            .set(location.copy(imageList = imageList))
    }

    fun updateLocationNameFromFirestore(locationName: String, location: Location) =
        mFirestore
            .collection(LOCATIONS)
            .document(location.id!!)
            .set(location.copy(locationName = locationName))


    fun getLocationsFromFirestore() =
        mFirestore
            .collection(LOCATIONS)

    fun getSectionNameFromFirestore() =
        mFirestore
            .collection(SECTION_NAME)
            .document(SECTION_NAME)
            .get()

    fun addLocationToFirestore(location: Location) {
        location.id?.let {
            mFirestore
                .collection(LOCATIONS)
                .document(it)
                .set(location)
        }
    }

    fun changeSectionNameFromFirestore(sectionName: String) {
        mFirestore
            .collection(SECTION_NAME)
            .document(SECTION_NAME)
            .set(Section(sectionName))
    }
}
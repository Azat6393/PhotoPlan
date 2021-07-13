package com.azatberdimyradov.photoplan.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getFirestore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun getFirebaseStorage() = FirebaseStorage.getInstance()

}